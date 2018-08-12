package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AlreadyLoggedInServlet extends HttpServlet {

    private static final String ALREADY_LOGGED_IN_JSP = "/WEB-INF/jsp/userSystem/alreadyLoggedIn.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ALREADY_LOGGED_IN_JSP).forward(request, response);
    }
}
