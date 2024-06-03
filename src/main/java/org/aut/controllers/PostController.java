package org.aut.controllers;

import org.aut.dataAccessors.*;
import org.aut.models.Comment;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class PostController {
    public static TreeMap<User, Comment> getCommentsOfPost(String postId) throws SQLException {
        TreeMap<User, Comment> map = new TreeMap<>();
        ArrayList<Comment> comments = CommentAccessor.getCommentsOfPost(postId);
        for (Comment comment : comments) {
            try {
                map.put(UserAccessor.getUserById(comment.getUserId()), comment);
            } catch (NotFoundException ignored) {
            }
        }
        return map;
    }
}
