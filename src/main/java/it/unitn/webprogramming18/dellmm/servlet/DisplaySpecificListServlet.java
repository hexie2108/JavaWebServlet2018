/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlet;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luca_morgese
 */
public class DisplaySpecificListServlet extends HttpServlet {

    private ListDAO listDAO;
    private ProductDAO productDAO;
    private PermissionDAO permissionDAO;
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
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
            categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get DAOs for user storage system", ex);
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
            
            //Obtains listId from parameter on URL
            int listId = Integer.parseInt(request.getParameter("listId"));
            
            //Obtains ListBean with listId
            it.unitn.webprogramming18.dellmm.javaBeans.List list;
            try {
                list = listDAO.getByPrimaryKey(listId);
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get the list with specified ID");
            }
            
            //Gets all products on list
            List<Product> products;
            try {
                products = productDAO.getProductsInListByListId(listId);
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get list products");
            }
            
            //Gets set of permissions associated to the list
            List<Permission> permissions;
            try {
                permissions = permissionDAO.getPermissionsOnListByListId(listId);
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get permissions set of list");
            }
            
            //Gets the category object of the list
            CategoryList categoryList;
            try {
                categoryList = categoryListDAO.getByPrimaryKey(listId);
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get list category");
            }
            
            request.setAttribute("list", list);
            request.setAttribute("products", products);
            request.setAttribute("permissions", permissions);
            request.setAttribute("categoryList", categoryList);
            
            request.getRequestDispatcher("/WEB-INF/jsp/yourList").forward(request, response);
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Displays comprehensive info about a list";
    }// </editor-fold>

}
