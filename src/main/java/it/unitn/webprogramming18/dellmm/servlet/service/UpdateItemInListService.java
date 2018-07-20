package it.unitn.webprogramming18.dellmm.servlet.service;

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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet per la pagina di categoria
 *
 * @author mikuc
 */
public class UpdateItemInListService extends HttpServlet
{

        @Override
        public void init() throws ServletException
        {

        }

        /**
         * aggiungere un elemento in lista modalità per utente anonimo e utente
         * registrato
         *
         * @param req
         * @param resp
         * @throws ServletException
         * @throws IOException
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String listId = null;
                listId = request.getParameter("listId");
                String productId = null;
                productId = request.getParameter("productId");

                if (listId == null || productId == null)
                {
                        throw new ServletException("manca il parametro id della lista o id del prodotto");
                }

                boolean repeatItem = false;
                String resultOfAddItem = null;

                //in caso di utente anonimo
                if (listId.equals("default"))
                {

                        Cookie cookOfList = null;
                        Cookie[] cookies = request.getCookies();
                        //se utente ha una cookie della lista locale
                        if (cookies != null && cookies.length > 0)
                        {
                                for (Cookie cookie : cookies)
                                {
                                        if (cookie.getName().equals("localShoppingList"))
                                        {
                                                cookOfList = cookie;
                                        }
                                }
                        }

                        //se la lista locale non esiste oppure è vuoto
                        if (cookOfList == null || cookOfList.equals(""))
                        {
                                cookOfList = new Cookie("localShoppingList", productId);
                                cookOfList.setPath(getServletContext().getContextPath());
                                response.addCookie(cookOfList);
                                resultOfAddItem = "ok";

                        }
                        //esiste già una lista locale non vuota
                        else
                        {
                                //controlla se esiste già tale prodotto in lista
                                String[] productIdList = cookOfList.getValue().split(",");
                                for (String string : productIdList)
                                {
                                        if (productId.equals(string))
                                        {

                                                //in caso esiste
                                                //lanciare un messaggio sul schermo dell'utente
                                                //throw new ServletException("elemento è già presente nella lista!");
                                                repeatItem = true;
                                                resultOfAddItem = "error";
                                        }
                                }
                                if (!repeatItem)
                                {
                                        //altrimenti aggiungere elemento
                                        cookOfList.setValue(cookOfList.getValue() + "," + productId);
                                        cookOfList.setPath(getServletContext().getContextPath());
                                        response.addCookie(cookOfList);
                                        resultOfAddItem = "ok";
                                }
                        }

                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                //passare lo risultato  di inserimento
               request.getSession().setAttribute("resultOfAddItem", resultOfAddItem);
                response.sendRedirect(response.encodeRedirectURL(prevUrl));

        }

        /**
         * eliminare o comprato un elemento in lista
         *
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

        }

}
