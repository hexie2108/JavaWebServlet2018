/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;

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
        permissionDAO = new JDBCPermissionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //invoca doGet
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //get id lista
        String listId = request.getParameter("listId");
        //se listId è nullo
        CheckErrorUtils.isNull(listId, "manca il parametro id lista");

        //get user corrente
        User user = (User) request.getSession().getAttribute("user");
        //get permesso dell'utente su tale lista
        Permission permission;
        try {
            permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
            //se il permesso è  vuoto
            CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");
        } catch (DAOException ex) {
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
