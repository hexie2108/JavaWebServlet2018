/*
 * AA 2016-2017
 * Introduction to Web Programming
 * Common - DAO
 * UniTN
 */
package it.unitn.webprogramming18.dellmm.db.utils.factories;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;

import java.sql.Connection;

/**
 * L'interfaccia più base da implementare per ogni fabbrica di DAO
 * {@code DAOFactor(y)}
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public interface DAOFactory {


    /**
     * chiudere la connessione
     *
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public void shutdown();

    /**
     * Returns the concrete {@link DAO db} which type is the class passed as
     * parameter.
     *
     * @param <DAO_CLASS>  the class name of the {@code db} to get.
     * @param daoInterface the class instance of the {@code db} to get.
     * @return the concrete {@code db} which type is the class passed as
     * parameter.
     * @throws DAOFactoryException if an error occurred during the operation.
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DAOFactoryException;
}
