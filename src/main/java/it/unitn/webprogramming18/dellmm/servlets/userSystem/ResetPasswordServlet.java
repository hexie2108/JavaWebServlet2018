package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
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
 * visualizza la pagina per reset password
 *
 * @author mikuc
 */
public class ResetPasswordServlet extends HttpServlet
{

        public static final String ID_KEY = "id";

        public static final String MSG_KEY = "message";

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/resetPassword.jsp";

        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {
                DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
                if (daoFactory == null)
                {
                        throw new ServletException("Impossible to get db factory for user storage system");
                }

                try
                {
                        userDAO = daoFactory.getDAO(UserDAO.class);
                }
                catch (DAOFactoryException ex)
                {
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
                String resetPwdLink = request.getParameter("resetPwdLink");

                if (email == null) {
                    ServletUtility.sendError(request, response, 400,  "validateUser.errors.EMAIL_MISSING");
                    return;
                }
                if (resetPwdLink == null) {
                    ServletUtility.sendError(request, response, 400,  "validateUser.errors.RESETPWD_PARAMETER_MISSING");
                    return;
                }

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
