package it.unitn.webprogramming18.dellmm.db.daos;

import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;

import java.util.List;


public interface CategoryListDAO extends DAO<CategoryList, Integer> {
    /**
     * Returns the number of {@link CategoryList categoryList} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Returns the {@link CategoryList categoryList} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code categoryList} to get.
     * @return the {@code categoryList} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public CategoryList getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid {@link CategoryList categoryList} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code categoryList}.
     * @throws DAOException if an error occurred during the information
     *                      retrieving.
     */
    @Override
    public List<CategoryList> getAll() throws DAOException;

    /**
     * Update the categoryList passed as parameter and returns it.
     *
     * @param categoryList the categoryList used to update the persistence system.
     * @return the updated categoryList.
     * @throws DAOException if an error occurred during the action.
     */
    @Override
    public CategoryList update(CategoryList categoryList) throws DAOException;
}
