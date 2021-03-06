/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
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
public class DisplaySpecificListServlet extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/mylist.jsp";

    private ListDAO listDAO;
    private ProductDAO productDAO;
    private PermissionDAO permissionDAO;
    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            listDAO = daoFactory.getDAO(ListDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
            commentDAO = daoFactory.getDAO(CommentDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ListDAO or ProductDAO or PermissionDAO or CommendDAO for user storage system", ex);
        }
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
                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                //ottiene listid
                String listIdInString = request.getParameter("listId");
                //se listid è nullo
                if(listIdInString == null) {
                    ServletUtility.sendError(request, response, 400, "error.missingListId");
                    return;
                }

        //trasforma listid in intero
        int listId = Integer.parseInt(listIdInString);

                ShoppingList shoppingList = null;
                List<Product> listProductsNotBuy = null;
                List<Product> listProductsBought = null;
                //la lista dei commenti
                List<Comment> listComment = null;
                //il numero di commenti
                Integer numberOfComment = null;
                //permesso dell'utente attuale su tale lista
                Permission userPermissionsOnList = null;
                //lista di permesso su tale lista
                List<Permission> generalPermissionsOnList = null;
                try
                {
                        //get istanza della lista attuale
                        shoppingList = listDAO.getByPrimaryKey(listId);
                        //get lista di prodotto ancora da comprare
                        listProductsNotBuy = productDAO.getProductsNotBuyInListByListId(listId);
                        //get lista di prodtto già comprato
                        listProductsBought = productDAO.getProductsBoughtInListByListId(listId);
                        //get lista del commento
                        listComment = commentDAO.getCommentsOnListByListId2(listId);
                        //get numero del commento
                        numberOfComment = commentDAO.getNumberOfCommentsByListId(listId);
                        //get il permesso dell'utente attuale su tale lista
                        userPermissionsOnList = permissionDAO.getUserPermissionOnListByIds(user.getId(), listId);


                        if (userPermissionsOnList == null)
                        {
                                ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList");
                                return;
                        }

                        //in caso che utente è il proprietario
                        if (shoppingList.getOwnerId() == user.getId())
                        {
                                //get la lista di permesso su tale lista spesa
                                generalPermissionsOnList = permissionDAO.getSharingPermissionsOnListByListId(listId, user.getId());
                        }

        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

                //set titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, shoppingList.getName());
                //set l'istanza lista come l'attributo della richiesta
                request.setAttribute("list", shoppingList);
                //set il permesso dell'utente  come l'attributo della richiesta
                request.setAttribute("userPermissionsOnList", userPermissionsOnList);

        //se lista non è vuoto
        if (listProductsNotBuy.size() > 0) {
            //set lista di prodotto ancora da comprare come l'attributo della richiesta
            request.setAttribute("listProductsNotBuy", listProductsNotBuy);
        }

        //se lista non è vuoto
        if (listProductsBought.size() > 0) {
            //set lista di prodotto già comprato come l'attributo della richiesta
            request.setAttribute("listProductsBought", listProductsBought);
        }

        //se lista non è vuoto
        if (listComment.size() > 0) {
            //set lista di commento e il numero di commento come l'attributo della richiesta
            request.setAttribute("listComment", listComment);
            request.setAttribute("numberOfComment", numberOfComment);
        }

        //se lista non è vuoto
        if (generalPermissionsOnList != null && generalPermissionsOnList.size() > 0) {
            //set lista di permesso come l'attributo della richiesta
            request.setAttribute("generalPermissionsOnList", generalPermissionsOnList);
        }

        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
    }

}
