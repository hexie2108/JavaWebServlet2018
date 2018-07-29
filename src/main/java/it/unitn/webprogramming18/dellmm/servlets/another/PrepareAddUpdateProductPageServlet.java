/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.another;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luca_morgese
 */
public class PrepareAddUpdateProductPageServlet extends HttpServlet {

    private CategoryProductDAO categoryProductDAO;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get categoryProductDAO for user storage system", ex);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            
            //Check on permission to modify list if a modify action has been selected
            //Assumed existing request parameters set by front end servlet-invoking jsp are being used
            //Could be done using only listId, without flags, but they could come in handy in certain contexts.
            Boolean modify = Boolean.valueOf(request.getParameter("modifyProductFlag"));
            Boolean create = Boolean.valueOf(request.getParameter("createProductFlag"));
            
            Integer listId = Integer.valueOf(request.getParameter("listId"));
            
            if (!user.isIsAdmin()) {
                //it means a std user is accessing AddUpdateProductPage
                //So a std user cannot modify a product, and can access products only via "add product to list" action
                if (listId == null || modify == true) {
                    //Only admins can modify products
                    request.getRequestDispatcher("/WEB-INF/jsp/nonAuthorizedActionErrorPage").forward(request, response);  
                }
            }
            //End check
            
            //Gets the list of all CategoryList
            java.util.List<CategoryProduct> productCategories;
            try {
                productCategories = categoryProductDAO.getAll();
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get the product categories");
            }
            
            request.setAttribute("productCategories", productCategories);
            request.getRequestDispatcher("/WEB-INF/jsp/addUpdateProduct.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that prepares data for addUpdateProductPage and checks user permission to access such page";
    }

}
