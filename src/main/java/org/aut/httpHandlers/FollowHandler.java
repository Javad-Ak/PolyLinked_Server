package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.controllers.FollowController;
import org.aut.dataAccessors.FollowAccessor;
import org.aut.models.Follow;
import org.aut.models.User;

import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.UnauthorizedException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public class FollowHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        int code = 405;

        JSONObject jsonObject = JsonHandler.getObject(exchange.getRequestBody());
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");
        try {
            User user = LoginHandler.getUserByToken(jwt);
            Follow newFollow = new Follow(jsonObject);
            if (!user.getUserId().equals(newFollow.getFollower_id()))
                throw new UnauthorizedException("User unauthorized");

            switch (method) {
                case "POST":
                    if (!jsonObject.isEmpty()) {
                        FollowController.addFollow(newFollow);
                        code = 200;
                    }
                    break;

                case "DELETE":
                    Follow follow = new Follow(jsonObject);
                    if (!jsonObject.isEmpty()) {
                        FollowController.deleteFollow(follow);
                        code = 200;

                    } else if (!FollowAccessor.followExists(follow)) {
                        throw new NotFoundException("User not found");
                    }
                    break;
            }
        } catch (UnauthorizedException e) {
            code = 401;
        } catch (SQLException e) {
            code = 500;
        } catch (NotAcceptableException e) {
            code = 406;
        } catch (NotFoundException e) {
            code = 404;
        }

        exchange.sendResponseHeaders(code, 0);
        exchange.close();
    }
}
