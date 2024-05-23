package org.aut.dataAccessors;

import java.sql.*;

import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.json.JSONObject;

public class UserAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private UserAccessor() {
    }

    public synchronized static void addUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, email, password, firstName, LastName) " +
                "VALUES (?,?,?,?,?);");
        statement.setString(1, user.getId());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getFirstName());
        statement.setString(5, user.getLastName());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static User getUser(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?;");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        statement.close();

        return jsonObject == null ? null : new User(jsonObject); // null -> not found
    }
}
