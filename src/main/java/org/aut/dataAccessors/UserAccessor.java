package org.aut.dataAccessors;

import java.sql.*;
import org.aut.models.User;

public class UserAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private UserAccessor() {
    }

    public synchronized static void addUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) " +
                "VALUES (?, ?)");
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static User getUser(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next())
            return new User(resultSet.getString(1), resultSet.getString(2));

        return null; // not found
    }
}
