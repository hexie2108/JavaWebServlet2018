package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;

import it.unitn.webprogramming18.dellmm.db.daos.NotificationDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link NotificationDAO} interface.
 */
public class JDBCNotificationDAO extends JDBCDAO<Notification, Integer> implements NotificationDAO {
    public JDBCNotificationDAO(Connection con) {
        super(con);
    }

	private Notification getNotificationFromResultSet(ResultSet rs) throws SQLException{
		Notification notification = new Notification();

		//TODO: Sistamare nomi che non coincidono
		notification.setId(rs.getInt("id"));
		notification.setDate(rs.getTimestamp("data"));
		notification.setText(rs.getString("text"));
		notification.setStatus(rs.getBoolean("stato"));
		notification.setUserId(rs.getInt("User_id"));

		return notification;
	}

	@Override
	public Long getCount() throws DAOException {
		try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Notifica")) {
			ResultSet counter = stmt.executeQuery();
			if (counter.next()) {
				return counter.getLong(1);
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to count notification", ex);
		}

		return 0L;
	}

	@Override
	public Notification getByPrimaryKey(Integer primaryKey) throws DAOException {
		Notification notification = null;
		if (primaryKey == null) {
			throw new DAOException("primaryKey is null");
		}
		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Notifica WHERE id = ?")) {
			stm.setInt(1, primaryKey);
			try (ResultSet rs = stm.executeQuery()) {
				if(rs.next()) {
					notification = getNotificationFromResultSet(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the notification for the passed primary key", ex);
		}

		return notification;
	}

	@Override
	public List<Notification> getAll() throws DAOException {
		List<Notification> notificationList = new ArrayList<>();

		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Notifica")) {
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					notificationList.add(getNotificationFromResultSet(rs));
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the list of notification", ex);
		}

		return notificationList;
	}

	@Override
	public Notification update(Notification notification) throws DAOException {
		if (notification == null) {
			throw new DAOException("parameter not valid", new IllegalArgumentException("The passed notification is null"));
		}

		try(PreparedStatement stm = CON.prepareStatement(
				"UPDATE Notifica SET " +
						"data = ?," +
						"text = ?," +
						"User_id = ?" +
						"WHERE id = ?"
		)) {

		    //TODO: Controllare getStatus mancante
			stm.setTimestamp(1, notification.getDate());
			stm.setString(2, notification.getText());
			stm.setInt(3, notification.getUserId());
			stm.setInt(4,notification.getId());
			if (stm.executeUpdate() != 1) {
				throw new DAOException("Impossible to update the notification");
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to update the notification", ex);
		}

		return notification;
	}
}
