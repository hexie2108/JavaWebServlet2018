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

                //se utente non è ancora loggato
                if (request.getSession().getAttribute("user") == null)
                {

                        prevUrl = request.getParameter(FormValidator.PREV_URL_KEY);
                        //se è nullo, set URL di provenienza
                         if(prevUrl==null)
                        {
                               prevUrl = request.getHeader("Referer");
                        }

                       //se è nullo, set la provenienza come string vuota
                        if(prevUrl==null)
                        {
                                prevUrl ="";
                        }

                        //set il titolo della pagina
                        request.setAttribute(ConstantsUtils.HEAD_TITLE, "login");
                        request.getRequestDispatcher(JSP_PAGE_PATH+"?"+FormValidator.PREV_URL_KEY+"="+prevUrl).forward(request, response);
                }

                //se utente è già loggato
                else
                {
                        prevUrl = getServletContext().getContextPath();
                        response.sendRedirect(response.encodeRedirectURL(prevUrl));
                }
        }

}
