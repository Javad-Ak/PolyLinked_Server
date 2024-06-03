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
            String [] splitURI = exchange.getRequestURI().getPath().split("/");
            if (splitURI.length != 3) {
                throw new NotAcceptableException("Invalid request");
            }

            try {
                UserAccessor.getUserById(user.getUserId());
            } catch (NotFoundException e) {
                throw new UnauthorizedException("User unauthorized");
            }

            String path = splitURI[2];
            User seekedUser = UserAccessor.getUserById(path);

            if (method.equals("GET")) {

                HashMap<User, File> fullFollowers = FollowController.getFollowersOf(seekedUser.getUserId());
                exchange.getResponseHeaders().set("X-Total-Count", "" + fullFollowers.size() );
                exchange.sendResponseHeaders(200, 0);
                OutputStream outputStream = exchange.getResponseBody();

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
