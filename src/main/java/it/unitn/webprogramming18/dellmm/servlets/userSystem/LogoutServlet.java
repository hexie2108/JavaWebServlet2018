package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.servlet.http.Cookie;

public class LogoutServlet extends HttpServlet
{

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                HttpSession session = request.getSession(false);
                if (session != null)
                {
                        session.invalidate();
                        session = null;
                        //crea un cookie vuoto con time 0 per eliminare cookie vecchio
                        Cookie cookie = new Cookie("autoLoginKey", "");
                        cookie.setPath(request.getServletContext().getContextPath());
                        //set la vita di cookie per 0 secondi
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }
                response.sendRedirect(response.encodeRedirectURL(prevUrl));

        }
}
