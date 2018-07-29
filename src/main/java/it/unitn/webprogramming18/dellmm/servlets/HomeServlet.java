package it.unitn.webprogramming18.dellmm.servlets;

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
 * servlet per la pagina di home
 *
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
                int numberCatForSlider = Integer.parseInt(getServletContext().getInitParameter("quantityCatForSlider"));
                //get numero di prodotto per singola pagina
                int numebrProductForList = Integer.parseInt(getServletContext().getInitParameter("quantityItemForHome"));
                //posizione di start di query per get lista di prodotto
                int startPosition = 0;
                //get parametro di paginazione
                String page = request.getParameter("page");

                //se non è nullo
                if (page != null && Integer.parseInt(page) > 1)
                {
                        //aggiorna la posizione di start di query
                        startPosition = (Integer.parseInt(page) - 1) * numebrProductForList;
                }
                else
                {
                        page = "1";
                }

                try
                {

                        //get e set la lista di cat per slider nella richesta
                        request.setAttribute("categoryListForSlider", categoryProductDAO.getCategoryProductList(0, numberCatForSlider));
                        //get e set la lista di prodotto per visualizzazione nella richesta
                        request.setAttribute("productList", productDAO.getPublicProductList(startPosition, numebrProductForList));
                        //get e set il numero di paginazione
                        int totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProduct() * 1.0 / numebrProductForList);
                        request.setAttribute("numberOfPageRest", (totalNumberOfPage - Integer.parseInt(page)));
                        //set url per la paginazione
                        request.setAttribute("basePath", request.getContextPath()+"?");
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //inoltra a jsp
                request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);

        }

}
