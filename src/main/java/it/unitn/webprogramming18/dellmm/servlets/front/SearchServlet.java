/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet per la pagina di ricerca
 *
 * @author mikuc
 */
public class SearchServlet extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/search.jsp";

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ProductDAO for user storage system", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //get parola ricercata
        String searchWords = request.getParameter("searchWords");
        
        ResourceBundle rb = i18n.getBundle(request);
        
        //se parola non esiste
        if (searchWords == null) {
            ServletUtility.sendError(request, response, 400, rb.getString("servlet.errors.nullSearchWord"));
            return;
        }
        if (searchWords.equals("")) {
            ServletUtility.sendError(request, response, 400, rb.getString("servlet.errors.emptySearchWord"));
            return;
        }

        //get ordine richiesta
        String order = request.getParameter("order");
        //se non è vuota
        if (order != null) {
            //ma con valore diverso da quelli prefissati
            if (!order.equals("categoryName") && !order.equals("productName") && !order.equals("relevance")) {
                ServletUtility.sendError(request, response, 400, rb.getString("servlet.errors.invalidSortParameter"));
                return;
            }
        }
        //altrimenti, assegna il valore default
        else {
            order = "relevance";
        }

        // get direzione richiesta(oridne ascendente/discendente)
        String direction = request.getParameter("direction");

        // Se vuoto assegna default, se non valido crea exception
        if (direction != null) {
            if(!direction.equals("asc") && !direction.equals("desc")) {
                ServletUtility.sendError(request, response, 400, rb.getString("servlet.errors.invalidDirectionParameter"));
                return;
            }
        }
        else {
            direction = order.equals("relevance")?"desc":"asc";
        }

        String[] catId = request.getParameterValues("catId");
        List<Integer> categories;
        if(catId == null) {
            categories = new ArrayList<Integer>();
        } else {
            categories = Arrays.stream(catId).map(Integer::parseInt).collect(Collectors.toList());
        }


        //get numero di prodotto per singola pagina
        int numebrProductForList = ConstantsUtils.NUMBER_PRODUCT_FOR_SEARCH;
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
            //get la lista di prodotto secondo la parola ricercata
            productList = productDAO.search(searchWords, order, direction, categories, startPosition, numebrProductForList);
            //get il numero totale di pagine
            totalNumberOfPage = (int) Math.ceil(productDAO.getCountOfPublicProductByNameSearch(searchWords) * 1.0 / numebrProductForList);

        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        //set titolo della pagina nella richesta
        request.setAttribute(ConstantsUtils.HEAD_TITLE, "Search: " + searchWords);
        //set la lista di prodotto nella richesta
        request.setAttribute(ConstantsUtils.PRODUCT_LIST, productList);
        //set il numero di pagine resti
        request.setAttribute(ConstantsUtils.NUMBER_OF_PAGE_REST, (totalNumberOfPage - Integer.parseInt(page)));
        //set url per la paginazione
        request.setAttribute(ConstantsUtils.PATH_FOR_PAGINATION, request.getContextPath() + request.getServletPath() + "?searchWords=" + URLEncoder.encode(searchWords, "utf-8") + "&order=" + order + "&");

        //inoltra jsp
        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
    }

}
