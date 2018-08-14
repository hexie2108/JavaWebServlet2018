/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.MD5Utils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        /**
         * inizializza DAO
         *
         * @throws ServletException
         */
        @Override
        public void init() throws ServletException
        {
                userDAO = new JDBCUserDAO();
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                //get l'azione che vuoi fare
                String action = request.getParameter("action");
                //se azione è nullo
                CheckErrorUtils.isNull(action, "manca il parametro action");
                //get email
                String email = request.getParameter("email");
                //se email è nullo
                CheckErrorUtils.isNull(email, "manca il parametro email");

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
                        //get l'azione che vuoi fare
                        String password = request.getParameter("password");
                        //se azione è nullo
                        CheckErrorUtils.isNull(password, "manca il parametro password");

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
                                throw new ServletException("errore durente la codifica dei caratteri", ex);
                        }
                        catch (NoSuchAlgorithmException ex)
                        {
                                throw new ServletException("errore per la mancanza dell'algoritmo MD5 in ambiente di esecuzione", ex);
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

                //se valore di action è sconosciuto
                else
                {
                        throw new ServletException("valore di action non riconosciuto");

                }
        }

}
