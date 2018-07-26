package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.VerifyLinkMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;

import javax.mail.MessagingException;
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
import java.io.UnsupportedEncodingException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@WebServlet(name = "RegisterServlet")
@MultipartConfig
public class RegisterServlet extends HttpServlet {
    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/register.jsp";

    private UserDAO userDAO;
    private EmailFactory emailFactory;

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

        emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
        if (emailFactory == null) {
            throw new ServletException("Impossible to get email factory for email system");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
    }

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
        String firstPassword = request.getParameter(RegistrationValidator.FIRST_PWD_KEY);
        String secondPassword = request.getParameter(RegistrationValidator.SECOND_PWD_KEY);
        String infPrivacy = request.getParameter(RegistrationValidator.INF_PRIVACY_KEY);


        Part avatarPart = request.getPart(RegistrationValidator.AVATAR_KEY);

        // Usa il validator per verifiacare la conformità
        HashMap<String, String> messages = RegistrationValidator.createValidationMessages(
                userDAO,
                firstName,
                lastName,
                email,
                firstPassword,
                secondPassword,
                infPrivacy,
                avatarPart
                );

        // In caso i campi non siano validi ricarica la pagina con gli errori indicati
        if (!messages.isEmpty()) {
            request.setAttribute("messages", messages);
            request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
            return;
        }

        String uuidAvatar = UUID.randomUUID().toString();

        try (InputStream fileContent = avatarPart.getInputStream()) {
            File file = new File(path.toString(), uuidAvatar.toString());
            System.out.println(file.toPath());
            Files.copy(fileContent, file.toPath());
        } catch (FileAlreadyExistsException ex) { // Molta sfiga
            getServletContext().log("File \"" + uuidAvatar.toString() + "\" already exists on the server");
            response.sendError(500,"File \"" + uuidAvatar.toString() + "\" already exists on the server");
            return;
        } catch (RuntimeException ex) {
            //TODO: handle better the exception
            getServletContext().log("impossible to upload the file", ex);
            throw ex;
        }

        // Genera l'utente, manda la mail di verifica e in caso visualizza gli errori
        try {
            User user = userDAO.generateUser(
                    firstName,
                    lastName,
                    email,
                    firstPassword,
                    uuidAvatar);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            try {
                emailFactory.sendMail(
                        "Registration",
                        "Registration",
                        VerifyLinkMail.createMessage(user),
                        "registrazioneprogettowebprog@gmail.com"
                ); // Per ora le mandiamo a noi stessi per evitare casini
            } catch (MessagingException | UnsupportedEncodingException ex) {
                // TODO : Cambiare in notification?
                ArrayList<String> errorList = (ArrayList<String>) session.getAttribute("errors");
                if (errorList == null) {
                    errorList = new ArrayList<>();
                }

                errorList.add("Impossible to send the email. Please check the email in user's settings and click resend");
                session.setAttribute("errors", new ArrayList<String>());
            }


            // Se la registrazione ha abuto successo vai alla pagina base/default (index)
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            response.sendRedirect(response.encodeRedirectURL(contextPath));
        } catch (DAOException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                messages.put(RegistrationValidator.EMAIL_KEY, "Email già utilizzata");
                request.setAttribute("messages", messages);

                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
                return;
            }

            response.sendError(500, "Impossible register the user. The server returned: " + e.getMessage());
        }
    }
}
