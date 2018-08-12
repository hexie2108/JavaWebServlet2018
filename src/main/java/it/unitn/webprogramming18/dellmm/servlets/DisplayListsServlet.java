/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author luca_morgese
 */
public class DisplayListsServlet extends HttpServlet {

    private ListDAO listDAO;
    private ProductDAO productDAO;
    private CategoryListDAO categoryListDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            listDAO = daoFactory.getDAO(ListDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
            categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get db factory for user storage system", ex);
        }
    }


    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {

            int userId = user.getId();
            java.util.List<it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList> ownedLists;
            java.util.List<it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList> sharedLists;

            //Gets all lists
            try {
                ownedLists = listDAO.getOwnedUserListsByUserId(userId);
                sharedLists = listDAO.getSharedWithUserListsByUserId(userId);
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get the user's lists");
            }

            //For each owned list, gets its products and puts both in a vector <List, Product>
            //Structure: HashMap<listBean, List<productBean>>
            HashMap<it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList, java.util.List<Product>> completeOwnedLists
                    = (HashMap<it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList, java.util.List<Product>>) new HashMap();

            for (it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList l : ownedLists) {
                java.util.List<Product> products = new ArrayList();
                try {
                    products = productDAO.getProductsInListByListId(l.getId());
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Impossible to get products in list " + l.getId());
                }
                completeOwnedLists.put(l, products);
            }


            //Does the exact same thing with shared with user lists
            //Structure: HashMap<listBean, List<productBean>>
            HashMap<it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList, java.util.List<Product>> completeSharedLists
                    = (HashMap<it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList, java.util.List<Product>>) new HashMap();

            for (it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList l : sharedLists) {
                java.util.List<Product> products = new ArrayList();
                try {
                    products = productDAO.getProductsInListByListId(l.getId());
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Impossible to get products in list " + l.getId());
                }
                completeSharedLists.put(l, products);
            }

            //Utility attribute to possibly display category name for each category
            request.setAttribute("categoryListDAO", categoryListDAO);

            request.setAttribute("ownedLists", completeOwnedLists);
            request.setAttribute("sharedLists", completeOwnedLists);

            request.getRequestDispatcher("/WEB-INF/jsp/yourHome.jsp").forward(request, response);

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for displaying user specific lists in format List -> products in it";
    }// </editor-fold>

}
