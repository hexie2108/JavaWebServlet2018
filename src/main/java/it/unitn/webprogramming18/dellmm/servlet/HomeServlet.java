
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
 *servlet per la pagina di home
 * @author mikuc
 */
public class HomeServlet extends HttpServlet
{

        private ProductDAO productDAO;
        private CategoryProductDAO categoryProductDAO;

        @Override
        public void init() throws ServletException
        {
                //inizializza due istanza dao per categoria e prodotto
                productDAO = new JDBCProductDAO();
                categoryProductDAO = new JDBCCategoryProductDAO();
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
                //set titolo della pagina
                request.setAttribute("head_title", "Home");
                //get numero di categoria per il slider
                int numberProductForSlider = Integer.parseInt(getServletContext().getInitParameter("quantityCatForSlider"));
                //posizione di start di query per get lista di prodotto, servira per paginazione futura
                int startPosition =0;
                //get numero di prodotto per singola pagina
                int numebrProductForList = Integer.parseInt(getServletContext().getInitParameter("quantityItemForHome"));
                try
                {
                        //get e set la lista di cat per slider nella richesta
                        request.setAttribute("sliderProductList", categoryProductDAO.getCategoryProductList(0, numberProductForSlider));
                        //get e set la lista di prodotto per visualizzazione nella richesta
                        request.setAttribute("productList", productDAO.getProductList(startPosition, numebrProductForList));
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //inoltra a jsp
               
                request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
                
        }

}
