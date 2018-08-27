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
            String query = request.getParameter("q");
            HashMap<String, Double> dbRis = productDAO.getNameTokensFiltered(query, null);

            String[] queryTokens = query.split("[\\s\\p{Punct}]");

            JaroWinkler jw = new JaroWinkler();

            StringBuilder stringBuilder  = new StringBuilder();

            for(String k: dbRis.keySet()) {
                System.out.println(k);
            }

            for(String token: queryTokens) {
                AbstractMap.Entry<String, Double> l =
                        dbRis
                            .entrySet()
                            .stream()
                            .map(e -> new AbstractMap.SimpleEntry<String, Double>(e.getKey(), jw.similarity(e.getKey(), token)))
                            .max(Map.Entry.comparingByValue()).orElse(null);

                if (l != null) {
                    System.out.println(l.getKey());
                    System.out.println(l.getValue());
                }

                if (l != null && l.getValue() > CONSIDER_TYPO) {

                    dbRis.remove(l.getKey());
                    stringBuilder.append(l.getKey());
                } else {
                    stringBuilder.append(token);
                }

                stringBuilder.append(" ");
            }

            String prefix = stringBuilder.toString();

            List<String> toSend = dbRis.entrySet()
                    .stream()
                    .sorted(HashMap.Entry.comparingByValue())
                    .map(e -> prefix + e.getKey())
                    .collect(Collectors.toList());

            ServletUtility.sendJSON(request, response, 200, toSend);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "da");
        }

        //request.getRequestDispatcher("/WEB-INF/searchTest.jsp").forward(request, response);
        //response.sendRedirect("/WEB-INF/searchTest.jsp");

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
