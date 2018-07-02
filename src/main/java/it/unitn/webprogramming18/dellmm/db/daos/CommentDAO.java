package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.util.HashMap;

import java.util.List;

public interface CommentDAO extends DAO<Comment, Integer> {
    /**
     * Returns the number of {@link Comment comment} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Returns the {@link Comment comment} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code comment} to get.
     * @return the {@code comment} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public Comment getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Comment comment} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code comment}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public List<Comment> getAll() throws DAOException;

    /**
     * Update the comment passed as parameter and returns it.
     * @param comment the comment used to update the persistence system.
     * @return the updated comment.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Comment update(Comment comment) throws DAOException;
    
    /**
     * Get all the comments on a list given listId
     * 
     * @param listId
     * @return a "pair" of type HashMap<Integer, String> with Integer being the userId
     * @throws DAOException 
     */
    public HashMap<Integer, String> getCommentsOnListByListId(String listId) throws DAOException;
}
