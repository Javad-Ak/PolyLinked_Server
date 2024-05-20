package org.aut.models;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;

public class User {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User(String email, String password, String firstName, String lastName) {
        this.id = "user" + RandomUtils.nextInt(99999) + RandomStringUtils.randomAlphanumeric(4);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(JSONObject json) {
        this.id = json.getString("id");
        this.email = json.getString("email");
        this.password = json.getString("password");
        this.firstName = json.getString("first_name");
        this.lastName = json.getString("last_name");
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

    private boolean validateFields() {
        if (!EmailValidator.getInstance().isValid(email)) return false;

//        TODO: others
        return true;
    }
}
