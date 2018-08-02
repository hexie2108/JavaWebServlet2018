/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

/**
 * filtro che pre elabrora i dati della lista per utente loggato nella pagina home, search e categoria
 * @author mikuc
 */
public class FrontPagePrintListFilter implements Filter
{

        private ProductDAO productDAO;
        private ListDAO listDAO;
        private PermissionDAO permissionDAO;

        /**
         * Init method for this filter
         *
         * @param filterConfig
         */
        @Override
        public void init(FilterConfig filterConfig)
        {
                listDAO = new JDBCListDAO();
                permissionDAO = new JDBCPermissionDAO();
                productDAO = new JDBCProductDAO();
        }

        /**
         *
         * @param request The servlet request we are processing
         * @param response The servlet response we are creating
         * @param chain The filter chain we are processing
         *
         * @exception IOException if an input/output error occurs
         * @exception ServletException if a servlet error occurs
         */
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {

                User user = (User) ((HttpServletRequest) request).getSession().getAttribute("user");
                //se utente è loggato
                if (user != null)
                {
                        getAllAddableListByUserId(request, user.getId());

                        getAllMyList(request, user.getId());

                        Integer listId = (Integer) ((HttpServletRequest) request).getSession().getAttribute("myListId");
                        //se utente ha almeno una lista 
                        if (listId != null)
                        {
                                getMyListPermission(request, user.getId(), listId);
                                getProductsOfMyList(request, listId);
                        }

                }
                
          

                chain.doFilter(request, response);

        }

        /**
         * dato request e userId, memorizza la lista delle shopping list
         * aggiungibile dall'utente
         *
         * @param request richiesta
         * @param userId id utente
         * @throws ServletException
         */
        public void getAllAddableListByUserId(ServletRequest request, int userId) throws ServletException
        {

                List<ShoppingList> listOfShoopingList;
                try
                {
                        listOfShoopingList = listDAO.getAllAddableListByUserId(userId);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //se possiede qualche lista con il permesso di aggiungere il prodotto
                if (listOfShoopingList != null && listOfShoopingList.size() > 0)
                {
                        request.setAttribute("addbleLists", listOfShoopingList);
                }

        }

        /**
         * dato lista id e utente id, ritorna il permesso dell'utente sulla
         * lista corrente lita
         *
         * @param request
         * @param userId
         * @param listId
         */
        public void getMyListPermission(ServletRequest request, int userId, int listId) throws ServletException
        {
                Permission permission = null;
                try
                {
                        //get il permesso sulla lista
                        permission = permissionDAO.getUserPermissionOnListByIds(userId, listId);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                if (permission != null)
                {
                        request.setAttribute("MylistPermission", permission);
                }

        }

        /**
         * datto id lista , set la lista del prodotto in richiesta
         *
         * @param request richiesta
         * @param list id lista
         */
        public void getProductsOfMyList(ServletRequest request, int listId) throws ServletException
        {
                List<Product> productsOfMyList = null;
                List<Product> productsBoughtOfMyList = null;

                try
                {
                        //combina la lista di prodotto ancora da comprare e la lista di prodotto già comprato
                        productsOfMyList = productDAO.getProductsNotBuyInListByListId(listId);
                        productsBoughtOfMyList = productDAO.getProductsBoughtInListByListId(listId);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                if (productsOfMyList != null && productsOfMyList.size() > 0)
                {
                        request.setAttribute("productsOfMyList", productsOfMyList);
                }
                if (productsBoughtOfMyList != null && productsBoughtOfMyList.size() > 0)
                {
                        request.setAttribute("productsBoughtOfMyList", productsBoughtOfMyList);
                }

        }

        /**
         * get la lista di shopping lista manipolabile da utente specificato
         *
         * @param request richiesta
         * @param userId id utente
         */
        public void getAllMyList(ServletRequest request, int userId) throws ServletException
        {
                List<ShoppingList> allMyList = null;
                try
                {
                        //ottiene tutte liste dell'utente
                        allMyList = listDAO.getAllListByUserId(userId);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //se possiede qualche liste
                if (allMyList != null && allMyList.size() > 0)
                {

                        //memoriazza tutte le liste manipolabile
                        request.setAttribute("allMyList", allMyList);

                        //se non è ancora selezionata una lista nella sessione, si seleziona la prima lista come default
                        if (((HttpServletRequest) request).getSession().getAttribute("myListId") == null)
                        {
                                //memorizza id della prima lista come la liste corrente
                                ((HttpServletRequest)request).getSession().setAttribute("myListId", allMyList.get(0).getId());
                        }

                }
        }

        /**
         * Destroy method for this filter
         */
        @Override
        public void destroy()
        {
        }

}
