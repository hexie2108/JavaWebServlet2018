package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * The JDBC implementation of the {@link ListDAO} interface.
 */
public class JDBCListDAO extends JDBCDAO<List, Integer> implements ListDAO {
    public JDBCListDAO(Connection con) {
        super(con);
    }

	private List getListFromResultSet(ResultSet rs) throws SQLException{
		List list = new List();
		
		list.setId(rs.getInt("id"));
		list.setName(rs.getString("name"));
		list.setOwnerId(rs.getInt("OwnerId"));
		list.setCategoryList(rs.getInt("CategoryList"));

		return list;
	}

	@Override
	public Long getCount() throws DAOException {
		try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM List")) {
			ResultSet counter = stmt.executeQuery();
			if (counter.next()) {
				return counter.getLong(1);
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to count list", ex);
		}

		return 0L;
	}

	@Override
	public List getByPrimaryKey(Integer primaryKey) throws DAOException {
		List list = null;
		if (primaryKey == null) {
			throw new DAOException("primaryKey is null");
		}
		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM List WHERE id = ?")) {
			stm.setInt(1, primaryKey);
			try (ResultSet rs = stm.executeQuery()) {
				if(rs.next()) {
					list = getListFromResultSet(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the list for the passed primary key", ex);
		}

		return list;
	}

	@Override
	public java.util.List<List> getAll() throws DAOException {
		java.util.List<List> lists = new ArrayList<>();

		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM List")) {
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					lists.add(getListFromResultSet(rs));
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the list of List", ex);
		}

		return lists;
	}

	@Override
	public List update(List list) throws DAOException {
		if (list == null) {
			throw new DAOException("parameter not valid", new IllegalArgumentException("The passed list is null"));
		}

		try(PreparedStatement stm = CON.prepareStatement(
				"UPDATE List SET " +
						"name = ?," +
						"OwnerId = ?," +
						"CategoryList = ?" +
						"WHERE id = ?"
		)) {

		    stm.setString(1,list.getName());
		    stm.setInt(2,list.getOwnerId());
		    stm.setInt(3,list.getCategoryList());
			stm.setInt(4, list.getId());
			if (stm.executeUpdate() != 1) {
				throw new DAOException("Impossible to update the list");
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to update the list", ex);
		}

		return list;
	}
}
