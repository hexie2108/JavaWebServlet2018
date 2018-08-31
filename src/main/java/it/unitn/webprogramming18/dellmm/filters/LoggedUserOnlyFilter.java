package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 *
 * se utente non è loggato , e richiede un url che non contiene "service",
 * memorizza url di pagina richiesta in nextUrl e rindirizza alla pagina di
 * Login. se utente non è loggato, e richiede un url che contiene "service",
 * memorizza url di pagina di provenienza in nextUrl e rindirizza alla pagina di
 * login
 *
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

                // nextUrl  permette utente di ritornerà a la pagina richiesta dopo aver fatto login
                String prevUrl = null;
                //se nel url contiene "service"
                if (uri.contains("service/"))
                {
                        //memorizza url della provenienza prima della richiesta
                        prevUrl = request.getHeader("Referer");
                }
                else
                {
                        // memorizza url della richiesta attuale,  
                        prevUrl = request.getRequestURI();
                }
                if (prevUrl == null)
                {
                        prevUrl = "";
                }

                //get il percorso base
                String contextPath = request.getServletContext().getContextPath();
                if (!contextPath.endsWith("/"))
                {
                        contextPath += "/";
                }

                response.sendRedirect(contextPath + ConstantsUtils.LOGIN + "?prevUrl=" + URLEncoder.encode(prevUrl, "UTF-8")
                );

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
