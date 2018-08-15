package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;

import java.util.List;

public interface ProductInListDAO extends DAO<ProductInList, Integer>
{

        /**
         * Returns the number of {@link ProductInList productInList} stored on
         * the persistence system of the application.
         *
         * @return the number of records present into the storage system.
         * @throws DAOException if an error occurred during the information
         * retrieving.
         */
        @Override
        public Long getCount() throws DAOException;

        /**
         * Persists the new productInList passed as parameter to the storage
         * system.
         *
         * @param productInList the new productInList to insert as entry
         * @return the id of the new persisted record.
         * @throws DAOException if an error occurred during the persist action.
         */
        public Integer insert(ProductInList productInList) throws DAOException;

        /**
         * Returns the {@link ProductInList productInList} with the primary key
         * equals to the one passed as parameter.
         *
         * @param primaryKey the {@code id} of the {@code productInList} to get.
         * @return the {@code productInList} with the id equals to the one
         * passed as parameter or {@code null} if no entities with that id is
         * not present into the storage system.
         * @throws DAOException if an error occurred during the information
         * retrieving.
         */
        @Override
        public ProductInList getByPrimaryKey(Integer primaryKey) throws DAOException;

        /**
         * Returns the list of all the valid {@link ProductInList productInList}
         * stored by the storage system.
         *
         * @return the list of all the valid {@code productInList}.
         * @throws DAOException if an error occurred during the information
         * retrieving.
         */
        @Override
        public List<ProductInList> getAll() throws DAOException;

        /**
         * Update the productInList passed as parameter and returns it.
         *
         * @param productInList the productInList used to update the persistence
         * system.
         * @return the updated productInList.
         * @throws DAOException if an error occurred during the action.
         */
        @Override
        public ProductInList update(ProductInList productInList) throws DAOException;

        /**
         * check l'esistenza della relazione tra un prodotto e una lista
         *
         * @param productId id prodotto
         * @param listId id lista
         * @return TRUE se esiste tale relazione, FALSE se non eisiste
         * @throws DAOException if an error occurred during the action.
         */
        public Boolean checkIsProductInListByIds(Integer productId, Integer listId) throws DAOException;

        /**
         *
         * get la relazione tra un prodotto e una lista
         *
         * @param listId id lista
         * @param productId id prodotto
         * @return
         * @throws DAOException
         */
        public ProductInList getByProductIdAndListId(Integer productId, Integer listId) throws DAOException;

        /**
         * elimina la relazione tra un prodotto e una lista
         *
         * @param listId id lista
         * @param productId id prodotto
         * @throws DAOException
         */
        public void deleteByProductIdAndListId(Integer productId, Integer listId) throws DAOException;

}
