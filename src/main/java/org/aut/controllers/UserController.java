package org.aut.controllers;

import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;
import java.sql.SQLException;

public class UserController {
    private UserController() {
    }

    public static void addUser(User user) throws SQLException {
        if (!UserExists(user.getUsername())) UserAccessor.addUser(user);
    }

    public static boolean UserExists(String username) throws SQLException {
        return UserAccessor.getUser(username) != null;
    }
}
