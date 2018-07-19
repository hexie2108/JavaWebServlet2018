
package it.unitn.webprogramming18.dellmm.servlet.another;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *aggiornare la lista
 * @author luca_morgese
 */
public class AddUpdateListServlet extends HttpServlet
{

        private ListDAO listDAO;

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
                        listDAO = daoFactory.getDAO(ListDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get ListDAO for user storage system", ex);
                }
        }

        /**
         * Handles the HTTP <code>POST</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                //get la sessione attuale dell'utente, senza creare una nuova
                HttpSession session = request.getSession(false);
                //get istanza user dalla sessione
                User user = (User) session.getAttribute("user");
                //se utente non ha sessione o non ha loggato
                if (session == null || user == null)
                {
                        //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                }
                //se è un utente loggato
                else
                {
                        //get i parametri neccessari per aggiornare la lista
                        Integer listId = null;
                        listId = Integer.valueOf(request.getParameter("listId"));
                        Integer ownerId = user.getId();
                        Integer categoryList = Integer.valueOf(request.getParameter("categoryList"));

                        String name = request.getParameter("name");
                        String description = request.getParameter("description");
                        String img = request.getParameter("img");

                        
                        
                        try
                        {
                                
                                ShoppingList list = new ShoppingList();

                                list.setId(listId);
                                list.setName(name);
                                list.setDescription(description);
                                list.setImg(img);
                                list.setOwnerId(ownerId);
                                list.setCategoryList(categoryList);

                                //se lista ha già un id, è un inserimento
                                if (listId == null)
                                {
                                        listDAO.insert(list);
                                }
                                //altrimenti è un aggiornamento della lista
                                else
                                {
                                        listDAO.update(list);
                                }
                        }
                        catch (DAOException ex)
                        {
                                ex.printStackTrace();
                                throw new ServletException("Impossible to update or create new list");
                        }

                        response.sendRedirect("/WEB-INF/jsp/yourHome.jsp");
                }
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo()
        {
                return "Servlet that gets info from form to update or create a new list";
        }

}
