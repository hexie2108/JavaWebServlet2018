package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
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
        user.setResetPwdEmailLink(rs.getString("resendPwdEmailLink"));

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
                        "resendPwdEmailLink = ? " +
                        "WHERE id = ?"
        )) {

            stm.setString(1, user.getName());
            stm.setString(2, user.getSurname());
            stm.setString(3, user.getEmail());
            stm.setString(4, user.getPassword());
            stm.setString(5, user.getImg());
            stm.setBoolean(6, user.isIsAdmin());
            stm.setString(7, user.getVerifyEmailLink());
            stm.setString(8, user.getResetPwdEmailLink());
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
    public User getByEmail(String email) throws DAOException {
        User user = null;

        if (email == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed email is null"));
        }

        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * FROM User " +
                        "WHERE email = ?"
        )) {
            stm.setString(1, email);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    user = getUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Impossible to find the user");
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
    
    public void changePassword(String resetLink, String newPassword) throws DAOException {
        try(PreparedStatement stm = CON.prepareStatement(
                "UPDATE User SET resendPwdEmailLink=NULL, password=?" +
                        " WHERE resendPwdEmailLink= ?"
        )){
            stm.setString(1, newPassword );
            stm.setString(2, resetLink);

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible update the password");
            }
        } catch (SQLException e) {
            throw new DAOException("Impossible to update the password");
        }
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

    public User generateUser(String first_name, String last_name, String email, String password, String imageName) throws DAOException {
        if(first_name == null || last_name == null || email == null || password == null) {
            throw new DAOException(
                "parameter not valid",
                new IllegalArgumentException(
                    "The passed email, password, last name or first name is null"
                )
            );
        }

        int userId=0;
        String verifyLink = UUID.randomUUID().toString();

        boolean successo=false;
        for(int tentativi=0; (tentativi<5)&&(!successo); tentativi++) {
            successo = true;
            try{
                try
                {
                    PreparedStatement std = CON.prepareStatement(
                            "INSERT INTO User (name, surname, email, password, img, isAdmin, verifyEmailLink, resendPwdEmailLink)" +
                            "VALUES (?,?,?,?,?,FALSE,?,NULL)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    std.setString(1, first_name);
                    std.setString(2, last_name);
                    std.setString(3, email);
                    std.setString(4, password);
                    std.setString(5, imageName);
                    std.setString(6,verifyLink);

                    if (std.executeUpdate() != 1) {
                        throw new DAOException("Impossible to insert the user");
                    }

                    try(ResultSet rs = std.getGeneratedKeys()){
                        if(rs.next()) {
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

        // Avendo fallito per 5 volte a generare uuid unici mandiamo un errore
        // in quanto in condizioni normali è estremamente improbabile
        if(!successo) {
            throw new DAOException("Impossible to create the user");
        }

        User user=new User();
        user.setId(userId);
        user.setName(first_name);
        user.setSurname(last_name);
        user.setEmail(email);
        user.setPassword(password);
        user.setVerifyEmailLink(verifyLink);
        user.setResetPwdEmailLink(null); //TODO: Convertire resendEmailLink in resetPassword?

        return user;
    }
}
