package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.util.JDBCUtils;

import java.sql.*;
import java.util.*;

/**
 * The JDBC implementation of the {@link ProductDAO} interface.
 */
public class JDBCProductDAO extends JDBCDAO<Product, Integer> implements ProductDAO {
    public JDBCProductDAO(ConnectionPool cp) {
        super(cp);
    }

    private final static double MIN_RELEVANCE = 0.4;

    private static final String[] VALID_ORDER= {"categoryName", "productName", "relevance"};
    private static final String[] VALID_DIRECTION = {"asc", "desc"};

    private Product getProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();

        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setImg(rs.getString("img"));
        product.setLogo(rs.getString("logo"));
        product.setCategoryProductId(rs.getInt("categoryProductId"));

        Integer privateListId = rs.getInt("privateListId");
        if(rs.wasNull()) {
            privateListId = null;
        }

        product.setPrivateListId(privateListId);

        return product;
    }

    @Override
    public List<Product> getProductsInListByListId(Integer listId) throws DAOException {
        List<Product> products = new ArrayList<>();

        if (listId == null) {
            throw new DAOException("listId is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                + " WHERE  ProductInList.listId = ?")) {
            stm.setInt(1, listId);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    products.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return products;
    }

    @Override
    public Long getCount() throws DAOException {

        Connection CON = CP.getConnection();

        //try-with-resource, libera risorse in ogni caso
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public Integer insert(Product product) throws DAOException {

        if (product == null) {
            throw new DAOException("product bean is null");
        }

        Connection CON = CP.getConnection();

        //try-with-resource, libera risorse in ogni caso
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Product (name, description, img, logo, categoryProductId, privateListId) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, product.getName());
            stm.setString(2, product.getDescription());
            stm.setString(3, product.getImg());
            stm.setString(4, product.getLogo());
            stm.setInt(5, product.getCategoryProductId());

            if (product.getPrivateListId() == null) {
                stm.setNull(6, Types.INTEGER);
            } else {
                stm.setInt(6, product.getPrivateListId());
            }

            stm.executeUpdate();

            //get id dell'elemnto appena aggiunto
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                //se non è null, set id.
                product.setId(rs.getInt(1));
            }

            return product.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

    }

    @Override
    public Product update(Product product) throws DAOException {
        if (product == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed product is null"));
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement(
                " UPDATE Product SET "
                        + " name =?, "
                        + " description =?, "
                        + " img =?, "
                        + " logo =?, "
                        + " categoryProductId =?, "
                        + " privateListId =? "
                        + " WHERE id = ? "
        )) {
            stm.setString(1, product.getName());
            stm.setString(2, product.getDescription());
            stm.setString(3, product.getImg());
            stm.setString(4, product.getLogo());
            stm.setInt(5, product.getCategoryProductId());

            if (product.getPrivateListId() == null) {
                stm.setNull(6, Types.INTEGER);
            } else {
                stm.setInt(6, product.getPrivateListId());
            }

            stm.setInt(7, product.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the product");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return product;
    }

    @Override
    public Product getByPrimaryKey(Integer primaryKey) throws DAOException {
        Product product = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    product = getProductFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the product for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return product;
    }

    @Override
    public List<Product> getAll() throws DAOException {
        List<Product> productList = new ArrayList<>();

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productList.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productList;
    }

    @Override
    public List<Product> getPublicProductList(Integer index, Integer number) throws DAOException {
        if (index == null || index < 0 || number == null || number < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
        }

        List<Product> productList = new ArrayList<>();

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE privateListId IS NULL ORDER BY id DESC LIMIT ?,?")) {
            stm.setInt(1, index);
            stm.setInt(2, number);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productList.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productList;

    }

    @Override
    public Integer getCountOfPublicProduct() throws DAOException {

        Integer number = null;

        Connection CON = CP.getConnection();

        //try-with-resource, libera risorse in ogni caso
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product WHERE privateListId IS NULL")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                number = counter.getInt(1);
            }

        } catch (SQLException ex) {
            throw new DAOException("Impossible to count product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return number;

    }

    @Override
    public List<Product> getPublicProductListByCatId(Integer catId, Integer index, Integer number) throws DAOException {
        if (catId == null || index == null || index < 0 || number == null || number < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
        }

        List<Product> productList = new ArrayList<>();

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Product WHERE categoryProductId = ? AND privateListId IS NULL ORDER BY id DESC LIMIT ?,?")) {
            stm.setInt(1, catId);
            stm.setInt(2, index);
            stm.setInt(3, number);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productList.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productList;

    }

    @Override
    public Integer getCountOfPublicProductByCatId(Integer catId) throws DAOException {

        Integer number = null;

        if (catId == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed parameters is not valid"));
        }

        Connection CON = CP.getConnection();

        //try-with-resource, libera risorse in ogni caso
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Product WHERE categoryProductId = ? AND privateListId IS NULL")) {
            stmt.setInt(1, catId);
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                number = counter.getInt(1);
            }

        } catch (SQLException ex) {
            throw new DAOException("Impossible to count product of category", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return number;

    }


    public List<Product> getPublicProductListByNameSearch(String name, String order, Integer index, Integer number) throws DAOException {
        return search(name, order, "asc", new ArrayList<>(),index, number);
    }

    public List<AbstractMap.SimpleEntry<Product, String>> filter(
            Integer id,
            String name,
            String description,
            List<Integer> categories,
            Boolean publicOnly,
            Integer privateListId,
            OrderableColumns order,
            Boolean direction,
            Integer start,
            Integer length
    ) throws DAOException{
        if (categories == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The categories parameter is null"));
        }

        if (categories.stream().anyMatch(Objects::isNull)) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The catId parameters is not valid(must be a list of numbers)"));
        }

        if (publicOnly == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The publicOnly parameter is null"));
        }

        if (order == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The order parameter is null"));
        }

        if (direction == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The direction parameter is null"));
        }

        if (start == null || start < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The start parameter is not valid(must be present and >= 0)"));
        }

        if (length == null || length < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The length parameter is not valid(must be present and >= 0)"));
        }

        StringBuilder sbSql = new StringBuilder( 1024 );
        sbSql.append(" ( ");

        for( int i=0; i < categories.size(); i++ ) {
            if( i > 0 ) sbSql.append( "," );
            sbSql.append( " ?" );
        } // for
        sbSql.append( " ) " );

        List<AbstractMap.SimpleEntry<Product, String>> productList = new ArrayList<>();

        Connection CON = CP.getConnection();

        String sqlDirection = direction?" ASC ": " DESC ";
        String orderBy = null;
        switch (order) {
            case ID: orderBy = "Product.id"; break;
            case NAME: orderBy = "Product.name"; break;
            case DESCRIPTION: orderBy = "Product.description"; break;
            case PRIVATE_LIST_ID: orderBy = "Product.privateListId"; break;
            case CATEGORY_NAME: orderBy = "CategoryProduct.name"; break;
        }

        String sql = "SELECT Product.*, CategoryProduct.name as categoryName " +
                " FROM Product JOIN CategoryProduct " +
                " ON Product.categoryProductId = CategoryProduct.id " +
                " WHERE " +
                    "(? IS NULL OR Product.id LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                    "(? IS NULL OR Product.name LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                    "(? IS NULL OR Product.description LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                    (publicOnly?" (Product.privateListId IS NULL) ":"(? IS NULL OR Product.privateListId LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) ") +
                (!categories.isEmpty()?" AND (Product.categoryProductId IN " + sbSql.toString() + ") ":" ") +
                " ORDER BY " + orderBy + " " + sqlDirection + " " +
                " LIMIT ?,? ";

        try (PreparedStatement stm = CON.prepareStatement(sql)) {
            JDBCUtils.setNullOrInt(stm, 1, id);
            JDBCUtils.setNullOrInt(stm, 2, id);

            stm.setString(3, name);
            stm.setString(4, name);

            stm.setString(5, description);
            stm.setString(6, description);

            int i = 7;

            if(!publicOnly) {
                JDBCUtils.setNullOrInt(stm, i, privateListId);
                i++;
                JDBCUtils.setNullOrInt(stm, i, privateListId);
                i++;
            }


            i = JDBCUtils.setIntArray(stm, i, categories);

            stm.setInt(i, start);
            stm.setInt(i+1, length);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productList.add(new AbstractMap.SimpleEntry<>(getProductFromResultSet(rs), rs.getString("categoryName")) );
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productList;
    }

    public Long getCountFilter(Integer id, String name, String description, List<Integer> categories, Boolean publicOnly, Integer privateListId) throws DAOException{
        if (categories == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The categories parameter is null"));
        }

        if (categories.stream().anyMatch(Objects::isNull)) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The catId parameters is not valid(must be a list of numbers)"));
        }

        if (publicOnly == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The publicOnly parameter is null"));
        }

        StringBuilder sbSql = new StringBuilder( 1024 );
        sbSql.append(" ( ");

        for( int i=0; i < categories.size(); i++ ) {
            if( i > 0 ) sbSql.append( "," );
            sbSql.append( " ?" );
        } // for
        sbSql.append( " ) " );

        Connection CON = CP.getConnection();

        String sql = "SELECT COUNT(*) " +
                " FROM Product JOIN CategoryProduct " +
                " ON Product.categoryProductId = CategoryProduct.id " +
                " WHERE " +
                "(? IS NULL OR Product.id LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                "(? IS NULL OR Product.name LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                "(? IS NULL OR Product.description LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) AND " +
                (publicOnly?" (Product.privateListId IS NULL) ":"(? IS NULL OR Product.privateListId LIKE CONCAT('%',TRIM(BOTH \"'\" FROM QUOTE(?)),'%')) ") +
                (!categories.isEmpty()?" AND Product.categoryProductId IN " + sbSql.toString():" ");

        try (PreparedStatement stm = CON.prepareStatement(sql)) {
            JDBCUtils.setNullOrInt(stm, 1, id);
            JDBCUtils.setNullOrInt(stm, 2, id);

            stm.setString(3, name);
            stm.setString(4, name);

            stm.setString(5, description);
            stm.setString(6, description);

            int i = 7;

            if(!publicOnly) {
                JDBCUtils.setNullOrInt(stm, i, privateListId);
                i++;
                JDBCUtils.setNullOrInt(stm, i, privateListId);
                i++;
            }

            JDBCUtils.setIntArray(stm, i, categories);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the number of products filtered", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }


    public List<Product> search(
            String toSearch,
            String order,
            String direction,
            List<Integer> categories,
            Integer index,
            Integer number
    ) throws DAOException {
        if (toSearch == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The toSearch parameter is null"));
        }

        if (order == null || Arrays.stream(VALID_ORDER).noneMatch(order::equals)) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The order parameter is not one of " + Arrays.toString(VALID_ORDER)));
        }

        if (direction == null || Arrays.stream(VALID_DIRECTION).noneMatch(direction::equals)) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The direction parameter is not one of " + Arrays.toString(VALID_DIRECTION)));
        }

        if (index == null || index < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The index parameter is not valid(must be present and >= 0)"));
        }

        if (number == null || number < 0) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The number parameter is not valid(must be present and >= 0)"));
        }

        if (categories.stream().anyMatch(Objects::isNull)) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The catId parameters is not valid(must be a list of numbers)"));
        }

        StringBuilder sbSql = new StringBuilder( 1024 );
        sbSql.append(" ( ");

        for( int i=0; i < categories.size(); i++ ) {
            if( i > 0 ) sbSql.append( "," );
            sbSql.append( " ?" );
        } // for
        sbSql.append( " ) " );

        List<Product> productList = new ArrayList<>();

        Connection CON = CP.getConnection();

        String sqlDirection = direction.equalsIgnoreCase("asc")?"ASC": "DESC";

        String sql;
        if (order.equalsIgnoreCase("categoryName")) {
            sql =
                    "SELECT Product.* " +
                            "FROM Product JOIN CategoryProduct " +
                            "ON Product.categoryProductId = CategoryProduct.id " +
                            "WHERE Product.privateListId IS NULL AND MATCH(Product.name, Product.description) AGAINST (? IN NATURAL  LANGUAGE  MODE) >= " + MIN_RELEVANCE + " " +
                                (!categories.isEmpty()?" AND Product.categoryProductId IN " + sbSql.toString():" ") +
                            " ORDER BY CategoryProduct.name " + sqlDirection + " " +
                            "LIMIT ?,? ";
        } else if (order.equalsIgnoreCase("productName")) {
            sql =
                    "SELECT * " +
                            "FROM Product " +
                            "WHERE privateListId IS NULL " +
                                "AND MATCH(name, description) AGAINST (? IN NATURAL  LANGUAGE  MODE) >= " + MIN_RELEVANCE + " " +
                                (!categories.isEmpty()?" AND categoryProductId IN " + sbSql.toString(): " ") +
                            " ORDER BY name " + sqlDirection + " " +
                            " LIMIT ?,?";
        } else {
            sql =
                    "SELECT P.* " +
                            "FROM " +
                            "(SELECT *, MATCH(Product.name, Product.description) AGAINST (? IN NATURAL  LANGUAGE  MODE) as rev " +
                            "FROM Product " +
                            "WHERE privateListId IS NULL " +
                                (!categories.isEmpty()?" AND categoryProductId IN "+ sbSql.toString(): " ") +
                            " ) AS P " +
                            "WHERE rev >= " + MIN_RELEVANCE + " " +
                            " ORDER BY rev " + sqlDirection + " " +
                            " LIMIT ?,?";
        }

        try (PreparedStatement stm = CON.prepareStatement(sql)) {
            stm.setString(1, toSearch);
            int i = 2;

            for(Integer cat : categories) {
                stm.setInt(i, cat);
                i++;
            }

            stm.setInt(i, index);
            stm.setInt(i+1, number);


            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    productList.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return productList;
    }


    @Override
    public Long getCountSearch(String toSearch, List<Integer> categories) throws DAOException {

        if (toSearch == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The toSearch parameter is null"));
        }

        if (categories.stream().anyMatch(Objects::isNull)) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The catId parameters is not valid(must be a list of numbers)"));
        }

        StringBuilder sbSql = new StringBuilder( 1024 );
        sbSql.append(" ( ");

        for( int i=0; i < categories.size(); i++ ) {
            if( i > 0 ) sbSql.append( "," );
            sbSql.append( " ?" );
        } // for
        sbSql.append( " ) " );

        Connection CON = CP.getConnection();

        String sql =
            "SELECT COUNT(*) " +
                    "FROM Product " +
                    "WHERE privateListId IS NULL " +
                    "AND MATCH(name, description) AGAINST (? IN NATURAL  LANGUAGE  MODE) >= " + MIN_RELEVANCE + " " +
                    (!categories.isEmpty()?" AND categoryProductId IN " + sbSql.toString(): " ");

        try (PreparedStatement stm = CON.prepareStatement(sql)) {
            stm.setString(1, toSearch);
            int i = 2;

            for(Integer cat : categories) {
                stm.setInt(i, cat);
                i++;
            }

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public List<Product> getProductsNotBuyInListByListId(Integer listId) throws DAOException {
        List<Product> products = new ArrayList<>();

        if (listId == null) {
            throw new DAOException("listId is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                + " WHERE  ProductInList.listId = ? AND ProductInList.status = \"0\"")) {
            stm.setInt(1, listId);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    products.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return products;
    }

    @Override
    public List<Product> getProductsBoughtInListByListId(Integer listId) throws DAOException {

        List<Product> products = new ArrayList<>();

        if (listId == null) {
            throw new DAOException("listId is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                + " WHERE  ProductInList.listId = ? AND ProductInList.status = \"1\"")) {
            stm.setInt(1, listId);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    products.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return products;

    }

    @Override
    public List<Product> getPrivateProductByListId(Integer listId) throws DAOException {

        List<Product> products = new ArrayList<>();

        if (listId == null) {
            throw new DAOException("listId is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("SELECT Product.* FROM ProductInList JOIN Product ON ProductInList.productId = Product.id "
                + " WHERE  ProductInList.listId = ? AND Product.privateListId IS NOT NULL")) {
            stm.setInt(1, listId);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    products.add(getProductFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of productInList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return products;

    }

    @Override
    public void deleteProductById(Integer productId) throws DAOException {

        if (productId == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed productId is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                " DELETE FROM Product WHERE "
                        + " id = ? "
        )) {
            stm.setInt(1, productId);
            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to delete the product");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the product", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }


    @Override
    public List<Product> getListProductFromLogNotEmailYetByUserId(Integer userId, Timestamp currentTime, Integer predictionDay) throws DAOException {
        List<Product> products = new ArrayList<>();

        if (userId == null || currentTime == null || predictionDay == null)
        {
            throw new DAOException("userId or currentTime or  predictionDay is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT Product.* FROM Log JOIN Product ON Log.productId = Product.id "
                + " WHERE " +
                        "Log.emailStatus = 0 AND " +
                        "Log.userId = ? AND (Log.last2 IS NOT NULL)  AND " +
                        "( (TIME_TO_SEC( TIMEDIFF( last1, last2 )) - TIME_TO_SEC( TIMEDIFF( ?, last1 ))) BETWEEN 0 AND 60*60*24*? )"))

        {
            stm.setInt(1, userId);
            stm.setTimestamp(2, currentTime);
            stm.setInt(3, predictionDay);

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
        }
        finally
        {
            ConnectionPool.close(CON);
        }

        return products;

    }

    public HashMap<String, Double> getNameTokensFiltered(String query, Integer listId, List<Integer> categories) throws DAOException {
        HashMap<String, Double> r = new HashMap<>();

        if (query == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The query parameter is null"));
        }

        StringBuilder sbSql = new StringBuilder( 1024 );
        sbSql.append(" ( ");

        for( int i=0; i < categories.size(); i++ ) {
            if( i > 0 ) sbSql.append( "," );
            sbSql.append( " ?" );
        } // for
        sbSql.append( " ) " );


        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * " +
                "FROM (" +
                    "SELECT Product.name, MATCH(Product.name, Product.description) AGAINST (? IN NATURAL LANGUAGE MODE) as rev " +
                    "FROM Product " +
                    "WHERE (Product.privateListId IS NULL OR Product.privateListId=?) " +
                    (!categories.isEmpty()?" AND Product.categoryProductId IN " + sbSql.toString():" ") +
                    " ) AS P " +
                "WHERE P.rev >= " + MIN_RELEVANCE
        )) {
            stm.setString(1, query);

            if (listId == null) {
                stm.setNull(2, Types.INTEGER);
            } else {
                stm.setInt(2, listId);
            }

            int i = 3;

            for(Integer cat : categories) {
                stm.setInt(i, cat);
                i++;
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(1);
                    Double rev = rs.getDouble(2);

                    for(String token: name.split("[\\s\\p{Punct}]")) {
                        if(r.containsKey(token)) {
                            r.put(token, r.get(token) + rev);
                        } else {
                            r.put(token, rev);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of name, rev of product", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return r;
    }
}
