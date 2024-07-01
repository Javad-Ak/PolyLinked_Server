package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.dataAccessors.ProfileAccessor;
import org.aut.models.Profile;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ProfileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");

        try {
            User user = LoginHandler.getUserByToken(jwt);

            switch (method) {
                case "POST":
                case "PUT": {
                    InputStream inputStream = exchange.getRequestBody();

                    Profile profile = MultipartHandler.readObject(inputStream, Profile.class);
                    if (!profile.getUserId().equals(user.getUserId())) throw new UnauthorizedException("Unauthorized");

                    if (method.equals("POST")) ProfileAccessor.addProfile(profile);
                    else ProfileAccessor.updateProfile(profile);

                    File oldPic = MediaAccessor.getMedia(profile.getUserId(), MediaAccessor.MediaPath.PROFILES);
                    File newPic = MultipartHandler.readToFile(inputStream, MediaAccessor.MediaPath.PROFILES.value(), profile.getUserId());
                    if (newPic != null && oldPic != null) {
                        Files.delete(oldPic.toPath());
                    }

                    File oldBG = MediaAccessor.getMedia("bg" + profile.getUserId(), MediaAccessor.MediaPath.BACKGROUNDS);
                    File newBG = MultipartHandler.readToFile(inputStream, MediaAccessor.MediaPath.BACKGROUNDS.value(), profile.getUserId());
                    if (oldBG != null && newBG != null) {
                        Files.delete(oldBG.toPath());
                    }
                    inputStream.close();
                    exchange.sendResponseHeaders(200, 0);
                }
                break;
                case "GET": {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    if (path.length != 4) throw new NotAcceptableException("Not acceptable");

                    Profile profile = ProfileAccessor.getProfile(path[3]);

                    exchange.sendResponseHeaders(200, 0);
                    OutputStream outputStream = exchange.getResponseBody();
                    JsonHandler.sendObject(outputStream, profile.toJson());

                    outputStream.close();
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