package org.aut.controllers;

import org.aut.dataAccessors.FollowAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.PermissionDeniedException;

import java.sql.SQLException;

public class FollowController {
    public static void addFollow(Follow follow) throws SQLException, NotFoundException , PermissionDeniedException {
        if (!UserController.userExistsById(follow.follower()) || !UserController.userExistsById(follow.followed())) {
            throw new NotFoundException("User not found");
        } else if (FollowAccessor.followExists(follow)) {
            throw new PermissionDeniedException("Follow already exists");
        } else if( follow.followed().equals(follow.follower())) {
            throw new PermissionDeniedException("User Can't follow himself");
        }
        FollowAccessor.addFollow(follow);
    }
}
