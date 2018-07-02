package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import java.util.List;

public interface UserDAO extends DAO<User, Integer> {
    /**
     * Returns the number of {@link User user} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Returns the {@link User user} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code user} to get.
     * @return the {@code user} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public User getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link User user} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code user}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public List<User> getAll() throws DAOException;

    /**
     * Update the user passed as parameter and returns it.
     * @param user the categoryList used to update the persistence system.
     * @return the updated user.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public User update(User user) throws DAOException;

    /**
     * Return the user with the email and password specified
     * @param email the email of the user to return
     * @param password the password of the user to return
     * @return The user requested
     * @throws DAOException
     */
    public User getByEmailAndPassword(String email, String password) throws DAOException;
    
    /**
     * Given an email, the function checks if the user is registered in the DB,
     * thus if it's registered on the application.
     * 
     * @param email
     * @return 0 if user is not registered,
     *         1 if user is registered,
     *         -1 if >1 is returned by query,
     *         -2 if function does not work
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    public int checkUserRegisteredByEmail(String email) throws DAOException;

}
