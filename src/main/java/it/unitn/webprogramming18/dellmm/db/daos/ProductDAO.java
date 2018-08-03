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
     * Update the product passed as parameter and returns it.
     *
     * @param product the product used to update the persistence system.
     * @return the updated product.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Product update(Product product) throws DAOException;
    
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
     * get la lista di prodotto pubblico in base a id decrescente
     * @param index  l'indice in cui inizia a prendere
     * @param number quantità che vuoi ottenere
     * @return la lista di prodotto pubblico
     * @throws DAOException 
     */
    public List<Product> getPublicProductList(Integer index, Integer number) throws DAOException;
    
    /**
     * get il numero del prodotto pubblico
     * @return il numero del prodotto pubblico
     * @throws DAOException 
     */
    public Integer getCountOfPublicProduct() throws DAOException;
    
    /**
     * gel lista di prodotto di una determina categoria in base a id decrescente
     * @param catId id della categoria
     * @param index  l'indice in cui inizia a prendere
     * @param number quantità che vuoi ottenere
     * @return la lista di prodotto publico di una determinata categoria
     * @throws DAOException 
     */
    public List<Product> getPublicProductListByCatId(Integer catId, Integer index, Integer number) throws DAOException;
    
    
    /**
     * get il numero del prodotto pubblico di una categoria specificato 
     * @param catId id categoria
     * @return il numero del prodotto pubblico di una categoria specificato 
     * @throws DAOException 
     */
    public Integer getCountOfPublicProductByCatId(Integer catId) throws DAOException;
    

    
    /**
     * trovare i prodotti pubblici corrispondenti dato un nome di prodotto (anche incompleto)
     * @param name nome da ricercare
     * @param order indicare ordinamento, può essere "categoryName" o "productName"
     * @param index  l'indice in cui inizia a prendere
     * @param number quantità che vuoi ottenere
     * @return la lista di prodtto corrispondente
     * @throws DAOException 
     */
    public List<Product> getPublicProductListByNameSearch(String name, String order, Integer index, Integer number) throws DAOException;

    
        /**
     * get il numero del prodotto pubblico con nome corrisondente 
     * @param name nome da ricercare
     * @return il numero del prodotto pubblico con nome corrisondente 
     * @throws DAOException 
     */
    public Integer getCountOfPublicProductByNameSearch(String name) throws DAOException;
 

    /**
     * get i prodotto di una lista
     *
     * @param listId
     * @return la lista di prodotto di una lista
     * @throws DAOException if an error occurred during the action.
     */
    public List<Product> getProductsInListByListId(Integer listId) throws DAOException;
    
       /**
     * get i prodotti ancora da comprare di una lista
     *
     * @param listId
     * @return la lista di prodotto ancora da comprare di una lista
     * @throws DAOException if an error occurred during the action.
     */
    public List<Product> getProductsNotBuyInListByListId(Integer listId) throws DAOException;
    
     /**
     * get i prodotti già comprato di una lista
     *
     * @param listId
     * @return la lista di prodotto già comprato di una lista
     * @throws DAOException if an error occurred during the action.
     */
    public List<Product> getProductsBoughtInListByListId(Integer listId) throws DAOException;
}
