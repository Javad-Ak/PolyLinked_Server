package org.aut.controllers;

import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;

import java.sql.SQLException;

public class UserController {
    private UserController() {
    }

    public static void addUser(User user) throws SQLException {
        if (!userExists(user.getEmail())) UserAccessor.addUser(user);
    }

    public static boolean userExists(String email) throws SQLException {
        return UserAccessor.getUserByEmail(email) != null;
    }

    public static boolean authenticate(String email, String password) throws SQLException {
        User user = UserAccessor.getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
