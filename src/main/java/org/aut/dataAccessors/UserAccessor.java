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
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id , first_name , last_name , email, password) " +
                "VALUES (?,?,?,? ?)");
        statement.setNString(1, user.getId());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getPassword());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static User getUser(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        statement.close();

        return jsonObject ==  null ? null : new User(jsonObject); // null -> not found
    }
}
