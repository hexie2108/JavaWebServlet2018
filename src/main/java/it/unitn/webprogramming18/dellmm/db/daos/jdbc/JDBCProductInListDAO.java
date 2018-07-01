package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;

import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link ProductInList} interface.
 */
public class JDBCProductInListDAO extends JDBCDAO<ProductInList, Integer> implements ProductInListDAO{
    public JDBCProductInListDAO(Connection con) {
        super(con);
    }

	private ProductInList getProductInListFromResultSet(ResultSet rs) throws SQLException{
		ProductInList productInList = new ProductInList();

		productInList.setId(rs.getInt("id"));
		productInList.setProductId(rs.getInt("productId"));
		productInList.setListId(rs.getInt("listId"));

		return productInList;
	}

	@Override
	public Long getCount() throws DAOException {
		try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM ProductInList")) {
			ResultSet counter = stmt.executeQuery();
			if (counter.next()) {
				return counter.getLong(1);
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to count productInList", ex);
		}

		return 0L;
	}

	@Override
	public ProductInList getByPrimaryKey(Integer primaryKey) throws DAOException {
		ProductInList productInList = null;
		if (primaryKey == null) {
			throw new DAOException("primaryKey is null");
		}
		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM ProductInList WHERE id = ?")) {
			stm.setInt(1, primaryKey);
			try (ResultSet rs = stm.executeQuery()) {
				if(rs.next()) {
					productInList = getProductInListFromResultSet(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the productInList for the passed primary key", ex);
		}

		return productInList;
	}

	@Override
	public List<ProductInList> getAll() throws DAOException {
		List<ProductInList> productInLists = new ArrayList<>();

		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM ProductInList")) {
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					productInLists.add(getProductInListFromResultSet(rs));
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the list of productInList", ex);
		}

		return productInLists;
	}

	@Override
	public ProductInList update(ProductInList productInList) throws DAOException {
		if (productInList == null) {
			throw new DAOException("parameter not valid", new IllegalArgumentException("The passed productInList is null"));
		}

		try(PreparedStatement stm = CON.prepareStatement(
				"UPDATE ProductInList SET " +
						"productId = ?," +
						"listId = ? " +
						"WHERE id = ?"
		)) {

			stm.setInt(1, productInList.getProductId());
			stm.setInt(2, productInList.getListId());
			stm.setInt(3, productInList.getId());
			if (stm.executeUpdate() != 1) {
				throw new DAOException("Impossible to update the productInList");
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to update the productInList", ex);
		}

		return productInList;
	}
}
