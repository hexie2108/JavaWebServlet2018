package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;

import java.util.List;

public interface CategoryProductDAO extends DAO<CategoryProduct, Integer> {
    /**
     * Returns the number of {@link CategoryProduct categoryProduct} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Persists the new categoryProduct passed as parameter
     * to the storage system.
     *
     * @param categoryProduct the new categoryProduct to insert as entry
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     */
    public Integer insert(CategoryProduct categoryProduct) throws DAOException;


    /**
     * Returns the {@link CategoryProduct categoryProduct} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code categoryProduct} to get.
     * @return the {@code categoryProduct} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public CategoryProduct getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link CategoryProduct categoryProduct} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code categoryProduct}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List<CategoryProduct> getAll() throws DAOException;

    /**
     * get la lista di categoria di prodotto in base a id decrescente
     *
     * @param index  l'indice in cui inizia a prendere
     * @param number numero che vuoi ottenere
     * @return la lista di categoria di prodotto
     * @throws DAOException
     */
    public List<CategoryProduct> getCategoryProductList(Integer index, Integer number) throws DAOException;


    /**
     * Update the categoryProduct passed as parameter and returns it.
     *
     * @param categoryProduct the categoryProduct used to update the persistence system.
     * @return the updated categoryProduct.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public CategoryProduct update(CategoryProduct categoryProduct) throws DAOException;

    public List<CategoryProduct> filter(Integer id, String name, String description) throws DAOException;

    public void delete(Integer id) throws DAOException;
}
