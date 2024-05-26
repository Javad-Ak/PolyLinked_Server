package org.aut.controllers;

import org.aut.dataAccessors.FollowAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;

import java.sql.SQLException;

public class FollowController {
    public static void addFollow(String follower_id, String followed_id) throws SQLException, NotFoundException {
        if (UserController.userExistsById(follower_id) || UserController.userExistsById(followed_id)) {
            throw new NotFoundException("User not found");
        }
        FollowAccessor.addFollow(new Follow(follower_id , followed_id ));
    }
}
