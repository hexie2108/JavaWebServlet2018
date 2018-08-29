package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.i18n;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * visualizza la pagina per inviare email di reset password
 *
 * @author mikuc
 */
public class ForgotPasswordServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/forgotPassword.jsp";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                ResourceBundle rb = i18n.getBundle(request);

                //set il titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, rb.getString("frontPage.title.forgotPassword"));
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
        }
}
