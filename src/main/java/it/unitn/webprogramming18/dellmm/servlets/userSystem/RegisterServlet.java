package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.i18n;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class RegisterServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/register.jsp";

        /**
         * occupa la visualizzazione la pagia con form per la registrazione
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

               String prevUrl = request.getHeader("Referer");
                //se URL di provenienza sono le pagine di user system, ignorarlo
                for (int i = 0; i < FormValidator.pageNameOfUserSystem.length; i++)
                {
                        if (prevUrl != null && prevUrl.contains(FormValidator.pageNameOfUserSystem[i]))
                        {
                                prevUrl = null;
                        }
                }
                //se è nullo, set la provenienza come string vuota
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }

                ResourceBundle rb = i18n.getBundle(request);
                //set il titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, rb.getString("login.label.register"));
                //set url di provenienza
                request.setAttribute(FormValidator.PREV_URL_KEY, prevUrl);
                //inoltra a jsp
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);

        }

}
