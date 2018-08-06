package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.C3p0Util;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link ListDAO} interface.
 */
public class JDBCListDAO extends JDBCDAO<ShoppingList, Integer> implements ListDAO
{

        private ShoppingList getListFromResultSet(ResultSet rs) throws SQLException
        {
                ShoppingList list = new ShoppingList();

                list.setId(rs.getInt("id"));
                list.setName(rs.getString("name"));
                list.setDescription(rs.getString("description"));
                list.setImg(rs.getString("img"));
                list.setOwnerId(rs.getInt("ownerId"));
                list.setCategoryList(rs.getInt("categoryList"));

                return list;
        }

        @Override
        public Long getCount() throws DAOException
        {

                CON = C3p0Util.getConnection();
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM List"))
                {
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                return counter.getLong(1);
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count list", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return 0L;
        }

        @Override
        public Integer insert(ShoppingList list) throws DAOException
        {
                if (list == null)
                {
                        throw new DAOException("list bean is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("INSERT INTO List (name, description, img, ownerId, categoryList) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS))
                {

                        stm.setString(1, list.getName());
                        stm.setString(2, list.getDescription());
                        stm.setString(3, list.getImg());
                        stm.setInt(4, list.getOwnerId());
                        stm.setInt(5, list.getCategoryList());

                        stm.executeUpdate();

                        ResultSet rs = stm.getGeneratedKeys();
                        if (rs.next())
                        {
                                list.setId(rs.getInt(1));
                        }

                        return list.getId();
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to insert the new list", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }
        }

        @Override
        public ShoppingList update(ShoppingList list) throws DAOException
        {
                if (list == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed list is null"));
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            " UPDATE List SET "
                            + " name = ?, "
                            + " description = ?, "
                            + " img = ?, "
                            + " ownerId = ?, "
                            + " categoryList = ? "
                            + " WHERE id = ? "
                ))
                {

                        stm.setString(1, list.getName());
                        stm.setString(2, list.getDescription());
                        stm.setString(3, list.getImg());
                        stm.setInt(4, list.getOwnerId());
                        stm.setInt(5, list.getCategoryList());
                        stm.setInt(6, list.getId());
                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update the list");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the list", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return list;
        }

        @Override
        public ShoppingList getByPrimaryKey(Integer primaryKey) throws DAOException
        {
                ShoppingList list = null;
                if (primaryKey == null)
                {
                        throw new DAOException("primaryKey is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM List WHERE id = ?"))
                {
                        stm.setInt(1, primaryKey);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        list = getListFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list for the passed primary key", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return list;
        }

        @Override
        public List<ShoppingList> getAll() throws DAOException
        {
                List<ShoppingList> lists = new ArrayList<>();

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM List"))
                {
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        lists.add(getListFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of List", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return lists;
        }

        @Override
        public List<ShoppingList> getOwnedUserListsByUserId(Integer userId) throws DAOException
        {

                List<ShoppingList> lists = new ArrayList<>();

                if (userId == null)
                {
                        throw new DAOException("userId is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM List WHERE List.ownerId = ?"))
                {
                        stm.setInt(1, userId);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        lists.add(getListFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the owner list of user's List", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return lists;
        }

        @Override
        public List<ShoppingList> getSharedWithUserListsByUserId(Integer userId) throws DAOException
        {
                List<ShoppingList> lists = new ArrayList<>();
                if (userId == null)
                {
                        throw new DAOException("userId is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT List.* FROM Permission JOIN List ON Permission.listId = List.id WHERE Permission.userId = ? "
                            + " AND List.ownerId <> ? "))
                {
                        stm.setInt(1, userId);
                        stm.setInt(2, userId);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        lists.add(getListFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the shared list of user's List", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return lists;
        }

        @Override
        public List<ShoppingList> getAllListByUserId(Integer userId) throws DAOException
        {
                List<ShoppingList> lists = new ArrayList<>();
                if (userId == null)
                {
                        throw new DAOException("userId is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT List.* FROM Permission JOIN List ON Permission.listId = List.id WHERE Permission.userId = ?"))
                {
                        stm.setInt(1, userId);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        lists.add(getListFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of user's List", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return lists;
        }

        @Override
        public List<ShoppingList> getAllAddableListByUserId(Integer userId) throws DAOException
        {
                List<ShoppingList> lists = new ArrayList<>();
                if (userId == null)
                {
                        throw new DAOException("userId is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT List.* FROM Permission JOIN List ON Permission.listId = List.id WHERE Permission.userId = ? AND Permission.addObject = 1"))
                {
                        stm.setInt(1, userId);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        lists.add(getListFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of user's List", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return lists;

        }

        @Override
        public Integer getNumberOfProductsInListByListId(Integer listId) throws DAOException
        {
                Integer res = null;
                if (listId == null)
                {
                        throw new DAOException("listId is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT COUNT(*) FROM List JOIN ProductInList "
                            + " ON List.id = ProductInList.listId "
                            + " WHERE List.id = ?"))
                {
                        stm.setInt(1, listId);
                        ResultSet counter = stm.executeQuery();
                        if (counter.next())
                        {
                                res = counter.getInt(1);
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count list", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return res;
        }

        public void deleteListByListId(Integer listId) throws DAOException
        {

                if (listId == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed listId is null"));
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            " DELETE FROM List WHERE "
                            + " id = ? "
                ))
                {
                        stm.setInt(1, listId);
                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to delete the list");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the list", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }
        }

}
