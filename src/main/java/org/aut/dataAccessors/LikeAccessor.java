package org.aut.dataAccessors;

import org.aut.models.Like;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;

public class LikeAccessor {
    private static final Connection connection = DataBaseAccessor.getConnection();

    private LikeAccessor() {
    }

    static void createTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS likes (" +
                    "postId TEXT NOT NULL" +
                    ", userId TEXT NOT NULL" +
                    ", date BIGINT NOT NULL" +
                    ", FOREIGN KEY (postId, userId)" +
                    " REFERENCES posts (postId, userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addLike(Like like) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO likes (postId, userId, date) " +
                "VALUES (?,?,?);");
        statement.setString(1, like.getPostId());
        statement.setString(2, like.getUserId());
        statement.setLong(3, like.getDate());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void deleteLike(String postId, String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM likes WHERE postId = ? AND userId = ?;");
        statement.setString(1, postId);
        statement.setString(2, userId);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static Like getLike(String postId, String userId) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE postId = ? AND userId = ?;");
        statement.setString(1, postId);
        statement.setString(2, userId);
        Like like = getLikeFromResultSet(statement.executeQuery());
        statement.close();
        return like;
    }

    private static Like getLikeFromResultSet(ResultSet resultSet) throws SQLException, NotFoundException {
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        resultSet.close();
        if (jsonObject == null) {
            throw new NotFoundException("User not Found");
        } else {
            Like like;
            try {
                like = new Like(jsonObject);
            } catch (NotAcceptableException e) {
                throw new NotFoundException("User not Found");
            }
            return like;
        }
    }
}
