/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlet.service;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCLogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mikuc
 */
public class ChangeListService extends HttpServlet
{

        private PermissionDAO permissionDAO;

        @Override
        public void init() throws ServletException
        {

                permissionDAO = new JDBCPermissionDAO();
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                doGet(request, response);

        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //get id lista
                String listId = null;
                listId = request.getParameter("listId");

                if (listId == null)
                {
                        throw new ServletException("manca il parametro action o  id del prodotto o id della lista da aggiungere");
                }

                //get user corrente
                User user = (User) request.getSession().getAttribute("user");
                //se non hai loggato
                if (user == null)
                {
                        throw new ServletException("non sei loggato");
                }
                //get permesso dell'utente su tale lista
                Permission permission;
                try
                {
                        permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //se il permesso è  vuoto
                if (permission == null)
                {
                        throw new ServletException("non hai nessun permesso su tale lista");
                }
                
                //memorizza  id della nuova lista 
                request.getSession().setAttribute("myListId", Integer.parseInt(listId));
                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
                
                
        }

}
