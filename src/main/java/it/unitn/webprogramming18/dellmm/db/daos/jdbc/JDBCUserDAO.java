package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.C3p0Util;
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
public class JDBCUserDAO extends JDBCDAO<User, Integer> implements UserDAO
{

        private User getUserFromResultSet(ResultSet rs) throws SQLException
        {
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

                return user;
        }

        @Override
        public Long getCount() throws DAOException
        {
                CON = C3p0Util.getConnection();
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User"))
                {
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                return counter.getLong(1);
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count user", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return 0L;
        }

        @Override
        public User getByPrimaryKey(Integer primaryKey) throws DAOException
        {
                User user = null;

                if (primaryKey == null)
                {
                        throw new DAOException("primaryKey is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User WHERE id = ?"))
                {
                        stm.setInt(1, primaryKey);
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
                        throw new DAOException("Impossible to get the user for the passed primary key", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return user;
        }

        @Override
        public List<User> getAll() throws DAOException
        {
                List<User> userList = new ArrayList<>();

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM User"))
                {
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        userList.add(getUserFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of user", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return userList;
        }

        @Override
        public User update(User user) throws DAOException
        {
                if (user == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed user is null"));
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE User SET "
                            + "name = ?,"
                            + "surname = ?,"
                            + "email = ?,"
                            + "password = ?,"
                            + "img = ?,"
                            + "isAdmin = ?,"
                            + "verifyEmailLink = ?,"
                            + "resetPwdEmailLink = ? "
                            + "WHERE id = ?"
                ))
                {

                        stm.setString(1, user.getName());
                        stm.setString(2, user.getSurname());
                        stm.setString(3, user.getEmail());
                        stm.setString(4, user.getPassword());
                        stm.setString(5, user.getImg());
                        stm.setBoolean(6, user.isIsAdmin());
                        stm.setString(7, user.getVerifyEmailLink());
                        stm.setString(8, user.getResetPwdEmailLink());
                        stm.setInt(9, user.getId());
                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update the user");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the user", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return user;
        }

        @Override
        public User getByEmail(String email) throws DAOException
        {
                User user = null;

                if (email == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed email is null"));
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "SELECT * FROM User "
                            + "WHERE email = ?"
                ))
                {
                        stm.setString(1, email);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        user = getUserFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException e)
                {
                        throw new DAOException("Impossible to find the user");
                } finally
                {
                        C3p0Util.close(CON);
                }

                return user;
        }

        @Override
        public User getByEmailAndPassword(String email, String password) throws DAOException
        {
                User user = null;

                if (email == null || password == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed email or password is null"));
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "SELECT * FROM User "
                            + "WHERE email = ? AND password = ?"
                ))
                {
                        stm.setString(1, email);
                        stm.setString(2, password);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        user = getUserFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException e)
                {
                        throw new DAOException("Impossible to find the user");
                } finally
                {
                        C3p0Util.close(CON);
                }

                return user;
        }

        @Override
        public boolean changePassword(String resetLink, String newPassword) throws DAOException
        {

                if (!resetLink.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))
                {
                        throw new IllegalArgumentException("Argument must be string rappresentation of uuid");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE User SET resendPwdEmailLink=NULL, password=?"
                            + " WHERE resendPwdEmailLink= ?"
                ))
                {
                        stm.setString(1, newPassword);
                        stm.setString(2, resetLink);

                        if (stm.executeUpdate() != 1)
                        {
                                return false;
                        }
                }
                catch (SQLException e)
                {
                        throw new DAOException("Impossible to update the password");
                } finally
                {
                        C3p0Util.close(CON);
                }

                return true;
        }

        @Override
        public int checkUserRegisteredByEmail(String email) throws DAOException
        {
                int res = -1;
                if (email == null)
                {
                        throw new DAOException("email is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM User WHERE  EXISTS "
                            + "(SELECT * FROM   User WHERE  User.email = ?)"))
                {
                        stmt.setString(1, email);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next())
                        {
                                res = rs.getInt(1);
                                if (res > 1)
                                {
                                        return -1;
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to return result", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return res;
        }

        @Override
        public User generateUser(String first_name, String last_name, String email, String password, String imageName) throws DAOException
        {
                if (first_name == null || last_name == null || email == null || password == null)
                {
                        throw new DAOException(
                                    "parameter not valid",
                                    new IllegalArgumentException(
                                                "The passed email, password, last name or first name is null"
                                    )
                        );
                }

                int userId = 0;
                String verifyLink = UUID.randomUUID().toString();

                boolean successo = false;
                for (int tentativi = 0; (tentativi < 5) && (!successo); tentativi++)
                {
                        successo = true;

                        CON = C3p0Util.getConnection();
                        try
                        {

                                try
                                {
                                        PreparedStatement std = CON.prepareStatement(
                                                    "INSERT INTO User (name, surname, email, password, img, isAdmin, verifyEmailLink, resendPwdEmailLink)"
                                                    + "VALUES (?,?,?,?,?,FALSE,?,NULL)",
                                                    Statement.RETURN_GENERATED_KEYS
                                        );
                                        std.setString(1, first_name);
                                        std.setString(2, last_name);
                                        std.setString(3, email);
                                        std.setString(4, password);
                                        std.setString(5, imageName);
                                        std.setString(6, verifyLink);

                                        if (std.executeUpdate() != 1)
                                        {
                                                throw new DAOException("Impossible to insert the user");
                                        }

                                        try (ResultSet rs = std.getGeneratedKeys())
                                        {
                                                if (rs.next())
                                                {
                                                        userId = rs.getInt(1);
                                                }
                                        }
                                }
                                catch (SQLIntegrityConstraintViolationException integrity_ex)
                                {
                                        if (integrity_ex.getMessage().matches("Duplicate entry '.*?' for key 'UUID_[A-Z]*?_UNIQUE'"))
                                        {
                                                successo = false;
                                        }
                                        else
                                        {
                                                throw new DAOException("Impossible to create the user", integrity_ex);
                                        }
                                }
                        }
                        catch (SQLException ex)
                        {
                                ex.printStackTrace();
                                throw new DAOException("Impossible to create the user", ex);
                        } finally
                        {
                                C3p0Util.close(CON);
                        }
                }

                // Avendo fallito per 5 volte a generare uuid unici mandiamo un errore
                // in quanto in condizioni normali è estremamente improbabile
                if (!successo)
                {
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
                user.setResetPwdEmailLink(null); //TODO: Convertire resendEmailLink in resetPassword?

                return user;
        }

        public List<User> filter(Integer id, String email, String name, String surname, Boolean isAdmin) throws DAOException
        {
                List<User> userList = new ArrayList<>();

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "SELECT * FROM User WHERE "
                            + "(? IS NULL OR id LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                            + "(? IS NULL OR email LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                            + "(? IS NULL OR name LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                            + "(? IS NULL OR surname LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND "
                            + "(? IS NULL OR isAdmin = ?)"))
                {
                        if (id == null)
                        {
                                stm.setNull(1, Types.INTEGER);
                                stm.setNull(2, Types.INTEGER);
                        }
                        else
                        {
                                stm.setString(1, id.toString());
                                stm.setString(2, id.toString());
                        }

                        stm.setString(3, email);
                        stm.setString(4, email);

                        stm.setString(5, name);
                        stm.setString(6, name);

                        stm.setString(7, surname);
                        stm.setString(8, surname);

                        if (isAdmin == null)
                        {
                                stm.setNull(9, Types.BOOLEAN);
                                stm.setNull(10, Types.BOOLEAN);
                        }
                        else
                        {
                                stm.setBoolean(9, isAdmin);
                                stm.setBoolean(10, isAdmin);
                        }

                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        userList.add(getUserFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of user", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return userList;
        }

        @Override
        public void delete(int id) throws DAOException
        {

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "DELETE FROM User WHERE id = ?"
                ))
                {
                        stm.setInt(1, id);

                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to delete the user");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to delete the user", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }
        }
}
