package org.aut.controllers;

import org.aut.dataAccessors.FollowAccessor;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.NotAcceptableException;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static void deleteFollow(Follow follow) throws SQLException, NotFoundException, NotAcceptableException {
        if (follow == null) {
            throw new NotAcceptableException("Null follow");
        } else if (!UserController.userExistsById(follow.getFollowed()) || !UserController.userExistsById(follow.getFollower())) {
            throw new NotFoundException("User not found");
        } else if (!FollowAccessor.followExists(follow)) {
            throw new NotFoundException("Follow not found");
        }
        FollowAccessor.deleteFollow(follow);
    }

    public static HashMap <User, File> getFollowersOf(String userId) throws SQLException , NotAcceptableException {
        HashMap <User, File> fullFollowers = new HashMap <>();
        ArrayList<User> followers = FollowAccessor.getFollowers(userId);
        for (User user : followers) {
            fullFollowers.put(user, MediaAccessor.getMedia(user.getUserId() , MediaAccessor.MediaPath.PROFILES));
        }
        return fullFollowers;
    }
}
