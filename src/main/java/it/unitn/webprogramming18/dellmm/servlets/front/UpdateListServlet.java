package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet della pagina per inserire/modificare la lista
 *
 * @author luca_morgese
 */
public class UpdateListServlet extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/updateList.jsp";

    private PermissionDAO permissionDAO;
    private ListDAO listDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
            listDAO = daoFactory.getDAO(ListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get PermissionDAO or ListDAO for user storage system", ex);
        }
    }

    /**
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

                String listId = request.getParameter("listId");
                //se listId = nullo, allora siamo in caso di creazione di una nuova lista

                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                if (listId == null)
                {
                        //set titolo della pagina
                        request.setAttribute(ConstantsUtils.HEAD_TITLE, rb.getString("frontPage.title.newList"));
                }
                //in caso di update della lista esistente
                else
                {
                        //get user corrente
                        User user = (User) request.getSession().getAttribute("user");



                        ShoppingList shoppingList = null;
                        Permission permission = null;
                        try
                        {
                                //get list bean
                                shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                                //se lista è nullo
                                if(shoppingList == null) {
                                    ServletUtility.sendError(request, response, 400, "error.ListNotExist");
                                    return;
                                }

                                //get permesso dell'utente su tale lista
                                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                                //se il permesso è  nullo
                                if (permission == null)
                                {
                                        ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList"); //non hai nessun permesso su tale lista
                                        return;
                                }
                                //se utente non ha il permesso di modificare la lista
                                if (!permission.isModifyList())
                                {
                                        ServletUtility.sendError(request, response, 400, "permission.modifyListNotAllowed"); //non hai il permesso di modificare tale lista
                                        return;
                                }
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }

                        //set titolo della pagina
                        request.setAttribute(ConstantsUtils.HEAD_TITLE, rb.getString("frontPage.title.updateList"));
                        //set beans di shoppingList nella richiesta
                        request.setAttribute("list", shoppingList);

                }

                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
        }

}
