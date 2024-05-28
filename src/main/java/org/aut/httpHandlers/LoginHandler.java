package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.aut.controllers.UserController;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.JwtHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) return;
        JSONObject received = JsonHandler.getObject(exchange.getRequestBody()); // A json with email and password

        JSONObject response = new JSONObject();
        int code;
        try {
            if (UserController.authenticate(received.getString("email"), received.getString("password"))) {
                code = 200; // success
                User user = UserAccessor.getUserByEmail(received.getString("email"));
                assert user != null; // Already checked in authentication.
                response.put("JWT", JwtHandler.generateToken(user.getId()));
            } else {
                code = 403; // permission denied
                response.put("failure", "User unauthorized.");
            }
        } catch (Exception e) {
            code = 500; // server conflict
            response.put("failure", "Something went wrong. Try again later.");
            System.out.println(e.getMessage());
        }
        exchange.sendResponseHeaders(code, response.toString().getBytes().length);
        JsonHandler.sendObject(exchange.getResponseBody(), response);
        exchange.close();
    }

    public static User getUserByToken(String token) throws SQLException {
        Claims claims = JwtHandler.verifyToken(token);
        User user;
        try {
            user = UserAccessor.getUserById(claims.getSubject());
        } catch (JwtException e) {
            return null;
        }
        return user;
    }
}
