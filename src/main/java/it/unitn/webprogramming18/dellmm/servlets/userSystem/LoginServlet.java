package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

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

        private static final String LOGIN_JSP = "/WEB-INF/jsp/userSystem/login.jsp";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //set il titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, "login");
                request.getRequestDispatcher(LOGIN_JSP).forward(request, response);

        }

}
