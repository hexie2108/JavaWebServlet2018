package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminUserOnlyFilter")
public class AdminUserOnlyFilter implements Filter {
    private void refuse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Non fare caching di errori
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
 

        ServletUtility.sendError(request, response, 401, "generic.errors.userNotAdmin");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        boolean authorized =
                session != null &&
                        session.getAttribute("user") != null &&
                        ((User) session.getAttribute("user")).isIsAdmin();

        // Se l'utente non è autenticato
        if (!authorized) {
            refuse(request, response);
            return;
        }

        // Se l'utente è autenticato facciamo continuare
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
