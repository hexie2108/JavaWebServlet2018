/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.filters;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * filtro che occupa la funziona di fast login
 *
 * @author mikuc
 */
public class AutoLoginFilter implements Filter
{

        private UserDAO userDAO;
        private String fastLoginkey = null;
        private Cookie[] cookies = null;
        private User user = null;

        private HttpServletRequest request = null;
        private HttpServletResponse response = null;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
                this.request = (HttpServletRequest) request;
                this.response = (HttpServletResponse) response;

                //get la sessione
                HttpSession session = this.request.getSession();

                // Se  l'utente non è autenticato
                if (session.getAttribute("user") == null)
                {

                        //inizializza variabile a null;
                        fastLoginkey = null;
                        //get la cookie dell'utente
                        cookies = this.request.getCookies();
                        //se utente ha la cookie di auto login key
                        if (cookies != null && cookies.length > 0)
                        {
                                for (Cookie cookie : cookies)
                                {
                                        if (cookie.getName().equals("autoLoginKey"))
                                        {
                                                fastLoginkey = cookie.getValue();
                                        }
                                }
                        }
                        //se fast login key non è vuoto, check key e prova a effettuare auto login
                        if (fastLoginkey != null)
                        {
                                try
                                {
                                        user = userDAO.getUserByFastLoginKey(fastLoginkey, System.currentTimeMillis());
                                }
                                catch (DAOException ex)
                                {
                                        throw new ServletException(ex.getMessage(), ex);
                                }
                                //se è riuscito a loggare, set user in session
                                if (user != null)
                                {
                                        session.setAttribute("user", user);
                                }
                                //se fast login key non è valido, cancella cookie, per evitare di rifare auto login
                                else
                                {
                                        //crea un cookie vuoto con time 0 per eliminare cookie vecchio
                                        Cookie cookie = new Cookie("autoLoginKey", "");
                                        cookie.setPath(request.getServletContext().getContextPath());
                                        //set la vita di cookie per 0 secondi
                                        cookie.setMaxAge(0);
                                       this.response.addCookie(cookie);
                                }
                        }
                }

                chain.doFilter(request, response);
        }

        @Override
        public void destroy()
        {
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            DAOFactory daoFactory = (DAOFactory) filterConfig.getServletContext().getAttribute("daoFactory");
            if (daoFactory == null) {
                    throw new ServletException("Impossible to get db factory for user storage system");
            }

            try {
                    userDAO = daoFactory.getDAO(UserDAO.class);
            } catch (DAOFactoryException ex) {
                    throw new ServletException("Impossible to get UserDAO for user storage system", ex);
            }
        }

}
