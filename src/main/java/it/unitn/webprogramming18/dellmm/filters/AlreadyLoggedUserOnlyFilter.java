package it.unitn.webprogramming18.dellmm.filters;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * se utente è loggato e sta navigando le pagine di userSystem dedicato per user
 * non loggato
 *
 */
public class AlreadyLoggedUserOnlyFilter implements Filter
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

                // Se esiste la sessione oppure l'utente  è autenticato
                if (session != null && session.getAttribute("user") != null)
                {
                        //get il percorso base
                        String contextPath = request.getServletContext().getContextPath();
                        //indirizza a home
                        response.sendRedirect(contextPath);
                }
                else
                {


                        chain.doFilter(req, resp);
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
