package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

                //get i parametri necessari
                String email = request.getParameter(FormValidator.EMAIL_KEY);
                String verifyEmailLink = request.getParameter("verifyEmailLink");
                
                CheckErrorUtils.isNull(email, "il parametro email è nullo");
                CheckErrorUtils.isNull(verifyEmailLink, "il parametro verifyEmailLink è nullo");
                
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
