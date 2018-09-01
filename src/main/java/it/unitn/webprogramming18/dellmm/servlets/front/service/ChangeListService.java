/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * il servizio che occupa lo scambio di lista nelle pagine front-end
 *
 * @author mikuc
 */
public class ChangeListService extends HttpServlet {

    private PermissionDAO permissionDAO;

    /**
     * inizializza DAO
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get PermissionDAO for user storage system", ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //invoca doGet
        doGet(request, response);
    }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //get id lista
                String listId = request.getParameter("listId");
                //se listId è nullo
               if (listId == null) {
                   ServletUtility.sendError(request, response, 400, "error.missingListId");
                   return;
               }

                //get user corrente
                User user = (User) request.getSession().getAttribute("user");
                //get permesso dell'utente su tale lista
                Permission permission;
                try
                {
                        permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                        //se il permesso è  vuoto
                        if (permission == null)
                        {
                                ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList");
                                return;
                        }
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

        //memorizza  id della nuova lista
        request.getSession().setAttribute("myListId", Integer.parseInt(listId));

        //ritorna alla pagina di provenienza
        String prevUrl = request.getHeader("Referer");
        if (prevUrl == null) {
            prevUrl = getServletContext().getContextPath();
        }
        response.sendRedirect(response.encodeRedirectURL(prevUrl));

    }

}
