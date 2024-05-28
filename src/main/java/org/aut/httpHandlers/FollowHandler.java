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
                JSONObject jsonObjectP = new JSONObject(exchange.getRequestBody());
                String jwtP = exchange.getRequestHeaders().getFirst("JWT");
                try {
                    Follow newFollow = new Follow(jsonObjectP);
                    if (!jsonObjectP.isEmpty() && jwtP != null && LoginHandler.getUserByToken(jwtP) != null && LoginHandler.getUserByToken(jwtP).getId().equals(newFollow.follower())) {
                        FollowController.addFollow(newFollow);
                        code = 200; //success
                        response.put("success", "The user has been followed successfully");
                    } else if (jwtP == null) {
                        throw new PermissionDeniedException("JWT is missing");
                    } else if (jsonObjectP.isEmpty()) {
                        throw new PermissionDeniedException("Request body is missing");
                    } else if (LoginHandler.getUserByToken(jwtP) == null || !LoginHandler.getUserByToken(jwtP).getId().equals(newFollow.follower())) {
                        throw new PermissionDeniedException("User unauthorized");
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
                JSONObject jsonD = new JSONObject(exchange.getRequestBody());
                String jwtD = exchange.getRequestHeaders().getFirst("JWT");
                try {
                    Follow follow = new Follow(jsonD);
                    if (!jsonD.isEmpty() && FollowAccessor.followExists(follow) && jwtD != null && LoginHandler.getUserByToken(jwtD) != null && LoginHandler.getUserByToken(jwtD).getId().equals(follow.follower())) {
                        FollowAccessor.deleteFollow(follow);
                        code = 200;
                        response.put("success", "The user has been unfollowed successfully");
                    } else if (jsonD.isEmpty()) {
                        throw new PermissionDeniedException("Request body is missing");
                    } else if (!FollowAccessor.followExists(follow)) {
                        throw new NotFoundException("User not found");
                    } else if (jwtD == null) {
                        throw new PermissionDeniedException("JWT is missing");
                    } else if (LoginHandler.getUserByToken(jwtD) == null || !LoginHandler.getUserByToken(jwtD).getId().equals(follow.follower())) {
                        throw new PermissionDeniedException("User unauthorized");
                    }
                } catch (PermissionDeniedException e) {
                    code = 409;
                    response.put("failure", "Permission denied");
                } catch (NotFoundException e) {
                code = 404;
                response.put("failure", "Follower or Followed not found");
                }
                catch (Exception e) {
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
