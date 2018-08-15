package it.unitn.webprogramming18.dellmm.db.utils.factories.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;

import java.beans.PropertyVetoException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This JDBC implementation of {@code DAOFactory}.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public class JDBCDAOFactory implements DAOFactory {

    private static JDBCDAOFactory instance;

    private ConnectionPool cp;

    /**
     * The private constructor used to create the singleton instance of this
     * {@code DAOFactory}.
     *
     * @param dbUrl the url to access the database.
     * @throws DAOFactoryException if an error occurred during
     *                             {@code DAOFactory} creation.
     */
    private JDBCDAOFactory(String dbUrl, String dbUser, String dbPwd) throws DAOFactoryException {
        super();

        final String DRIVER = "com.mysql.jdbc.Driver";

        // Check if driver exists before creating ConnectionPool
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            throw new RuntimeException(cnfe.getMessage(), cnfe.getCause());
        }

        cp = new ConnectionPool(dbUrl, dbUser, dbPwd, DRIVER);
    }

    /**
     * Call this method before use the instance of this class.
     *
     * @param dbUrl the url to access to the database.
     * @throws DAOFactoryException if an error occurred during db factory
     *                             configuration.
     */
    public static void configure(String dbUrl, String dbUser, String dbPwd) throws DAOFactoryException {
        if (instance == null) {
            instance = new JDBCDAOFactory(dbUrl, dbUser, dbPwd);
        } else {
            throw new DAOFactoryException("DAOFactory already configured. You can call configure only one time");
        }
    }

    /**
     * Returns the singleton instace of this {@link DAOFactory}.
     *
     * @return the singleton instance of this {@code DAOFactory}.
     * @throws DAOFactoryException if an error occurred if this db factory is
     *                             not yet configured.
     */
    public static JDBCDAOFactory getInstance() throws DAOFactoryException {
        if (instance == null) {
            throw new DAOFactoryException("DAOFactory not yet configured. Call DAOFactory.configure(String dbUrl) before use the class");
        }
        return instance;
    }

    /**
     * Shutdowns the access to the storage system.
     */
    @Override
    public void shutdown() {
        cp.destroy();
    }

    /**
     * Returns the concrete {@link DAO db} which type is the class passed as
     * parameter.
     *
     * @param <DAO_CLASS>  the class name of the {@code db} to get.
     * @param daoInterface the class instance of the {@code db} to get.
     * @return the concrete {@code db} which type is the class passed as
     * parameter.
     * @throws DAOFactoryException if an error occurred during the
     *                             operation.
     */
    @Override
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DAOFactoryException {
        Package pkg = daoInterface.getPackage();
        String prefix = pkg.getName() + ".jdbc.JDBC";

        try {
            Class daoClass = Class.forName(prefix + daoInterface.getSimpleName());

            Constructor<DAO_CLASS> constructor = daoClass.getConstructor(ConnectionPool.class);
            DAO_CLASS daoInstance = constructor.newInstance(cp);
            if (!(daoInstance instanceof JDBCDAO)) {
                throw new DAOFactoryException("The daoInterface passed as parameter doesn't extend JDBCDAO class");
            }

            return daoInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException ex) {
            ex.printStackTrace();
            throw new DAOFactoryException("Impossible to return the DAO", ex);
        }
    }


    // TODO: Da togliere
    public ConnectionPool getCP() {
        return cp;
    }
}
