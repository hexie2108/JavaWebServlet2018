package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;

import java.util.List;

public interface PermissionDAO extends DAO<Permission, Integer> {
    /**
     * Returns the number of {@link Permission permissino} stored on the persistence system
     * of the application.
     *
     * @return the number of records that are into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;
    
    /**
     * Persists the new permission passed as parameter
     * to the storage system.
     * 
     * @param permission the new permission to insert as entry
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     */
    public Integer insert(Permission permission) throws DAOException;

    /**
     * Returns the {@link Permission permission} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code permission} to get.
     * @return the {@code permission} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Permission getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Permission permission} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code permission}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List<Permission> getAll() throws DAOException;

    /**
     * Update the permission passed as parameter and returns it.
     *
     * @param permission the permission used to update the persistence system.
     * @return the updated permission.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Permission update(Permission permission) throws DAOException;
    
    /**
     * Returns the set of permissions on a specified list
     * 
     * @param listId
     * @return a List containing Permission objects associated to the specified list
     * @throws DAOException if an error occurred during the action
     */
    public List<Permission> getPermissionsOnListByListId(Integer listId) throws DAOException;
    
    /**
     * ATTENTION: This function must be called on known (non-owner list_alpha user, list_alpha) pairs, as for how our DB is designed
     * we have to add separate check on ownerId's List field.
     * Gets a Permission bean containing specified user's permission on specified list
     * 
     * @param userId user's id
     * @param listId list's id
     * @return a Permission bean containing user's permission on list, NULL if user cannot access list
     *         NULL IS RETURNED ALSO IF A PAIR (owner list_alpha user, list_alpha) IS PASSED AS PARAMETER
     * @throws DAOException if an error occurred during the action
     */
    public Permission getUserPermissionOnListByIds(Integer userId, Integer listId) throws DAOException;
}
