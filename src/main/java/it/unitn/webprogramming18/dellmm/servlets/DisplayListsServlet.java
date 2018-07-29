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
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * visualizza tutte le liste possiedute dall'utente
 *
 * @author luca_morgese
 */
public class DisplayListsServlet extends HttpServlet
{

        private ListDAO listDAO;
        private ProductDAO productDAO;
        private CategoryListDAO categoryListDAO;
        private PermissionDAO permissionDAO;
        private CommentDAO commentDAO;

        @Override
        public void init()
        {

                listDAO = new JDBCListDAO();
                productDAO = new JDBCProductDAO();
                categoryListDAO = new JDBCCategoryListDAO();
                permissionDAO = new JDBCPermissionDAO();
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
                //se non è un utente loggato
                if (session == null || user == null)
                {
                        //rindirizza alla pagina di login
                        String loginUrl = request.getContextPath() + "/login";
                        response.sendRedirect(response.encodeRedirectURL(loginUrl));
                }
                else
                {
                        //get le liste di prodotto per ogni lista che possiede
                        int userId = user.getId();
                        List<ShoppingList> ownedLists;
                        List<ShoppingList> sharedLists;
                        try
                        {
                                ownedLists = listDAO.getOwnedUserListsByUserId(userId);
                                sharedLists = listDAO.getSharedWithUserListsByUserId(userId);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }

                        //associando ogni lista una hashmap
                        //se è una lista owner, verrà associato: la lista di prodotto, il numero di sharing, il numero di commento
                        //se è una lista condivisa , verrà associato: la lista di prodotto, il permesso su tale lista, il numero di commento
                        HashMap<ShoppingList, HashMap<String, Object>> completeOwnedLists = new HashMap();
                        HashMap<ShoppingList, HashMap<String, Object>> completeSharedLists = new HashMap();
                        HashMap<String, Object> singleCompleteList = null;
                        List<Product> productsList = null;

                        for (ShoppingList shoppingList : ownedLists)
                        {
                                singleCompleteList = new HashMap();

                                try
                                {
                                        //get la lista di prodotto
                                        productsList = productDAO.getProductsNotBuyInListByListId(shoppingList.getId());
                                        singleCompleteList.put("productsListNotBuy", productsList);
                                        productsList = productDAO.getProductsBoughtInListByListId(shoppingList.getId());
                                        singleCompleteList.put("productsListBought", productsList);
                                        //get numero di condivisione
                                        singleCompleteList.put("numberOfShared", permissionDAO.getNumberSharedByListId(shoppingList.getId()));
                                        //get numero di commento
                                        singleCompleteList.put("numberComment", commentDAO.getNumberOfCommentsByListId(shoppingList.getId()));
                                        //aggiunge map locale nella map globale
                                        completeOwnedLists.put(shoppingList, singleCompleteList);
                                }
                                catch (DAOException ex)
                                {
                                        throw new ServletException(ex.getMessage(), ex);
                                }
                        }

                        for (ShoppingList shoppingList : sharedLists)
                        {
                                singleCompleteList = new HashMap();

                                try
                                {
                                        //get la lista di prodotto
                                        productsList = productDAO.getProductsNotBuyInListByListId(shoppingList.getId());
                                        singleCompleteList.put("productsListNotBuy", productsList);
                                        productsList = productDAO.getProductsBoughtInListByListId(shoppingList.getId());
                                        singleCompleteList.put("productsListBought", productsList);

                                        //get numero di commento
                                        singleCompleteList.put("numberComment", commentDAO.getNumberOfCommentsByListId(shoppingList.getId()));

                                        //get l'oggetto permesso
                                        singleCompleteList.put("permission", permissionDAO.getUserPermissionOnListByIds(userId, shoppingList.getId()));

                                        //aggiunge map locale nella map globale
                                        completeSharedLists.put(shoppingList, singleCompleteList);
                                }
                                catch (DAOException ex)
                                {
                                        throw new ServletException(ex.getMessage(), ex);
                                }
                        }

                        // se ha almeno una lista owner
                        if (ownedLists.size() > 0)
                        {
                                
                                request.setAttribute("ownedLists", ownedLists);
                                request.setAttribute("ownedListsMap", completeOwnedLists);
                        }
                        // se ha almeno una lista condivisa
                        if (sharedLists.size() > 0)
                        {
                                request.setAttribute("sharedLists", sharedLists);            
                                request.setAttribute("sharedListsMap", completeSharedLists);
                        }
                        //set titolo della pagina
                        request.setAttribute("head_title", "le mie liste");

                        //inoltra a jsp
                        request.getRequestDispatcher("/WEB-INF/jsp/mylists.jsp").forward(request, response);

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
                return "Servlet for displaying user specific lists in format List -> products in it";
        }// </editor-fold>

}
