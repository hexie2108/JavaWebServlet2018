/*
 * AA 2016-2017
 * Introduction to Web Programming
 * Common - DAO
 * UniTN
 */
package it.unitn.webprogramming18.dellmm.db.utils.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.C3p0Util;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;

import java.sql.Connection;
import java.util.HashMap;

/**
 * il classe astratto di DAO per JDBC, da estendere
 *
 * @param <ENTITY_CLASS>      tipo di entità/classe bean da gestire.
 * @param <PRIMARY_KEY_CLASS> tipo della chiave primaria di entità/tabella da gestire
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public abstract class JDBCDAO<ENTITY_CLASS, PRIMARY_KEY_CLASS> implements DAO<ENTITY_CLASS, PRIMARY_KEY_CLASS> {

    // HashMap di altre classi DAOs che possono interagire con questo DAO
    protected final HashMap<Class, DAO> FRIEND_DAOS;

    protected final transient ConnectionPool CP;

    /**
     * il costruttore base di JDBC DAO salva la ConnectionPool da usare per
     * ottente una connessione e successivamente accedere al database
     *
     * @param cp ottiene la connessione da una {@code ConnectionPool}.
     */
    protected JDBCDAO(ConnectionPool cp) {
        super();
        FRIEND_DAOS = new HashMap<>();
        CP = cp;
    }

    // TODO: Da togliere
    protected JDBCDAO() {
        super();
        FRIEND_DAOS = new HashMap<>();
        CP = C3p0Util.getConnectionPool();
    }

    /**
     * se questo DAO può interagire con il classe DAO passato, allora
     * restituisce il DAO della classe passato
     *
     * @param <DAO_CLASS> il tipo di classe del DAO che può interagire con
     *                    questo DAO.
     * @param daoClass    il nome classe del DAO che può interagire con questo DAO.
     * @return l'istanza di DAO o null se tipo del DAO passato non può
     * interagire con questo DAO
     * @throws DAOFactoryException se c'è un errore.
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    @Override
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoClass) throws DAOFactoryException {
        return (DAO_CLASS) FRIEND_DAOS.get(daoClass);
    }


}
