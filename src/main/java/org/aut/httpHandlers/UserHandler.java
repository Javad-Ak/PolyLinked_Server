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
//        String[] splitPath = exchange.getRequestURI().getPath().split("/"); // later usage

        JSONObject response = new JSONObject();
        int code = 400;
        switch (method) {
            case "POST":
                JSONObject jsonObject = JsonHandler.getObject(exchange.getRequestBody());
                User newUser = new User(jsonObject);
                try {
                    if (!jsonObject.isEmpty() && !UserController.userExists(newUser.getEmail())) {
                        UserController.addUser(newUser);
                        code = 200; // success
                        response.put("success", "User with email " + newUser.getEmail() + " created.");

//                    TODO : Files.createDirectories
                    } else if (!jsonObject.isEmpty()) {
                        code = 409; // already exists
                        response.put("failure", "User with email " + newUser.getEmail() + " already exists.");
                    }
                } catch (Exception e) {
                    code = 500; // server conflict
                    response.put("failure", "Something went wrong. Try again later.");
                    System.out.println(e.getMessage());
                }
                break;

//                TODO other cases
        }

        exchange.sendResponseHeaders(code, response.toString().getBytes().length);
        JsonHandler.sendObject(exchange.getResponseBody(), response);
        exchange.close();
    }
}
