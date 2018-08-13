package it.unitn.webprogramming18.dellmm.servlets.admin;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet(name = "JSONUsersServlet")
@MultipartConfig
public class JSONUsersServlet extends HttpServlet {
    private UserDAO userDAO = null;

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
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String admin = request.getParameter("admin");

        Integer iId;
        try{
            iId = id == null || id.trim().isEmpty()? null: Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 400, "users.errors.idNotInt");
            return;
        }

        if(email != null && email.trim().isEmpty()) {
            email = null;
        }

        if(name != null && name.trim().isEmpty()) {
            name = null;
        }

        if(surname != null && surname.trim().isEmpty()) {
            surname = null;
        }

        Boolean bAdmin = admin == null || admin.trim().isEmpty()? null: Boolean.parseBoolean(admin);


        try {
            List<User> users = userDAO.filter(iId, email, name, surname, bAdmin);

            ServletUtility.sendJSON(request, response, 200, users);
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
        try{
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
            String avatarsFolder = getServletContext().getInitParameter("avatarsFolder");
            if (avatarsFolder == null) {
                throw new ServletException("Avatars folder not configured");
            }

            String realContextPath = request.getServletContext().getRealPath(File.separator);
            if (!realContextPath.endsWith("/")) {
                realContextPath += "/";
            }

            Path path = Paths.get(realContextPath + avatarsFolder);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

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

            /* Usa il validator per verifiacare la conformità
            Map<String, String> messages =
                    FormValidator.partialValidate(userDAO, kv)
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap((Map.Entry<String, FormValidator.ErrorMessage> e) -> e.getKey(),
                                    (Map.Entry<String, FormValidator.ErrorMessage> e) -> FormValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                                    )
                            );

            if (!messages.isEmpty()) {
                ServletUtility.sendValidationError(request, response, 400, messages);
                return;
            }

*/


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
                    avatarName = UUID.randomUUID().toString();

                    try (InputStream fileContent = avatarImg.getInputStream()) {
                        File file = new File(path.toString(), avatarName.toString());
                        Files.copy(fileContent, file.toPath());
                    } catch (FileAlreadyExistsException ex) { // Molta sfiga
                        ServletUtility.sendError(request, response, 500, "generic.errors.fileCollision");
                        getServletContext().log("File \"" + avatarName.toString() + "\" already exists on the server");
                        return;
                    } catch (RuntimeException ex) {
                        ServletUtility.sendError(request, response, 500, "generic.errors.unuploudableFile");
                        getServletContext().log("impossible to upload the file", ex);
                        return;
                    }
                }

                String oldImg = user.getImg();

                user.setImg(avatarName);

                if (FormValidator.DEFAULT_AVATARS.stream().noneMatch(oldImg::equals) ) {
                    Path toDelete = Paths.get(path.toString(), oldImg);
                    try {
                        Files.delete(toDelete);
                    } catch (IOException e) {
                        // If we can't delete the old image we just log and continue
                        getServletContext().log("File " + toDelete.toString() + " cannot be delete");
                    }
                }
            }

            try {
                userDAO.update(user);
            } catch (DAOException e) {
                ServletUtility.sendError(request, response, 500, "generic.unupdatableUser");
                return;
            }

            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        } else if (action.equalsIgnoreCase("delete")) {
            try {
                userDAO.delete(iId);
            } catch (DAOException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 400, "users.errors.impossibleDeleteUser");
                return;
            }

            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
