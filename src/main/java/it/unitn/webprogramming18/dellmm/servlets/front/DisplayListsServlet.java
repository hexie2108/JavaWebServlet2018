package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
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

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/mylists.jsp";

        private ListDAO listDAO;
        private ProductDAO productDAO;
        private PermissionDAO permissionDAO;
        private CommentDAO commentDAO;

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
                        productDAO = daoFactory.getDAO(ProductDAO.class);
                        permissionDAO = daoFactory.getDAO(PermissionDAO.class);
                        commentDAO = daoFactory.getDAO(CommentDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get ListDAO or ProductDAO or PermissionDAO or CommendDAO for user storage system", ex);
                }

        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                HttpSession session = request.getSession(false);
                User user = (User) session.getAttribute("user");
                

                //get le liste di prodotto per ogni lista che possiede
                List<ShoppingList> ownedLists;
                List<ShoppingList> sharedLists;
                try
                {
                        ownedLists = listDAO.getOwnedUserListsByUserId(user.getId());
                        sharedLists = listDAO.getSharedWithUserListsByUserId(user.getId());
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //associando ogni lista una hashmap
                //se è una lista owner, verrà associato: la lista di prodotto ancora da comprare, la lista di prodotto già comprato, la lista di permesso sharing , il numero di sharing, il numero di commento
                //se è una lista condivisa , verrà associato: la lista di prodotto ancora da comprare, la lista di prodotto già comprato, il permesso su tale lista, il numero di commento
                HashMap<ShoppingList, HashMap<String, Object>> completeOwnedLists = new HashMap<>();
                HashMap<ShoppingList, HashMap<String, Object>> completeSharedLists = new HashMap<>();
                HashMap<String, Object> singleCompleteList = null;
                List<Product> productsList = null;

                //per ogni lista owner
                for (ShoppingList shoppingList : ownedLists)
                {
                        singleCompleteList = new HashMap<>();

                        try
                        {
                                //get la lista di prodotto ancora da comprare
                                productsList = productDAO.getProductsNotBuyInListByListId(shoppingList.getId());
                                singleCompleteList.put("productsListNotBuy", productsList);

                                //get la lista di prodotto già comprato
                                productsList = productDAO.getProductsBoughtInListByListId(shoppingList.getId());
                                singleCompleteList.put("productsListBought", productsList);

                                //get numero di condivisione
                                singleCompleteList.put("numberOfShared", permissionDAO.getNumberSharedByListId(shoppingList.getId()));
                                //get numero di commento
                                singleCompleteList.put("numberComment", commentDAO.getNumberOfCommentsByListId(shoppingList.getId()));

                                //associando tale map locale con la lista nella map globale
                                completeOwnedLists.put(shoppingList, singleCompleteList);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                }

                //per ogni lista condivisa
                for (ShoppingList shoppingList : sharedLists)
                {
                        singleCompleteList = new HashMap<>();

                        try
                        {
                                //get la lista di prodotto ancora da comprare
                                productsList = productDAO.getProductsNotBuyInListByListId(shoppingList.getId());
                                singleCompleteList.put("productsListNotBuy", productsList);

                                //get la lista di prodotto già comprato
                                productsList = productDAO.getProductsBoughtInListByListId(shoppingList.getId());
                                singleCompleteList.put("productsListBought", productsList);

                                //get numero di commento
                                singleCompleteList.put("numberComment", commentDAO.getNumberOfCommentsByListId(shoppingList.getId()));
                                //get l'oggetto permesso
                                singleCompleteList.put("permission", permissionDAO.getUserPermissionOnListByIds(user.getId(), shoppingList.getId()));

                                //associando tale map locale con la lista nella map globale
                                completeSharedLists.put(shoppingList, singleCompleteList);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                }

                //set titolo della pagina
                ResourceBundle bundle = i18n.getBundle(request);
                request.setAttribute(ConstantsUtils.HEAD_TITLE, bundle.getString("frontPage.title.myLists"));

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

                //inoltra a jsp
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);

        }

}
