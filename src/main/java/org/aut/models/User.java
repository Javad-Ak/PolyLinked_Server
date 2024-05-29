package org.aut.models;

import java.util.Date;
import java.util.Random;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.util.UUID;

public class User implements JsonSerializable {
    private final String userId; // UUID
    private final String email; // valid
    private final String password; // > 7 ch, int
    private final String firstName; // 20 ch
    private final String lastName; // 40 ch
    private final String additionalName; // 20 ch
    private final Date createDate;

    public User(String email, String password, String firstName, String lastName, String additionalName) throws NotAcceptableException {
        String id = "user" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        validateFields(email, password, firstName, lastName, additionalName);
        this.userId = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        createDate = new Date(System.currentTimeMillis());
    }

    public User(JSONObject json) throws NotAcceptableException {
        userId = json.getString("userId");
        email = json.getString("email");
        password = json.getString("password");
        firstName = json.getString("firstName");
        lastName = json.getString("lastName");
        additionalName = json.getString("additionalName");
        createDate = new Date(json.getLong("createDate"));
        validateFields(email, password, firstName, lastName, additionalName);
    }

    @Override
    public String toString() {
        return "{" +
                "userId:" + userId +
                ", email:" + email +
                ", password:" + password +
                ", firstName:" + firstName +
                ", lastName:" + lastName +
                ", additionalName:" + additionalName +
                ", createDate:" + createDate.getTime() +
                '}';
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(toString());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserId() {
        return userId;
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

    public String getAdditionalName() {
        return additionalName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    private static void validateFields(String email, String password, String firstName, String lastName, String additionalName) throws NotAcceptableException {
        if ((firstName == null || lastName == null || email == null || password == null) ||
                (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) ||
                (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) ||
                (!firstName.matches("(?i)^[a-z]{1,20}$")) ||
                (!lastName.matches("(?i)^[a-z]{1,40}$")) ||
                (!additionalName.matches("(?i)^[a-z]{0,20}$")))
            throw new NotAcceptableException("invalid argument");
    }
}
