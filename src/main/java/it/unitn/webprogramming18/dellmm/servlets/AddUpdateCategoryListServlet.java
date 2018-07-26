/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
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
public class AddUpdateCategoryListServlet extends HttpServlet {


    private CategoryListDAO categoryListDAO;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
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
            Integer categoryListId = null;
            categoryListId = Integer.valueOf(request.getParameter("categoryListId"));
            
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String img1 = request.getParameter("img1");
            String img2 = request.getParameter("img2");
            String img3 = request.getParameter("img3");
            
            try {
                CategoryList categoryList = new CategoryList();

                categoryList.setId(categoryListId);

                categoryList.setName(name);
                categoryList.setDescription(description);
                categoryList.setImg1(img1);
                categoryList.setImg2(img2);
                categoryList.setImg3(img3);

                if (categoryListId == null) {
                    categoryListDAO.insert(categoryList);
                } else {
                    categoryListDAO.update(categoryList);
                }
                
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to update or create new list category");
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
        return "Inserts into persistence system new CategoryList entry";
    }// </editor-fold>

}
