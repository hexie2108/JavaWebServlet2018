package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.C3p0Util;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link ProductDAO} interface.
 */
public class JDBCProductDAO extends JDBCDAO<Product, Integer> implements ProductDAO
{

        private Product getProductFromResultSet(ResultSet rs) throws SQLException
        {
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
        public Long getCount() throws DAOException
        {

                CON = C3p0Util.getConnection();

                //try-with-resource, libera risorse in ogni caso
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product"))
                {
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                return counter.getLong(1);
                        }

                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return 0L;
        }

        public Integer insert(Product product) throws DAOException
        {

                if (product == null)
                {
                        throw new DAOException("product bean is null");
                }

                CON = C3p0Util.getConnection();

                //try-with-resource, libera risorse in ogni caso
                try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Product (name, description, img, logo, categoryProductId, privateListId) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS))
                {

                        stm.setString(1, product.getName());
                        stm.setString(2, product.getDescription());
                        stm.setString(3, product.getImg());
                        stm.setString(4, product.getLogo());
                        stm.setInt(5, product.getCategoryProductId());
                        stm.setInt(6, product.getPrivateListId());

                        stm.executeUpdate();

                        //get id dell'elemnto appena aggiunto
                        ResultSet rs = stm.getGeneratedKeys();
                        if (rs.next())
                        {
                                //se non è null, set id.
                                product.setId(rs.getInt(1));
                        }

                        return product.getId();
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to insert the new product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

        }

        @Override
        public Product update(Product product) throws DAOException
        {
                if (product == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed product is null"));
                }

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement(
                            " UPDATE Product SET "
                            + " name =?, "
                            + " description =?, "
                            + " img =?, "
                            + " logo =?, "
                            + " categoryProductId =? "
                            + " privateListId =? "
                            + " WHERE id = ? "
                ))
                {
                        stm.setString(1, product.getName());
                        stm.setString(2, product.getDescription());
                        stm.setString(3, product.getImg());
                        stm.setString(4, product.getLogo());
                        stm.setInt(5, product.getCategoryProductId());
                        stm.setInt(6, product.getPrivateListId());
                        stm.setInt(7, product.getId());

                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update the product");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return product;
        }

        @Override
        public Product getByPrimaryKey(Integer primaryKey) throws DAOException
        {
                Product product = null;
                if (primaryKey == null)
                {
                        throw new DAOException("primaryKey is null");
                }

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE id = ?"))
                {
                        stm.setInt(1, primaryKey);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        product = getProductFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the product for the passed primary key", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return product;
        }

        @Override
        public List<Product> getAll() throws DAOException
        {
                List<Product> productList = new ArrayList<>();

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product"))
                {
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        productList.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return productList;
        }

        @Override
        public List<Product> getPublicProductList(Integer index, Integer number) throws DAOException
        {
                if (index == null || index < 0 || number == null || number < 0)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
                }

                List<Product> productList = new ArrayList<>();

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE privateListId IS NULL ORDER BY id DESC LIMIT ?,?"))
                {
                        stm.setInt(1, index);
                        stm.setInt(2, number);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        productList.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return productList;

        }

        @Override
        public Integer getCountOfPublicProduct() throws DAOException
        {

                Integer number = null;

                CON = C3p0Util.getConnection();

                //try-with-resource, libera risorse in ogni caso
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product WHERE privateListId IS NULL"))
                {
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                number = counter.getInt(1);
                        }

                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return number;

        }

        @Override
        public List<Product> getPublicProductListByCatId(Integer catId, Integer index, Integer number) throws DAOException
        {
                if (catId == null || index == null || index < 0 || number == null || number < 0)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
                }

                List<Product> productList = new ArrayList<>();

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE categoryProductId = ? AND privateListId IS NULL ORDER BY id DESC LIMIT ?,?"))
                {
                        stm.setInt(1, catId);
                        stm.setInt(2, index);
                        stm.setInt(3, number);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        productList.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return productList;

        }

        @Override
        public Integer getCountOfPublicProductByCatId(Integer catId) throws DAOException
        {

                Integer number = null;

                if (catId == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
                }

                CON = C3p0Util.getConnection();

                //try-with-resource, libera risorse in ogni caso
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product WHERE categoryProductId = ? AND privateListId IS NULL"))
                {
                        stmt.setInt(1, catId);
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                number = counter.getInt(1);
                        }

                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count product of category", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return number;

        }

        @Override
        public List<Product> getPublicProductListByNameSearch(String name, String order, Integer index, Integer number) throws DAOException
        {
                if (name == null || (!order.equals("categoryName") && !order.equals("productName")) || index == null || index < 0 || number == null || number < 0)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
                }

                List<Product> productList = new ArrayList<>();

                CON = C3p0Util.getConnection();

                String sql = null;
                if (order.equals("categoryName"))
                {
                        sql = "SELECT P.* FROM Product P, Categoryproduct C WHERE P.categoryProductId=C.id AND P.name LIKE ? AND P.privateListId IS NULL ORDER BY C.name ASC  LIMIT ?,?";

                }
                else
                {
                        sql = "SELECT * FROM Product WHERE name LIKE ? AND privateListId IS NULL ORDER BY name ASC  LIMIT ?,?";
                }
                try (PreparedStatement stm = CON.prepareStatement(sql))
                {
                        stm.setString(1, "%" + name + "%");
                        stm.setInt(2, index);
                        stm.setInt(3, number);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        productList.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of product", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return productList;

        }

        @Override
        public Integer getCountOfPublicProductByNameSearch(String name) throws DAOException

        {

                Integer number = null;

                if (name == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
                }

                CON = C3p0Util.getConnection();

                //try-with-resource, libera risorse in ogni caso
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product WHERE name LIKE ? AND privateListId IS NULL"))
                {
                        stmt.setString(1, "%" + name + "%");
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                number = counter.getInt(1);
                        }

                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count product of search", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return number;

        }

        
        
        
        
        
        
        @Override
        public List<Product> getProductsInListByListId(Integer listId) throws DAOException
        {
                List<Product> products = new ArrayList<>();

                if (listId == null)
                {
                        throw new DAOException("listId is null");
                }

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                            + " WHERE  ProductInList.listId = ?"))
                {
                        stm.setInt(1, listId);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        products.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of productInList", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return products;
        }

        @Override
        public List<Product> getProductsNotBuyInListByListId(Integer listId) throws DAOException
        {

                List<Product> products = new ArrayList<>();

                if (listId == null)
                {
                        throw new DAOException("listId is null");
                }

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                            + " WHERE  ProductInList.listId = ? AND ProductInList.status = \"0\""))
                {
                        stm.setInt(1, listId);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        products.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of productInList", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return products;

        }

        @Override
        public List<Product> getProductsBoughtInListByListId(Integer listId) throws DAOException
        {

                List<Product> products = new ArrayList<>();

                if (listId == null)
                {
                        throw new DAOException("listId is null");
                }

                CON = C3p0Util.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                            + " WHERE  ProductInList.listId = ? AND ProductInList.status = \"1\""))
                {
                        stm.setInt(1, listId);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        products.add(getProductFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of productInList", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return products;

        }
}
