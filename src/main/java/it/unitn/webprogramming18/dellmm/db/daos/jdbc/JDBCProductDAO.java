package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link ProductDAO} interface.
 */
public class JDBCProductDAO extends JDBCDAO<Product, Integer> implements ProductDAO {
    public JDBCProductDAO(Connection con) {
        super(con);
    }

    private Product getProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();

        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setImg(rs.getString("img"));
        product.setLogo(rs.getString("logo"));
        product.setCategoryProductId(rs.getInt("categoryProductId"));

        return product;
    }

    @Override
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count product", ex);
        }

        return 0L;
    }

    public Integer insert(Product product) throws DAOException {
        if (product == null) {
            throw new DAOException("product bean is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Product (name, description, img, logo, categoryProductId, privateListId) VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, product.getName());
            stm.setString(2, product.getDescription());
            stm.setString(3, product.getImg());
            stm.setString(4, product.getLogo());
            stm.setInt(5, product.getCategoryProductId());
            stm.setInt(6, product.getPrivateListId());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }

            return product.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new product", ex);
        }
    }

    @Override
    public Product getByPrimaryKey(Integer primaryKey) throws DAOException {
        Product product = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    product = getProductFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the product for the passed primary key", ex);
        }

        return product;
    }

    @Override
    public List<Product> getAll() throws DAOException {
        List<Product> productList = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productList.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        }

        return productList;
    }

    @Override
    public Product update(Product product) throws DAOException {
        if (product == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed product is null"));
        }

        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE Product SET " +
                        "name =?," +
                        "description =?," +
                        "img =?," +
                        "logo =?," +
                        "categoryProductId =?, " +
                        "privateListId =? " +
                        "WHERE id = ?"
        )) {
            stm.setString(1, product.getName());
            stm.setString(2, product.getDescription());
            stm.setString(3, product.getImg());
            stm.setString(4, product.getLogo());
            stm.setInt(5, product.getCategoryProductId());
            stm.setInt(6, product.getPrivateListId());
            stm.setInt(7, product.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the product");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the product", ex);
        }

        return product;
    }


    public List<Product> getProductsInListByListId(Integer listId) throws DAOException {
        List<Product> products = new ArrayList<>();

        if (listId == null) {
            throw new DAOException("listId is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                + "WHERE  ProductInList.listId = ?")) {
            stm.setInt(1, listId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    products.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of productInList", ex);
        }

        return products;
    }
}
