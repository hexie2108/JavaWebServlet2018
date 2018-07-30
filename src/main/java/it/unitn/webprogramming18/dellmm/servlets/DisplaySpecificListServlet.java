/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * visualizza una lista specificato datto id lista
 *
 * @author luca_morgese
 */
public class DisplaySpecificListServlet extends HttpServlet
{

        private ListDAO listDAO;
        private ProductDAO productDAO;
        private PermissionDAO permissionDAO;
        private CategoryListDAO categoryListDAO;
        private CommentDAO commentDAO;

        @Override
        public void init() throws ServletException
        {

                listDAO = new JDBCListDAO();
                productDAO = new JDBCProductDAO();
                permissionDAO = new JDBCPermissionDAO();
                categoryListDAO = new JDBCCategoryListDAO();
                commentDAO = new JDBCCommentDAO();

        }

        /**
         * Handles the HTTP <code>GET</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                HttpSession session = request.getSession(false);
                User user = (User) session.getAttribute("user");

                //Obtains listId from parameter on URL
                String listIdInString = request.getParameter("listId");
                if (listIdInString == null)
                {
                        throw new ServletException("manca il parametro id della lista");
                }
                int listId = Integer.parseInt(listIdInString);

                //Obtains ListBean with listId
                ShoppingList shoppingList = null;
                //Gets all products on list
                List<Product> listProductsNotBuy = null;
                List<Product> listProductsBought = null;
                //la lista dei commenti
                List<Comment> listComment = null;
                
                //permesso dell'utente su tale lista
                Permission userPermissionsOnList = null;
                //lista di permesso su tale lista
                List<Permission> generalPermissionsOnList = null;
                try
                {
                        shoppingList = listDAO.getByPrimaryKey(listId);
                        listProductsNotBuy = productDAO.getProductsNotBuyInListByListId(listId);
                        listProductsBought = productDAO.getProductsBoughtInListByListId(listId);
                        listComment = commentDAO.getCommentsOnListByListId2(listId);
                        userPermissionsOnList = permissionDAO.getUserPermissionOnListByIds(user.getId(), listId);
                        if (userPermissionsOnList == null)
                        {
                                throw new ServletException("non hai nessun permesso su questa lista");
                        }
                        //in caso sono proprietario
                        if (shoppingList.getOwnerId() == user.getId())
                        {
                                generalPermissionsOnList = permissionDAO.getPermissionsOnListByListId(listId);
                        }

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                request.setAttribute("list", shoppingList);
                if (listProductsNotBuy.size() > 0)
                {
                        request.setAttribute("listProductsNotBuy", listProductsNotBuy);
                }
                if (listProductsBought.size() > 0)
                {
                        request.setAttribute("listProductsBought", listProductsBought);
                }
                if (listComment.size() > 0)
                {
                        request.setAttribute("listComment", listComment);
                }
                if (generalPermissionsOnList != null)
                {
                        request.setAttribute("generalPermissionsOnList", generalPermissionsOnList);
                }
                if (userPermissionsOnList != null)
                {
                        request.setAttribute("userPermissionsOnList", userPermissionsOnList);
                }

                request.getRequestDispatcher("/WEB-INF/jsp/mylist.jsp").forward(request, response);
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
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo()
        {
                return "Displays comprehensive info about a list";
        }// </editor-fold>

}
