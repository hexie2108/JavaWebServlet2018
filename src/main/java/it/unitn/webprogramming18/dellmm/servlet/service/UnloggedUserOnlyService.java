/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlet.service;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * i servizio per la gestione della lista in modalità anonimo
 *
 * @author mikuc
 */
public class UnloggedUserOnlyService extends HttpServlet
{

        /**
         * Handles the HTTP <code>POST</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                doGet(request, response);

        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                //get azione che vuoi fare
                String action = null;
                action = request.getParameter("action");
                if (action == null)
                {
                        throw new ServletException("manca il parametro action");
                }
                //memorizza il risultato dell'operazione
                String result = "";

                //in caso di inserimento
                if (action.equals("insert"))
                {
                        //per controllare la ripetizione
                        boolean repeatItem = false;

                        String productId = null;
                        productId = request.getParameter("productId");

                        if (productId == null)
                        {
                                throw new ServletException("manca il parametro  id del prodotto");
                        }

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
                        if (cookOfList == null || cookOfList.getValue().equals(""))
                        {
                                cookOfList = new Cookie("localShoppingList", productId);
                                cookOfList.setPath(getServletContext().getContextPath());
                                response.addCookie(cookOfList);
                                result = "InsertOk";

                        }
                        //esiste già una lista locale non vuota
                        else
                        {
                                //controlla se esiste già tale prodotto in lista
                                List<String> productIdList = new ArrayList(Arrays.asList(cookOfList.getValue().split(",")));
                                for (String string : productIdList)
                                {
                                        if (productId.equals(string))
                                        {

                                                //in caso esiste
                                                //lanciare un messaggio sul schermo dell'utente
                                                repeatItem = true;

                                        }
                                }
                                //se non cè la ripetizione
                                if (!repeatItem)
                                {

                                        //altrimenti aggiungere elemento
                                        cookOfList.setValue(cookOfList.getValue() + "," + productId);
                                        cookOfList.setPath(getServletContext().getContextPath());
                                        response.addCookie(cookOfList);
                                        result = "InsertOk";
                                }
                                //se cè una ripetizione
                                else
                                {
                                        result = "InsertFail";
                                }
                        }

                }
                //in caso elimina un prodotto dalla lista
                else if (action.equals("delete"))
                {

                        //per sapere l'avvenuta della eliminazione
                        boolean successDelete = false;

   
                        String productId = null;
                        productId = request.getParameter("productId");

                        if ( productId == null)
                        {
                                throw new ServletException("manca il parametro  id del prodotto");
                        }

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
                                //errore nella cancellazione
                                result = "DeleteFail";

                        }
                        //esiste già una lista locale non vuota
                        else
                        {
                                //controlla se esiste tale prodotto in lista
                                List<String> productIdList = new ArrayList(Arrays.asList(cookOfList.getValue().split(",")));
                                for (int i = 0; i < productIdList.size(); i++)
                                {
                                        if (productId.equals(productIdList.get(i)))
                                        {
                                                //quando ha trovato
                                                productIdList.remove(i);
                                                successDelete = true;
                                                i = productIdList.size();
                                        }
                                }
                                //se non ha trovato prodotto
                                if (!successDelete)
                                {
                                        result = "DeleteFail";
                                }
                                //se ha trovato
                                else
                                {
                                         result = "DeleteOk";
                                        //aggiorna cookie
                                        cookOfList.setValue(String.join(",", productIdList));
                                        cookOfList.setPath(getServletContext().getContextPath());
                                        response.addCookie(cookOfList);
                                }

                        }

                }

                //in caso cambia la categoria della lista locale
                else if (action.equals("changeListCategory"))
                {
                        String categoryList = null;
                        categoryList = request.getParameter("categoryList");
                        if (categoryList == null || categoryList == "-1")
                        {
                                throw new ServletException("manca il parametro id categoria della lista");
                        }
                        Cookie cookOfListCategory = new Cookie("localShoppingListCategory", categoryList);
                        cookOfListCategory.setPath(getServletContext().getContextPath());
                        response.addCookie(cookOfListCategory);
                        result = "ChangeListCategoryOk";

                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if(prevUrl==null){
                        prevUrl = getServletContext().getContextPath();
                }
                //passare lo risultato  di inserimento
                request.getSession().setAttribute("result", result);
                response.sendRedirect(response.encodeRedirectURL(prevUrl));

        }

}
