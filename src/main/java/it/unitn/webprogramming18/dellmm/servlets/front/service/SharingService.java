package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per inserire/modificare/eliminare il permesso condiviso
 *
 * @author mikuc
 */
public class SharingService extends HttpServlet {

    private PermissionDAO permissionDAO;
    private ListDAO listDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
            listDAO = daoFactory.getDAO(ListDAO.class);
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get PermissionDAO or ListDAO or UserDAO for user storage system", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //Languages bundle
        ResourceBundle rb = i18n.getBundle(request);
        
        //string che memorizza il risultato dell'operazione
        String result = null;

        //get l'azione che vuoi fare
        String action = request.getParameter("action");
        //se azione è nullo
        if(action == null){
            ServletUtility.sendError(request, response, 400, "users.errors.missingAction"); //manca il parametro action
            return;
	}
        //get id lista
        String listId = request.getParameter("listId");
        //se id lista è nullo
        CheckErrorUtils.isNull(listId, rb.getString("error.missingListId"));

        //solo il proprietario della lista può manipolare sulla condivisione
        //quindi ogni volta deve controllare prima se utente corrente è il proprietario della lista
        //get user corrente
        User user = (User) request.getSession().getAttribute("user");
        ShoppingList shoppingList = null;
        try {
            //get shoppingList bean
            shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
            //se lista è nullo
            CheckErrorUtils.isNull(shoppingList, rb.getString("error.ListNotExist"));
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        // controllare  se utente corrente è il proprietario della lista
        if (user.getId() != shoppingList.getOwnerId()) {
            ServletUtility.sendError(request, response, 400, "permission.shareListNotAllowed"); //"non sei il proprietario, non hai diritto di manipolare la sharing");
            return;
        }

        String modifyList = null;
        String deleteList = null;
        String addObject = null;
        String deleteObject = null;

        //in caso di inserimento
        if (action.equals("insert")) {
            //get email di utente da condividere
            String userEmail = request.getParameter("userEmail");

            if (userEmail == null || userEmail.equals("")) {
                ServletUtility.sendError(request, response, 400, "upgradeUserToAdmin.errors.emailMissing"); //"non esiste utente con tale email o email vuota
                return;
            }

            User userToShare = null;
            try {
                //get user beans dell'utente da condividere
                userToShare = userDAO.getByEmail(userEmail);
                //se  user beans  è  nullo
                if(userToShare == null){
                    ServletUtility.sendError(request, response, 400, "servlet.errors.noUserWithSuchEmail"); //non esiste l'utente con tale email
                    return;
                }

                //se utente da condividere ha già un permesso su questa lista (ripetizione)
                if (permissionDAO.getUserPermissionOnListByIds(userToShare.getId(), Integer.parseInt(listId)) != null) {
                    ServletUtility.sendError(request, response, 400, "servlet.errors.listAlreadySharedWithUser"); //Lista già condivisa con l'utente
                    return;
                }
            } catch (DAOException ex) {
                throw new ServletException(ex.getMessage(), ex);
            }

            //get i permessi che vuoi assegnare dai parametri
            modifyList = request.getParameter("modifyList");
            deleteList = request.getParameter("deleteList");
            addObject = request.getParameter("addObject");
            deleteObject = request.getParameter("deleteObject");

            //crea beans di permesso
            Permission permission = new Permission();
            permission.setListId(Integer.parseInt(listId));
            permission.setUserId(userToShare.getId());
            if (modifyList != null) {
                permission.setModifyList(true);
            }
            if (deleteList != null) {
                permission.setDeleteList(true);
            }
            if (addObject != null) {
                permission.setAddObject(true);
            }
            if (deleteObject != null) {
                permission.setDeleteObject(true);
            }

            //inserire il permesso in DB
            try {
                permissionDAO.insert(permission);
            } catch (DAOException ex) {
                new ServletException(ex.getMessage(), ex);
            }

            result = "sharingInsertOk";

        }

        //in caso di update
        else if (action.equals("update")) {
            //get id permesso
            String permissionId = request.getParameter("permissionId");
            //se il permissionId è  nullo
            CheckErrorUtils.isNull(permissionId, rb.getString("error.missingPermissionId"));

            //get beans di permesso
            Permission permission = null;
            try {
                permission = permissionDAO.getByPrimaryKey(Integer.parseInt(permissionId));
                //se il permesso è  nullo
                CheckErrorUtils.isNull(permissionId, rb.getString("error.PermissionNotExist"));
            } catch (DAOException ex) {
                throw new ServletException(ex.getMessage(), ex);
            }

            //get i permessi che vuoi modificare dai parametri
            modifyList = request.getParameter("modifyList");
            deleteList = request.getParameter("deleteList");
            addObject = request.getParameter("addObject");
            deleteObject = request.getParameter("deleteObject");
            //aggiornare i vari permessi
            if (modifyList != null) {
                permission.setModifyList(true);
            } else {
                permission.setModifyList(false);
            }
            if (deleteList != null) {
                permission.setDeleteList(true);
            } else {
                permission.setDeleteList(false);
            }
            if (addObject != null) {
                permission.setAddObject(true);
            } else {
                permission.setAddObject(false);
            }
            if (deleteObject != null) {
                permission.setDeleteObject(true);
            } else {
                permission.setDeleteObject(false);
            }

            //update il permesso in DB
            try {
                permissionDAO.update(permission);
            } catch (DAOException ex) {
                new ServletException(ex.getMessage(), ex);
            }

            result = "sharingUpdateOk";

        }

        //in caso di delete
        else if (action.equals("delete")) {

            //get id permesso
            String permissionId = request.getParameter("permissionId");
            //se il permissionId è  nullo
            CheckErrorUtils.isNull(permissionId, rb.getString("error.missingPermissionId"));

            //elimina il permesso
            try {
                permissionDAO.deletePermissionById(Integer.parseInt(permissionId));
            } catch (DAOException ex) {
                new ServletException(ex.getMessage(), ex);
            }

            result = "sharingDeleteOk";

        }

        //se valore di action è sconosciuto
        else {
            throw new ServletException(rb.getString("errors.unrecognizedAction"));
        }

        //ritorna alla pagina di provenienza
        String prevUrl = request.getHeader("Referer");
        if (prevUrl == null) {
            prevUrl = getServletContext().getContextPath();
        }
        //passare lo risultato  di inserimento
        request.getSession().setAttribute("result", result);
        response.sendRedirect(response.encodeRedirectURL(prevUrl));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
