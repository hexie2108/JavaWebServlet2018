package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The JDBC implementation of the {@link UserDAO} interface.
 */
public class JDBCUserDAO extends JDBCDAO<User, Integer> implements UserDAO {
    // TODO: Da togliere
    public JDBCUserDAO() {
        super();
    }

    public JDBCUserDAO(ConnectionPool cp) {
        super(cp);
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
                user.setResetPwdEmailLink(rs.getString("resetPwdEmailLink"));
                user.setAcceptedPrivacy(rs.getBoolean("acceptedPrivacy"));
                user.setLastLoginTimeMillis(rs.getLong("lastLoginTimeMillis"));
                user.setKeyForFastLogin(rs.getString("keyForFastLogin"));

        return user;
    }

    @Override
    public Long getCount() throws DAOException {
        Connection CON = CP.getConnection();
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count user", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public User getByPrimaryKey(Integer primaryKey) throws DAOException {
        User user = null;

        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    user = getUserFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the user for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return user;
    }

    @Override
    public List<User> getAll() throws DAOException {
        List<User> userList = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    userList.add(getUserFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of user", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return userList;
    }

    @Override
    public User update(User user) throws DAOException {
        if (user == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed user is null"));
        }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            " UPDATE User SET "
                            + " name = ?,"
                            + " surname = ?,"
                            + " email = ?,"
                            + " password = ?,"
                            + " img = ?,"
                            + " isAdmin = ?,"
                            + " verifyEmailLink = ?,"
                            + " resetPwdEmailLink = ? ,"
                            + " acceptedPrivacy = ? ,"
                            + " lastLoginTimeMillis = ? ,"
                            + " keyForFastLogin = ? "
                            + "WHERE id = ?"
                )) {
                        stm.setString(1, user.getName());
                        stm.setString(2, user.getSurname());
                        stm.setString(3, user.getEmail());
                        stm.setString(4, user.getPassword());
                        stm.setString(5, user.getImg());
                        stm.setBoolean(6, user.isIsAdmin());
                        stm.setString(7, user.getVerifyEmailLink());
                        stm.setString(8, user.getResetPwdEmailLink());
                        stm.setBoolean(9, user.isAcceptedPrivacy());
                        stm.setLong(10, user.getLastLoginTimeMillis());
                        stm.setString(11, user.getKeyForFastLogin());
                        stm.setInt(12, user.getId());
                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update the user");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the user", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

        return user;
    }

    @Override
    public User getByEmail(String email) throws DAOException {
        User user = null;

        if (email == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed email is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * FROM User "
                        + "WHERE email = ?"
        )) {
            stm.setString(1, email);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    user = getUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Impossible to find the user");
        } finally {
            ConnectionPool.close(CON);
        }

        return user;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) throws DAOException {
        User user = null;

        if (email == null || password == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed email or password is null"));
        }

        Connection CON = CP.getConnection();
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
        } finally {
            ConnectionPool.close(CON);
        }

        return user;
    }

    @Override
    public boolean changePassword(String resetLink, String newPassword) throws DAOException {

        if (!resetLink.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException("Argument must be string rappresentation of uuid");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE User SET resetPwdEmailLink=NULL, password=?"
                        + " WHERE resetPwdEmailLink= ?"
        )) {
            stm.setString(1, newPassword);
            stm.setString(2, resetLink);

            if (stm.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            throw new DAOException("Impossible to update the password");
        } finally {
            ConnectionPool.close(CON);
        }

        return true;
    }

    @Override
    public int checkUserRegisteredByEmail(String email) throws DAOException {
        int res = -1;
        if (email == null) {
            throw new DAOException("email is null");
        }

        Connection CON = CP.getConnection();
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
        } finally {
            ConnectionPool.close(CON);
        }

        return res;
    }

    @Override
    public boolean checkExistenceOfEmail(String email) throws DAOException {
        boolean flag = false;
        if (email == null) {
            throw new DAOException("email is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User WHERE  User.email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int res = rs.getInt(1);
                if (res > 0) {
                    flag = true;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to return result", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return flag;
    }

        @Override
        public User generateUser(String first_name, String last_name, String email, String password, String imageName, boolean acceptedPrivacy) throws DAOException
        {
                if (first_name == null || last_name == null || email == null || password == null || imageName == null)
                {
                        throw new DAOException(
                                    "parameter not valid",
                                    new IllegalArgumentException(
                                                "The passed email, password, last name ,first name , o imageName is null"
                                    )
                        );
                }

        int userId = 0;
        String verifyLink = UUID.randomUUID().toString();

        boolean successo = false;

        Connection CON = CP.getConnection();
        for (int tentativi = 0; (tentativi < 5) && (!successo); tentativi++) {
            successo = true;

            try {

                                try
                                {
                                        PreparedStatement std = CON.prepareStatement(
                                                    "INSERT INTO User(name, surname, email, password, img, isAdmin, verifyEmailLink, resetPwdEmailLink	, acceptedPrivacy, lastLoginTimeMillis, keyForFastLogin)"
                                                    + " VALUES (?,?,?,?,?,FALSE,?,NULL, ?, NULL, NULL) ",
                                                    Statement.RETURN_GENERATED_KEYS
                                        );
                                        std.setString(1, first_name);
                                        std.setString(2, last_name);
                                        std.setString(3, email);
                                        std.setString(4, password);
                                        std.setString(5, imageName);
                                        std.setString(6, verifyLink);
                                        std.setBoolean(7, acceptedPrivacy);

                    if (std.executeUpdate() != 1) {
                        throw new DAOException("Impossible to insert the user");
                    }

                    try (ResultSet rs = std.getGeneratedKeys()) {
                        if (rs.next()) {
                            userId = rs.getInt(1);
                        }
                    }
                } catch (SQLIntegrityConstraintViolationException integrity_ex) {
                    if (integrity_ex.getMessage().matches("Duplicate entry '.*?' for key 'UUID_[A-Z]*?_UNIQUE'")) {
                        successo = false;
                    } else {
                        throw new DAOException("Impossible to create the user", integrity_ex);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException("Impossible to create the user", ex);
            }

        }

        ConnectionPool.close(CON);

        // Avendo fallito per 5 volte a generare uuid unici mandiamo un errore
        // in quanto in condizioni normali è estremamente improbabile
        if (!successo) {
            throw new DAOException("Impossible to create the user");
        }

                User user = new User();
                user.setId(userId);
                user.setName(first_name);
                user.setSurname(last_name);
                user.setEmail(email);
                user.setPassword(password);
                user.setImg(imageName);
                user.setVerifyEmailLink(verifyLink);
                user.setResetPwdEmailLink(null);
                user.setAcceptedPrivacy(acceptedPrivacy);
                user.setLastLoginTimeMillis(null);
                user.setKeyForFastLogin(null);

        return user;
    }

    public List<User> filter(Integer id, String email, String name, String surname, Boolean isAdmin) throws DAOException {
        List<User> userList = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * FROM User WHERE "
                        + "(? IS NULL OR id LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                        + "(? IS NULL OR email LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                        + "(? IS NULL OR name LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                        + "(? IS NULL OR surname LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                        + "(? IS NULL OR isAdmin = ?)")) {
            if (id == null) {
                stm.setNull(1, Types.INTEGER);
                stm.setNull(2, Types.INTEGER);
            } else {
                stm.setString(1, id.toString());
                stm.setString(2, id.toString());
            }

            stm.setString(3, email);
            stm.setString(4, email);

            stm.setString(5, name);
            stm.setString(6, name);

            stm.setString(7, surname);
            stm.setString(8, surname);

            if (isAdmin == null) {
                stm.setNull(9, Types.BOOLEAN);
                stm.setNull(10, Types.BOOLEAN);
            } else {
                stm.setBoolean(9, isAdmin);
                stm.setBoolean(10, isAdmin);
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    userList.add(getUserFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of user", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return userList;
    }

    @Override
    public void delete(Integer id) throws DAOException {

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "DELETE FROM User WHERE id = ?"
        )) {
            stm.setInt(1, id);

                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to delete the user");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to delete the user", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }
        }

        @Override
        public void updateLastLoginTimeAndFastLoginKey(Integer id, Long timeMillis, String fastLoginKey) throws DAOException
        {
                if (id == null || timeMillis == null || fastLoginKey == null)
                {
                        throw new DAOException("id, timestamp o fastLoginKey is null");
                }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE User SET lastLoginTimeMillis = ? , keyForFastLogin = ? WHERE id = ?"
                ))
                {

                        stm.setLong(1, timeMillis);
                        stm.setString(2, fastLoginKey);
                        stm.setInt(3, id);

                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update last login time of user");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the user", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

        }

        @Override
        public User getUserByFastLoginKey(String fastLoginKey, Long currentTimeMillis) throws DAOException
        {

                User user = null;

                if (fastLoginKey == null || currentTimeMillis == null)
                {
                        throw new DAOException("fastLoginKey o currentTimeMillis is null");
                }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User WHERE (? - lastLoginTimeMillis ) < (1000*60*60*24*30) AND   keyForFastLogin = ?"))
                {
                        stm.setLong(1, currentTimeMillis);
                        stm.setString(2, fastLoginKey);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        user = getUserFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the user for the passed keyForFastLogin", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return user;

        }

        @Override
        public boolean activateUserByEmailAndVerifyLink(String email, String verifyEmailLink) throws DAOException
        {
                boolean result = true;
                if (email == null || verifyEmailLink == null)
                {
                        throw new DAOException("email o verifyEmailLink is null");
                }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE User SET verifyEmailLink = NULL WHERE email = ? AND verifyEmailLink = ?"
                ))
                {

                        stm.setString(1, email);
                        stm.setString(2, verifyEmailLink);

                        //se non ha trovato
                        if (stm.executeUpdate() != 1)
                        {
                                result = false;
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the user", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return result;
        }

        @Override
        public boolean checkUserByEmailAndResetPwdLink(String email, String resetPwdLink) throws DAOException
        {
                boolean flag = false;
                if (email == null || resetPwdLink == null)
                {
                        throw new DAOException("email o resetPwdLink is null");
                }

                Connection CON = CP.getConnection();
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User WHERE  email = ? AND resetPwdEmailLink = ?"))
                {
                        stmt.setString(1, email);
                        stmt.setString(2, resetPwdLink);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next())
                        {
                                int res = rs.getInt(1);
                                if (res > 0)
                                {
                                        flag = true;
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to return result", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return flag;
        }
}
