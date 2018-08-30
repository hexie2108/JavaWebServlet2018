package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
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
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
                throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
                userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
                throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }

        emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
        if (emailFactory == null)
        {
                throw new ServletException("Impossible to get email factory for email system");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        //Language bundle
        ResourceBundle rb = i18n.getBundle(request);   
        
        String email = request.getParameter(FormValidator.EMAIL_KEY);
        if(!FormValidator.validateEmail(email)) {
            ServletUtility.sendError(request, response, 400, "validateUser.errors.EMAIL_NOT_VALID");
            return;
        } //"email non è valido");
        if(!(!FormValidator.checkEmailRepeat(email, userDAO))) {
            ServletUtility.sendError(request, response, 400, "validateUser.errors.EMAIL_NOT_EXISTING");
            return;            
        }//, "email non esiste");

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
            throw new ServletException(rb.getString("errors.createAndSendEmail"), ex);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new ServletException(rb.getString("errros.noSuchAlgorithmMD5"), ex);
        }

        //ritorna alla pagina di login
        String prevUrl = getServletContext().getContextPath() + "/login?notice=awaitingResetPassword";
        response.sendRedirect(response.encodeRedirectURL(prevUrl));
    }
}
