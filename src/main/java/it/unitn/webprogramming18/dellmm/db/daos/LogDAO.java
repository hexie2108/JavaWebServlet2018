package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;

import java.util.List;

public interface LogDAO extends DAO<Log, Integer> {
    /**
     * Returns the number of {@link Log log} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Returns the {@link Log log} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code log} to get.
     * @return the {@code log} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public Log getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Log log} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code log}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public List<Log> getAll() throws DAOException;

    /**
     * Update the log passed as parameter and returns it.
     * @param log the log used to update the persistence system.
     * @return the updated log.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Log update(Log log) throws DAOException;
}
