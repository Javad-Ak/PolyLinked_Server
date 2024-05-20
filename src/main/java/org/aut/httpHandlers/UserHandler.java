package org.aut.httpHandlers;

import org.aut.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.json.JSONObject;

import java.io.*;


public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splittedPath = exchange.getRequestURI().getPath().split("/"); // later usage

        JSONObject response = new JSONObject();
        switch (method) {
            case "POST":
                JSONObject jsonObject = JsonHandler.getJsonObject(exchange.getRequestBody());
                User newUser = new User(jsonObject);

                try {
                    if (!jsonObject.toString().isEmpty() && !UserController.UserExists(newUser.getUsername())) {
                        UserController.addUser(newUser);
                        response.put("message", "User " + newUser.getFirstName() + " " + newUser.getLastName() + " created");
//                    TODO : Files.createDirectories
                    } else {
                        response.put("message", "User " +  newUser.getFirstName() + " " + newUser.getLastName() + " already exists");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

//                TODO other cases
        }
        exchange.sendResponseHeaders(200, response.toString().getBytes().length); // 200 -> successful connection
        JsonHandler.sendJsonObject(exchange.getResponseBody(), response);
        exchange.close();
    }
}
