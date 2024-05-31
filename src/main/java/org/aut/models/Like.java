package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Like implements JsonSerializable {
    private final String userId;
    private final String postId;
    private final Date date;


    public Like(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        date = new Date(System.currentTimeMillis());
    }

    public Like(JSONObject json) throws NotAcceptableException {
        try {
            this.userId = json.getString("userId");
            this.postId = json.getString("postId");
            this.date = new Date(json.getLong("date"));
        } catch (JSONException e) {
            throw new NotAcceptableException("Json parse failed.");
        }
    }


    @Override
    public String toString() {
        return "{userId:" + userId + ", postId:" + postId + ", date:" + date + "}";
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public long getDate() {
        return date.getTime();
    }
}
