package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
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

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
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
        User user = null;

        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
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

        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE User SET " +
                        "name = ?," +
                        "surname = ?," +
                        "email = ?," +
                        "password = ?," +
                        "img = ?," +
                        "isAdmin = ?," +
                        "verifyEmailLink = ?," +
                        "resendEmailLink = ? " +
                        "WHERE id = ?"
        )) {

            stm.setString(1, user.getName());
            stm.setString(2, user.getSurname());
            stm.setString(3, user.getEmail());
            stm.setString(4, user.getPassword());
            stm.setString(5, user.getImg());
            stm.setBoolean(6, user.isIsAdmin());
            stm.setString(7, user.getVerifyEmailLink());
            stm.setString(8, user.getResendEmailLink());
            stm.setInt(9, user.getId());
            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the user");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the user", ex);
        }

        return user;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) throws DAOException {
        User user = null;

        if (email == null || password == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed email or password is null"));
        }

        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * FROM User " +
                        "WHERE email = ? AND password = ?"
        )) {
            stm.setString(1, email);
            stm.setString(2, password);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    user = getUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Impossible to find the user");
        }

        return user;
    }


    public int checkUserRegisteredByEmail(String email) throws DAOException {
        int res = -1;
        if (email == null) {
            throw new DAOException("email is null");
        }
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User WHERE  EXISTS "
                + "(SELECT * FROM   User WHERE  User.email = ?)")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
                if (res > 1) {
                    return -1;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to return result", ex);
        }
        return res;
    }
}
