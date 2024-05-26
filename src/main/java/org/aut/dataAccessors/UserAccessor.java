package org.aut.dataAccessors;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;

import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.json.JSONObject;

public class UserAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private UserAccessor() {
    }

    public static void createUserTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id TEXT NOT NULL" +
                    ", email TEXT NOT NULL" +
                    ", password VARCHAR(20) NOT NULL" +
                    ", firstName VARCHAR(20) NOT NULL " +
                    ", lastName VARCHAR(40) NOT NULL" +
                    ", additionalName VARCHAR(20)" +
                    ", createDate BIGINT(20)" +
                    ", PRIMARY KEY (id, email));");

        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, email, password, firstName, LastName, additionalName, createDate) " +
                "VALUES (?,?,?,?,?,?,?);");
        statement.setString(1, user.getId());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getFirstName());
        statement.setString(5, user.getLastName());
        statement.setString(6, user.getAdditionalName());
        statement.setLong(7, user.getCreateDate().getTime());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static User getUserByEmail(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?;");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        resultSet.close();
        statement.close();

        return jsonObject == null ? null : new User(jsonObject); // null -> not found
    }

    public synchronized static User getUserById(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?;");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        resultSet.close();
        statement.close();

        return jsonObject == null ? null : new User(jsonObject); // null -> not found
    }
}
