package it.unitn.webprogramming18.dellmm.servlets.another;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import java.io.IOException;
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
public class PrepareAutocompleteDataServlet extends HttpServlet
{

        private ProductDAO productDAO;

        private static final double CONSIDER_TYPO = 0.8;

        @Override
        public void init() throws ServletException
        {
                DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
                if (daoFactory == null)
                {
                        throw new ServletException("Impossible to get db factory for user storage system");
                }

                try
                {
                        productDAO = daoFactory.getDAO(ProductDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get ProductDAO or ProductInListDAO for user storage system", ex);
                }
        }

        /**
         * Processes requests for both HTTP <code>GET</code> and
         * <code>POST</code> methods.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String query = request.getParameter("searchWords");
                Integer listId = null;

                {
                        Object list = ((HttpServletRequest) request).getSession().getAttribute("myListId");
                        listId = list == null ? null : (Integer) list;
                }

                List<Integer> categories;
                try
                {
                        String[] catId = request.getParameterValues("catId");
                        if (catId == null)
                        {
                                categories = new ArrayList<Integer>();
                        }
                        else
                        {
                                categories = Arrays.stream(catId).map(Integer::parseInt).collect(Collectors.toList());
                        }
                }
                catch (NumberFormatException e)
                {
                        ServletUtility.sendError(request, response, 400, ""); // TODO: to i18n
                        return;
                }

                HashMap<String, Double> dbRis;

                try
                {
                        dbRis = productDAO.getNameTokensFiltered(query, listId, categories);
                }
                catch (DAOException e)
                {
                        e.printStackTrace();
                        ServletUtility.sendError(request, response, 500, "da");
                        return;
                }

                String[] queryTokens = query.split("[\\s\\p{Punct}]");

                JaroWinkler jw = new JaroWinkler();

                Map<String, Double> prefix = new HashMap<>();
                prefix.put("", 1.);

                for (int i = 0; i < queryTokens.length; i++)
                {
                        final String token = queryTokens[i];

                        // Set low bar for small length, last tokens high bar otherwise
                        final double filterBar
                                    = 0.6
                                    - ((i == queryTokens.length - 1) ? 1. : 0.) * (1. - ((double) Math.min(Math.max(0, token.length() - 3), 6)) / 6.) * 0.3;

                        // Get the 3 most similar string(similar to token) in dbRis and map <key, similarity key-token> to use as corrections for typos
                        Map<String, Double> l
                                    = dbRis
                                                .entrySet()
                                                .stream()
                                                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), jw.similarity(e.getKey(), token)))
                                                .filter(e -> e.getValue() > filterBar)
                                                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                                                .limit(3)
                                                .collect(Collectors.toMap(
                                                            AbstractMap.SimpleEntry::getKey,
                                                            AbstractMap.SimpleEntry::getValue
                                                ));

                        // Remove token used for typos
                        for (String k : l.keySet())
                        {
                                dbRis.remove(k);
                        }

                        // Create all combinations between prefix strings and l, use as score the sum(we'll later do an average) and pick 15 higher scores
                        prefix = prefix.entrySet()
                                    .stream()
                                    .flatMap(p -> l.entrySet().stream().map(w -> new AbstractMap.SimpleEntry<>(
                                    p.getKey() + " " + w.getKey(),
                                    p.getValue() + w.getValue()
                        )))
                                    .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                                    .limit(15)
                                    .collect(Collectors.toMap(
                                                AbstractMap.SimpleEntry::getKey,
                                                AbstractMap.SimpleEntry::getValue
                                    ));
                }

                // Concatenate prefix with prefix + an extra word, calculate for both the avarage
                prefix = Stream.concat(
                            prefix.entrySet().stream().map(w -> new AbstractMap.SimpleEntry<>(
                            w.getKey(),
                            (w.getValue()) / ((double) (queryTokens.length)))
                            ),
                            prefix
                                        .entrySet()
                                        .stream()
                                        .flatMap(
                                                    p -> dbRis.entrySet()
                                                                .stream()
                                                                .map(w -> new AbstractMap.SimpleEntry<>(
                                                                p.getKey() + " " + w.getKey(),
                                                                (p.getValue() + w.getValue() / ((double) (queryTokens.length + 1))) / ((double) (queryTokens.length + 1)))
                                                                )
                                        )
                                        .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                                        .limit(3)
                ).collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                ));

                // Sort the values, pick first 10, get only strings and send
                ServletUtility.sendJSON(request, response, 200,
                            prefix
                                        .entrySet()
                                        .stream()
                                        .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                                        .limit(10)
                                        .map(e -> e.getKey().substring(1).toLowerCase())
                                        .collect(Collectors.toList()));
        }

        /**
         * Handles the HTTP <code>GET</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {
                processRequest(request, response);
        }

        /**
         * Handles the HTTP <code>POST</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {
                processRequest(request, response);
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo()
        {
                return "Prepares data for search autocomplete";
        }

}
