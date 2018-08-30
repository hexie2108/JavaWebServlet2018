/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.MD5Utils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * il servizio che effettua controlla esistenza di email, controlla lo stato di
 * attivazione ,e test di login
 *
 * @author mikuc
 */
public class CheckUserService extends HttpServlet
{

        private UserDAO userDAO;
        private PermissionDAO permissionDAO;

        /**
         * inizializza DAO
         *
         * @throws ServletException
         */
        @Override
        public void init() throws ServletException
        {
                DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
                if (daoFactory == null)
                {
                        throw new ServletException("Impossible to get db factory for user storage system");
                }

                try
                {
                        userDAO = daoFactory.getDAO(UserDAO.class);
                        permissionDAO = daoFactory.getDAO(PermissionDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get UserDAO for user storage system", ex);
                }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                //get l'azione che vuoi fare
                String action = request.getParameter("action");
                //se azione è nullo
                if (action == null)
                {
                        ServletUtility.sendError(request, response, 400, rb.getString("users.errors.missingAction")); //manca il parametro action
                        return;
                }
                //get email
                String email = request.getParameter("email");
                //se email è nullo
                if (email == null)
                {
                        ServletUtility.sendError(request, response, 400, rb.getString("validateUser.errors.EMAIL_MISSING")); //il parametro email è nullo
                        return;
                }

                if (action.equals("existence"))
                {
                        boolean esistence = false;
                        try
                        {
                                esistence = userDAO.checkExistenceOfEmail(email);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        response.setContentType("text/plain; charset=utf-8");
                        response.getWriter().print(esistence ? "1" : "0");
                }
                else if (action.equals("login"))
                {
                        //get password
                        String password = request.getParameter("password");
                        //se password è null
                        if (password == null)
                        {
                                ServletUtility.sendError(request, response, 400, rb.getString("validateUser.errors.PASSWORD_MISSING")); //manca il parametro password
                                return;
                        }

                        int result = -1;
                        User user = null;
                        try
                        {
                                user = userDAO.getByEmailAndPassword(email, MD5Utils.getMD5(password));
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

                        //in caso, email e password non corrisponde
                        if (user == null)
                        {
                                result = 0;
                        }
                        //in caso, account non è ancora attivato
                        else if (user.getVerifyEmailLink() != null)
                        {
                                result = 1;
                        }
                        //in caso di account normale già attivato
                        else
                        {
                                result = 2;
                        }

                        response.setContentType("text/plain; charset=utf-8");
                        response.getWriter().print(result);

                }
                //in caso di check lo stato di attivazione di utente
                else if (action.equals("checkActivationStatus"))
                {

                        User user = null;
                        int result;
                        try
                        {
                                user = userDAO.getByEmail(email);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        //se utente non esiste
                        if (user == null)
                        {
                                result = 0;
                        }
                        //se utente è già attivato
                        else if (user.getVerifyEmailLink() == null)
                        {
                                result = 1;
                        }
                        //ok
                        else
                        {
                                result = 2;
                        }

                        response.setContentType("text/plain; charset=utf-8");
                        response.getWriter().print(result);

                }

                else if (action.equals("existencePermission"))
                {
                        int result = 0;
                        try
                        {
                                User user = userDAO.getByEmail(email);
                                //se utente esite
                                if (user != null)
                                {
                                        result = 1;
                                        //get listId
                                        String listId = request.getParameter("listId");
                                        if (listId == null)
                                        {
                                                ServletUtility.sendError(request, response, 400, rb.getString("error.missingListId")); //manca il parametro password
                                                return;
                                        }
                                        //se non ha ancora permesso su tale lista
                                         if(permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId)) == null){
                                                result = 2;
                                         }
                                }
                                
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        
                        response.setContentType("text/plain; charset=utf-8");
                        response.getWriter().print(result);

                      

                }

                //se valore di action è sconosciuto
                else
                {
                        throw new ServletException(rb.getString("errors.unrecognizedAction"));

                }
        }

}
