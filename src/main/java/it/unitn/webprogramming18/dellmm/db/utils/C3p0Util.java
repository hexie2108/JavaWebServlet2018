/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.db.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
                        ds = new ComboPooledDataSource();

                        ds.setDriverClass("com.mysql.jdbc.Driver");
                        ds.setBreakAfterAcquireFailure(false);
                        ds.setTestConnectionOnCheckout(false);
                        ds.setTestConnectionOnCheckin(false);
                        ds.setMaxPoolSize(100);
                        //ogni volta incrementa 3 connessioni
                        ds.setAcquireIncrement(3);
                        //ogni 60 secondo check i connessioni liberi nel pool
                        ds.setIdleConnectionTestPeriod(60);
                        //numero di prova dopo il fallimento
                        ds.setAcquireRetryAttempts(10);
                        //intervallo di tempo tra due prove
                        ds.setAcquireRetryDelay(1000);
                        ds.setMaxIdleTime(60);
                        ds.setMaxStatements(8);
                        ds.setMaxStatementsPerConnection(5);
                        ds.setJdbcUrl(dbUrl);
                        ds.setUser(dbUser);
                        ds.setPassword(dbPwd);
                }
                catch (PropertyVetoException ex)
                {
                        ex.printStackTrace();

                }
        }

        /**
         * liberare intera pool
         */
        public static final void destroy()
        {
                if (ds != null)
                {
                        ds.close();
                }
        }

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
                                ex.printStackTrace();
                        }
                }
               
        }

        /**
         * estrare una connessione valida
         *
         * @return una connessione
         * @throws SQLException
         */
        public static final synchronized Connection getConnection()
        {
                Connection conn=null;
                try
                {
                        conn= ds.getConnection();
                }
                catch (SQLException ex)
                {
                        System.err.println("errore nell'assegnazione della nuova connessione: "+ex.getMessage());
                        ex.printStackTrace();
                }
                return conn;

        }
}
