package org.aut.controllers;

import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;

import java.sql.SQLException;

public class UserController {
    private UserController() {
    }

    public static void addUser(User user) throws SQLException {
        if (!userExistsByEmail(user.getEmail())) UserAccessor.addUser(user);
    }

    public static boolean userExistsByEmail(String email) throws SQLException {
        return UserAccessor.getUserByEmail(email) != null;
    }
    public static boolean userExistsById(String id) throws SQLException {
        return UserAccessor.getUserById(id) != null;
    }

    public static boolean authenticate(String email, String password) throws SQLException {
        User user = UserAccessor.getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
