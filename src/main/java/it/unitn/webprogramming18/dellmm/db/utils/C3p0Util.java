package it.unitn.webprogramming18.dellmm.db.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * pisciana di connessione, che occupa di creare/mantenere una serie di
 * connessioni in attivi, assegnare dao quando ci sono le richieste
 *
 * @author mikuc
 */
public class C3p0Util
{

        private static ComboPooledDataSource ds;

        /**
         * inizializza il database
         *
         * @param dbUrl url di database
         * @param dbUser user
         * @param dbPwd password
         */
        public static final void initDBPool(String dbUrl, String dbUser, String dbPwd)
        {
                try
                {
                      //inizializzazione della piscina
                        ds = new ComboPooledDataSource();

                        ds.setDriverClass("com.mysql.jdbc.Driver");
                        ds.setBreakAfterAcquireFailure(false);
                        ds.setTestConnectionOnCheckout(false);
                        ds.setTestConnectionOnCheckin(true);
                        ds.setMaxPoolSize(100);
                        //ogni volta incrementa 3 connessioni
                        ds.setAcquireIncrement(3);
                        //ogni 60 secondo check i connessioni liberi nel pool
                        ds.setIdleConnectionTestPeriod(60);
                        //numero di prova dopo il fallimento
                        ds.setAcquireRetryAttempts(10);
                        //intervallo di tempo tra due prove
                        ds.setAcquireRetryDelay(1000);
                        ds.setMaxIdleTime(180);
                        ds.setMaxStatements(8);
                        ds.setMaxStatementsPerConnection(5);
                        ds.setJdbcUrl(dbUrl);
                        ds.setUser(dbUser);
                        ds.setPassword(dbPwd);
                }
                catch (PropertyVetoException ex)
                {
                        System.err.println("errore durante l'inizializzazione della piscina: " + ex.getMessage());
                }
        }

        /**
         * distrugge la piscina
         */
        public static final void destroy()
        {
                if (ds != null)
                {
                        ds.close();
                }
        }

        /**
         * un metodo statico per liberare e ristituire la connessione nella
         * piscina
         *
         * @param conn
         */
        public static final void close(Connection conn)
        {
                if (conn != null)
                {
                        try
                        {
                                conn.close();
                        }
                        catch (SQLException ex)
                        {
                                System.err.println("errore durante la liberazione della connessione: " + ex.getMessage());
                        }
                }

        }

        /**
         * assegnare una connessione valida
         *
         * @return l'oggetto connessione
         */
        public static final synchronized Connection getConnection()
        {
                Connection conn = null;
                try
                {
                        conn = ds.getConnection();
                }
                catch (SQLException ex)
                {
                        System.err.println("errore durante l'assegnazione della nuova connessione: " + ex.getMessage());
                }
                return conn;

        }
}
