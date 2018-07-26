/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luca_morgese
 */
public class AddUpdateCategoryProductServlet extends HttpServlet {

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
            throw new ServletException("Impossible to get ListDAO for user storage system", ex);
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
            
            //Being null or not null defines if occurring action is of "modify catList" or "create catList"
            Integer categoryProductId = null;
            categoryProductId = Integer.valueOf(request.getParameter("categoryProductId"));
            
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String img = request.getParameter("img");
            
            try {
                CategoryProduct categoryProduct = new CategoryProduct();

                categoryProduct.setId(categoryProductId);

                categoryProduct.setName(name);
                categoryProduct.setDescription(description);
                categoryProduct.setImg(img);

                if (categoryProductId == null) {
                    categoryProductDAO.insert(categoryProduct);
                } else {
                    categoryProductDAO.update(categoryProduct);
                }
                
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to update or create new product category");
            }
            
            response.sendRedirect("/WEB-INF/jsp/yourHome.jsp");
        }
    }

    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Inserts into persistence system new CategoryProduct entry";
    }// </editor-fold>

}
