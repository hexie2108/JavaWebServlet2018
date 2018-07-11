package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link LogDAO} interface.
 */
public class JDBCLogDAO extends JDBCDAO<Log, Integer> implements LogDAO {
    public JDBCLogDAO(Connection con) {
        super(con);
    }

    private Log getLogFromResultSet(ResultSet rs) throws SQLException {
        Log log = new Log();

        log.setId(rs.getInt("id"));
        log.setProductId(rs.getInt("productId"));
        log.setUserId(rs.getInt("userId"));
        log.setLast1(rs.getTimestamp("last1"));
        log.setLast2(rs.getTimestamp("last2"));
        log.setLast3(rs.getTimestamp("last3"));
        log.setLast4(rs.getTimestamp("last4"));

        return log;
    }

    @Override
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Log")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count log", ex);
        }

        return 0L;
    }

    public Integer insert(Log log) throws DAOException {
        if (log == null) {
            throw new DAOException("log bean is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Log (productId, userId, last1, last2, last3, last4) VALUES (?,?,?,?,?,?)", 
                                                                Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, log.getProductId());
            stm.setInt(2, log.getUserId());
            stm.setTimestamp(3, log.getLast1());
            stm.setTimestamp(4, log.getLast2());
            stm.setTimestamp(5, log.getLast3());
            stm.setTimestamp(6, log.getLast4());

            stm.executeUpdate();
            
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                log.setId(rs.getInt(1));
            }
            
            return log.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new log", ex);
        }
    }
    
    @Override
    public Log getByPrimaryKey(Integer primaryKey) throws DAOException {
        Log log = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    log = getLogFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the log for the passed primary key", ex);
        }

        return log;
    }

    @Override
    public List<Log> getAll() throws DAOException {
        List<Log> logList = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    logList.add(getLogFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of log", ex);
        }

        return logList;
    }

    @Override
    public Log update(Log log) throws DAOException {
        if (log == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed log is null"));
        }

        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE Log SET " +
                        "productId =?," +
                        "userId =?," +
                        "last1 =?," +
                        "last2 =?," +
                        "last3 =?," +
                        "last4 =? " +
                        "WHERE id = ?"
        )) {
            stm.setInt(1, log.getProductId());
            stm.setInt(2, log.getUserId());
            stm.setTimestamp(2, log.getLast1());
            stm.setTimestamp(3, log.getLast2());
            stm.setTimestamp(4, log.getLast3());
            stm.setTimestamp(5, log.getLast4());
            stm.setInt(6, log.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the log");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the log", ex);
        }

        return log;
    }

    public Log getUserProductLogByIds(Integer userId, Integer productId) throws DAOException {
        Log log = null;
        if (userId == null || productId == null) {
            throw new DAOException("One or both arguments (userId, productId) are null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log WHERE userId = ? AND productId = ?")) {
            stm.setInt(1, userId);
            stm.setInt(2, productId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    log = getLogFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the log for the passed userId and productId", ex);
        }

        return log;
    }
}
