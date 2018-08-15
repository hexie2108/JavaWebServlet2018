/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        productDAO = new JDBCProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //get parola ricercata
        String searchWords = request.getParameter("searchWords");
        //se parola non esiste
        if (searchWords == null) {
            throw new ServletException("la parola da cercare è nullo");
        }
        if (searchWords.equals("")) {
            throw new ServletException("la parola da cercare è vuota");
        }

        //get ordine richiesta
        String order = request.getParameter("order");
        //se non è vuota
        if (order != null) {
            //ma con valore diverso da quelli prefissati
            if (!order.equals("categoryName") && !order.equals("productName") && !order.equals("relevance")) {
                throw new ServletException("il parametro di ordinamento non valido");
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
                throw new ServletException("il parametro direction non è valido");
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
