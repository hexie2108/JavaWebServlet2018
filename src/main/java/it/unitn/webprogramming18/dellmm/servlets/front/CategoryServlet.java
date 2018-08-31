package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet per la pagina di categoria
 *
 * @author mikuc
 */
public class CategoryServlet extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/category.jsp";

    private ProductDAO productDAO;
    private CategoryProductDAO categoryProductDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ProductDAO or CategoryProductDAO for user storage system", ex);
        }
    }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {
                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);
                //get id categoria
                String catId = request.getParameter("catId");
                //se id categoria non esiste
                CheckErrorUtils.isNull(catId, rb.getString("error.missingCategoryProductId"));

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
                CheckErrorUtils.isNull(categoriaCorrente, rb.getString("error.CategoryProductNotExist"));

        //get numero di prodotto per singola pagina
        int numebrProductForList = ConstantsUtils.NUMBER_PRODUCT_FOR_CATEGORY;

        //posizione di start di query per get lista di prodotto
        int startPosition = 0;
        //get parametro di paginazione
        String page = request.getParameter("page");
        //se non è nullo
        if (page != null && Integer.parseInt(page) > 1) {
            //aggiorna la posizione di start di query
            startPosition = (Integer.parseInt(page) - 1) * numebrProductForList;
        } else {
            page = "1";
        }

        List<Product> productList = null;
        int totalNumberOfPage;
        try {
            //get la lista di prodotto
            productList = productDAO.getPublicProductListByCatId(Integer.parseInt(catId), startPosition, numebrProductForList);
            //get il numero totale di pagine
            totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProductByCatId(Integer.parseInt(catId)) * 1.0 / numebrProductForList);

        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        //set titolo della pagina nella richesta
        request.setAttribute(ConstantsUtils.HEAD_TITLE, categoriaCorrente.getName());
        //set beans di categoria corrente  nella richesta
        request.setAttribute("categoria", categoriaCorrente);
        //set la lista di prodotto nella richesta
        request.setAttribute(ConstantsUtils.PRODUCT_LIST, productList);
        //set il numero di pagine resti
        request.setAttribute(ConstantsUtils.NUMBER_OF_PAGES, totalNumberOfPage);

        //inoltra a jsp
        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);

    }
}
