package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * se utente è loggato e ha fatto una richiesta di JSON oppure POST rindirizza
 * alla pagina 401
 * <p>
 * se utente è loggato e ha fatto una richiesta GET, rindirizza alla pagina di
 * Login.
 */
@WebFilter(filterName = "UnloggedUserOnlyFilter")
public class UnloggedUserOnlyFilter implements Filter {

    private void refuse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        if (uri.matches("^.*\\.json$") || !request.getMethod().equalsIgnoreCase("GET")) {
            ServletUtility.sendError(request, response, 401, "generic.errors.userLogged");
        } else {
            // Se la richiesta è un get facciamo un redirect alla pagina che chiede all'utente se fare logout
            // cercando di mantenere l'url  in nextUrl in modo da reindirizzare l'utente automaticamente a
            // login avvenuto con successo

            String contextPath = request.getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            String prevUrl = request.getParameter("prevUrl");

            if (prevUrl == null) {
                prevUrl = contextPath;
            }

            // memorizza url della richiesta attuale in nextUrl,  che permette utente di ritornerà a questa pagina dopo il login
            response.sendRedirect(
                    contextPath + ConstantsUtils.ALREADY_LOGGED_IN + "?" +
                            "prevUrl" + "=" + URLEncoder.encode(prevUrl, "UTF-8") +
                            "&" + "nextUrl" + "=" + URLEncoder.encode(request.getRequestURI(), "UTF-8")
            );
        }
    }


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        // Se l'utente è autenticato
        if (session != null && session.getAttribute("user") != null) {
            refuse(request, response);
            return;
        }

        // Se l'utente non è autenticato facciamo continuare
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
