package org.aut.controllers;

import org.aut.dataAccessors.*;
import org.aut.models.Comment;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class PostController {
    public static TreeMap<Comment, User> getCommentsOfPost(String postId) throws SQLException {
        TreeMap<Comment, User> map = new TreeMap<>();
        ArrayList<Comment> comments = CommentAccessor.getCommentsOfPost(postId);
        for (Comment comment : comments) {
            try {
                if (map.containsKey(comment)) continue;
                User user = UserAccessor.getUserById(comment.getUserId());
                map.put(comment, user);
            } catch (NotFoundException ignored) {
            }
        }
        return map;
    }
}
