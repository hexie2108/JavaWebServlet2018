package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per inserire e eliminare il commento utente può inserire il commento
 * solo sulla lista che ha qualche permesso utente può eliminare solo il proprio
 * commento
 *
 * @author mikuc
 */
public class CommentService extends HttpServlet {

    private PermissionDAO permissionDAO;
    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
            commentDAO = daoFactory.getDAO(CommentDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get PermissionDAO or CommentDAO for user storage system", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //string che memorizza il risultato dell'operazione
        String result = null;
        //get l'azione che vuoi fare
        String action = request.getParameter("action");
        //se azione è nullo
        CheckErrorUtils.isNull(action, "manca il parametro action");

        //in caso di inserimento
        if (action.equals("insert")) {
            //get id lista
            String listId = request.getParameter("listId");
            //get testo di commento
            String commentText = request.getParameter("commentText");
            //se manca parametro id lista
            CheckErrorUtils.isNull(listId, "manca il parametro listId");
            //se manca testo di commento
            CheckErrorUtils.isNull(commentText, "manca il parametro commentText");
            //se il  testo di commento è vuoto
            if ("".equals(commentText)) {
                throw new ServletException("il contenuto del commento è vuoto");
            }

            //get user corrente
            User user = (User) request.getSession().getAttribute("user");
            //get permesso dell'utente su tale lista
            Permission permission = null;
            try {
                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                //se il permesso è  vuoto
                CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");

                //crea istanza di commento
                Comment comment = new Comment();
                //set id user
                comment.setUserId(user.getId());
                //set id lista
                comment.setListId(Integer.parseInt(listId));
                //set il contenuto
                comment.setText(commentText);

                //inserisce
                commentDAO.insert(comment);
            } catch (DAOException ex) {
                new ServletException(ex.getMessage(), ex);
            }
            //set il risultato
            result = "commentInsertOk";

        }

        //in caso di delete
        else if (action.equals("delete")) {
            //get id commento
            String commentId = request.getParameter("commentId");
            //se id commento è nullo
            CheckErrorUtils.isNull(commentId, "manca il parametro commentId");

            int commentUserId = 0;
            //get user corrente
            User user = (User) request.getSession().getAttribute("user");

            try {
                //get user id di commento
                commentUserId = commentDAO.getByPrimaryKey(Integer.parseInt(commentId)).getUserId();

                //se utente corrente corrisponde id utente del commento
                if (user.getId() == commentUserId) {
                    //elimina il commento
                    commentDAO.deleteCommentById(Integer.parseInt(commentId));
                    result = "commentDeleteOk";
                }
                //altrimenti errore
                else {
                    throw new ServletException("non sei il proprietario del commento specificato");
                }
            } catch (DAOException ex) {
                throw new ServletException(ex.getMessage(), ex);
            }
        } else {
            throw new ServletException("valore di action non riconosciuto");
        }

        //ritorna alla pagina di provenienza
        String prevUrl = request.getHeader("Referer");
        if (prevUrl == null) {
            prevUrl = getServletContext().getContextPath();
        }
        //set il risultato
        request.getSession().setAttribute("result", result);
        response.sendRedirect(response.encodeRedirectURL(prevUrl));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
