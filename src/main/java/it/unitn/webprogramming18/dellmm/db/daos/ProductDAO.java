package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;

import java.util.List;

public interface ProductDAO extends DAO<Product, Integer> {
    /**
     * Returns the number of {@link Product product} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;
    
    /**
     * Persists the new product passed as parameter
     * to the storage system.
     * 
     * @param product the new product to insert as entry
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     */
    public Integer insert(Product product) throws DAOException;

    /**
     * Returns the {@link Product product} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code product} to get.
     * @return the {@code product} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Product getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Product product} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code product}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List<Product> getAll() throws DAOException;

    /**
     * Update the product passed as parameter and returns it.
     *
     * @param product the product used to update the persistence system.
     * @return the updated product.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Product update(Product product) throws DAOException;

    /**
     * Get a list of all the products in a dellmm.list with specified listId
     *
     * @param listId
     * @return A list of the products in a dellmm.list
     * @throws DAOException if an error occurred during the action.
     */
    public List<Product> getProductsInListByListId(Integer listId) throws DAOException;
    
    /**
     * Returns the list of all the products' names stored in the
     * storage system.
     *
     * @return the list of all the names of the products
     * @throws DAOException if an error occurred during the information retrieving.
     */
    public List<String> getAllPublicProductsNames() throws DAOException;
}
