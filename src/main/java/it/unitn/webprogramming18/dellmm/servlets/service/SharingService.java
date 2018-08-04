package it.unitn.webprogramming18.dellmm.servlets.service;

import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per inserire/modificare/eliminare il permesso condiviso
 *
 * @author mikuc
 */
public class SharingService extends HttpServlet
{

        private PermissionDAO permissionDAO;
        private ListDAO listDAO;
        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {
                permissionDAO = new JDBCPermissionDAO();
                listDAO = new JDBCListDAO();
                userDAO = new JDBCUserDAO();
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {

                //string che memorizza il risultato dell'operazione
                String result = null;

                //get l'azione che vuoi fare
                String action = request.getParameter("action");
                //get id lista
                String listId = request.getParameter("listId");

                if (action == null || listId == null)
                {
                        throw new ServletException("manca il parametro action o listId");
                }

                //solo il proprietario della lista può manipolare sulla condivisione
                //quindi ogni volta deve controllare prima se utente corrente è il proprietario della lista
                //get user corrente
                User user = (User) request.getSession().getAttribute("user");
                ShoppingList shoppingList = null;
                try
                {
                        //get shoppingList bean
                        shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                if (shoppingList == null)
                {
                        throw new ServletException("hai dato un lista id invalido");
                }
                // controllare  se utente corrente è il proprietario della lista
                if (user.getId() != shoppingList.getOwnerId())
                {
                        throw new ServletException("non sei il proprietario, non hai diritto di manipolare la sharing");
                }

                String modifyList = null;
                String deleteList = null;
                String addObject = null;
                String deleteObject = null;

                //in caso di inserimento
                if (action.equals("insert"))
                {
                        //get email di utente da condividere
                        String userEmail = request.getParameter("userEmail");

                        if (userEmail == null || userEmail.equals(""))
                        {
                                throw new ServletException("manca il parametro userEmail o userEmail è vuoto");
                        }

                        User userToShare = null;
                        try
                        {
                                //get user beans dell'utente da condividere
                                userToShare = userDAO.getByEmail(userEmail);
                                //se  user beans  è  vuoto
                                if (userToShare == null)
                                {
                                        throw new ServletException("non esiste l'utente con tale email");
                                }
                                //se utente da condividere ha già un permesso su questa lista (ripetizione)
                                if (permissionDAO.getUserPermissionOnListByIds(userToShare.getId(), Integer.parseInt(listId)) != null)
                                {
                                        throw new ServletException("l'utente è stato già condiviso");
                                }
                        }
                        catch (DAOException ex)
                        {
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
                        if (modifyList != null)
                        {
                                permission.setModifyList(true);
                        }
                        if (deleteList != null)
                        {
                                permission.setDeleteList(true);
                        }
                        if (addObject != null)
                        {
                                permission.setAddObject(true);
                        }
                        if (deleteObject != null)
                        {
                                permission.setDeleteObject(true);
                        }

                        //inserire il permesso in DB
                        try
                        {
                                permissionDAO.insert(permission);
                        }
                        catch (DAOException ex)
                        {
                                new ServletException(ex.getMessage(), ex);
                        }

                        result = "sharingInsertOk";

                }

                //in caso di update
                else if (action.equals("update"))
                {
                        //get id permesso
                        String permissionId = request.getParameter("permissionId");
                        if (permissionId == null)
                        {
                                throw new ServletException("manca il parametro permission Id");
                        }

                        //get beans di permesso 
                        Permission permission = null;
                        try
                        {
                                permission = permissionDAO.getByPrimaryKey(Integer.parseInt(permissionId));
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        //se il permesso è  nullo
                        if (permission == null)
                        {
                                throw new ServletException("non esiste il permesso con tale id");
                        }

                        //get i permessi che vuoi modificare dai parametri
                        modifyList = request.getParameter("modifyList");
                        deleteList = request.getParameter("deleteList");
                        addObject = request.getParameter("addObject");
                        deleteObject = request.getParameter("deleteObject");
                        //aggiornare i vari permessi
                        if (modifyList != null)
                        {
                                permission.setModifyList(true);
                        }
                        else
                        {
                                permission.setModifyList(false);
                        }
                        if (deleteList != null)
                        {
                                permission.setDeleteList(true);
                        }
                        else
                        {
                                permission.setDeleteList(false);
                        }
                        if (addObject != null)
                        {
                                permission.setAddObject(true);
                        }
                        else
                        {
                                permission.setAddObject(false);
                        }
                        if (deleteObject != null)
                        {
                                permission.setDeleteObject(true);
                        }
                        else
                        {
                                permission.setDeleteObject(false);
                        }

                        //update il permesso in DB
                        try
                        {
                                permissionDAO.update(permission);
                        }
                        catch (DAOException ex)
                        {
                                new ServletException(ex.getMessage(), ex);
                        }

                        result = "sharingUpdateOk";

                }

                //in caso di delete
                else if (action.equals("delete"))
                {

                        //get id permesso
                        String permissionId = request.getParameter("permissionId");
                        if (permissionId == null)
                        {
                                throw new ServletException("manca il parametro permission Id");
                        }
                        //elimina il permesso
                        try
                        {
                                permissionDAO.deletePermissionById(Integer.parseInt(permissionId));
                        }
                        catch (DAOException ex)
                        {
                                new ServletException(ex.getMessage(), ex);
                        }

                        result = "sharingDeleteOk";

                }

                //se valore di action è sconosciuto
                else
                {

                        new ServletException("valore di action non riconosciuto");

                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }
                //passare lo risultato  di inserimento
                request.getSession().setAttribute("result", result);
                response.sendRedirect(response.encodeRedirectURL(prevUrl));

        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {
                doGet(request, response);
        }

}
