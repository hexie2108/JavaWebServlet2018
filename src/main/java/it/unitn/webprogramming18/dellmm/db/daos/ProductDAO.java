package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import java.sql.Timestamp;

import java.util.AbstractMap;
import java.util.HashMap;
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
     * get la lista di prodotto pubblico in base a id decrescente
     *
     * @param index  l'indice in cui inizia a prendere
     * @param number quantità che vuoi ottenere
     * @return la lista di prodotto pubblico
     * @throws DAOException
     */
    public List<Product> getPublicProductList(Integer index, Integer number) throws DAOException;

    /**
     * get il numero del prodotto pubblico
     *
     * @return il numero del prodotto pubblico
     * @throws DAOException
     */
    public Integer getCountOfPublicProduct() throws DAOException;

    /**
     * gel lista di prodotto di una determina categoria in base a id decrescente
     *
     * @param catId  id della categoria
     * @param index  l'indice in cui inizia a prendere
     * @param number quantità che vuoi ottenere
     * @return la lista di prodotto publico di una determinata categoria
     * @throws DAOException
     */
    public List<Product> getPublicProductListByCatId(Integer catId, Integer index, Integer number) throws DAOException;


    /**
     * get il numero del prodotto pubblico di una categoria specificato
     *
     * @param catId id categoria
     * @return il numero del prodotto pubblico di una categoria specificato
     * @throws DAOException
     */
    public Integer getCountOfPublicProductByCatId(Integer catId) throws DAOException;


    /**
     * trovare i prodotti pubblici corrispondenti dato un nome di prodotto (with incomplete name, misspelling, ...)
     *
     * @param name   nome da ricercare
     * @param order  indicare ordinamento, può essere "categoryName" o "productName"
     * @param index  l'indice in cui inizia a prendere
     * @param number quantità che vuoi ottenere
     * @return la lista di prodtto corrispondente
     * @throws DAOException
     */
    public List<Product> getPublicProductListByNameSearch(String name, String order, Integer index, Integer number) throws DAOException;

    public enum OrderableColumns{
        ID,
        NAME,
        DESCRIPTION,
        CATEGORY_NAME,
        PRIVATE_LIST_ID
    }

    /**
     * Filter all products(public and private) given a set of constraints
     * @param id substring of the id to search
     * @param name string that must be in the name(substring)
     * @param description string that must be in the description(substring)
     * @param categories list of categories id in which the product can be(if empty all categories are considered)
     * @param publicOnly true to show only public objects, false to show both public and private
     * @param privateListId substring of the list id(for a private object) if not null only private object are shown
     * @param order set the column used to order the results
     * @param direction true for ascendent order, false for descendent order
     * @param start index from which start
     * @param length number of objects to retrieve
     * @return list of the objects that respect specified conditions
     * @throws DAOException
     */
    public List<AbstractMap.SimpleEntry<Product, String>> filter(Integer id, String name, String description, List<Integer> categories, Boolean publicOnly, Integer privateListId, OrderableColumns order, Boolean direction, Integer start, Integer length) throws DAOException;

    /**
     * Get number of products(public and private) that respects given a set of constraints
     * @param id substring of the id to search
     * @param name string that must be in the name(substring)
     * @param description string that must be in the description(substring)
     * @param categories list of categories id in which the product can be(if empty all categories are considered)
     * @param publicOnly true to show only public objects, false to show both public and private
     * @param privateListId substring of the list id(for a private object) if not null only private object are shown
     * @return number of the objects that respect specified conditions
     * @throws DAOException
     */
    public Long getCountFilter(Integer id, String name, String description, List<Integer> categories, Boolean publicOnly, Integer privateListId) throws DAOException;

    /**
     * Finds all public product given a text to search in the name or description of the product(with incomplete name, misspelling, ...)
     * @param toSearch text to search in name or description of the product
     * @param order used to select the field for which the results are ordered. can be {"categoryName", "productName", "relevance"}
     * @param direction direction of the sort(big to small, small to big.  can be {"asc", "desc"}
     * @param categories array of the categories in which the product can be
     * @param index index from where to start
     * @param number number of result to get
     * @return list with the products requested
     * @throws DAOException
     */
    public List<Product> search(String toSearch, String order, String direction,List<Integer> categories, Integer index, Integer number) throws DAOException;

    /**
     * get il numero del prodotto pubblico con nome corrisondente
     *
     * @param name nome da ricercare
     * @param categories array of the categories in which the product can be
     * @return il numero del prodotto pubblico con nome corrisondente
     * @throws DAOException
     */
    public Long getCountSearch(String name, List<Integer> categories) throws DAOException;


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


    /**
     * get tutti i prodotti privati di una lista
     *
     * @param listId id lista
     * @return lista di prodtti privati
     */
    public List<Product> getPrivateProductByListId(Integer listId) throws DAOException;


    /**
     * elimina il prodotto
     *
     * @param productId id del prodotto
     * @throws DAOException
     */
    public void deleteProductById(Integer productId) throws DAOException;
    
    
    /**
     * get lista di prodotto dal log , in cui non è stato ancora inviato email , di un specifico utente
     * @param userId id utente
     * @param currentTime user corrente
     * @param predictionDay se la previsione di riaqsuito è minore tale numero di giorno,  invoca funzione
     * @return  lista di prodotto
     */
    public List<Product> getListProductFromLogNotEmailYetByUserId(Integer userId, Timestamp currentTime,  Integer predictionDay)  throws DAOException;

    /**
     * Get a list of common tokens in the products name that can be found with a query(with a relevance score) using a specific list(or null for no specific list)
     * @param query string to search
     * @param list id of the list(optional) to use together with the default products
     * @param categories list of categories to consider(if empty all are considered)
     * @return list of common tokens in the products name(with a relevance score)
     * @throws DAOException
     */
    public HashMap<String, Double> getNameTokensFiltered(String query, Integer list, List<Integer> categories) throws DAOException;
}
