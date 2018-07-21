package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.utils.C3p0Util;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The JDBC implementation of the {@link PermissionDAO} interface.
 */
public class JDBCPermissionDAO extends JDBCDAO<Permission, Integer> implements PermissionDAO
{

        private Permission getPermissionFromResultSet(ResultSet rs) throws SQLException
        {
                Permission permission = new Permission();

                permission.setId(rs.getInt("id"));
                permission.setListId(rs.getInt("listId"));
                permission.setUserId(rs.getInt("userId"));
                permission.setAddObject(rs.getBoolean("addObject"));
                permission.setModifyList(rs.getBoolean("modifyList"));
                permission.setDeleteObject(rs.getBoolean("deleteObject"));
                permission.setDeleteList(rs.getBoolean("deleteList"));

                return permission;
        }

        @Override
        public Long getCount() throws DAOException
        {
                CON = C3p0Util.getConnection();
                try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Permission"))
                {
                        ResultSet counter = stmt.executeQuery();
                        if (counter.next())
                        {
                                return counter.getLong(1);
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to count permission", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return 0L;
        }

        public Integer insert(Permission permission) throws DAOException
        {
                if (permission == null)
                {
                        throw new DAOException("permission bean is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Permission (addObject, deleteObject, modifyList, deleteList, listId, userId) VALUES (?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS))
                {

                        stm.setBoolean(1, permission.isAddObject());
                        stm.setBoolean(2, permission.isDeleteList());
                        stm.setBoolean(3, permission.isModifyList());
                        stm.setBoolean(4, permission.isDeleteList());
                        stm.setInt(5, permission.getListId());
                        stm.setInt(6, permission.getUserId());

                        stm.executeUpdate();

                        ResultSet rs = stm.getGeneratedKeys();
                        if (rs.next())
                        {
                                permission.setId(rs.getInt(1));
                        }

                        return permission.getId();
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to insert the new permission", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }
        }

        @Override
        public Permission getByPrimaryKey(Integer primaryKey) throws DAOException
        {
                Permission permission = null;
                if (primaryKey == null)
                {
                        throw new DAOException("primaryKey is null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Permission WHERE id = ?"))
                {
                        stm.setInt(1, primaryKey);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        permission = getPermissionFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the permission for the passed primary key", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return permission;
        }

        @Override
        public List<Permission> getAll() throws DAOException
        {
                List<Permission> permissionList = new ArrayList<>();

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Permission"))
                {
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        permissionList.add(getPermissionFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of permission", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return permissionList;
        }

        @Override
        public Permission update(Permission permission) throws DAOException
        {
                if (permission == null)
                {
                        throw new DAOException("parameter not valid", new IllegalArgumentException("The passed permission is null"));
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement(
                            "UPDATE Permission SET "
                            + "addObject = ?,"
                            + "modifyList = ?,"
                            + "deleteObject = ?,"
                            + "deleteList = ?,"
                            + "listId=?,"
                            + "userId=?  "
                            + "WHERE id = ?"
                ))
                {

                        stm.setBoolean(1, permission.isAddObject());
                        stm.setBoolean(2, permission.isModifyList());
                        stm.setBoolean(3, permission.isDeleteObject());
                        stm.setBoolean(4, permission.isDeleteList());
                        stm.setInt(5, permission.getListId());
                        stm.setInt(6, permission.getUserId());
                        stm.setInt(7, permission.getId());

                        if (stm.executeUpdate() != 1)
                        {
                                throw new DAOException("Impossible to update the permission");
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to update the permission", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return permission;
        }

        @Override
        public List<Permission> getPermissionsOnListByListId(Integer listId) throws DAOException
        {
                List<Permission> permissionList = new ArrayList<>();

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Permission WHERE Permission.listId = ?"))
                {
                        stm.setInt(1, listId);
                        try (ResultSet rs = stm.executeQuery())
                        {
                                while (rs.next())
                                {
                                        permissionList.add(getPermissionFromResultSet(rs));
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the list of permission for specified list", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return permissionList;
        }

        public Permission getUserPermissionOnListByIds(Integer userId, Integer listId) throws DAOException
        {
                Permission permission = null;
                if (userId == null || listId == null)
                {
                        throw new DAOException("One or both parameters (userId, listId) are null");
                }

                CON = C3p0Util.getConnection();
                try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Permission WHERE userId = ? AND listId = ?"))
                {
                        stm.setInt(1, userId);
                        stm.setInt(2, listId);

                        try (ResultSet rs = stm.executeQuery())
                        {
                                if (rs.next())
                                {
                                        permission = getPermissionFromResultSet(rs);
                                }
                        }
                }
                catch (SQLException ex)
                {
                        throw new DAOException("Impossible to get the permission for the passed userId and listId", ex);
                } finally
                {
                        C3p0Util.close(CON);
                }

                return permission;
        }
}
