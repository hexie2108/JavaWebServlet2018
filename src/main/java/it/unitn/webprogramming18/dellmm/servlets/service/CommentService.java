package it.unitn.webprogramming18.dellmm.servlets.service;

import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCommentDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per inserire e eliminare il commento
 * utente può inserire il commento solo sulla lista che ha qualche permesso
 * utente può eliminare solo il proprio commento
 *
 * @author mikuc
 */
public class CommentService extends HttpServlet
{

        private PermissionDAO permissionDAO;
        private CommentDAO commentDAO;

        @Override
        public void init() throws ServletException
        {
                permissionDAO = new JDBCPermissionDAO();
                commentDAO = new JDBCCommentDAO();
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {
                
                //string che memorizza il risultato dell'operazione
                String result = null;
                //get l'azione che vuoi fare
                String action = request.getParameter("action");
                if (action == null)
                {
                        throw new ServletException("manca il parametro action");
                }

                //in caso di inserimento
                if (action.equals("insert"))
                {
                        //get id lista
                        String listId = request.getParameter("listId");
                        //get testo di commento
                        String commentText = request.getParameter("commentText");
                        //se manca parametro id lista o testo di commento
                        if (listId == null || commentText == null)
                        {
                                throw new ServletException("manca il parametro id lista o contenuto del commento");
                        }
                        //se il  testo di commento è vuoto
                        if ("".equals(commentText))
                        {
                                throw new ServletException("il contenuto del commento è vuoto");
                        }

                        //get user corrente
                        User user = (User) request.getSession().getAttribute("user");
                        //get permesso dell'utente su tale lista
                        Permission permission=null;
                        try
                        {
                                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        //se il permesso è  vuoto
                        if (permission == null)
                        {
                                throw new ServletException("non hai nessun permesso di inserire il commento su tale lista");
                        }

                        //crea istanza di commento
                        Comment comment = new Comment();
                        //set id user
                        comment.setUserId(user.getId());
                        //set id lista
                        comment.setListId(Integer.parseInt(listId));
                        //set il contenuto
                        comment.setText(commentText);
                        //inserisce
                        try
                        {
                                commentDAO.insert(comment);
                        }
                        catch (DAOException ex)
                        {
                                new ServletException(ex.getMessage(), ex);
                        }
                        //set il risultato
                        result = "commentInsertOk";

                }

                //in caso di delete
                else if (action.equals("delete"))
                {
                        //get id commento
                        String commentId = request.getParameter("commentId");
                        //se id commento è nullo
                        if (commentId == null)
                        {
                                throw new ServletException("manca il parametro id del commento");
                        }

                        int commentUserId = 0;
                        //get user corrente
                        User user = (User) request.getSession().getAttribute("user");
                        
                        try
                        {
                                //get user id di commento
                                commentUserId = commentDAO.getByPrimaryKey(Integer.parseInt(commentId)).getUserId();

                                //se utente corrente corrisponde id utente del commento
                                if (user.getId() == commentUserId)
                                {
                                        //elimina il commento
                                        commentDAO.deleteCommentById(Integer.parseInt(commentId));
                                        result = "commentDeleteOk";
                                }
                                //altrimenti errore
                                else
                                {
                                        throw new ServletException("non sei il proprietario del commento specificato");
                                }
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                }
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
                //set il risultato  
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
