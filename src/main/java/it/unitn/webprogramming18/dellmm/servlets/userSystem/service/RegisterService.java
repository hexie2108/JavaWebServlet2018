package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.VerifyLinkMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;

import static it.unitn.webprogramming18.dellmm.util.FileUtils.isValidFileExtension;

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
import java.nio.file.StandardCopyOption;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

public class RegisterService extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/register.jsp";

    private UserDAO userDAO;
    private EmailFactory emailFactory;

    @Override
    public void init() throws ServletException {

        userDAO = new JDBCUserDAO();

        emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
        CheckErrorUtils.isNull(emailFactory, "Impossible to get email factory for email system");

    }

    /**
     * post occupa il servizio della registrazione
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //get percorso dell'avatar
        String realContextPath = request.getServletContext().getRealPath("/");
        Path path = Paths.get(realContextPath + ConstantsUtils.IMAGE_BASE_PATH + File.separator + ConstantsUtils.IMAGE_OF_USER);

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
        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }

        String imageName = avatar;

        if (RegistrationValidator.DEFAULT_AVATARS.stream().noneMatch(avatar::equals)) {

            //genera un nome temporaneo
            imageName = UUID.randomUUID().toString();

            //controlla se il file è un tipo di immagine valido
            if (!isValidFileExtension(avatarImg.getContentType())) {
                throw new ServletException("il tipo di file non è valido");
            }

            File file = null;

            try (InputStream fileContent = avatarImg.getInputStream()) {
                file = new File(path.toString(), imageName);
                Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (FileAlreadyExistsException ex) {
                // Molta sfiga
                getServletContext().log("File \"" + imageName.toString() + "\" already exists on the server");
                ServletUtility.sendError(request, response, 500, "generic.errors.fileCollision");
                return;
            } catch (RuntimeException ex) {
                ServletUtility.sendError(request, response, 500, "generic.errors.unuploudableFile");
                getServletContext().log("impossible to upload the file", ex);
                return;
            }

            FileUtils.convertJPG(file, path.toString(), ConstantsUtils.IMAGE_OF_USER_WIDTH, ConstantsUtils.IMAGE_OF_USER_HEIGHT);
        }

        // Genera l'utente, manda la mail di verifica e in caso visualizza gli errori
        try {
            User user = userDAO.generateUser(
                    firstName,
                    lastName,
                    email,
                    firstPassword,
                    imageName);

            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            //manda email
            try {
                emailFactory.sendMail(
                        "Registration",
                        "Registration",
                        VerifyLinkMail.createMessage(user),
                        "hexie2109@gmail.com"
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

            if (request.getRequestURI().endsWith(".json")) {
                ServletUtility.sendJSON(request, response, 200, new HashMap<>());
            } else {
                // Se la registrazione ha avuto successo vai alla pagina base/default (index)
                String contextPath = getServletContext().getContextPath();
                if (!contextPath.endsWith("/")) {
                    contextPath += "/";
                }

                response.sendRedirect(response.encodeRedirectURL(contextPath));
            }
        } catch (DAOException ex) {
            if (ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
                messages.put(RegistrationValidator.EMAIL_KEY, "Email già utilizzata");

                ServletUtility.sendValidationError(request, response, 400, messages);
                return;
            }

            getServletContext().log("impossible to register the user", ex);
            ServletUtility.sendError(request, response, 500, "generic.errors.unupdatableUser");
        }
    }
}
