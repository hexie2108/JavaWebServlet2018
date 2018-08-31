package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per aggiungere/eliminare il prodotto dalla default lista per utente
 * anonimo
 *
 * @author mikuc
 */
public class UpdateItemInListUnloggedUserOnlyService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //memorizza il risultato dell'operazione
        String result = null;
        //get azione che vuoi fare
        String action = request.getParameter("action");
        //se azione è nullo
        if(action == null){
            ServletUtility.sendError(request, response, 400, "users.errors.missingAction"); //manca il parametro action
            return;
	}

        //in caso di inserimento
        if (action.equals("insert")) {
            //variabile per controllare la ripetizione
            boolean repeatItem = false;

                        //get id prodotto da aggiungere
                        String productId = request.getParameter("productId");
                        //se id prodotto è nullo
                        CheckErrorUtils.isNull(productId, rb.getString("error.missingProductId"));

            //get la cookie della lista locale
            Cookie cookOfList = null;
            Cookie[] cookies = request.getCookies();
            //se utente ha una cookie della lista locale
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("localShoppingList")) {
                        cookOfList = cookie;
                    }
                }
            }

            //se la lista locale non esiste oppure è vuoto
            if (cookOfList == null || cookOfList.getValue().equals(""))
            {
                //crea una nuova cookie per la lista locale, e inserisce il id prodotto da aggiungere
                cookOfList = new Cookie("localShoppingList", productId);
                cookOfList.setPath(getServletContext().getContextPath());
                //dura un mese
                cookOfList.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cookOfList);
                result = "InsertOk";
            }
            
            //se esiste già una lista locale non vuota
            else {
                //controlla se esiste la ripetizione
                //trasforma string di cookie in arrayList
                List<String> productIdList = new ArrayList(Arrays.asList(cookOfList.getValue().split(",")));
                for (String string : productIdList) {
                    if (productId.equals(string)) {
                        //in caso esiste set flag true
                        repeatItem = true;

                    }
                }

                //se non cè la ripetizione
                if (!repeatItem) {
                    //aggiunge id nella cookie
                    cookOfList.setValue(cookOfList.getValue() + "," + productId);
                    cookOfList.setPath(getServletContext().getContextPath());
                    //aggiorna la vita di cookie
                    cookOfList.setMaxAge(60 * 60 * 24 * 30);
                    response.addCookie(cookOfList);
                    result = "InsertOk";
                }

                //se cè una ripetizione
                else {
                    result = "InsertFail";
                }
            }

        }
        //in caso elimina un prodotto dalla lista
        else if (action.equals("delete")) {

            //flag per sapere l'avvenuta della eliminazione
            boolean successDelete = false;

                        //get id prodotto da eliminare
                        String productId = request.getParameter("productId");
                        //se id prodotto è nullo
                        CheckErrorUtils.isNull(productId, rb.getString("error.missingProductId"));

            //get la cookie della lista locale
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
            if (cookOfList == null || cookOfList.equals("")) {
                //errore nella cancellazione
                result = "DeleteFail";

            }
            //se esiste una lista locale non vuota
            else {
                //controlla se esiste tale prodotto in lista
                //trasforma string di cookie in arrayList
                List<String> productIdList = new ArrayList(Arrays.asList(cookOfList.getValue().split(",")));
                for (int i = 0; i < productIdList.size(); i++) {
                    if (productId.equals(productIdList.get(i))) {
                        //quando trova, elimina tale elemento dalla arrayList
                        productIdList.remove(i);
                        //set flag true
                        successDelete = true;
                        //esce dal ciclo
                        i = productIdList.size();
                    }
                }

                //se non ha trovato prodotto
                if (!successDelete) {
                    result = "DeleteFail";
                }

                //se ha trovato
                else {

                    //aggiorna cookie
                    cookOfList.setValue(String.join(",", productIdList));
                    cookOfList.setPath(getServletContext().getContextPath());
                    //aggiorna la vita di cookie
                    cookOfList.setMaxAge(60 * 60 * 24 * 30);
                    response.addCookie(cookOfList);
                    //set il risultato
                    result = "DeleteOk";
                }

            }

        }

                //in caso cambia la categoria della lista locale
                else if (action.equals("changeListCategory"))
                {
                        //get id categoria
                        String categoryList = request.getParameter("categoryList");
                        //se id è vuota
                        if (categoryList == null || categoryList.equals("-1"))
                        {
                                throw new ServletException(rb.getString("error.missingCategoryListId"));
                        }
                        //crea o aggiorna cookie della categoria per la lista locale
                        Cookie cookOfListCategory = new Cookie("localShoppingListCategory", categoryList);
                        cookOfListCategory.setPath(getServletContext().getContextPath());
                        //aggiorna la vita di cookie
                        cookOfListCategory.setMaxAge(60 * 60 * 24 * 30);
                        response.addCookie(cookOfListCategory);
                        //set il risultato
                        result = "ChangeListCategoryOk";

                        //eliminare cookie di  check di negozio in vicinanza per riattivare check
                        Cookie NearShopChecked = new Cookie("NearShopChecked", "");
                         NearShopChecked.setPath(getServletContext().getContextPath());
                        //set la vita di cookie per 0 secondi
                        NearShopChecked.setMaxAge(0);
                        response.addCookie(NearShopChecked);

                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }
                //passare lo risultato  di inserimento
                request.getSession().setAttribute("result", result);
                response.sendRedirect(response.encodeRedirectURL(prevUrl));

        }

}
