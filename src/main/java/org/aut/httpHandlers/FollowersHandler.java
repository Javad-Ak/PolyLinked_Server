package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.controllers.FollowController;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;
import org.aut.utils.MultipartHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;

public class FollowersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        int code = 405;

        String jwt = exchange.getRequestHeaders().getFirst("Authorization");
        try {
            User user = LoginHandler.getUserByToken(jwt);
            String path = exchange.getRequestURI().getPath().split("/")[2];
            User seekedUser = UserAccessor.getUserById(path);

            try {
                UserAccessor.getUserById(user.getUserId());
            } catch (NotFoundException e) {
                throw new UnauthorizedException("User unauthorized");
            }

            if (method.equals("GET")) {

                exchange.sendResponseHeaders(200, 0);
                OutputStream outputStream = exchange.getResponseBody();

                HashMap<User, File> fullFollowers = FollowController.getFollowersOf(seekedUser.getUserId());
                MultipartHandler.writeMap(outputStream, fullFollowers);
                outputStream.close();


            } else {
                exchange.sendResponseHeaders(405, 0);
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
