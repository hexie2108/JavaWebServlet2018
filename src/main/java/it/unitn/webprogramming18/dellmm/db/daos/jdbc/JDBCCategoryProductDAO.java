package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link CategoryProductDAO} interface.
 */
public class JDBCCategoryProductDAO extends JDBCDAO<CategoryProduct, Integer> implements CategoryProductDAO {
    public JDBCCategoryProductDAO(Connection con) {
        super(con);
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM CategoryProduct")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count categoryProduct", ex);
        }

        return 0L;
    }
    
   public Integer insert(CategoryProduct categoryProduct) throws DAOException {
        if (categoryProduct == null) {
            throw new DAOException("categoryProduct bean is null");
        }
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
        }
   }

    @Override
    public CategoryProduct getByPrimaryKey(Integer primaryKey) throws DAOException {
        CategoryProduct categoryProduct = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryProduct WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    categoryProduct = getCategoryProductFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the categoryProduct for the passed primary key", ex);
        }

        return categoryProduct;
    }

    @Override
    public List<CategoryProduct> getAll() throws DAOException {
        List<CategoryProduct> categoryProductList = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryProduct")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    categoryProductList.add(getCategoryProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of categoryProduct", ex);
        }

        return categoryProductList;
    }

    @Override
    public CategoryProduct update(CategoryProduct categoryProduct) throws DAOException {
        if (categoryProduct == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed categoryProduct is null"));
        }

        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE CategoryProduct SET " +
                        "name =?," +
                        "description =?," +
                        "img =?  " +
                        "WHERE id = ?"
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
        }

        return categoryProduct;
    }
}
