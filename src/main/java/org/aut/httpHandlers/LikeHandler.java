package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.dataAccessors.LikeAccessor;
import org.aut.models.Like;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

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

                }
                break;
                case "DELETE": {
                    String path = exchange.getRequestURI().getPath().split("/")[2];
                    Like like = LikeAccessor.getLike(path.split("&")[0], path.split("&")[1]);
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
