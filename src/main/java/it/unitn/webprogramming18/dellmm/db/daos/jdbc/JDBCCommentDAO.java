package it.unitn.webprogramming18.dellmm.db.daos.jdbc;

import it.unitn.webprogramming18.dellmm.db.daos.CommentDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.jdbc.JDBCDAO;
import it.unitn.webprogramming18.dellmm.javaBeans.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The JDBC implementation of the {@link CommentDAO} interface.
 */
public class JDBCCommentDAO extends JDBCDAO<Comment, Integer> implements CommentDAO {
    public JDBCCommentDAO(Connection con) {
        super(con);
    }

    private Comment getCommentFromResultSet(ResultSet rs) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getInt("id"));
        comment.setUserId(rs.getInt("userId"));
        comment.setListId(rs.getInt("listId"));
        comment.setText(rs.getString("text"));

        return comment;
    }

    @Override
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM Comment")) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count comment", ex);
        }

        return 0L;
    }

    public Integer insert(Comment comment) throws DAOException {
        if (comment == null) {
            throw new DAOException("comment bean is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO Comment (userId, listId, text) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, comment.getUserId());
            stm.setInt(2, comment.getListId());
            stm.setString(3, comment.getText());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                comment.setId(rs.getInt(1));
            }

            return comment.getId();
        } catch (SQLException ex) {
            throw new DAOException("Impossible to insert the new comment", ex);
        }
    }

    @Override
    public Comment getByPrimaryKey(Integer primaryKey) throws DAOException {
        Comment comment = null;
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Comment WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    comment = getCommentFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the comment for the passed primary key", ex);
        }

        return comment;
    }

    @Override
    public List<Comment> getAll() throws DAOException {
        List<Comment> commentList = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM Comment")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    commentList.add(getCommentFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of comment", ex);
        }

        return commentList;
    }

    @Override
    public Comment update(Comment comment) throws DAOException {
        if (comment == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed comment is null"));
        }

        try (PreparedStatement stm = CON.prepareStatement(
                "UPDATE Comment SET " +
                        "userId = ?," +
                        "listId = ?," +
                        "text = ? " +
                        "WHERE id = ?"
        )) {

            stm.setInt(1, comment.getUserId());
            stm.setInt(2, comment.getListId());
            stm.setString(3, comment.getText());
            stm.setInt(4, comment.getId());
            if (stm.executeUpdate() != 1) {
                throw new DAOException("Impossible to update the comment");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the comment", ex);
        }

        return comment;
    }

    public HashMap<Integer, String> getCommentsOnListByListId(String listId) throws DAOException {
        HashMap<Integer, String> comments = new HashMap<>();

        if (listId == null) {
            throw new DAOException("listId is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT Comment.userId, Comment.text FROM Comment WHERE Comment.listId = ?")) {
            stm.setString(1, listId);
            try (ResultSet rs = stm.executeQuery()) {
                Integer userId = null;
                String text = null;
                while (rs.next()) {
                    userId = rs.getInt("userId");
                    text = rs.getString("comment");
                    comments.put(userId, text);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of comment", ex);
        }

        return comments;
    }
}
