package org.aut.controllers;

import org.aut.dataAccessors.CommentAccessor;
import org.aut.dataAccessors.LikeAccessor;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.dataAccessors.PostAccessor;
import org.aut.models.Comment;
import org.aut.models.User;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class PostController {
    public static TreeMap<User, File> getLikersOfPost(String postId) throws SQLException {
        TreeMap<User, File> map = new TreeMap<>();
        ArrayList<User> users = LikeAccessor.getLikersOfPost(postId);
        for (User user : users) {
            map.put(user, MediaAccessor.getMedia(user.getUserId(), MediaAccessor.MediaPath.PROFILES));
        }
        return map;
    }

    public static TreeMap<Comment, File> getCommentsOfPost(String postId) throws SQLException {
        TreeMap<Comment, File> map = new TreeMap<>();
        ArrayList<Comment> comments = CommentAccessor.getCommentsOfPost(postId);
        for (Comment comment : comments) {
            map.put(comment, MediaAccessor.getMedia(comment.getId(), MediaAccessor.MediaPath.COMMENTS));
        }
        return map;
    }
}
