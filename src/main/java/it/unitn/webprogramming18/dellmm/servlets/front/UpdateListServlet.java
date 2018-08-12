package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        permissionDAO = new JDBCPermissionDAO();
        listDAO = new JDBCListDAO();

    }

    /**
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String listId = request.getParameter("listId");
        //se listId = nullo, allora siamo in caso di creazione di una nuova lista
        if (listId == null) {
            //set titolo della pagina
            request.setAttribute(ConstantsUtils.HEAD_TITLE, "crea la nuova lista");
        }

        //in caso di update della lista esistente
        else {
            //get user corrente
            User user = (User) request.getSession().getAttribute("user");

            ShoppingList shoppingList = null;
            Permission permission = null;
            try {
                //get list bean
                shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                //se lista è nullo
                CheckErrorUtils.isNull(shoppingList, "non eiste la lista con tale id lista");

                //get permesso dell'utente su tale lista
                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                //se il permesso è  nullo
                CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");
                //se utente non ha il permesso di modificare la lista
                CheckErrorUtils.isFalse(permission.isModifyList(), "non hai il permesso di modificare tale lista");

            } catch (DAOException ex) {
                throw new ServletException(ex.getMessage(), ex);
            }

            //set titolo della pagina
            request.setAttribute(ConstantsUtils.HEAD_TITLE, "aggriona la lista");
            //set beans di shoppingList nella richiesta
            request.setAttribute("list", shoppingList);

        }

        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
    }

}
