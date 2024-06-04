package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.controllers.HashtagController;
import org.aut.models.Post;
import org.aut.models.User;
import org.aut.utils.MultipartHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.TreeMap;

public class HashtagHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");

        try {
            if (method.equals("GET")) {
                LoginHandler.getUserByToken(jwt);

                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length != 3) throw new NotAcceptableException("Invalid path");
                String input = path[2];

                TreeMap<Post, User> posts = HashtagController.hashtagDetector(input);
                if (!posts.isEmpty()) {
                    exchange.getResponseHeaders().add("X-Total-Count", String.valueOf(posts.size()));
                    exchange.sendResponseHeaders(200, 0);
                    OutputStream outputStream = exchange.getResponseBody();

                    System.out.println(posts);
                    MultipartHandler.writeMap(outputStream, posts);
                    outputStream.close();
                } else {
                    throw new NotFoundException("Not found");
                }
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
        } catch (UnauthorizedException e){
            exchange.sendResponseHeaders(401, 0);
        } catch (SQLException e) {
            exchange.sendResponseHeaders(500, 0);
        } catch (NotAcceptableException e) {
            exchange.sendResponseHeaders(406, 0);
        } catch (NotFoundException e) {
            exchange.sendResponseHeaders(404, 0);
        }
        exchange.close();
    }
}
