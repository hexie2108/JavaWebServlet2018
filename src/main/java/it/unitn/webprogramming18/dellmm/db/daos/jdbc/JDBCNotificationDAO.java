package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.NotificationDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Notification;

import java.sql.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * The JDBC implementation of the {@link NotificationDAO} interface.
 */
public class JDBCNotificationDAO extends JDBCDAO<Notification, Integer> implements NotificationDAO {
    // TODO: Da togliere
    public JDBCNotificationDAO() {
        super();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public JDBCNotificationDAO(ConnectionPool cp) {
        super(cp);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Calendar cal = Calendar.getInstance();


    private Notification getNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();

        notification.setId(rs.getInt("id"));
        notification.setDate(rs.getTimestamp("date", cal));
        notification.setText(rs.getString("text"));
        notification.setStatus(rs.getBoolean("status"));
        notification.setUserId(rs.getInt("userId"));

        return notification;
    }

    @Override
    public Long getCount() throws DAOException {
        Connection CON = CP.getConnection();
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Notification")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count notification", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    public Integer insert(Notification notification) throws DAOException {
        if (notification == null) {
            throw new DAOException("notification bean is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Notification (date, text, status, userId) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {

            stm.setTimestamp(1, notification.getDate());
            stm.setString(2, notification.getText());
            stm.setBoolean(3, notification.isStatus());
            stm.setInt(4, notification.getUserId());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                notification.setId(rs.getInt(1));
            }

            return notification.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new notification", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }

    @Override
    public Notification getByPrimaryKey(Integer primaryKey) throws DAOException {
        Notification notification = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Notification WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    notification = getNotificationFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the notification for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return notification;
    }

    @Override
    public List<Notification> getAll() throws DAOException {
        List<Notification> notificationList = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Notification")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    notificationList.add(getNotificationFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of notification", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return notificationList;
    }

    @Override
    public Notification update(Notification notification) throws DAOException {
        if (notification == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed notification is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE Notification SET " +
                        "date = ?," +
                        "text = ?," +
                        "status = ?, " +
                        "userId = ? " +
                        "WHERE id = ?"
        )) {

            stm.setTimestamp(1, notification.getDate());
            stm.setString(2, notification.getText());
            stm.setBoolean(3, notification.isStatus());
            stm.setInt(4, notification.getUserId());
            stm.setInt(5, notification.getId());
            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the notification");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the notification", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return notification;
    }

    public List<Notification> getNotificationsByUserId(Integer userId, Boolean read) throws DAOException {
        List<Notification> notificationList = new ArrayList<>();
        if (userId == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT Notification.* FROM "
                        + "Notification JOIN User ON  User.id = Notification.userId "
                        + "WHERE  User.id = ? AND  (? IS NULL OR Notification.status = ?) "
                        + "ORDER BY Notification.date DESC")) {
            stm.setInt(1, userId);
            if (read == null) {
                stm.setNull(2, Types.BOOLEAN);
                stm.setBoolean(3, false); // Dummy data
            } else {
                stm.setBoolean(2, read);
                stm.setBoolean(3, read);
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    notificationList.add(getNotificationFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of notification", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return notificationList;
    }
}
