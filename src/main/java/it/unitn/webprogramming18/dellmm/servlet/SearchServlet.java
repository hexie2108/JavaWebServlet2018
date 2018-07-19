/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlet;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mikuc
 */
public class SearchServlet extends HttpServlet
{

        private ProductDAO productDAO;

        @Override
        public void init() throws ServletException
        {
                productDAO = new JDBCProductDAO();

        }

        // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
        /**
         * Handles the HTTP <code>GET</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws
         * it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {
                //get parola ricercata
                String searchWords = null;
                searchWords = request.getParameter("searchWords");
               //se parola non esiste
                if (searchWords == null)
                {
                        throw new ServletException("la parola da cercare è vuota");
                }
                
                //set titolo della pagina nella richesta
                request.setAttribute("head_title", "Search: " + searchWords);
                
                //get ordine richiesta
                String order = null;
                order = request.getParameter("order");
                //se non è vuota
                if (order != null)
                {
                        //ma con valore diverso da quelli prefissati
                        if (!order.equals("categoryName") && !order.equals("productName"))
                        {
                                throw new ServletException("il parametro di ordinamento non valido");
                        }
                }
                //altrimenti, assegna il valore default
                else
                {
                        order = "productName";
                }
             
                //posizione di start di query per get lista di prodotto, servira per paginazione futura        
                int startPosition = 0;
                //get numero di prodotto per singola pagina
                int numebrProductForList = Integer.parseInt(getServletContext().getInitParameter("quantityItemForSearch"));

                try
                {
                        //get e set la lista di prodotto secondo la parola ricercata e ordine richiesta per visualizzazione nella richesta
                        request.setAttribute("productList", productDAO.getProductListByNameSearch(searchWords, order, startPosition, numebrProductForList));

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                
                //inoltra jsp
                request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
        }

}
