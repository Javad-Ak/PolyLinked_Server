package org.aut.models;

import org.json.JSONObject;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(JSONObject json) {
        this.username = json.getString("username");
        this.password = json.getString("password");
    }

    @Override
    public String toString() {
        return "{" +
                "username:" + username +
                ", password:" + password +
                '}';
    }

    public JSONObject toJSON() {
        return new JSONObject(toString());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
