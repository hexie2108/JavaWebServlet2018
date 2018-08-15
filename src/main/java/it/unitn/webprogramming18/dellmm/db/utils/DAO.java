/*
 * AA 2016-2017
 * Introduction to Web Programming
 * Common - DAO
 * UniTN
 */
package it.unitn.webprogramming18.dellmm.db.utils;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import java.util.List;

/**
 * L'interfaccia più base da implementare per ogni DAO
 *
 * @param <ENTITY_CLASS> tipo di entità/classe bean da gestire.
 * @param <PRIMARY_KEY> tipo della chiave primaria di entità/tabella da gestire
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public interface DAO<ENTITY_CLASS, PRIMARY_KEY>
{

    /**
     * Ritorna il numero totale di records della entità/tabella
     * {@code ENTITY_CLASS} nella database
     *
     * @return il numero totale di records.
     * @throws DAOException se si è verificato un errore durante il recupero
     * dell'informazione
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public Long getCount() throws DAOException;

    /**
     * recupera una istanza {@code ENTITY_CLASS} dal database attraverso il
     * chiave primaria passata
     *
     * @param primaryKey ID da passare per ottere l'istanza
     * @return l'istanza {@code ENTITY_CLASS} ricercata oppure {@code null} se
     * non ha trovato
     * @throws DAOException se si è verificato un errore durante il recupero
     * dell'informazione retrieving.
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public ENTITY_CLASS getByPrimaryKey(PRIMARY_KEY primaryKey) throws DAOException;

    /**
     * Restituisce la lista di tutte le entità di tipo {@code ENTITY_CLASS}
     *
     * @return la lista di tutte le entità di tipo {@code ENTITY_CLASS}.
     * @throws DAOException se si è verificato un errore durante il recupero
     * dell'informazione retrieving.
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public List<ENTITY_CLASS> getAll() throws DAOException;

    /**
     * Aggiorna l'entità passata nel database
     *
     * @param entity l'entità che vuole aggiornare
     * @return l'entità aggiornato
     * @throws DAOException se si è verificato un errore durante il recupero
     * dell'informazione
     * @author Stefano Chirico
     * @since 1.0.170418
     */
    public ENTITY_CLASS update(ENTITY_CLASS entity) throws DAOException;

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
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoClass) throws DAOFactoryException;

    
    
    
}
