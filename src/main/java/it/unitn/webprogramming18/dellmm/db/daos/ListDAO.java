package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.List;

public interface ListDAO extends DAO<List, Integer> {
    /**
     * Returns the number of {@link List list} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Returns the {@link List list} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code list} to get.
     * @return the {@code list} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link List list} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code list}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public java.util.List<List> getAll() throws DAOException;

    /**
     * Update the list passed as parameter and returns it.
     *
     * @param list the list used to update the persistence system.
     * @return the updated list.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public List update(List list) throws DAOException;

    /**
     * Gets all the lists that the specified user owns
     *
     * @param userId
     * @return the list of the owned lists of the user
     * @throws DAOException if an error occurred during the action.
     */
    public java.util.List<List> getOwnedUserListsByUserId(Integer userId) throws DAOException;

    /**
     * Gets all the lists shared with the specified user
     *
     * @param userId
     * @return the list of the lists shared with the specified user
     * @throws DAOException if an error occurred during the action.
     */
    public java.util.List<List> getSharedWithUserListsByUserId(Integer userId) throws DAOException;
    
    /**
     * Gets the count of products in a list specified via listId, passed as parameter
     * 
     * @param listId
     * @return Number of products in specified list
     * @throws DAOException if an error occurred during the action.
     */
    public Integer getNumberOfProductsInListByListId(Integer listId) throws DAOException;
}
