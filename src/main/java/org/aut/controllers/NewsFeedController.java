package org.aut.controllers;

import org.aut.dataAccessors.ConnectAccessor;
import org.aut.dataAccessors.FollowAccessor;
import org.aut.dataAccessors.PostAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Post;
import org.aut.models.User;
import org.aut.utils.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NewsFeedController {
    public static TreeMap<Post, User> fetchFeed(String userId) throws SQLException, NotFoundException {
        try {
            List<User> related = new ArrayList<>();
            related.addAll(FollowAccessor.getFollowings(userId));
            related.addAll(ConnectAccessor.getConnectionsOf(userId));

            List<User> network = ConnectAccessor.getNetworkOf(userId);
            network.addAll(FollowAccessor.getNetWork(userId));

            ArrayList<Post> posts = new ArrayList<>();
            for (User user : network) posts.addAll(PostAccessor.getPostsLikedBy(user.getUserId()));
            for (User user : related) posts.addAll(PostAccessor.getPostsOf(user.getUserId()));

            TreeMap<Post, User> feed = new TreeMap<>();
            for (Post post : posts) {
                if (post.getPostId().equals(userId)) continue;
                feed.put(post, UserAccessor.getUserById(post.getUserId()));
            }

            return feed;
        } catch (Exception e) {
            throw new NotFoundException("empty feed");
        }
    }
}
