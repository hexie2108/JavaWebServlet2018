package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.MD5Utils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
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
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                //get i parametri necessari
                String email = request.getParameter(FormValidator.EMAIL_KEY);
                String password = request.getParameter(FormValidator.FIRST_PWD_KEY);
                String prevUrl = request.getParameter(FormValidator.PREV_URL_KEY);
                String remember = request.getParameter(FormValidator.REMEMBER_KEY);

        if(!FormValidator.validateEmail(email)) {
            ServletUtility.sendError(request, response, 400, "validateUser.errors.EMAIL_NOT_VALID");
            return;
        }
        if(password == null){
            ServletUtility.sendError(request, response, 400, "validateUser.errors.PASSWORD_MISSING"); //il parametro password è nullo
            return;
        }

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
            ServletUtility.sendError(request, response, 400, "servlet.errors.noUserWithSuchEmailAndPwd"); //No user con email o pwd
            return;
        }

        if (user.getVerifyEmailLink() != null) //servlet.errors.accountWaitingForActivation
        {
            ServletUtility.sendError(request, response, 400, "servlet.errors.accountWaitingForActivation"); //Attesa di attivazione
            return;
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
                                throw new ServletException(rb.getString("errors.unsupportedEncoding"), ex);
                        }
                        catch (NoSuchAlgorithmException ex)
                        {
                                throw new ServletException(rb.getString("errros.noSuchAlgorithmMD5"), ex);
                        }
                }

                //check se utente ha accettato Privacy
                //se non ha ancora fatto, visualizza popup di privacy
                if(!user.isAcceptedPrivacy()){
                        request.getSession().setAttribute("result", "privacy");
                }

                //eliminare cookie di  check di negozio in vicinanza per riattivare check
                Cookie NearShopChecked = new Cookie("NearShopChecked", "");
                NearShopChecked.setPath(getServletContext().getContextPath());
                //set la vita di cookie per 0 secondi
                NearShopChecked.setMaxAge(0);
                response.addCookie(NearShopChecked);
                //eliminare cookie di  notifica
                Cookie notifica = new Cookie("notifica", "");
                notifica.setPath(getServletContext().getContextPath());
                //set la vita di cookie per 0 secondi
                notifica.setMaxAge(0);
                response.addCookie(notifica);

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
