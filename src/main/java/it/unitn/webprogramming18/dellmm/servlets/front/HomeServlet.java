package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {

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

                List<CategoryProduct> categoryListForSlider = null;
                List<Product> productList = null;
                int totalNumberOfPage;

                try
                {
                        //get la lista di categoria di prodotto
                        categoryListForSlider = categoryProductDAO.getCategoryProductList(0, numberCatForSlider);
                        //get la lista di prodotto
                        productList = productDAO.getPublicProductList(startPosition, numebrProductForList);
                        //get il numero totale di pagine
                        totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProduct() * 1.0 / numebrProductForList);

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //set titolo della pagina
                request.setAttribute("head_title", "Home");
                //set la lista di categoria di prodotto nella richesta
                request.setAttribute("categoryListForSlider", categoryListForSlider);
                //set la lista di prodotto nella richesta
                request.setAttribute("productList", productList);
                 //set il numero di pagine resti
                request.setAttribute("numberOfPageRest", (totalNumberOfPage - Integer.parseInt(page)));
                //set url per la paginazione
                request.setAttribute("basePath", request.getContextPath() + "?");
                
                a(request);

                //inoltra a jsp
                request.getRequestDispatcher("/WEB-INF/jsp/front/home.jsp").forward(request, response);

        }
        
        
        public void a(HttpServletRequest request){
                request.setAttribute("a", "hallo a");
        }

}
