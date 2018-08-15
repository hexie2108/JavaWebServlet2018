package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * ll servizio che manda l'email di reset password
 *
 * @author mikuc
 */
public class ForgotPasswordService extends HttpServlet
{

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
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                String email = request.getParameter(FormValidator.EMAIL_KEY);
                CheckErrorUtils.isFalse(FormValidator.validateEmail(email), "email non è valido");
                CheckErrorUtils.isFalse(!FormValidator.checkEmailRepeat(email, userDAO), "email non esiste");

                User user = null;
                try
                {
                        user = userDAO.getByEmail(email);
                        user.setResetPwdEmailLink(UUID.randomUUID().toString());
                        userDAO.update(user);

                        emailFactory.sendEmailOfRestPassword(user, request);

                }
                catch (DAOException ex)
                {

                        throw new ServletException(ex.getMessage(), ex);
                }

                catch (MessagingException ex)
                {
                        throw new ServletException("errore durente la creazione e l'invio del email per la registrazione", ex);
                }
                catch (UnsupportedEncodingException ex)
                {
                        throw new ServletException("errore durente la codifica dei caratteri", ex);
                }

                //ritorna alla pagina di login
                String prevUrl = getServletContext().getContextPath() + "/login?notice=awaitingResetPassword";
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }
}
