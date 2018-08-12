package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Notification;

import java.util.List;

public interface NotificationDAO extends DAO<Notification, Integer> {
    /**
     * Returns the number of {@link Notification notification} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Persists the new notification passed as parameter
     * to the storage system.
     *
     * @param notification the new notification to insert as entry
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     */
    public Integer insert(Notification notification) throws DAOException;

    /**
     * Returns the {@link Notification notification} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code notification} to get.
     * @return the {@code notification} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Notification getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Notification notification} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code notification}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List<Notification> getAll() throws DAOException;

    /**
     * Update the notification passed as parameter and returns it.
     *
     * @param notification the notification used to update the persistence system.
     * @return the updated notification.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Notification update(Notification notification) throws DAOException;

    /**
     * Get all the unread notifications for a specified user
     *
     * @param userId The id of the notifications' user
     * @param read   null if both read and unread notifications, true for read notification, false for unread notifications
     * @return the list of all the notifications the specified user has not already read
     * @throws DAOException if an error occurred during the action.
     */
    public List<Notification> getNotificationsByUserId(Integer userId, Boolean read) throws DAOException;
}
