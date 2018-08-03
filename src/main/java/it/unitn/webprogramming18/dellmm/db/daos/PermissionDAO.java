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
     * Returns the set of permissions on a specified list, exclude owner
     * 
     * @param listId id di lista
     * @param UserId id di utente proprietario
     * @return a List containing Permission objects associated to the specified list
     * @throws DAOException if an error occurred during the action
     */
    public List<Permission> getSharingPermissionsOnListByListId(Integer listId, Integer UserId) throws DAOException;
    
    /**
     * get il permesso di un utente su una lista specificata
     * 
     * @param userId user's id
     * @param listId list's id
     * @return il Permission bean oppure null, se utente non ha nessun permesso
     * @throws DAOException if an error occurred during the action
     */
    public Permission getUserPermissionOnListByIds(Integer userId, Integer listId) throws DAOException;
    
    
    /**
     * get il numero di condivisione di una lista , esclude proprietario
     * @param listId
     * @return il numero di persone condiviso (esclude owner stesso)
     * @throws DAOException 
     */
    public Integer getNumberSharedByListId (Integer listId) throws DAOException;
}
