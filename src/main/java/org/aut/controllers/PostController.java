package org.aut.controllers;

import org.aut.dataAccessors.LikeAccessor;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.models.User;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PostController {
    public static HashMap<User, File> getLikersOfPost(String postId) throws SQLException {
        HashMap<User, File> map = new HashMap<>();
        ArrayList<User> users = LikeAccessor.getLikersOfPost(postId);
        for (User user : users) {
            map.put(user, MediaAccessor.getMedia(user.getUserId(), MediaAccessor.MediaPath.PROFILES));
        }
        return map;
    }
}
