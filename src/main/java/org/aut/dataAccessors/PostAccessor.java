package org.aut.dataAccessors;

import org.aut.models.Post;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;

public class PostAccessor {
    private static final Connection connection = DataBaseAccessor.getConnection();

    private PostAccessor() {
    }

    static void createTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (" +
                    "postId TEXT NOT NULL" +
                    ", userId TEXT NOT NULL" +
                    ", text TEXT NOT NULL" +
                    ", date BIGINT NOT NULL" +
                    ", FOREIGN KEY (userId)" +
                    " REFERENCES users (userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addPost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (postId, userId, text, date) " +
                "VALUES (?,?,?,?);");
        statement.setString(1, post.getPostId());
        statement.setString(2, post.getUserId());
        statement.setString(3, post.getText());
        statement.setLong(4, post.getDate());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void deletePost(String postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE postId = ?;");
        statement.setString(1, postId);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static Post getPostById(String postId) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE postId = ?;");
        return getPostFromResultSet(postId, statement);
    }

    public synchronized static void updatePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE posts SET userId = ? , text = ? , date = ? WHERE postId = ?;");
        statement.setString(1, post.getUserId());
        statement.setString(2, post.getText());
        statement.setLong(3, post.getDate());
        statement.setString(4, post.getPostId());
        statement.executeUpdate();
        statement.close();
    }

    private static Post getPostFromResultSet(String input, PreparedStatement statement) throws SQLException, NotFoundException {
        statement.setString(1, input);
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        resultSet.close();
        statement.close();
        if (jsonObject == null) {
            throw new NotFoundException("User not Found");
        } else {
            Post post;
            try {
                post = new Post(jsonObject);
            } catch (NotAcceptableException e) {
                throw new NotFoundException("User not Found");
            }
            return post;
        }
    }
}
