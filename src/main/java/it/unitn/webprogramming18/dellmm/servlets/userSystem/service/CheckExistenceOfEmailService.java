/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * il servizio che controlla esistenza di email ritorna true se esiste, false se
 * non esiste
 *
 * @author mikuc
 */
public class CheckExistenceOfEmailService extends HttpServlet
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
                //invoca doGet
                doGet(request, response);
        }
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //get email
                String email = request.getParameter("email");
                //se email è nullo
                CheckErrorUtils.isNull(email, "manca il parametro email");
                
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
        
}
