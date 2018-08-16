package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.ConnectionPool;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link CategoryListDAO} interface.
 */
public class JDBCCategoryListDAO extends JDBCDAO<CategoryList, Integer> implements CategoryListDAO {
    public JDBCCategoryListDAO(ConnectionPool cp) {
        super(cp);
    }

    private CategoryList getCategoryListFromResultSet(ResultSet rs) throws SQLException {
        CategoryList categoryList = new CategoryList();

        categoryList.setId(rs.getInt("id"));
        categoryList.setName(rs.getString("name"));
        categoryList.setDescription(rs.getString("description"));
        categoryList.setImg1(rs.getString("img1"));
        categoryList.setImg2(rs.getString("img2"));
        categoryList.setImg3(rs.getString("img3"));

        return categoryList;
    }

    @Override
    public Integer insert(CategoryList categoryList) throws DAOException {
        if (categoryList == null) {
            throw new DAOException("categoryList bean is null");
        }

        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO CategoryList (name, description, img1, img2, img3) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, categoryList.getName());
            stm.setString(2, categoryList.getDescription());
            stm.setString(3, categoryList.getImg1());
            stm.setString(4, categoryList.getImg2());
            stm.setString(5, categoryList.getImg3());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                categoryList.setId(rs.getInt(1));
            }

            return categoryList.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new categoryList", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }

    @Override
    public Long getCount() throws DAOException {
        Connection CON = CP.getConnection();

        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM CategoryList")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count CategoryList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return 0L;
    }

    @Override
    public CategoryList getByPrimaryKey(Integer primaryKey) throws DAOException {
        CategoryList categoryList = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryList WHERE ID = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    categoryList = getCategoryListFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the categoryList for the passed primary key", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryList;
    }

    @Override
    public List<CategoryList> getAll() throws DAOException {
        List<CategoryList> categoryLists = new ArrayList<>();

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM CategoryList")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    categoryLists.add(getCategoryListFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of categoryList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryLists;
    }

    @Override
    public CategoryList update(CategoryList categoryList) throws DAOException {
        if (categoryList == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed categoryList is null"));
        }

        Connection CON = CP.getConnection();
        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE CategoryList SET "
                        + "name = ?,"
                        + "description = ?,"
                        + "img1 = ?, "
                        + "img2 = ?, "
                        + "img3 = ? "
                        + "WHERE id = ?"
        )) {

            stm.setString(1, categoryList.getName());
            stm.setString(2, categoryList.getDescription());
            stm.setString(3, categoryList.getImg1());
            stm.setString(4, categoryList.getImg2());
            stm.setString(5, categoryList.getImg3());
            stm.setInt(6, categoryList.getId());

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the categoryList");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the categoryList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryList;
    }

    @Override
    public List<CategoryList> filter(Integer id, String name, String description) throws DAOException {
        Connection CON = CP.getConnection();

        List<CategoryList> categoryLists = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT * FROM CategoryList WHERE " +
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
                    categoryLists.add(getCategoryListFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of categoryList", ex);
        } finally {
            ConnectionPool.close(CON);
        }

        return categoryLists;
    }

    @Override
    public void delete(Integer id) throws DAOException {
        Connection CON = CP.getConnection();

        try (PreparedStatement stm = CON.prepareStatement(
                "DELETE FROM CategoryList WHERE id = ?"
        )) {
            stm.setInt(1, id);

            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to delete the categoryList");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to delete the categoryList", ex);
        } finally {
            ConnectionPool.close(CON);
        }
    }
}
