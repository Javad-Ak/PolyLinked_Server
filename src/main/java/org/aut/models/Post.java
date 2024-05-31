package org.aut.models;

import java.util.Random;
import java.util.UUID;

public class Post {
    private final String postId;
    private final String userId;
    private final String text;
    // + media

    public Post(String userId, String text) {
        postId = "post" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        this.userId = userId;
        this.text = text;
    }
}
