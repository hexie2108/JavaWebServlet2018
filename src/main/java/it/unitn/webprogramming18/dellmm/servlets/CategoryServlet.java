package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet per la pagina di categoria
 *
 * @author mikuc
 */
public class CategoryServlet extends HttpServlet
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
                //get catid richesta
                String catid = null;
                catid = request.getParameter("catid");
                //se parametrocatid non esiste
                if (catid == null)
                {
                        throw new ServletException("manca parametri catid");
                }

                //get beans di categoria corrente
                CategoryProduct categoriaCorrente = null;
                try
                {
                        categoriaCorrente = categoryProductDAO.getByPrimaryKey(Integer.parseInt(catid));
                        
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //se beans di categoria non esiste
                if (categoriaCorrente == null)
                {
                        throw new ServletException("categoria che stai visitando non esiste");
                }

                //set titolo della pagina nella richesta
                request.setAttribute("head_title", categoriaCorrente.getName());
                //set beans di categoria corrente  nella richesta
                request.setAttribute("categoria", categoriaCorrente);

                //get numero di prodotto per singola pagina
                int numebrProductForList = Integer.parseInt(getServletContext().getInitParameter("quantityItemForCategory"));
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
                        //get e set la lista di prodotto della categoria corrente per visualizzazione nella richesta
                        request.setAttribute("productList", productDAO.getPublicProductListByCatId(Integer.parseInt(catid), startPosition, numebrProductForList));
                        //get e set il numero di paginazione
                        int totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProductByCatId(Integer.parseInt(catid)) * 1.0 / numebrProductForList);
                        request.setAttribute("numberOfPageRest", (totalNumberOfPage - Integer.parseInt(page)));
                        //set url per la paginazione
                         request.setAttribute("basePath", request.getContextPath()+request.getServletPath()+"?catid="+catid+"&");
                        
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //inoltra a jsp
                request.getRequestDispatcher("/WEB-INF/jsp/category.jsp").forward(request, response);
                
        }
}
