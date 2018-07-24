package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

@WebFilter(filterName = "LoggedUserOnlyFilter")
public class LoggedUserOnlyFilter implements Filter {

    private void refuse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = null;
        String uri = request.getRequestURI();

        // Non fare caching di errori
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");


        if (uri.matches("^.*\\.json$")) {
            // Se l'url finisce con .json rispondo con errore in json
            response.setContentType("application/json");
            response.setStatus(401);

            PrintWriter out = response.getWriter();
            out.println("{\"message\": \"User must be authenticated\"}");
        } else if (!request.getMethod().equalsIgnoreCase("GET")){
            // Se l'url non finisce con .json e non è un get rispondo con un semplice errore
            response.sendError(401, "User must be authenticated");

        } else {
            // Se l'url non finisce con .json ed è un get faccio un redirect alla pagina di login mantenendo l'url
            // richiesto in nextUrl in modo da reindirizzare l'utente automaticamente alla pagina precedentemente
            // richiesta a login avvenuto con successo

            String contextPath = request.getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            String prevUrl = request.getParameter("prevUrl");

            if (prevUrl == null) {
                prevUrl = contextPath;
            }

            response.sendRedirect(
                    contextPath + PagePathsConstants.LOGIN + "?" +
                            "prevUrl" + "=" + URLEncoder.encode(prevUrl, "UTF-8") +
                            "&" + "nextUrl" + "=" + URLEncoder.encode(request.getRequestURI(), "UTF-8")
            );
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        boolean authorized = session == null || session.getAttribute("user") == null;

        // Se l'utente non è autenticato
        if (authorized) {
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
