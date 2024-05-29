package org.aut.httpHandlers;

import org.aut.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        int code = 405; // method not found
        try {
            User user = null;
            if (method.equals("PUT") || method.equals("DELETE")) {
                String jwt = exchange.getRequestHeaders().getFirst("Authorization");
                user = LoginHandler.getUserByToken(jwt);
            }
            switch (method) {
                case "POST": {
                    JSONObject jsonObject = JsonHandler.getObject(exchange.getRequestBody());
                    User newUser = new User(jsonObject);
                    if (!jsonObject.isEmpty()) {
                        UserController.addUser(newUser);
                        code = 200; // success
                    } else {
                        throw new NotAcceptableException("json object is empty");
                    }
                    break;
                }


                case "PUT": {
                    JSONObject jsonObject = JsonHandler.getObject(exchange.getRequestBody());
                    User newUser = new User(jsonObject);
                    if(!user.getUserId().equals(newUser.getUserId())){
                        throw new UnauthorizedException("Unauthorized");
                    }
                    if (!jsonObject.isEmpty()) {
                        UserController.updateUser(newUser);
                        code = 200; // success
                    }
                    break;
                }

                case "DELETE":
                {
                        UserController.deleteUser(user);
                        code = 200; // success
                    break;
                }

            }
        } catch (SQLException e) {
            code = 500;
        } catch (NotAcceptableException e) {
            code = 409;
        } catch (NotFoundException e){
            code = 404;
        } catch (UnauthorizedException e) {
            code = 406;
        }

        exchange.sendResponseHeaders(code, 0);
        exchange.close();
    }
}
