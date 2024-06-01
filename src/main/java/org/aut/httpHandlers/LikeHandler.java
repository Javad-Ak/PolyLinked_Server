package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.controllers.PostController;
import org.aut.controllers.UserController;
import org.aut.dataAccessors.LikeAccessor;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.models.Like;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.MultipartHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class LikeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");

        try {
            User user = LoginHandler.getUserByToken(jwt);
            switch (method) {
                case "POST": {
                    InputStream inputStream = exchange.getRequestBody();

                    Like like = new Like(JsonHandler.getObject(inputStream));
                    if (!like.getUserId().equals(user.getUserId())) throw new UnauthorizedException("Unauthorized");

                    LikeAccessor.addLike(like);
                    inputStream.close();
                    exchange.sendResponseHeaders(200, 0);
                }
                break;
                case "GET": {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    if (path.length < 3) throw new NotAcceptableException("Invalid path");
                    String postId = path[2];

                    HashMap<User, File> map = PostController.getLikersOfPost(postId);
                    if (map.isEmpty())
                        throw new NotFoundException("Not found");
                    else {
                        exchange.getResponseHeaders().add("X-Total-Count", String.valueOf(map.size()));
                        exchange.sendResponseHeaders(200, 0);

                        OutputStream outputStream = exchange.getResponseBody();
                        MultipartHandler.writeMap(outputStream, map);
                        outputStream.close();
                    }
                }
                break;
                case "DELETE": {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    if (path.length < 3 || path[2].split("&").length < 2)
                        throw new NotAcceptableException("Invalid path");

                    Like like = LikeAccessor.getLike(path[2].split("&")[0], path[2].split("&")[1]);
                    if (!like.getUserId().equals(user.getUserId())) throw new UnauthorizedException("Unauthorized");

                    LikeAccessor.deleteLike(like.getPostId(), like.getUserId());
                    exchange.sendResponseHeaders(200, 0);
                }
                break;
                default:
                    exchange.sendResponseHeaders(405, 0);
                    break;
            }
        } catch (UnauthorizedException e) {
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
