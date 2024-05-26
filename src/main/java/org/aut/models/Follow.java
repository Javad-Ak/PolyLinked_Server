
package org.aut.models;

import org.json.JSONObject;

public record Follow(String follower, String followed) {

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
}


