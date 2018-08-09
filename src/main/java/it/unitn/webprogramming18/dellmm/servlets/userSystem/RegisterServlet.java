package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.VerifyLinkMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
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
import java.util.*;
import java.util.stream.Collectors;

public class RegisterServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/register.jsp";

        private UserDAO userDAO;
        private EmailFactory emailFactory;

        @Override
        public void init() throws ServletException
        {

                userDAO = new JDBCUserDAO();

                emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
                if (emailFactory == null)
                {
                        throw new ServletException("Impossible to get email factory for email system");
                }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                if (request.getRequestURI().endsWith(".json"))
                {
                        ServletUtility.sendError(request, response, 400, "generic.errors.postOnly");
                }
                else
                {
                        request.setAttribute(RegistrationValidator.AVATAR_KEY, RegistrationValidator.DEFAULT_AVATARS.get(0));

                        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
                }
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                // Ottieni configurazione cartella avatars
                String avatarsFolder = getServletContext().getInitParameter("avatarsFolder");
                if (avatarsFolder == null)
                {
                        throw new ServletException("Avatars folder not configured");
                }

                String realContextPath = request.getServletContext().getRealPath(File.separator);
                if (!realContextPath.endsWith("/"))
                {
                        realContextPath += "/";
                }

                Path path = Paths.get(realContextPath + avatarsFolder);

                if (!Files.exists(path))
                {
                        Files.createDirectories(path);
                }

                // Ottieni tutti i parametri
                String firstName = request.getParameter(RegistrationValidator.FIRST_NAME_KEY);
                String lastName = request.getParameter(RegistrationValidator.LAST_NAME_KEY);
                String email = request.getParameter(RegistrationValidator.EMAIL_KEY);
                String firstPassword = request.getParameter(RegistrationValidator.FIRST_PWD_KEY);
                String secondPassword = request.getParameter(RegistrationValidator.SECOND_PWD_KEY);
                String infPrivacy = request.getParameter(RegistrationValidator.INF_PRIVACY_KEY);
                String avatar = request.getParameter(RegistrationValidator.AVATAR_KEY);

                Part avatarImg = request.getPart(RegistrationValidator.AVATAR_IMG_KEY);

                // Usa il validator per verifiacare la conformità
                Map<String, String> messages
                            = RegistrationValidator.createValidationMessages(
                                        userDAO,
                                        firstName,
                                        lastName,
                                        email,
                                        firstPassword,
                                        secondPassword,
                                        infPrivacy,
                                        avatar,
                                        avatarImg
                            ).entrySet().stream().collect(Collectors.toMap(
                                        (Map.Entry<String, RegistrationValidator.ErrorMessage> e) -> e.getKey(),
                                        (Map.Entry<String, RegistrationValidator.ErrorMessage> e) -> RegistrationValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                            ));

                // In caso i campi non siano validi ricarica la pagina con gli errori indicati
                if (!messages.isEmpty())
                {
                        ServletUtility.sendValidationError(request, response, 400, messages);
                        return;
                }

                String imageName = avatar;

                if (RegistrationValidator.DEFAULT_AVATARS.stream().noneMatch(avatar::equals))
                {
                        imageName = UUID.randomUUID().toString();

                        try (InputStream fileContent = avatarImg.getInputStream())
                        {
                                File file = new File(path.toString(), imageName.toString());
                                Files.copy(fileContent, file.toPath());
                        }
                        catch (FileAlreadyExistsException ex)
                        { // Molta sfiga
                                getServletContext().log("File \"" + imageName.toString() + "\" already exists on the server");
                                ServletUtility.sendError(request, response, 500, "generic.errors.fileCollision");
                                return;
                        }
                        catch (RuntimeException ex)
                        {
                                ServletUtility.sendError(request, response, 500, "generic.errors.unuploudableFile");
                                getServletContext().log("impossible to upload the file", ex);
                                return;
                        }
                }

                // Genera l'utente, manda la mail di verifica e in caso visualizza gli errori
                try
                {
                        User user = userDAO.generateUser(
                                    firstName,
                                    lastName,
                                    email,
                                    firstPassword,
                                    imageName);

                        HttpSession session = request.getSession(true);
                        session.setAttribute("user", user);

                        try
                        {
                                emailFactory.sendMail(
                                            "Registration",
                                            "Registration",
                                            VerifyLinkMail.createMessage(user),
                                            "registrazioneprogettowebprog@gmail.com"
                                ); // Per ora le mandiamo a noi stessi per evitare casini
                        }
                        catch (MessagingException | UnsupportedEncodingException ex)
                        {
                                // TODO : Cambiare in notification?
                                ArrayList<String> errorList = (ArrayList<String>) session.getAttribute("errors");
                                if (errorList == null)
                                {
                                        errorList = new ArrayList<>();
                                }

                                errorList.add("Impossible to send the email. Please check the email in user's settings and click resend");
                                session.setAttribute("errors", new ArrayList<String>());
                        }

                        if (request.getRequestURI().endsWith(".json"))
                        {
                                ServletUtility.sendJSON(request, response, 200, new HashMap<>());
                                return;
                        }
                        else
                        {
                                // Se la registrazione ha abuto successo vai alla pagina base/default (index)
                                String contextPath = getServletContext().getContextPath();
                                if (!contextPath.endsWith("/"))
                                {
                                        contextPath += "/";
                                }

                                response.sendRedirect(response.encodeRedirectURL(contextPath));
                        }
                }
                catch (DAOException ex)
                {
                        if (ex.getCause() instanceof SQLIntegrityConstraintViolationException)
                        {
                                messages.put(RegistrationValidator.EMAIL_KEY, "Email già utilizzata");

                                ServletUtility.sendValidationError(request, response, 400, messages);
                                return;
                        }

                        getServletContext().log("impossible to register the user", ex);
                        ServletUtility.sendError(request, response, 500, "generic.errors.unupdatableUser");
                        return;
                }
        }
}
