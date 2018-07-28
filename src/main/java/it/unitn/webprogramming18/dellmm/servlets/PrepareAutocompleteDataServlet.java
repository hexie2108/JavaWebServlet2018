/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author luca_morgese
 */
public class PrepareAutocompleteDataServlet extends HttpServlet {

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
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        //Java List
        List<String> productsNamesList = new ArrayList();

        try {
            productsNamesList = productDAO.getAllPublicProductsNames();
        } catch (DAOException ex) {
            ex.printStackTrace();
            throw new ServletException("Impossible to get products' names");
        }
        
        HashMap<String, java.io.Serializable> productEntry;
        List<HashMap<String, java.io.Serializable>> productEntries = new ArrayList();
        HashMap<String, List<HashMap<String, java.io.Serializable>>> jsonData = new HashMap();
        
        for (int i = 0; i < productsNamesList.size(); i++) {
            productEntry = new HashMap();
            productEntry.put("id", i+1);
            productEntry.put("text", productsNamesList.get(i));

            productEntries.add(productEntry);
            productEntry.clear();
        }
        
        jsonData.put("results", productEntries);
        
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(jsonData));

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Prepares data for search autocomplete";
    }

}
