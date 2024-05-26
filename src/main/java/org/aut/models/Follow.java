
package org.aut.models;

import org.json.JSONObject;

public class Follow {

    String follower;
    String followed;
    public Follow(String follower, String followed) {
        this.follower = follower;
        this.followed = followed;
    }
    @Override
    public String toString() {
        return "{" +
                "follower:" + follower
                + ", followed:" + followed
                + "}";
    }
    public JSONObject toJSON (){return new JSONObject(toString());}
}


