package org.aut.httpHandlers;

import org.aut.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;
import java.io.*;
import java.sql.SQLException;

public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        int code = 405; // method not found
        try {
            switch (method) {
                case "POST":
                    JSONObject jsonObject = JsonHandler.getObject(exchange.getRequestBody());
                    User newUser = new User(jsonObject);
                    if (!jsonObject.isEmpty() && !UserController.userExistsByEmail(newUser.getEmail())) {
                        UserController.addUser(newUser);
                        code = 200; // success
                    } else {
                        code = 409; // already exists
                    }
                    break;

                case "PUT":
//                    TODO
                    break;

                case "DELETE":
//                    TODO
                    break;

            }
        } catch (SQLException e) {
            code = 500;
        } catch (NotAcceptableException e) {
            code = 406;
        }

        exchange.sendResponseHeaders(code, 0);
        exchange.close();
    }
}
