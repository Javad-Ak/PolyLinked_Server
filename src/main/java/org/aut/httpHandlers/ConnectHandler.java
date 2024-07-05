package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.controllers.ConnectController;
import org.aut.dataAccessors.ConnectAccessor;
import org.aut.dataAccessors.FollowAccessor;
import org.aut.models.Connect;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public class ConnectHandler implements HttpHandler {
    //may need to change
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        int code = 405;
        JSONObject jsonObject = JsonHandler.getObject(exchange.getRequestBody());
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");
        try {
            Connect connect = new Connect(jsonObject);
            User user = LoginHandler.getUserByToken(jwt);
            switch (method) {
                case "POST":
                    if (!jsonObject.isEmpty() && user.getUserId().equals(connect.getApplicant_id())) {
                        ConnectController.addConnect(connect);
                        code = 200;
                    } else if (!jsonObject.isEmpty()) {
                        code = 401;
                    }
                    break;

                case "PUT":
                    if (!jsonObject.isEmpty() && user.getUserId().equals(connect.getAcceptor_id())) {
                        ConnectController.updateConnect(connect);
                        code = 200;
                    } else if (!jsonObject.isEmpty()) {
                        code = 401;
                    }
                    break;

                case "DELETE":
                    if (!jsonObject.isEmpty() && (user.getUserId().equals(connect.getApplicant_id()) || user.getUserId().equals(connect.getAcceptor_id()))) {
                        ConnectController.deleteConnect(connect);
                        code = 200;
                    } else if (!jsonObject.isEmpty()) {
                        code = 401;
                    }
                    break;

                case "HEAD": {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    if (path.length != 3) throw new NotAcceptableException("Invalid path");
                    String userId = path[2];
                    String exists = "false";
                    for (Connect obj : ConnectAccessor.getWaitingConnectsOf(userId)) {
                        if (obj.getApplicant_id().equals(user.getUserId())) {
                            exists = "waiting";
                            break;
                        }
                    }
                    for (User obj : ConnectAccessor.getConnectionsOf(userId)) {
                        if (obj.getUserId().equals(user.getUserId())) {
                            exists = "true";
                            break;
                        }
                    }
                    exchange.getResponseHeaders().add("Exists", exists);
                    exchange.sendResponseHeaders(200, -1);
                }

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
