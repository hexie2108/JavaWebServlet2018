/*
 * AA 2016-2017
 * Introduction to Web Programming
 * Common - DAO
 * UniTN
 */
package it.unitn.webprogramming18.dellmm.db.utils.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * il classe astratto di DAO per JDBC, da estendere
 *
 *
 * @param <ENTITY_CLASS> tipo di entità/classe bean da gestire.
 * @param <PRIMARY_KEY_CLASS> tipo della chiave primaria di entità/tabella da gestire
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public abstract class JDBCDAO<ENTITY_CLASS, PRIMARY_KEY_CLASS> implements DAO<ENTITY_CLASS, PRIMARY_KEY_CLASS>
{

    // il puntatore di connessione {@link Connection} da usare per accedere al database
    protected Connection CON;
    // HashMap di altre classi DAOs che possono interagire con questo DAO
    protected final HashMap<Class, DAO> FRIEND_DAOS;

    /**
     * il costruttore base di JDBC DAO salva la connessione da usare per
     * accedere al database
     *
     * @param con ottine la connesione {@code Connection}.
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    protected JDBCDAO(Connection con)
    {
        super();
        this.CON = con;
        FRIEND_DAOS = new HashMap<>();
    }
    
    /**
     * costruttore vuoto , non serve più associare una connessione fissa
     */
    protected JDBCDAO()
    {
            FRIEND_DAOS = new HashMap<>();
    }

    /**
     * se questo DAO può interagire con il classe DAO passato, allora
     * restituisce il DAO della classe passato
     *
     * @param <DAO_CLASS> il tipo di classe del DAO che può interagire con
     * questo DAO.
     * @param daoClass il nome classe del DAO che può interagire con questo DAO.
     * @return l'istanza di DAO o null se tipo del DAO passato non può
     * interagire con questo DAO
     * @throws DAOFactoryException se c'è un errore.
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    @Override
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoClass) throws DAOFactoryException
    {
        return (DAO_CLASS) FRIEND_DAOS.get(daoClass);
    }
    
    
  
}
