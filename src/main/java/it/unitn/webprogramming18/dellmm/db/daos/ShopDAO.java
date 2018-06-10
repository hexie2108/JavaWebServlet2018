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
     * retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Returns the {@link Shop shop} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code shop} to get.
     * @return the {@code shop} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public Shop getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link Shop shop} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code shop}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     */
    @Override
    public List<Shop> getAll() throws DAOException;

    /**
     * Update the shop passed as parameter and returns it.
     * @param shop the shop used to update the persistence system.
     * @return the updated shop.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public Shop update(Shop shop) throws DAOException;
}
