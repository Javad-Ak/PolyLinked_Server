package org.aut.controllers;

import org.aut.dataAccessors.FollowAccessor;
import org.aut.models.Follow;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.NotAcceptableException;

import java.sql.SQLException;

public class FollowController {
    public static void addFollow(Follow follow) throws SQLException, NotFoundException, NotAcceptableException {
        if (follow == null) {
            throw new NotAcceptableException("Null follow");
        } else if (!UserController.userExistsById(follow.getFollower()) || !UserController.userExistsById(follow.getFollowed())) {
            throw new NotFoundException("User not found");
        } else if (FollowAccessor.followExists(follow)) {
            throw new NotAcceptableException("Follow already exists");
        } else if (follow.getFollowed().equals(follow.getFollower())) {
            throw new NotAcceptableException("User Can't follow himself");
        }

        FollowAccessor.addFollow(follow);
    }

}
