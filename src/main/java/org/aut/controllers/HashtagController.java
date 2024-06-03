package org.aut.controllers;

import org.aut.dataAccessors.PostAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Post;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class HashtagController {
    public static TreeMap<Post, User> hashtagDetector(String input) throws SQLException, NotFoundException {
        TreeMap<Post, User> map = new TreeMap<>();
        ArrayList<Post> posts = PostAccessor.getPostsByHashtag(input);
        for (Post post : posts) map.put(post, UserAccessor.getUserById(post.getUserId()));
        return map;
    }
}
