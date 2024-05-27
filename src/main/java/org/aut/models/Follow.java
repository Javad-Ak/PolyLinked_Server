
package org.aut.models;

import org.aut.utils.exceptions.PermissionDeniedException;
import org.json.JSONObject;

import java.util.Objects;

public final class Follow {
    private final String follower;
    private final String followed;

    public Follow(String follower, String followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public Follow(JSONObject json) throws PermissionDeniedException {
        validateFields(json.getString(follower()) , json.optString(followed()));
        followed = json.getString(follower());
        follower = json.getString(follower());
    }

    @Override
    public String toString() {
        return "{" +
                "follower:" + follower
                + ", followed:" + followed
                + "}";
    }

    public JSONObject toJSON() {
        return new JSONObject(toString());
    }

    private static void validateFields(String follower, String followed) throws PermissionDeniedException {
        if (follower == null || followed == null)
            throw new PermissionDeniedException("Follower or Followed fields cannot be null");
    }

    public String follower() {
        return follower;
    }

    public String followed() {
        return followed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Follow) obj;
        return Objects.equals(this.follower, that.follower) &&
                Objects.equals(this.followed, that.followed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, followed);
    }

}


