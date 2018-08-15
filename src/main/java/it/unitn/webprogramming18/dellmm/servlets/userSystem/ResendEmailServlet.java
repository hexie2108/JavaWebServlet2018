package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * page per visuallizare il form per chiedere rinvio di email
 *
 * @author mikuc
 */
public class ResendEmailServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/resendEmail.jsp";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //set il titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, "resend email");
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
        }

}
