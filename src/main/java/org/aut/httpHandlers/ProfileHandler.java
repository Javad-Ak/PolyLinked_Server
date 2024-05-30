package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.dataAccessors.ProfileAccessor;
import org.aut.models.Profile;
import org.aut.models.User;
import org.aut.utils.MultipartHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.sql.SQLException;

public class ProfileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");

        int code = 405;
        try {
            User user = LoginHandler.getUserByToken(jwt);

            switch (method) {
                case "POST":
                case "PUT": {
                    InputStream inputStream = exchange.getRequestBody();

                    Profile profile = new Profile(MultipartHandler.readJson(inputStream, Profile.class));
                    if (!profile.getUserId().equals(user.getUserId())) throw new UnauthorizedException("Unauthorized");

                    if (method.equals("POST")) ProfileAccessor.addProfile(profile);
                    else ProfileAccessor.updateProfile(profile);

                    MultipartHandler.readToFile(inputStream, Path.of(MediaAccessor.PATH_TO_PROFILES + "/" + profile.getUserId()));
                    MultipartHandler.readToFile(inputStream, Path.of(MediaAccessor.PATH_TO_BACKGROUNDS + "/" + profile.getUserId()));

                    inputStream.close();
                    code = 200;
                    break;
                }
                case "GET": {
                    String path = exchange.getRequestURI().getPath();
                    Profile profile = ProfileAccessor.getProfile(path);
                    if (profile == null) throw new NotFoundException("Profile not found");

                    File profilePicture = MediaAccessor.getProfile(profile.getUserId());
                    File background = MediaAccessor.getBackGround(profile.getUserId());

                    OutputStream outputStream = exchange.getResponseBody();
                    MultipartHandler.writeJson(outputStream, profile);
                    MultipartHandler.writeFromFile(outputStream, profilePicture);
                    MultipartHandler.writeFromFile(outputStream, background);

                    outputStream.close();
                    code = 200;
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