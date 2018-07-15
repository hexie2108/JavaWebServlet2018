package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.ResetPasswordMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@WebServlet(name = "ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    public static final String EMAIL_KEY = "email",
            PREV_URL_KEY = "prevUrl",
            ERR_NOEMAIL_KEY = "error_noEmail",
            ERR_NOVERIFIED_KEY = "error_noVerified",
            ERR_EMPTY_FIELD_KEY = "error_emptyField";

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(PagePathsConstants.FORGOT_PASSWORD_JSP).forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(EMAIL_KEY);

        if (email == null ||
                email.isEmpty()) {
            request.getRequestDispatcher(PagePathsConstants.FORGOT_PASSWORD_JSP + "?" +
                    ERR_EMPTY_FIELD_KEY + "=true"
            ).forward(request, response);
            return;
        }

        String prevUrl = request.getParameter(PREV_URL_KEY);

        // Se prevUrl è vuoto allora usa la pagina di default(index)
        if ((prevUrl == null) || (prevUrl.isEmpty())) {
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            prevUrl = contextPath;
        }

        User user;

        try {
            user = userDAO.getByEmail(email);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServletException("Impossible trovare l'utente");
        }

        if (user == null) {
            request.getRequestDispatcher(PagePathsConstants.FORGOT_PASSWORD_JSP + "?" +
                    ERR_NOEMAIL_KEY + "=true" +
                    "&" + PREV_URL_KEY + "=" + prevUrl +
                    "&" + EMAIL_KEY + "=" + email
            ).forward(request, response);
            return;
        }

        if (user.getVerifyEmailLink() != null) {
            request.getRequestDispatcher(PagePathsConstants.FORGOT_PASSWORD_JSP + "?" +
                    ERR_NOVERIFIED_KEY + "=true" +
                    "&" + PREV_URL_KEY + "=" + prevUrl +
                    "&" + EMAIL_KEY + "=" + email
            ).forward(request, response);
            return;
        }

        try {
            user.setResetPwdEmailLink(UUID.randomUUID().toString());
            userDAO.update(user);

            try {
                emailFactory.sendMail(
                        "Reset Password",
                        "Reset Password",
                        ResetPasswordMail.createMessage(user),
                        "registrazioneprogettowebprog@gmail.com"
                );
                // Per permetterci nel mentre di non creare account mail false per verificare gli account
                // TODO: Da cambiare
            } catch (MessagingException | UnsupportedEncodingException ex) {
                // TODO: Rimettere uuid_pw_rst a null?
                response.sendError(500, "Impossible to send the email");
                return;
            }
        } catch (DAOException ex) {
            response.sendError(500, "Impossible to create reset password link");
            return;
        }
    }
}
