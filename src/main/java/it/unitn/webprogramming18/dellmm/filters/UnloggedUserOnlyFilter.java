package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
 
@WebFilter(filterName = "UnloggedUserOnlyFilter")
public class UnloggedUserOnlyFilter implements Filter
{

        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
        {
                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) resp;

                HttpSession session = request.getSession(false);

                // Se l'utente è autenticato
                if (session != null && session.getAttribute("user") != null)
                {

                        if (!request.getMethod().equalsIgnoreCase("GET"))
                        {
                                //  Se la richiesta non è un get la rigettiamo immediatamente

                                response.sendError(401, "Authentication required");
                        }
                        else
                        {
                                // Se la richiesta è un get facciamo un redirect alla pagina che chiede all'utente se fare logout
                                // cercando di mantenere l'url  in nextUrl in modo da reindirizzare l'utente automaticamente a
                                // login avvenuto con successo

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

                                response.sendRedirect(
                                            contextPath + PagePathsConstants.ALREADY_LOGGED_IN + "?"
                                            + "prevUrl" + "=" + URLEncoder.encode(prevUrl, "UTF-8")
                                            + "&" + "nextUrl" + "=" + URLEncoder.encode(request.getRequestURI(), "UTF-8")
                                );
                        }

                        return;
                }

                // Se l'utente non è autenticato facciamo continuare
                chain.doFilter(req, resp);
        }

        public void init(FilterConfig config) throws ServletException
        {
        }

        public void destroy()
        {
        }
}
