package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;

import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link PermissionDAO} interface.
 */
public class JDBCPermissionDAO extends JDBCDAO<Permission, Integer> implements PermissionDAO {
    public JDBCPermissionDAO(Connection con) {
        super(con);
    }

	private Permission getPermissionFromResultSet(ResultSet rs) throws SQLException{
		Permission permission = new Permission() ;

		permission.setId(rs.getInt("id"));
		permission.setListId(rs.getInt("List_id"));
		permission.setUserId(rs.getInt("User_id"));
		permission.setAddObject(rs.getBoolean("addObject"));
		permission.setDeleteList(rs.getBoolean("deleteObject"));
		//TODO: Esiste anche un setModifyList

		return permission;
	}

	@Override
	public Long getCount() throws DAOException {
		try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Permission")) {
			ResultSet counter = stmt.executeQuery();
			if (counter.next()) {
				return counter.getLong(1);
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to count permission", ex);
		}

		return 0L;
	}

	@Override
	public Permission getByPrimaryKey(Integer primaryKey) throws DAOException {
		Permission permission = null;
		if (primaryKey == null) {
			throw new DAOException("primaryKey is null");
		}
		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Permission WHERE id = ?")) {
			stm.setInt(1, primaryKey);
			try (ResultSet rs = stm.executeQuery()) {
				if(rs.next()) {
					permission = getPermissionFromResultSet(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the permission for the passed primary key", ex);
		}

		return permission;
	}

	@Override
	public List<Permission> getAll() throws DAOException {
		List<Permission> permissionList = new ArrayList<>();

		try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Permission")) {
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					permissionList.add(getPermissionFromResultSet(rs));
				}
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to get the list of permission", ex);
		}

		return permissionList;
	}

	@Override
	public Permission update(Permission permission) throws DAOException {
		if (permission == null) {
			throw new DAOException("parameter not valid", new IllegalArgumentException("The passed permission is null"));
		}

		try(PreparedStatement stm = CON.prepareStatement(
				"UPDATE Permission SET " +
						"List_id=?," +
						"User_id=?  "+
						"WHERE id = ?"
		)) {

			// TODO: Mancano get i permission
			stm.setInt(1,permission.getListId());
			stm.setInt(2,permission.getUserId());
			stm.setInt(3,permission.getId());

			if (stm.executeUpdate() != 1) {
				throw new DAOException("Impossible to update the permission");
			}
		} catch (SQLException ex) {
			throw new DAOException("Impossible to update the permission", ex);
		}

		return permission;
	}
}
