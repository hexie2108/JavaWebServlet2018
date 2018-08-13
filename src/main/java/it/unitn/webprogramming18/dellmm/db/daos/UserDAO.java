package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.sql.Timestamp;

import java.util.List;

public interface UserDAO extends DAO<User, Integer>
{

        /**
         * Returns the number of {@link User user} stored on the persistence
         * system of the application.
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
         * parameter or {@code null} if no entities with that id is not present
         * into the storage system.
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
         *
         * @param user the categoryList used to update the persistence system.
         * @return the updated user.
         * @throws DAOException if an error occurred during the action.
         */
        @Override
        public User update(User user) throws DAOException;

        /**
         * Return the user with the email specified
         *
         * @param email the email of the user to return
         * @return The user requested
         * @throws DAOException
         */
        public User getByEmail(String email) throws DAOException;

        /**
         * Return the user with the email and password specified
         *
         * @param email the email of the user to return
         * @param password the password of the user to return
         * @return The user requested
         * @throws DAOException
         */
        public User getByEmailAndPassword(String email, String password) throws DAOException;

        /**
         *
         * @param resetLink the resetLink used(that is linked with the user)
         * @param newPassword the new password to set
         * @return 
         * @throws DAOException
         */
        public boolean changePassword(String resetLink, String newPassword) throws DAOException;

        /**
         * Given an email, the function checks if the user is registered in the
         * DB, thus if it's registered on the application.
         *
         * @param email
         * @return 0 if user is not registered, 1 if user is registered, -1 if
         * >1 is returned by query, -2 if function does not work
         * @throws DAOException if an error occurred during the information
         * retrieving.
         */
        public int checkUserRegisteredByEmail(String email) throws DAOException;
        
        /**
         * controlla se email esiste già o no in DB
         * @param email
         * @return true se esiste già, false se non esiste
         * @throws DAOException 
         */
        public boolean checkExistenceOfEmail(String email) throws DAOException;

        /**
         * Insert a user in the database and return a User with all the
         * fields(specified and automatically generated)
         *
         * @param first_name user's first name
         * @param last_name user's surname
         * @param email the email address of the user
         * @param password password of the user
         * @param imageName name of the (file) image
         * @return generated user
         * @throws it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException
         */
        public User generateUser(String first_name, String last_name, String email, String password, String imageName, boolean acceptedPrivacy) throws DAOException;

        /**
         * Filter the users
         *
         * @param id integer which as string is substring of user id
         * @param email string which is substring of user email
         * @param name string which is substring of user name
         * @param surname string which is substring of user surname
         * @param isAdmin true for admin, false for regular users, null for both
         * @return List of users that respect the conditions
         * @throws DAOException
         */
        public List<User> filter(Integer id, String email, String name, String surname, Boolean isAdmin) throws DAOException;

        /**
         * Delete user
         *
         * @param id user id
         * @throws DAOException
         */
        public void delete(Integer id) throws DAOException;
        
        
        /**
         * aggiornare il tempo di l'utlimo login dell'utente
         * @param id id utente
         * @param timeMillis tempo in secondo
         * @param fastLoginKey key per auto login
         * @throws DAOException 
         */
        public void updateLastLoginTimeAndFastLoginKey(Integer id, Long timeMillis, String fastLoginKey) throws DAOException;
        
        
        /**
         * prima si controlla il tempo di l'ultimo login, se è minore di 30 giorni, allora fastLoginKey è valido, altrimenti non è valido
         * poi, ritorna record di user con tale loginKey
         * @param fastLoginKey 
         * @return user beans se trova , null se non trova
         * @throws DAOException 
         */
        public User getUserByFastLoginKey(String fastLoginKey, Long currentTimeMillis) throws DAOException;
        
}
