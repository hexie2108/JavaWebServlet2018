package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.ShopDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link ShopDAO} interface.
 */
public class JDBCShopDAO extends JDBCDAO<Shop, Integer> implements ShopDAO
{

        public JDBCShopDAO(ConnectionPool cp)
        {
                super(cp);
        }

        private Shop getShopFromResultSet(ResultSet rs) throws SQLException
        {
                Shop shop = new Shop();

                shop.setId(rs.getInt("id"));
                shop.setName(rs.getString("name"));
                shop.setCategory(rs.getInt("category"));
                shop.setLat(rs.getDouble("lat"));
                shop.setLng(rs.getDouble("lng"));

                return shop;
        }

        @Override
        public Long getCount() throws DAOException
        {
                Connection CON = CP.getConnection();

                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Shop"))
                {
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                return counter.getLong(1);
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count Shop", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return 0L;
        }

        public Integer insert(Shop shop) throws DAOException
        {
                if (shop == null)
                {
                        throw new DAOException("shop bean is null");
                }

                Connection CON = CP.getConnection();

                try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Shop (name, lat, lng, category) VALUES (?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS))
                {

                        stm.setString(1, shop.getName());
                        stm.setDouble(2, shop.getLat());
                        stm.setDouble(3, shop.getLng());
                        stm.setInt(4, shop.getCategory());

                        stm.executeUpdate();

                        ResultSet rs = stm.getGeneratedKeys();
                        if (rs.next())
                        {
                                shop.setId(rs.getInt(1));
                        }

                        return shop.getId();
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to insert the new shop", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }
        }

        @Override
        public Shop getByPrimaryKey(Integer primaryKey) throws DAOException
        {
                if (primaryKey == null)
                {
                        throw new DAOException("primaryKey is null");
                }

                Connection CON = CP.getConnection();
                Shop shop = null;

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Shop WHERE id = ?"))
                {
                        stm.setInt(1, primaryKey);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        shop = getShopFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the shop for the passed primary key", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return shop;
        }

        @Override
        public List<Shop> getAll() throws DAOException
        {
                Connection CON = CP.getConnection();
                List<Shop> shopList = new ArrayList<>();

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Shop"))
                {
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        shopList.add(getShopFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of shop", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return shopList;
        }

        @Override
        public Shop update(Shop shop) throws DAOException
        {
                if (shop == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed shop is null"));
                }

                Connection CON = CP.getConnection();

                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE Shop SET "
                            + "name = ?,"
                            + "lat = ?,"
                            + "lng = ?,"
                            + "category = ? "
                            + "WHERE id = ?"
                ))
                {

                        stm.setString(1, shop.getName());
                        stm.setDouble(2, shop.getLat());
                        stm.setDouble(3, shop.getLng());
                        stm.setInt(4, shop.getCategory());
                        stm.setInt(5, shop.getId());
                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update the shop");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the shop", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return shop;
        }

        @Override
        public List<Shop> getShopsByCategory(String category) throws DAOException
        {
                if (category == null)
                {
                        throw new DAOException("category is null");
                }

                Connection CON = CP.getConnection();
                List<Shop> shopList = new ArrayList<>();

                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Shop WHERE Shop.category = ?"))
                {
                        stm.setString(1, category);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        shopList.add(getShopFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of shop of specified category", ex);
                }
                finally
                {
                        ConnectionPool.close(CON);
                }

                return shopList;
        }

        public List<Shop> getRelevantShopsInProximity(Integer userId, Double usrLng, Double usrLat) throws DAOException
        {
                if (userId == null || usrLat == null || usrLng == null)
                {
                        throw new DAOException("One or more parameters (userId, usrLat, usrLng) are null");
                }

        Connection CON = CP.getConnection();
        List<Shop> relevantShopsList = new ArrayList<>();
        //Va capito come tradurre la distanza da gradi a metri, e settare una distanza ragionevole
        double distance = 50;

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Shop " +
                                                          "JOIN  ((SELECT List.categoryList FROM   List " +
                                                                  "WHERE  ownerId = ?)" +
                                                                "UNION" +
                                                                 "(SELECT categoryList FROM   Permission " +
                                                                  "JOIN   List  ON Permission.listId = List.id " +
                                                                  "WHERE  userId = ?)) " +
                                                          "AS C " +
                                                          "ON Shop.category = C.categoryList " +
                                                          "WHERE " +
                                                          "ABS(ST_Distance_Sphere(POINT(?,?), POINT(Shop.lng, Shop.lat))) < ?")) {
            stm.setInt(1, userId);
            stm.setInt(2, userId);
            stm.setDouble(3, usrLng);
            stm.setDouble(4, usrLat);
            stm.setDouble(5, distance);

            //Get all shops in proximity
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    relevantShopsList.add(getShopFromResultSet(rs));
                }
            }

        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of relevant shops in proximity", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return relevantShopsList;
    }
}
