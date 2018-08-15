package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Shop;
import java.util.List;

public interface ShopDAO extends DAO<Shop, Integer> {
    /**
     * Returns the number of {@link Shop shop} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Persists the new shop passed as parameter
     * to the storage system.
     *
     * @param shop the new shop to insert as entry
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     */
    public Integer insert(Shop shop) throws DAOException;

    /**
     * Returns the {@link Shop shop} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code shop} to get.
     * @return the {@code shop} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Shop getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Shop shop} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code shop}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List<Shop> getAll() throws DAOException;

    /**
     * Update the shop passed as parameter and returns it.
     *
     * @param shop the shop used to update the persistence system.
     * @return the updated shop.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Shop update(Shop shop) throws DAOException;

    /**
     * Returns all the shops of a specified category
     *
     * @param category
     * @return a List containing all shops of category specified in parameter
     * @throws DAOException if an error occurred during the action.
     */
    public List<Shop> getShopsByCategory(String category) throws DAOException;
    
    /**
     * Returns a list of relevant shops at max R meters from user's position.
     * "Relevant" means that returned shops are of categories from user lists' categories.
     * Needs a pre-created list of all CategoryList IDs of all lists that the user can access.
     * 
     * @param categories list of ints representing relevant categories to check shops for
     * @param usrLat user's latitude
     * @param usrLng user's longitude
     * @return       described above
     * @throws DAOException if an error occurred during the action.
     */
    public List<Shop> getRelevantShopsInProximity(List<Integer> categories, Double usrLng, Double usrLat) throws DAOException;
}
