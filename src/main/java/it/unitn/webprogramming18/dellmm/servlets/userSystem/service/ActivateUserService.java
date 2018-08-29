package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * il servizio per attivare account
 *
 * @author mikuc
 */
public class ActivateUserService extends HttpServlet
{

        private UserDAO userDAO;

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
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            //Language bundle
            ResourceBundle rb = i18n.getBundle(request);

            //get i parametri necessari
            String email = request.getParameter(FormValidator.EMAIL_KEY);
            String verifyEmailLink = request.getParameter("verifyEmailLink");

            if(email == null){
                ServletUtility.sendError(request, response, 400, rb.getString("validateUser.errors.EMAIL_MISSING")); //il parametro email è nullo
                return;
            }
            if(verifyEmailLink == null){
                ServletUtility.sendError(request, response, 400, rb.getString("validateUser.errors.VERIFY_EMAIL_MISSING")); //il parametro verifyEmailLink è nullo
                return;
            }

            boolean flag;
            try
            {
                flag = userDAO.activateUserByEmailAndVerifyLink(email, verifyEmailLink);
            }
            catch (DAOException ex)
            {
                throw new ServletException(ex.getMessage(), ex);
            }

            String result;
            //se user è satao attivato correttamente
            if(flag)
            {
                result="activatedOK";
            }
            else{
                result = "activatedFAIL";
            }

            //ritorna alla pagina di login
            String prevUrl = getServletContext().getContextPath() + "/login?notice="+result;
            response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }

}
