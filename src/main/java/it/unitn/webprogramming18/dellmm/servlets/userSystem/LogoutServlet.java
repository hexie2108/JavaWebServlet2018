package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextUrl = request.getParameter("nextUrl");

        if (nextUrl == null) {
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            nextUrl = contextPath + "index.jsp";
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();

            session = null;
        }

        response.sendRedirect(nextUrl);
    }
}
