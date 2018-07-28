/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.util.List;
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
public class PrepareNamesAutocomplServlet extends HttpServlet {

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
            //Java List
            List<String> productsNames = null;
            
            try {
                productsNames = productDAO.getAllNames();
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get products' names");
            }
            
            
            request.setAttribute("productsNames", productsNames);
            request.setAttribute("productInListDAO", productInListDAO);

            //URL TO BE DEFINED
            request.getRequestDispatcher("/WEB-INF/jsp/RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
        
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
