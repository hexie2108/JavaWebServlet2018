/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlet;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * SOME STUFF (LINKS, ERROR ACTIONS) YET TO BE DEFINED
 * 
 * This servlet works as follows:
 * 
 * STD USER
 * The case where a std user is trying to modify a product has already been filtered in PrepareAddUpdateProductPageServlet,
 *      so if user.isAdmin() == false, this servlet _creates_ a private product, and links the product to the list,
 *      as we know that the only action a std user can be performing is that of creating a private product to add it to a list.
 *      In this scenario, listId _must not_ be null. If it's null, it's an error.
 * 
 * ADMIN USER
 * In case the user is an admin, we know that
 *      if listId parameter is null:
 *          the admin is either creating a new product, or updating an existing product.
 *              so if a productId parameter != null, it means that a product is being updated
 *              if productId parameter == null, a product is being created.
 *      if listId parameter != null:
 *          the admin is either creating a new product, which will be added to a selected list,
 *          or updating an existing product.
 *              so if a productId parameter != null, it means that only the action of updating a product will be performed.
 *              if a productId parameter == null, it means that a new product will be created, and then added to the list
 * 
 * @author luca_morgese
 */
public class AddUpdateProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    private ProductInListDAO productInListDAO;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            productInListDAO = daoFactory.getDAO(ProductInListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get DAOs for user storage system", ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            
            //ProductId, listId null or not null values drive flow of casistics
            Integer listId = Integer.valueOf(request.getParameter("listId"));
            Integer productId = Integer.valueOf(request.getParameter("productId"));
            
            //Non admin user. A "private" product must be created.
            //Non update action => productId attribute is existing and not null
            if (user.isIsAdmin() == false) {
                
                //Additional check on must-exist variables
                if (productId != null || listId == null) {
                    //ERROR abort action to implement
                }
                
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String img = request.getParameter("img");
                String logo = request.getParameter("logo");
                Integer categoryProductId = Integer.valueOf(request.getParameter("name"));
                Integer privateListId = Integer.valueOf(request.getParameter("lsitId"));
                
                try {
                    
                    //Creates product bean and insert in database
                    Product product = new Product();

                    product.setName(name);
                    product.setDescription(description);
                    product.setImg(img);
                    product.setLogo(logo);
                    product.setCategoryProductId(categoryProductId);
                    product.setPrivateListId(privateListId);

                    productId = productDAO.insert(product);
                    
                    //Inserts (and "links") newly created private product in list
                    ProductInList productInList = new ProductInList();
                    
                    productInList.setListId(listId);
                    productInList.setProductId(productId);

                    productInListDAO.insert(productInList);
                    
                    //LINK TO BE DEFINED
                    request.getRequestDispatcher("/WEB-INF/jsp/RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
                    return;
                    
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Impossible to add new private product");
                }
            
                
            //Admin user
            } else {
                
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String img = request.getParameter("img");
                String logo = request.getParameter("logo");
                Integer categoryProductId = Integer.valueOf(request.getParameter("name"));
                Integer privateListId = Integer.valueOf(request.getParameter("lsitId"));
         
                try {
                    
                    Product product = new Product();

                    product.setId(productId);
                    
                    product.setName(name);
                    product.setDescription(description);
                    product.setImg(img);
                    product.setLogo(logo);
                    product.setCategoryProductId(categoryProductId);
                    
                    //Product created by admin is universally visualizable
                    Integer nullInt = null;
                    product.setPrivateListId(nullInt);
                    
                    //new product is being created by admin
                    if (productId == null) {
                        productId = productDAO.insert(product);
                        //It is being created from "add to list" action, inserts it in admin-user's list as well
                        if (listId != null) {
                            ProductInList productInList = new ProductInList();
                            productInList.setListId(listId);
                            productInList.setProductId(productId);

                            productInListDAO.insert(productInList);
                            
                            //FORWARD LINK TO BE DEFINED
                            request.getRequestDispatcher("RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
                            return;
                        } else {
                            //REDIRECT PAGE TO BE DEFINED
                            response.sendRedirect("/WEB-INF/jsp/ADMIN_CREATE_PRODUCT.jsp");
                            return;
                        }
                        
                    //Else means that productId != null, so an existing product is being modified
                    //and no further action is required
                    } else {
                        productDAO.update(product);
                        if (listId != null) {
                            //FORWARD LINK TO BE DEFINED
                            request.getRequestDispatcher("RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
                            return;
                        } else {
                            //REDIRECT PAGE TO BE DEFINED
                            response.sendRedirect("/WEB-INF/jsp/ADMIN_CREATE_PRODUCT.jsp");
                            return;
                        }
                    }
                    
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Impossible to add new, or modify a, product");
                }
            }
            
            
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that manages product creation/update page accordingly to"
                + "user status (std, admin), "
                + "and user action (admin-> add/create product; std, admin -> insert product in list)";
    }// </editor-fold>

}
