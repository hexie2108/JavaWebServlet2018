package it.unitn.webprogramming18.dellmm.servlets.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UsersServlet")
public class UsersServlet extends HttpServlet {
    private static final String JSP_PAGE = "/WEB-INF/jsp/admin/users.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
    }
}
