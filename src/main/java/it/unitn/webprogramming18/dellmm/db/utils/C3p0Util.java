package it.unitn.webprogramming18.dellmm.db.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * pisciana di connessione, che occupa di creare/mantenere una serie di
 * connessioni in attivi, assegnare dao quando ci sono le richieste
 *
 * @author mikuc
 */
public class C3p0Util {

    private static ConnectionPool CONNECTION_POOL;

    /**
     * inizializza il database
     *
     * @param dbUrl  url di database
     * @param dbUser user
     * @param dbPwd  password
     */
    public static final void initDBPool(String dbUrl, String dbUser, String dbPwd) {
        CONNECTION_POOL = new ConnectionPool(dbUrl, dbUser, dbPwd, "com.mysql.jdbc.Driver");
    }

    /**
     * Initialize with a custom ConnectionPool
     * @param connectionPool
     */
    public static final void initDBPool(ConnectionPool connectionPool) {
        CONNECTION_POOL = connectionPool;
    }

    /**
     * distrugge la piscina
     */
    public static final void destroy() {
        if (CONNECTION_POOL != null) {
            CONNECTION_POOL.destroy();
        }
    }

    /**
     * un metodo statico per liberare e ristituire la connessione nella
     * piscina
     *
     * @param conn
     */
    public static final void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println("errore durante la liberazione della connessione: " + ex.getMessage());
            }
        }

    }

    /**
     * assegnare una connessione valida
     *
     * @return l'oggetto connessione
     */
    public static final synchronized Connection getConnection() {
        if (CONNECTION_POOL == null) {
            System.err.println("connection pool not initialized. return null");
            return null;
        }

        return CONNECTION_POOL.getConnection();
    }

    public static final ConnectionPool getConnectionPool() {
        return CONNECTION_POOL;
    }
}
