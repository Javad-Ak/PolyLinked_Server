package org.aut.models;

import java.util.Random;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;

public class User {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User(String email, String password, String firstName, String lastName) {
        if(validateFields(firstName , lastName , email , password)){
            this.id = "user" + new Random().nextInt(99999)+ UUID.randomUUID().toString().substring(4 , 27);
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Invalid Fields");
        }
    }

    public User(JSONObject json) {
        if(validateFields(json.getString("first-name") ,json.getString("last-name") , json.getString("email") , json.getString("password"))){
            this.id = json.getString("id");
            this.email = json.getString("email");
            this.password = json.getString("password");
            this.firstName = json.getString("first_name");
            this.lastName = json.getString("last_name");
        } else {
            throw new IllegalArgumentException("Invalid Fields");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", email:" + email +
                ", password:" + password +
                ", firstName:" + firstName +
                ", lastName:" + lastName +
                '}';
    }

    public JSONObject toJSON() {
        return new JSONObject(toString());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private boolean validateFields( String firstName , String lastName , String email ,String password) {
        if(firstName == null || lastName == null || email == null || password == null) return false;
        if (!EmailValidator.getInstance().isValid(email)) return false;
        if (!password.matches(".*[0-9].*") || !password.matches(".*[a-zA-Z].*")) return false;
        if (password.length() < 8) return false;
//        TODO: others(?)
        return true;
    }
}
