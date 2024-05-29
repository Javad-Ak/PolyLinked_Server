
package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

public final class Follow implements JsonSerializable {
    private final String follower;
    private final String followed;

    public Follow(String follower, String followed) throws NotAcceptableException {
        validateFields(follower, followed);
        this.follower = follower;
        this.followed = followed;
    }

    public Follow(JSONObject json) throws NotAcceptableException {
        validateFields(json.getString("follower"), json.getString("followed"));
        follower = json.getString("follower");
        followed = json.getString("followed");
    }

    public String getFollowed() {
        return followed;
    }

    public String getFollower() {
        return follower;
    }

    @Override
    public String toString() {
        return "{" +
                "follower:" + follower
                + ", followed:" + followed
                + "}";
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(toString());
    }

    private static void validateFields(String follower, String followed) throws NotAcceptableException {
        if (follower == null || followed == null || followed.equals(follower))
            throw new NotAcceptableException("Follower or Followed fields cannot be null");
    }

    public String follower() {
        return follower;
    }

    public String followed() {
        return followed;
    }
}


