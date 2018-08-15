package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.MD5Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.Cookie;

/**
 * il servizio di login generale
 *
 * @author mikuc
 */
public class LoginService extends HttpServlet
{

        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {

                userDAO = new JDBCUserDAO();

        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //get i parametri necessari
                String email = request.getParameter(FormValidator.EMAIL_KEY);
                String password = request.getParameter(FormValidator.FIRST_PWD_KEY);
                String prevUrl = request.getParameter(FormValidator.PREV_URL_KEY);
                String remember = request.getParameter(FormValidator.REMEMBER_KEY);

                CheckErrorUtils.isFalse(FormValidator.validateEmail(email), "email non è valido");
                CheckErrorUtils.isNull(password, "il parametro password è nullo");

                User user = null;
                String passwordMD5 = null;
                try
                {
                        passwordMD5 = MD5Utils.getMD5(password);
                        user = userDAO.getByEmailAndPassword(email, passwordMD5);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                catch (UnsupportedEncodingException ex)
                {
                        throw new ServletException("errore durente la codifica dei caratteri", ex);
                }
                catch (NoSuchAlgorithmException ex)
                {
                        throw new ServletException("errore per la mancanza dell'algoritmo MD5 in ambiente di esecuzione", ex);
                }

                if (user == null)
                {
                        throw new ServletException("non esiste utente con tale email e password");
                }

                if (user.getVerifyEmailLink() != null)
                {
                        throw new ServletException("account in attesa di attivazione");
                }

                // set user beans in session
                request.getSession().setAttribute("user", user);

                //se utente ha attivato funzione ricordarmi
                if (remember != null)
                {
                        try
                        {
                                //aggiorna il tempo di l'ultimo login
                                long currentTimeMills = System.currentTimeMillis();
                                //genera key for fastlogin
                                String fastLoginkey = MD5Utils.getMD5(passwordMD5 + currentTimeMills);
                                //memorizza il tempo di l'ultimo login in secondo e fastLoginKey
                                userDAO.updateLastLoginTimeAndFastLoginKey(user.getId(), currentTimeMills, fastLoginkey);

                                //memorizza la key in cookie
                                //crea o aggiorna cookie della categoria per la lista locale
                                Cookie cookie = new Cookie("autoLoginKey", fastLoginkey);
                                cookie.setPath(getServletContext().getContextPath());
                                //set la vita di cookie per 30 giorni
                                cookie.setMaxAge(60 * 60 * 24 * 30);
                                response.addCookie(cookie);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        catch (UnsupportedEncodingException ex)
                        {
                                throw new ServletException("errore durente la codifica dei caratteri", ex);
                        }
                        catch (NoSuchAlgorithmException ex)
                        {
                                throw new ServletException("errore per la mancanza dell'algoritmo MD5 in ambiente di esecuzione", ex);
                        }
                }

                // Se nextUrl è vuoto o nullo, usa pagina di default(index)
                if (prevUrl == null || prevUrl.isEmpty())
                {
                        String contextPath = getServletContext().getContextPath();
                        if (!contextPath.endsWith("/"))
                        {
                                contextPath += "/";
                        }
                        prevUrl = contextPath;
                }

                response.sendRedirect(response.encodeRedirectURL(prevUrl));

        }
}
