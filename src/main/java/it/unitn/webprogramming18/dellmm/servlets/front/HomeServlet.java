package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

import java.io.IOException;
import java.net.InetAddress;
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
public class HomeServlet extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/home.jsp";

    private ProductDAO productDAO;
    private CategoryProductDAO categoryProductDAO;

    @Override
    public void init() throws ServletException {
        //inizializza due istanza dao per categoria e prodotto
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //get numero di categoria per il slider
        int numberCatForSlider = ConstantsUtils.NUMBER_CATEGORY_FOR_SLIDER;
        //get numero di prodotto per singola pagina
        int numebrProductForList = ConstantsUtils.NUMBER_PRODUCT_FOR_HOME;
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

        List<CategoryProduct> categoryListForSlider = null;
        List<Product> productList = null;
        int totalNumberOfPage;

        try {
            //get la lista di categoria di prodotto
            categoryListForSlider = categoryProductDAO.getCategoryProductList(0, numberCatForSlider);
            //get la lista di prodotto
            productList = productDAO.getPublicProductList(startPosition, numebrProductForList);
            //get il numero totale di pagine
            totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProduct() * 1.0 / numebrProductForList);

        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        //set titolo della pagina
        request.setAttribute(ConstantsUtils.HEAD_TITLE, "Home");
        //set la lista di categoria di prodotto nella richesta
        request.setAttribute("categoryListForSlider", categoryListForSlider);
        //set la lista di prodotto nella richesta
        request.setAttribute(ConstantsUtils.PRODUCT_LIST, productList);
        //set il numero di pagine resti
        request.setAttribute(ConstantsUtils.NUMBER_OF_PAGE_REST, (totalNumberOfPage - Integer.parseInt(page)));
        //set url per la paginazione
        request.setAttribute(ConstantsUtils.PATH_FOR_PAGINATION, request.getContextPath() + "?");

        //inoltra a jsp
        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);

    }

}
