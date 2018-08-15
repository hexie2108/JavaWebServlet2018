package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link ProductInList} interface.
 */
public class JDBCProductInListDAO extends JDBCDAO<ProductInList, Integer> implements ProductInListDAO {

    private ProductInList getProductInListFromResultSet(ResultSet rs) throws SQLException {
        ProductInList productInList = new ProductInList();

        productInList.setId(rs.getInt("id"));
        productInList.setProductId(rs.getInt("productId"));
        productInList.setStatus(rs.getBoolean("status"));
        productInList.setListId(rs.getInt("listId"));

        return productInList;
    }

    @Override
    public Long getCount() throws DAOException {
        Connection CON = CP.getConnection();

        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM ProductInList")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public Integer insert(ProductInList productInList) throws DAOException {
        // TODO: Check missing finally?
        if (productInList == null) {
            throw new DAOException("productInList bean is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO ProductInList (productId, listId, status) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, productInList.getProductId());
            stm.setInt(2, productInList.getListId());
            stm.setBoolean(3, productInList.isStatus());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                productInList.setId(rs.getInt(1));
            }

            return productInList.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new productInList entry", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }

    @Override
    public ProductInList getByPrimaryKey(Integer primaryKey) throws DAOException {
        ProductInList productInList = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM ProductInList WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    productInList = getProductInListFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the productInList for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productInList;
    }

    @Override
    public List<ProductInList> getAll() throws DAOException {
        List<ProductInList> productInLists = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM ProductInList")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productInLists.add(getProductInListFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productInLists;
    }

    @Override
    public ProductInList update(ProductInList productInList) throws DAOException {
        if (productInList == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed productInList is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE ProductInList SET " +
                        "productId = ?," +
                        "listId = ?, " +
                        "status = ? " +
                        "WHERE id = ?"
        )) {

            stm.setInt(1, productInList.getProductId());
            stm.setInt(2, productInList.getListId());
            stm.setBoolean(3, productInList.isStatus());
            stm.setInt(4, productInList.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the productInList");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productInList;
    }

    @Override
    public Boolean checkIsProductInListByIds(Integer productId, Integer listId) throws DAOException {

        Boolean res = false;

        if (listId == null || productId == null) {
            throw new DAOException("One or both parameters (listId, productId) are null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM ProductInList WHERE listId = ? AND productId = ?")) {

            stm.setInt(1, listId);
            stm.setInt(2, productId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    res = true;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return res;
    }

    @Override
    public ProductInList getByProductIdAndListId(Integer productId, Integer listId) throws DAOException {
        ProductInList productInList = null;
        if (listId == null || productId == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed listId or  productId is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(" SELECT * FROM ProductInList WHERE "
                + " productId = ? AND "
                + " listId = ? ")) {
            stm.setInt(1, productId);
            stm.setInt(2, listId);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    productInList = getProductInListFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the productInList for the passed productId and listId", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productInList;

    }

    @Override
    public void deleteByProductIdAndListId(Integer productId, Integer listId) throws DAOException {
        if (listId == null || productId == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed listId or  productId is null"));
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement(
                " DELETE FROM ProductInList WHERE "
                        + "productId = ? AND "
                        + "listId = ? "
        )) {
            stm.setInt(1, productId);
            stm.setInt(2, listId);

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to delete the productInList");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }
}
