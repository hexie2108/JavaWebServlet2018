package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * occupa la visualizzazione la pagia con form per la login
 */
public class LoginServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/login.jsp";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String prevUrl = null;
                
                prevUrl = request.getParameter(FormValidator.PREV_URL_KEY);
                //controlla se l'accesso sono dovuto dal filtro di login check
                //se non c'è preUrl dal parametro, set preUrl come URL di provenienza
                if (prevUrl == null)
                {
                        prevUrl = request.getHeader("Referer");
                        //se URL di provenienza sono le pagine di user system, ignorarlo
                        for (int i = 0; i < FormValidator.pageNameOfUserSystem.length; i++)
                        {
                                if (prevUrl != null && prevUrl.contains(FormValidator.pageNameOfUserSystem[i]))
                                {
                                        prevUrl = null;
                                }
                        }
                }
                //se parametro non è vuoto, significa la pagina di provenienza sono accessibile solo da utente autenticato, quindi url per bottone cancell deve essere home del sito
                else{
                        request.setAttribute("cancell", "cancell");
                }

                //se è nullo, set la provenienza come string vuota
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }

                //set il titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, "login");
                request.setAttribute(FormValidator.PREV_URL_KEY, prevUrl);
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
        }

}


