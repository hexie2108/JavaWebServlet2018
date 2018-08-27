/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.another;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import info.debatty.java.stringsimilarity.JaroWinkler;

/**
 * @author luca_morgese
 */
public class PrepareAutocompleteDataServlet extends HttpServlet {

    private ProductDAO productDAO;

    private static final double CONSIDER_TYPO = 0.8;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ProductDAO or ProductInListDAO for user storage system", ex);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String query = request.getParameter("searchWords");
            Integer listId = null;

            {
                Object list = ((HttpServletRequest) request).getSession().getAttribute("myListId");
                listId = list == null? null: (Integer) list;
            }

            List<Integer> categories;
            try{
                String[] catId = request.getParameterValues("catId");
                if(catId == null) {
                    categories = new ArrayList<Integer>();
                } else {
                    categories = Arrays.stream(catId).map(Integer::parseInt).collect(Collectors.toList());
                }
            } catch (NumberFormatException e) {
                ServletUtility.sendError(request, response, 400, ""); // TODO: to i18n
                return;
            }

            HashMap<String, Double> dbRis = productDAO.getNameTokensFiltered(query, listId, categories);

            String[] queryTokens = query.split("[\\s\\p{Punct}]");

            JaroWinkler jw = new JaroWinkler();

            Map<String, Double> prefix = new HashMap<>();

            prefix.put("", 1.);

            for(int i=0; i<queryTokens.length; i++) {
                final String token = queryTokens[i];

                Stream<AbstractMap.SimpleEntry<String, Double> > stream =
                        dbRis
                        .entrySet()
                        .stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), jw.similarity(e.getKey(), token)));

                if (i != queryTokens.length -1) {
                    stream = stream.filter(e -> e.getValue() > CONSIDER_TYPO);
                } else {
                    stream = stream.filter(e -> e.getValue() > CONSIDER_TYPO*2/5);
                }

                Map<String, Double> l =
                        stream
                            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                            .limit(3)
                            .collect(Collectors.toMap(
                                    (AbstractMap.SimpleEntry<String,Double> e) -> e.getKey(),
                                    (AbstractMap.SimpleEntry<String,Double> e) -> e.getValue()
                            ));

                for(String k: l.keySet()) {
                    dbRis.remove(k);
                }

                prefix = prefix.entrySet()
                            .stream()
                            .flatMap(p -> l.entrySet().stream().map(w -> new AbstractMap.SimpleEntry<>(p.getKey() + " " + w.getKey(), p.getValue()*w.getValue())))
                            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                            .limit(15)
                            .collect(Collectors.toMap(
                                    e -> e.getKey(),
                                    e -> e.getValue()
                            ));
            }

            prefix = Stream.concat(
                        prefix.entrySet().stream(),
                        prefix
                            .entrySet()
                            .stream()
                            .flatMap(p -> dbRis.entrySet().stream().map(w -> new AbstractMap.SimpleEntry<>(p.getKey() + " " + w.getKey(), p.getValue()*w.getValue() * 0.25)))
                            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                            .limit(15)
            ).collect(Collectors.toMap(
                    e -> e.getKey(),
                    e -> e.getValue()
            ));

            ServletUtility.sendJSON(request, response, 200, prefix.entrySet().stream().sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) .map(e -> e.getKey()).collect(Collectors.toList()));

        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "da");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Prepares data for search autocomplete";
    }

}
