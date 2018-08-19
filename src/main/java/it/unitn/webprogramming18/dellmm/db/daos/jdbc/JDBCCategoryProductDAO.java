package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link CategoryProductDAO} interface.
 */
public class JDBCCategoryProductDAO extends JDBCDAO<CategoryProduct, Integer> implements CategoryProductDAO {
    public JDBCCategoryProductDAO(ConnectionPool cp) {
        super(cp);
    }

    private CategoryProduct getCategoryProductFromResultSet(ResultSet rs) throws SQLException {
        CategoryProduct categoryProduct = new CategoryProduct();

        categoryProduct.setId(rs.getInt("id"));
        categoryProduct.setName(rs.getString("name"));
        categoryProduct.setDescription(rs.getString("description"));
        categoryProduct.setImg(rs.getString("img"));

        return categoryProduct;
    }

    @Override
    public Long getCount() throws DAOException {
        Connection CON = CP.getConnection();
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM CategoryProduct")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count categoryProduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public Integer insert(CategoryProduct categoryProduct) throws DAOException {
        if (categoryProduct == null) {
            throw new DAOException("categoryProduct bean is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO CategoryProduct (name, description, img) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, categoryProduct.getName());
            stm.setString(2, categoryProduct.getDescription());
            stm.setString(3, categoryProduct.getImg());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                categoryProduct.setId(rs.getInt(1));
            }

            return categoryProduct.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new categoryProduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }

    @Override
    public CategoryProduct getByPrimaryKey(Integer primaryKey) throws DAOException {
        CategoryProduct categoryProduct = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryProduct WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    categoryProduct = getCategoryProductFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the categoryProduct for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryProduct;
    }

    @Override
    public List<CategoryProduct> getAll() throws DAOException {
        List<CategoryProduct> categoryProductList = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryProduct")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    categoryProductList.add(getCategoryProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of categoryProduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryProductList;
    }


    @Override
    public List<CategoryProduct> getCategoryProductList(Integer index, Integer number) throws DAOException {
        if (index == null || index < 0 || number == null || number < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
        }

        List<CategoryProduct> cateogryProductList = new ArrayList<>();

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryProduct ORDER BY id DESC LIMIT ?,?")) {
            stm.setInt(1, index);
            stm.setInt(2, number);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    cateogryProductList.add(getCategoryProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Catogeryproduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return cateogryProductList;

    }

    @Override
    public CategoryProduct update(CategoryProduct categoryProduct) throws DAOException {
        if (categoryProduct == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed categoryProduct is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE CategoryProduct SET "
                        + "name =?,"
                        + "description =?,"
                        + "img =?  "
                        + "WHERE id = ?"
        )) {

            stm.setString(1, categoryProduct.getName());
            stm.setString(2, categoryProduct.getDescription());
            stm.setString(3, categoryProduct.getImg());
            stm.setInt(4, categoryProduct.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the categoryProduct");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the categoryProduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryProduct;
    }

    @Override
    public List<CategoryProduct> filter(Integer id, String name, String description) throws DAOException {
        Connection CON = CP.getConnection();

        List<CategoryProduct> categoryLists = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * FROM CategoryProduct WHERE " +
                        "(? IS NULL OR id LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                        "(? IS NULL OR name LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                        "(? IS NULL OR description LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%'))"
        )) {
            if (id == null) {
                stm.setNull(1, Types.INTEGER);
                stm.setNull(2, Types.INTEGER);
            } else {
                stm.setString(1, id.toString());
                stm.setString(2, id.toString());
            }

            stm.setString(3, name);
            stm.setString(4, name);

            stm.setString(5, description);
            stm.setString(6, description);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    categoryLists.add(getCategoryProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of categoryProduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryLists;
    }

    @Override
    public void delete(Integer id) throws DAOException {
        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement(
                "DELETE FROM CategoryProduct WHERE id = ?"
        )) {
            stm.setInt(1, id);

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to delete the categoryProduct");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to delete the categoryProduct", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }
}
