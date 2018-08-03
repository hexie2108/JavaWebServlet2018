package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.ResetPasswordMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

@WebServlet(name = "ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    public static final String EMAIL_KEY = "email",
            PREV_URL_KEY = "prevUrl",
            ERR_NOEMAIL_KEY = "error_noEmail",
            ERR_NOVERIFIED_KEY = "error_noVerified",
            ERR_EMPTY_FIELD_KEY = "error_emptyField",
            ERR_GEN_RST_LINK_KEY = "error_gen_rst_link",
            ERR_SEND_MAIL = "error_send_email";

    private static final String FORGOT_PASSWORD_JSP = "/WEB-INF/jsp/forgotPassword.jsp";

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
        if(request.getRequestURI().endsWith(".json")) {
            ServletUtility.sendError(request, response, 400, "generic.errors.postOnly");
        } else {
            request.getRequestDispatcher(FORGOT_PASSWORD_JSP).forward(request, response);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(EMAIL_KEY);

        if (email == null || email.isEmpty()) {
            ServletUtility.sendError(request, response, 400,"validateUser.errors.EMAIL_MISSING");
            return;
        }
        User user;

        try {
            user = userDAO.getByEmail(email);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "forgotPassword.errors.unsearchableUser");
            return;
        }

        if (user == null) {
            ServletUtility.sendError(request, response, 400, "forgotPassword.errors.EMAIL_NOT_REGISTERED");
            return;
        }

        if (user.getVerifyEmailLink() != null) {
            ServletUtility.sendError(request, response, 400, "login.errors.noValidatedEmail");
            return;
        }

        try {
            user.setResetPwdEmailLink(UUID.randomUUID().toString());
            userDAO.update(user);

            // TODO: Risettare user?

            emailFactory.sendMail(
                    "Reset Password",
                    "Reset Password",
                    ResetPasswordMail.createMessage(user),
                    "registrazioneprogettowebprog@gmail.com"
            );
            // Per permetterci nel mentre di non creare account mail false per verificare gli account
            // TODO: Da cambiare

        } catch (DAOException ex) {
            ServletUtility.sendError(request, response, 500, "forgotPassword.errors.unresettableLinkRst");
            return;
        } catch (MessagingException | UnsupportedEncodingException ex) {
            ServletUtility.sendError(request, response, 500, "forgotPassword.errors.cantSendEmail");
            return;
        }

        if (request.getRequestURI().endsWith(".json")){
            ServletUtility.sendJSON(request,response, 200, new HashMap<>());
        } else {
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            response.sendRedirect(response.encodeRedirectURL(contextPath + "/" + PagePathsConstants.LOGIN));
        }
    }
}
