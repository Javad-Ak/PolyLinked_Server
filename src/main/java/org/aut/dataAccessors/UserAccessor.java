package org.aut.dataAccessors;

import java.sql.*;

import org.aut.models.User;

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

    public synchronized static User getUser(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        User newUser = null;
        if (resultSet.next()) newUser = new User(resultSet.getString(1), resultSet.getString(2));

        statement.close();
        return newUser; // null -> not found
    }
}
