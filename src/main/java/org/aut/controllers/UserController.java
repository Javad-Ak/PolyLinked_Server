package org.aut.controllers;

import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;

import java.sql.SQLException;

public class UserController {
    private UserController() {
    }

    public static void addUser(User user) throws SQLException {
        if (!userExistsByEmail(user.getEmail())) UserAccessor.addUser(user);
    }

    public static boolean userExistsByEmail(String email) throws SQLException {
        try {
            UserAccessor.getUserByEmail(email);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public static boolean userExistsById(String id) throws SQLException {
        try {
            UserAccessor.getUserById(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public static boolean authenticate(String email, String password) throws SQLException, NotFoundException {
        User user = UserAccessor.getUserByEmail(email);
        return user.getPassword().equals(password);
    }
}
