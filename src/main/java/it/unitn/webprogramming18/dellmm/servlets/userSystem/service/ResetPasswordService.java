package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.MD5Utils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ResetPasswordService extends HttpServlet
{

        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {
                userDAO = new JDBCUserDAO();
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String result=null;
                //get i parametri necessari
                String email = request.getParameter(FormValidator.EMAIL_KEY);
                String resetPwdLink = request.getParameter("resetPwdLink");
                String password = request.getParameter(FormValidator.FIRST_PWD_KEY);
                CheckErrorUtils.isNull(email, "il parametro email è nullo");
                CheckErrorUtils.isNull(resetPwdLink, "il parametro resetPwdLink è nullo");
                CheckErrorUtils.isFalse(FormValidator.validatePassword(password), "la password non è valido");

                try
                {
                        //se resetPwdLink è valido
                        if (userDAO.checkUserByEmailAndResetPwdLink(email, resetPwdLink))
                        {
                                //get user
                                User user = userDAO.getByEmail(email);
                                //set nuovo password
                                user.setPassword(MD5Utils.getMD5(password));
                                //cancella resetLink
                                user.setResetPwdEmailLink(null);
                                //aggiorna utente
                                userDAO.update(user);
                                result = "resetPasswordOK";
                        }
                        //se non è valido
                        else
                        {
                                //ritorna alla pagina di login
                                result = "resetLinkInvalid";

                        }
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                catch (NoSuchAlgorithmException ex)
                {
                        throw new ServletException("errore per la mancanza dell'algoritmo MD5 in ambiente di esecuzione", ex);
                }

                String prevUrl = getServletContext().getContextPath() + "/login?notice=" + result;
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }
}
