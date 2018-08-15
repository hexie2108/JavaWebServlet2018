package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * filtro che pre elabrora i dati della lista per utente loggato nella pagina
 * front-end , home, search e categoria
 *
 * @author mikuc
 */
public class FrontPagePrintListFilter implements Filter {

    private ProductDAO productDAO;
    private ListDAO listDAO;
    private PermissionDAO permissionDAO;

    /**
     * Inizializza le classi DAO
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        productDAO = new JDBCProductDAO();
        listDAO = new JDBCListDAO();
        permissionDAO = new JDBCPermissionDAO();
    }

    /**
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        User user = (User) ((HttpServletRequest) request).getSession().getAttribute("user");

        //se utente è loggato
        if (user != null) {
            //set la lista delle liste spese, aggiungibile
            getAllAddableListByUserId(request, user.getId());
            //set la lista di tutte le liste spese, visualizzabile
            getAllMyList(request, user.getId());
            //get la id lista preferita
            Integer listId = (Integer) ((HttpServletRequest) request).getSession().getAttribute("myListId");
            //se non è nullo
            if (listId != null) {
                //get e set il permesso su tale lista come attributo della richiesta
                getMyListPermission(request, user.getId(), listId);
                //get e set la lista di prodotto della lista come attributo della rihciesta
                getProductsOfMyList(request, listId);
            }

        }

        chain.doFilter(request, response);

    }

    /**
     * get e set la lista delle liste spese come attributo della richiesta,
     * in cui utente può inserire elemento nella lista,
     *
     * @param request richiesta
     * @param userId  id utente
     * @throws ServletException
     */
    public void getAllAddableListByUserId(ServletRequest request, int userId) throws ServletException {

        List<ShoppingList> listOfShoopingList;
        try {
            listOfShoopingList = listDAO.getAllAddableListByUserId(userId);
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }
        //se possiede almeno una lista in cui utente può inserire elemento,
        if (listOfShoopingList.size() > 0) {
            request.setAttribute("addbleLists", listOfShoopingList);
        }

    }

    /**
     * get e set la lista delle liste spese come attributo della richiesta,
     * in cui utente ha alcun permesso sulla lista,
     *
     * @param request richiesta
     * @param userId  id utente
     * @throws javax.servlet.ServletException
     */
    public void getAllMyList(ServletRequest request, int userId) throws ServletException {

        List<ShoppingList> allMyList = null;
        try {
            //ottiene tutte liste dell'utente
            allMyList = listDAO.getAllListByUserId(userId);
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }
        //se possiede qualche liste
        if (allMyList.size() > 0) {

            //memoriazza tutte le liste manipolabile
            request.setAttribute("allMyList", allMyList);

            //se non c'è ancora una lista preferita nella sessione
            if (((HttpServletRequest) request).getSession().getAttribute("myListId") == null) {
                //si seleziona la prima lista come preferita
                ((HttpServletRequest) request).getSession().setAttribute("myListId", allMyList.get(0).getId());
            }

        }
    }

    /**
     * get e set il permesso della lista come attributo della richiesta
     *
     * @param request
     * @param userId
     * @param listId
     */
    public void getMyListPermission(ServletRequest request, int userId, int listId) throws ServletException {
        Permission permission = null;
        try {
            //get il permesso sulla lista
            permission = permissionDAO.getUserPermissionOnListByIds(userId, listId);
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }
        if (permission != null) {
            request.setAttribute("MylistPermission", permission);
        }

    }

    /**
     * get e set la lista del prodotto ancora da comrare e del prodotto già comprato della lista spesa come attributo della richiesta
     *
     * @param request richiesta
     * @param list    id lista
     */
    public void getProductsOfMyList(ServletRequest request, int listId) throws ServletException {
        List<Product> productsOfMyList = null;
        List<Product> productsBoughtOfMyList = null;

        try {
            //la lista di prodotto ancora da comprare
            productsOfMyList = productDAO.getProductsNotBuyInListByListId(listId);
            //la lista di prodotto già comprato
            productsBoughtOfMyList = productDAO.getProductsBoughtInListByListId(listId);
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }
        //se ci sono i prodotto ancora da comprare
        if (productsOfMyList.size() > 0) {
            request.setAttribute("productsOfMyList", productsOfMyList);
        }
        //se ci sono i prodotto già comprato
        if (productsBoughtOfMyList.size() > 0) {
            request.setAttribute("productsBoughtOfMyList", productsBoughtOfMyList);
        }

    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

}
