package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Post implements JsonSerializable {
    private final String postId;
    private final String userId;
    private final String text;
    private final Date date;
    // + media file in resources

    public Post(String userId, String text) throws NotAcceptableException {
        if (text == null || text.trim().isEmpty() || text.trim().length() > 3000)
            throw new NotAcceptableException("invalid arguments");

        postId = "post" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        this.userId = userId;
        this.text = text.trim();
        date = new Date(System.currentTimeMillis());
    }

    public Post(JSONObject json) throws NotAcceptableException {
        try {
            postId = json.getString("postId");
            userId = json.getString("userId");
            text = json.getString("text").trim();
            date = new Date(json.getLong("date"));
        } catch (JSONException e) {
            throw new NotAcceptableException("invalid arguments");
        }

        if (text.trim().isEmpty() || text.trim().length() > 3000)
            throw new NotAcceptableException("invalid arguments");
    }

    @Override
    public String toString() {
        return "{" +
                "postId:" + postId +
                ", userId:" + userId +
                ", text:" + text +
                ", date:" + date.getTime() +
                '}';
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public long getDate() {
        return date.getTime();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(toString());
    }
}
