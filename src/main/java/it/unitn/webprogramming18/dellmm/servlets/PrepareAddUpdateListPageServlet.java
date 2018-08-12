/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author luca_morgese
 */
public class PrepareAddUpdateListPageServlet extends HttpServlet {

    private CategoryListDAO categoryListDAO;
    private PermissionDAO permissionDAO;
    private ListDAO listDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
            listDAO = daoFactory.getDAO(ListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get categoryListDAO, PermissionDAO or ListDAO for user storage system", ex);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {

            //Check on permission to modify list if a modify action has been selected
            //Assumed existing request parameters set by front end servlet-invoking jsp are being used
            //Could be done using only listId, without flags, but they could come in handy in certain contexts.
            Boolean modify = Boolean.valueOf(request.getParameter("modifyListFlag"));
            Boolean create = Boolean.valueOf(request.getParameter("createListFlag"));

            Integer listId = Integer.valueOf(request.getParameter("listId"));

            Permission userPermission;

            //If there's a listId parameter it should mean a modify action has been choosen
            if (listId != null) {
                //List bean
                ShoppingList list = new ShoppingList();
                try {
                    list = listDAO.getByPrimaryKey(listId);
                } catch (DAOException ex) {
                    Logger.getLogger(PrepareAddUpdateListPageServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                //If user is not owner and wants to modify the list
                if (!(user.getId() == list.getOwnerId()) && (create == false && modify == true)) {
                    try {
                        userPermission = permissionDAO.getUserPermissionOnListByIds(user.getId(), listId);

                        //User
                        if (!userPermission.isModifyList() && !user.isIsAdmin()) {
                            request.getRequestDispatcher("/WEB-INF/jsp/nonAuthorizedActionErrorPage").forward(request, response);
                        }

                    } catch (DAOException ex) {
                        Logger.getLogger(PrepareAddUpdateListPageServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //End check

            //Gets the list of all CategoryList
            java.util.List<CategoryList> listCategories;
            try {
                listCategories = categoryListDAO.getAll();
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to get the list categories");
            }

            request.setAttribute("listCategories", listCategories);
            request.getRequestDispatcher("/WEB-INF/jsp/addUpdateList").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for instantiating elements necessary to create/update a list";
    }

}
