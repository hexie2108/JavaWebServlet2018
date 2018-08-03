package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet(name = "ModifyUserServlet")
@MultipartConfig
public class ModifyUserServlet extends HttpServlet {
    private static final String MODIFY_USER_JSP = "/WEB-INF/jsp/modifyUser.jsp";

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get db factory for user storage system", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user.getImg().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            req.setAttribute(RegistrationValidator.AVATAR_KEY, "");
        } else {
            req.setAttribute(RegistrationValidator.AVATAR_KEY, user.getImg());
        }

        req.getRequestDispatcher(MODIFY_USER_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String firstName = request.getParameter(RegistrationValidator.FIRST_NAME_KEY);
        String lastName = request.getParameter(RegistrationValidator.LAST_NAME_KEY);
        String email = request.getParameter(RegistrationValidator.EMAIL_KEY);
        String avatar = request.getParameter(RegistrationValidator.AVATAR_KEY);

        Part avatarImg = request.getPart(RegistrationValidator.AVATAR_IMG_KEY);

        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");

        HashMap<String, Object> kv = new HashMap<>();

        if (firstName != null && !firstName.isEmpty()) {
            kv.put(RegistrationValidator.FIRST_NAME_KEY, firstName);
        } else {
            firstName = "";
        }

        if (lastName != null && !lastName.isEmpty()) {
            kv.put(RegistrationValidator.LAST_NAME_KEY, lastName);
        } else {
            lastName = "";
        }

        if (email != null && !email.isEmpty()) {
            kv.put(RegistrationValidator.EMAIL_KEY, email);
        } else {
            email = "";
        }

        if (avatar != null && !avatar.isEmpty()) {
            kv.put(RegistrationValidator.AVATAR_KEY, avatar);
            kv.put(RegistrationValidator.AVATAR_IMG_KEY, avatarImg);
        } else {
            avatar = "";
        }

        // Usa il validator per verifiacare la conformità
        Map<String, String> messages =
                RegistrationValidator.partialValidate(userDAO, kv)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                (Map.Entry<String, RegistrationValidator.ErrorMessage> e) -> e.getKey(),
                                (Map.Entry<String, RegistrationValidator.ErrorMessage> e) -> RegistrationValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                                )
                        );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
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

        if (!avatar.isEmpty()) {
            String avatarName = avatar;

            if(avatar.equals(RegistrationValidator.CUSTOM_AVATAR)) {
                avatarName = UUID.randomUUID().toString();

                try (InputStream fileContent = avatarImg.getInputStream()) {
                    File file = new File(path.toString(), avatarName.toString());
                    Files.copy(fileContent, file.toPath());
                } catch (FileAlreadyExistsException ex) { // Molta sfiga
                    ServletUtility.sendError(request, response, 500, "File collison or file already exists on the server");
                    getServletContext().log("File \"" + avatarName.toString() + "\" already exists on the server");
                    return;
                } catch (RuntimeException ex) {
                    ServletUtility.sendError(request, response, 500, "impossible to upload the file");
                    getServletContext().log("impossible to upload the file", ex);
                    return;
                }
            }

            String oldImg = user.getImg();

            user.setImg(avatarName);

            if (RegistrationValidator.DEFAULT_AVATARS.stream().noneMatch(oldImg::equals) ) {
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
            ServletUtility.sendError(request, response, 500, "Impossibile aggiornare l'utente");
            return;
        }

        if (request.getRequestURI().endsWith(".json")) {
            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        } else {
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            response.sendRedirect(contextPath + PagePathsConstants.MODIFY_USER);
        }
    }
}
