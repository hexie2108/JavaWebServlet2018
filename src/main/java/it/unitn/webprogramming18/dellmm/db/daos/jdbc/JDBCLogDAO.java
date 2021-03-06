package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link LogDAO} interface.
 */
public class JDBCLogDAO extends JDBCDAO<Log, Integer> implements LogDAO {
    public JDBCLogDAO(ConnectionPool cp) {
        super(cp);
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
        log.setEmailStatus(rs.getBoolean("emailStatus"));

        return log;
    }

    @Override
    public Long getCount() throws DAOException {
        Connection CON = CP.getConnection();
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Log")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count log", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public Integer insert(Log log) throws DAOException {
        if (log == null) {
            throw new DAOException("log bean is null");
        }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Log (productId, userId, last1, last2, last3, last4, emailStatus) VALUES (?,?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS))
                {

                        stm.setInt(1, log.getProductId());
                        stm.setInt(2, log.getUserId());
                        stm.setTimestamp(3, log.getLast1());
                        stm.setTimestamp(4, log.getLast2());
                        stm.setTimestamp(5, log.getLast3());
                        stm.setTimestamp(6, log.getLast4());
                        stm.setBoolean(7, log.isEmailStatus());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                log.setId(rs.getInt(1));
            }

            return log.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new log", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }

    @Override
    public Log getByPrimaryKey(Integer primaryKey) throws DAOException {
        Log log = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    log = getLogFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the log for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return log;
    }

    @Override
    public List<Log> getAll() throws DAOException {
        List<Log> logList = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    logList.add(getLogFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of log", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return logList;
    }

    @Override
    public Log update(Log log) throws DAOException {
        if (log == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed log is null"));
        }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE Log SET "
                            + " productId =?, "
                            + " userId =?, "
                            + " last1 =?, "
                            + " last2 =?, "
                            + " last3 =?, "
                            + " last4 =?, "
                            + " emailStatus =? "
                            + " WHERE id = ? "
                ))
                {
                        stm.setInt(1, log.getProductId());
                        stm.setInt(2, log.getUserId());
                        stm.setTimestamp(3, log.getLast1());
                        stm.setTimestamp(4, log.getLast2());
                        stm.setTimestamp(5, log.getLast3());
                        stm.setTimestamp(6, log.getLast4());
                        stm.setBoolean(7, log.isEmailStatus());
                        stm.setInt(8, log.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the log");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the log", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return log;
    }

    @Override
    public Log getUserProductLogByIds(Integer userId, Integer productId) throws DAOException {
        Log log = null;
        if (userId == null || productId == null) {
            throw new DAOException("One or both arguments (userId, productId) are null");
        }

        Connection CON = CP.getConnection();
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
        } finally {
            ConnectionPool.close(CON);
        }

        return log;
    }

    @Override
    public Log updateUserProductLogTableByIds(Integer userId, Integer productId) throws DAOException {
        Log log;
        if (userId == null || productId == null) {
            throw new DAOException("One or both arguments (userId, productId) are null");
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log WHERE userId = ? AND productId = ?")) {
            stm.setInt(1, userId);
            stm.setInt(2, productId);
            try (ResultSet rs = stm.executeQuery()) {

                //Entry exists
                if (rs.next()) {
                    log = getLogFromResultSet(rs);

                    //last1 surely not null
                    if (log.getLast2() == null) {
                        log.setLast2(timestamp);
                        log = update(log);
                        return log;
                    } else if (log.getLast3() == null) {
                        log.setLast3(timestamp);
                        log = update(log);
                        return log;
                    } else if (log.getLast4() == null) {
                        log.setLast4(timestamp);
                        log = update(log);
                        return log;
                    } else {
                        //All fields are full. Shift values and insert last
                        log.setLast1(log.getLast2());
                        log.setLast2(log.getLast3());
                        log.setLast3(log.getLast4());
                        log.setLast4(timestamp);
                        log = update(log);
                        return log;
                    }

                                        //Entry does not exist
                                }
                                else
                                {
                                        log = new Log();
                                        Timestamp nullts = null;
                                        log.setProductId(productId);
                                        log.setUserId(userId);
                                        log.setLast1(timestamp);
                                        log.setLast2(nullts);
                                        log.setLast3(nullts);
                                        log.setLast4(nullts);
                                        insert(log);
                                        return log;
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the log for the passed userId and productId", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }
        }

        @Override
        public Log getLogNotEmailYet(Timestamp currentTime, Integer predictionDay) throws DAOException
        {
                Log log = null;
                if (currentTime == null || predictionDay == null)
                {
                        throw new DAOException("currentTime or predictionDay is null");
                }

                Connection CON = CP.getConnection();
                //get log in cui, stato di email è false, esiste almeno 2 aquisti storici, e il tempo previsto di riaquisto è minore di 24 ore
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Log WHERE emailStatus = 0 AND (last2 IS NOT NULL) AND ( (TIME_TO_SEC( TIMEDIFF( last1, last2 )) - TIME_TO_SEC( TIMEDIFF( ?, last1 ))) < (60*60*24*?) )"))
                {
                        stm.setTimestamp(1, currentTime);
                        stm.setInt(2, predictionDay);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        log = getLogFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the log not email yet", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return log;

        }

        @Override
        public void setEmailStatusTrueByUserId(Integer userId) throws DAOException
        {
                if (userId == null)
                {
                        throw new DAOException("arguments (userId ) are null");
                }

                Connection CON = CP.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE Log SET emailStatus = 1 WHERE userId = ? AND emailStatus = 0"
                ))
                {
                        stm.setInt(1, userId);

                        //non si sa perché in thread continua saltare questo errore di update, ma update sono risulati correttamente
                       if (stm.executeUpdate() == 0)
                        {
                                throw new DAOException("Impossible to update the log user:"+userId);
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the log", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

        }

}
