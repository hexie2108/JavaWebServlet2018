/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.another;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductInListDAO;
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
public class PrepareAutocompleteDataServlet extends HttpServlet
{

        private ProductDAO productDAO;
        private ProductInListDAO productInListDAO;

        @Override
        public void init() throws ServletException
        {
              
                        productDAO = new JDBCProductDAO();
                        productInListDAO =  new JDBCProductInListDAO();
               
        }

        /**
         * Processes requests for both HTTP <code>GET</code> and
         * <code>POST</code> methods.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");

                

                HashMap<String, java.io.Serializable> productEntry;
                List<HashMap<String, java.io.Serializable>> productEntries = new ArrayList();
                HashMap<String, List<HashMap<String, java.io.Serializable>>> jsonData = new HashMap();

                for (int i = 0; i < 10; i++)
                {
                        productEntry = new HashMap();
                        productEntry.put("id", i + 1);
                        productEntry.put("text", "name"+i);

                        productEntries.add(productEntry);

                }

                jsonData.put("results", productEntries);

                PrintWriter out = response.getWriter();
                Gson gson = new Gson();
                out.print(gson.toJson(productEntries));

                //request.getRequestDispatcher("/WEB-INF/searchTest.jsp").forward(request, response);
                //response.sendRedirect("/WEB-INF/searchTest.jsp");

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
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {
                processRequest(request, response);
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
                    throws ServletException, IOException
        {
                processRequest(request, response);
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo()
        {
                return "Prepares data for search autocomplete";
        }

}
