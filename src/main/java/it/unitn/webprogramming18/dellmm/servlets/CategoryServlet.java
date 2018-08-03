package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import java.io.IOException;
import java.util.List;
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

                productDAO = new JDBCProductDAO();
                categoryProductDAO = new JDBCCategoryProductDAO();
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {
                //get id categoria
                String catId = request.getParameter("catId");
                //se id categoria non esiste
                if (catId == null)
                {
                        throw new ServletException("manca parametri catId");
                }

                //get beans di categoria
                CategoryProduct categoriaCorrente = null;
                try
                {
                        categoriaCorrente = categoryProductDAO.getByPrimaryKey(Integer.parseInt(catId));
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //se beans di categoria non esiste
                if (categoriaCorrente == null)
                {
                        throw new ServletException("non esiste tale categoria di prodotto");
                }

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

                List<Product> productList = null;
                int totalNumberOfPage;
                try
                {
                        //get la lista di prodotto
                        productList = productDAO.getPublicProductListByCatId(Integer.parseInt(catId), startPosition, numebrProductForList);
                        //get il numero totale di pagine
                        totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProductByCatId(Integer.parseInt(catId)) * 1.0 / numebrProductForList);

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //set titolo della pagina nella richesta
                request.setAttribute("head_title", categoriaCorrente.getName());
                //set beans di categoria corrente  nella richesta
                request.setAttribute("categoria", categoriaCorrente);
                //set la lista di prodotto nella richesta
                request.setAttribute("productList", productList);
                //set il numero di pagine resti
                request.setAttribute("numberOfPageRest", (totalNumberOfPage - Integer.parseInt(page)));
                //set url per la paginazione
                request.setAttribute("basePath", request.getContextPath() + request.getServletPath() + "?catId=" + catId + "&");

                //inoltra a jsp
                request.getRequestDispatcher("/WEB-INF/jsp/category.jsp").forward(request, response);

        }
}
