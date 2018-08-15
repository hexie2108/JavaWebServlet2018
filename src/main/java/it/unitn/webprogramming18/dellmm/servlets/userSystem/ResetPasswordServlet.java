package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * visualizza la pagina per reset password
 *
 * @author mikuc
 */

public class ResetPasswordServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/resetPassword.jsp";
        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {
                userDAO = new JDBCUserDAO();
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //get i parametri necessari
                String email = request.getParameter(FormValidator.EMAIL_KEY);
                String resetPwdLink = request.getParameter("resetPwdLink");

                CheckErrorUtils.isNull(email, "il parametro email è nullo");
                CheckErrorUtils.isNull(resetPwdLink, "il parametro resetPwdLink è nullo");

                try
                {
                        //se resetPwdLink è valido
                        if (userDAO.checkUserByEmailAndResetPwdLink(email, resetPwdLink))
                        {

                                //set il titolo della pagina
                                request.setAttribute(ConstantsUtils.HEAD_TITLE, "reset password");
                                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
                        }
                        //se non è valido
                        else
                        {
                                //ritorna alla pagina di login
                                String result = "resetLinkInvalid";
                                String prevUrl = getServletContext().getContextPath() + "/login?notice=" + result;
                                response.sendRedirect(response.encodeRedirectURL(prevUrl));
                        }
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

        }

}
