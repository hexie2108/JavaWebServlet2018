package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * il servizio per accettare privacy
 *
 * @author mikuc
 */
public class AcceptPrivacyService extends HttpServlet
{

        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {

                userDAO = new JDBCUserDAO();

        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //get user, accetta privacy
                User user = (User) request.getSession().getAttribute("user");
                user.setAcceptedPrivacy(true);

                try
                {
                        //aggiorna in db
                        userDAO.update(user);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if (prevUrl == null)
                {
                        //ritorna alla pagina di login
                        prevUrl = getServletContext().getContextPath();
                }
                
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }

}
