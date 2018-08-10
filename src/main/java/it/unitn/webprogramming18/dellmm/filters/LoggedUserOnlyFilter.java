package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * se utente non è loggato e ha fatto una richiesta di JSON oppure POST
 * rindirizza alla pagina 401
 *
 * se utente non è loggato e ha fatto una richiesta GET, rindirizza alla pagina
 * di Login.
 */
public class LoggedUserOnlyFilter implements Filter
{

        @Override
        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
        {

                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) resp;

                // Non fare caching
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", -1);
                response.setCharacterEncoding("UTF-8");

                //get la sessione
                HttpSession session = request.getSession(false);

                // Se non esiste la sessione oppure l'utente non è autenticato
                if (session == null || session.getAttribute("user") == null)
                {
                        refuse(request, response);
                        return;
                }

                // Se l'utente è autenticato facciamo continuare
                chain.doFilter(req, resp);
        }

        private void refuse(HttpServletRequest request, HttpServletResponse response) throws IOException
        {
                //get la parte di url esclude dominio es: /index.jsp
                String uri = request.getRequestURI();

                //se nel url contiene parola JSON oppure il metodo di richiesta non è get
                if (uri.matches("^.*\\.json$") || !request.getMethod().equalsIgnoreCase("GET"))
                {
                        ServletUtility.sendError(request, response, 401, "generic.errors.userNotLogged");
                }

                // Se l'url non finisce con .json ed è un get 
                else
                {
                        //faccio un redirect alla pagina di login mantenendo l'url

                        // richiesta a login avvenuto con successo
                        //get il percorso base
                        String contextPath = request.getServletContext().getContextPath();
                        if (!contextPath.endsWith("/"))
                        {
                                contextPath += "/";
                        }

                        String prevUrl = request.getParameter("prevUrl");
                        if (prevUrl == null)
                        {
                                prevUrl = contextPath;
                        }

                        // memorizza url della richiesta attuale in nextUrl,  che permette utente di ritornerà a questa pagina dopo il login
                        String nextUrl = request.getRequestURI();

                        response.sendRedirect(contextPath + ConstantsUtils.LOGIN + "?"
                                    + "prevUrl" + "=" + URLEncoder.encode(prevUrl, "UTF-8")
                                    + "&" + "nextUrl" + "=" + URLEncoder.encode(nextUrl, "UTF-8")
                        );
                }
        }

        @Override
        public void init(FilterConfig config) throws ServletException
        {
        }

        @Override
        public void destroy()
        {
        }
}
