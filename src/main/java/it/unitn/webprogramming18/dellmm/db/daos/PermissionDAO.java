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
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

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
}
