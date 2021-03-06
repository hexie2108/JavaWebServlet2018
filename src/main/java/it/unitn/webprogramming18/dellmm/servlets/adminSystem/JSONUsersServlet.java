package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "JSONUsersServlet")
@MultipartConfig
public class JSONUsersServlet extends HttpServlet {
    private UserDAO userDAO = null;

    private String subImg(HttpServletRequest request, HttpServletResponse response, Path path, String prevImg, InputStream inputStream) throws IOException, ServletException {
        // If prevImg is one of default's set prevImg to null to prevent deletion of the file
        if (prevImg != null && FormValidator.DEFAULT_AVATARS.stream().anyMatch(prevImg::equals)) {
            prevImg = null;
        }

        return ServletUtility.insertImage(request, path, prevImg, inputStream, ConstantsUtils.IMAGE_OF_USER_WIDTH, ConstantsUtils.IMAGE_OF_USER_HEIGHT);
    }

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters for ordering(what column)
        UserDAO.OrderableColumns column;

        { // Used to limit orderBy scope
            String columnName = DatatablesUtils.getColumnName(request, response);
            if(columnName == null) {
                return;
            }

            // Get column and save as enum, if not valid send error
            switch (columnName) {
                case "id":
                    column = UserDAO.OrderableColumns.ID;
                    break;
                case "name":
                    column = UserDAO.OrderableColumns.NAME;
                    break;
                case "surname":
                    column = UserDAO.OrderableColumns.SURNAME;
                    break;
                case "email":
                    column = UserDAO.OrderableColumns.EMAIL;
                    break;
                case "admin":
                    column = UserDAO.OrderableColumns.ADMIN;
                    break;
                default:
                    ServletUtility.sendError(request, response, 400, "datatables.errors.columnNameUnrecognized");
                    return;
            }
        }

        // get ordering direction
        Boolean dir = DatatablesUtils.getDirection(request, response);
        if(dir == null) {
            return;
        }

        // get parameters for pagination
        Integer iOffset = DatatablesUtils.getOffset(request, response);
        if (iOffset == null) {
            return;
        }

        Integer iLength = DatatablesUtils.getLength(request, response);
        if (iLength == null) {
            return;
        }

        Integer iId;
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String admin = request.getParameter("admin");

        {
            String id = request.getParameter("id");
            try {
                iId = id == null || id.trim().isEmpty() ? null : Integer.parseInt(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 400, "users.errors.idNotInt");
                return;
            }
        }

        if (email != null && email.trim().isEmpty()) {
            email = null;
        }

        if (name != null && name.trim().isEmpty()) {
            name = null;
        }

        if (surname != null && surname.trim().isEmpty()) {
            surname = null;
        }

        Boolean bAdmin = admin == null || admin.trim().isEmpty() ? null : Boolean.parseBoolean(admin);


        try {
            List<User> users = userDAO.filter(iId, email, name, surname, bAdmin, column, dir, iOffset, iLength);
            Long totalCount = userDAO.getCount();
            Long filteredCount = userDAO.getCountFilter(iId, email, name, surname, bAdmin);

            HashMap<String, Object> h = new HashMap<>();
            h.put("recordsTotal", totalCount);
            h.put("recordsFiltered", filteredCount);
            h.put("data", users);

            ServletUtility.sendJSON(
                    request,
                    response,
                    200,
                    h
            );
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "users.errors.impossibleDbFilter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        String id = request.getParameter("id");

        if (id == null) {
            ServletUtility.sendError(request, response, 400, "users.errors.idNotPresent");
            return;
        }

        int iId;
        try {
            iId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 400, "users.errors.idNotInt");
            return;
        }

        if (action == null) {
            ServletUtility.sendError(request, response, 400, "users.errors.missingAction");
        } else if (action.equalsIgnoreCase("modify")) {
            // Ottieni configurazione cartella avatars
            Path path = ServletUtility.getFolder(getServletContext(),"avatarsFolder");

            // Ottieni tutti i parametri
            String firstName = request.getParameter(FormValidator.FIRST_NAME_KEY);
            String lastName = request.getParameter(FormValidator.LAST_NAME_KEY);
            String email = request.getParameter(FormValidator.EMAIL_KEY);
            String password = request.getParameter(FormValidator.FIRST_PWD_KEY);
            String avatar = request.getParameter(FormValidator.AVATAR_KEY);

            Part avatarImg = request.getPart(FormValidator.AVATAR_IMG_KEY);

            HashMap<String, Object> kv = new HashMap<>();

            if (firstName != null && !firstName.isEmpty()) {
                kv.put(FormValidator.FIRST_NAME_KEY, firstName);
            } else {
                firstName = "";
            }

            if (lastName != null && !lastName.isEmpty()) {
                kv.put(FormValidator.LAST_NAME_KEY, lastName);
            } else {
                lastName = "";
            }

            if (email != null && !email.isEmpty()) {
                kv.put(FormValidator.EMAIL_KEY, email);
            } else {
                email = "";
            }

            if (avatar != null && !avatar.isEmpty()) {
                kv.put(FormValidator.AVATAR_KEY, avatar);
                kv.put(FormValidator.AVATAR_IMG_KEY, avatarImg);
            } else {
                avatar = "";
            }

            if (password != null && !password.isEmpty()) {
                kv.put(FormValidator.FIRST_PWD_KEY, password);
            } else {
                password = "";
            }

            // Usa il validator per verifiacare la conformità
            Map<String, String> messages =
                    UserValidator.partialValidate(userDAO, kv)
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(Map.Entry::getKey,
                                    (Map.Entry<String, UserValidator.ErrorMessage> e) -> UserValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                                    )
                            );

            if (!messages.isEmpty()) {
                ServletUtility.sendValidationError(request, response, 400, messages);
                return;
            }


            User user;
            try {
                user = userDAO.getByPrimaryKey(iId);
            } catch (DAOException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 500, "generic.errors.unsearchableUser");
                return;
            }

            if (!firstName.isEmpty()) {
                user.setName(firstName);
            }

            if (!lastName.isEmpty()) {
                user.setSurname(lastName);
            }

            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            if (!password.isEmpty()) {
                user.setPassword(password);
            }

            if (!avatar.isEmpty()) {
                String avatarName = avatar;

                if(avatar.equals(FormValidator.CUSTOM_AVATAR)) {
                    avatarName = subImg(request, response, path, user.getImg(), avatarImg.getInputStream());
                } else if (FormValidator.DEFAULT_AVATARS.stream().noneMatch(user.getImg()::equals)) {
                    ServletUtility.deleteFile(path, user.getImg(), getServletContext());
                }

                user.setImg(avatarName);
            }

            try {
                userDAO.update(user);
            } catch (DAOException e) {
                ServletUtility.sendError(request, response, 500, "generic.unupdatableUser");
                return;
            }

            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        } else if (action.equalsIgnoreCase("delete")) {
            Path path = ServletUtility.getFolder(getServletContext(),"avatarsFolder");

            try {
                // Get user to later delete its avatar image
                User user = userDAO.getByPrimaryKey(iId);
                // Delete the user
                userDAO.delete(iId);

                // Delete user images(if not a default one)
                if (FormValidator.DEFAULT_AVATARS.stream().noneMatch(user.getImg()::equals)) {

                    ServletUtility.deleteFile(path, user.getImg(), getServletContext());
                }
            } catch (DAOException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 400, "users.errors.impossibleDeleteUser");
                return;
            }

            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        }
    }
}
