package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link UserDAO} interface.
 */
public class JDBCUserDAO extends JDBCDAO<User, Integer> implements UserDAO {
    public JDBCUserDAO(Connection con) {
        super(con);
    }

	private User getUserFromResultSet(ResultSet rs) throws SQLException{
		User user = new User();

		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setSurname(rs.getString("surname"));
		user.setEmail(rs.getString("email"));
		user.setImg(rs.getString("img"));
		user.setIsAdmin(rs.getBoolean("isAdmin"));
		user.setVerifyEmailLink(rs.getString("verifyEmailLink"));
		user.setResendEmailLink(rs.getString("resendEmailLink"));

		return user;
	}

	@Override
	public Long getCount() throws DAOException {
		try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User")) {
			ResultSet counter = stmt.executeQuery();
			if (counter.next()) {
				return counter.getLong(1);
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to count user", ex);
		}

		return 0L;
	}

	@Override
	public User getByPrimaryKey(Integer primaryKey) throws DAOException {
		User user= null;
		if (primaryKey == null) {
			throw new DAOException("primaryKey is null");
		}
		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User WHERE id = ?")) {
			stm.setInt(1, primaryKey);
			try (ResultSet rs = stm.executeQuery()) {
				if(rs.next()) {
					user = getUserFromResultSet(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the user for the passed primary key", ex);
		}

		return user;
	}

	@Override
	public List<User> getAll() throws DAOException {
		List<User> userList = new ArrayList<>();

		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User")) {
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					userList.add(getUserFromResultSet(rs));
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the list of user", ex);
		}

		return userList;
	}

	@Override
	public User update(User user) throws DAOException {
		if (user == null) {
			throw new DAOException("parameter not valid", new IllegalArgumentException("The passed user is null"));
		}

		try(PreparedStatement stm = CON.prepareStatement(
				"UPDATE User SET " +
						"name = ?," +
						"surname = ?," +
						"email = ?," +
						"img = ?," +
						"isAdmin = ?," +
						"verifyEmailLink = ?," +
						"resendEmailLink = ? " +
						"WHERE id = ?"
		)) {

			stm.setString(1, user.getName());
			stm.setString(2, user.getSurname());
			stm.setString(3, user.getEmail());
			stm.setString(4, user.getImg());
			stm.setBoolean(5, user.isIsAdmin());
			stm.setString(6, user.getVerifyEmailLink());
			stm.setString(7, user.getResendEmailLink());
			stm.setInt(8, user.getId());
			if (stm.executeUpdate() != 1) {
				throw new DAOException("Impossible to update the user");
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to update the user", ex);
		}

		return user;
	}
}
