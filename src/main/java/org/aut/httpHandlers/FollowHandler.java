package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.controllers.FollowController;
import org.aut.dataAccessors.FollowAccessor;
import org.aut.models.Follow;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.PermissionDeniedException;
import org.json.JSONObject;

import java.io.IOException;

public class FollowHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        JSONObject response = new JSONObject();
        int code = 400;
        switch (method) {
            case "POST":
                JSONObject jsonObject = new JSONObject(exchange.getRequestBody());
                try {
                    Follow newFollow = new Follow(jsonObject);
                    if (!jsonObject.isEmpty()) {
                        FollowController.addFollow(newFollow);
                        code = 200; //success
                        response.put("success", "The user has been followed successfully");
                    }
                } catch (PermissionDeniedException e) {
                    code = 409;
                    response.put("failure", "Permission denied");
                } catch (NotFoundException e) {
                    code = 404;
                    response.put("failure", "User not found");
                } catch (Exception e) {
                    code = 500;
                    response.put("failure", "Something went wrong. Try again later");
                    System.out.println(e.getMessage());
                }
                break;
            case "DELETE":
                JSONObject json = new JSONObject(exchange.getRequestBody());
                try {
                    Follow newFollow = new Follow(json);
                    if (!json.isEmpty() && FollowAccessor.followExists(newFollow)) {
                        FollowAccessor.deleteFollow(newFollow);
                        code = 200;
                        response.put("success", "The user has been unfollowed successfully");
                    } else if (!json.isEmpty()) {
                        code = 409;
                        response.put("failure", "Follow not exists");
                    }
                } catch (Exception e) {
                    code = 500;
                    response.put("failure", "Something went wrong. Try again later");
                    System.out.println(e.getMessage());
                }
                break;


        }
        exchange.sendResponseHeaders(code, response.toString().getBytes().length);
        JsonHandler.sendObject(exchange.getResponseBody(), response);
        exchange.close();
    }
}
