package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotFoundException;
import org.aut.utils.exceptions.UnauthorizedException;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;

public class ResourceHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String method = exchange.getRequestMethod();
        if (!(method.equals("GET") || method.equals("HEAD")) || path.length != 4) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        try {
            File file = MediaAccessor.getMedia(path[3], MediaAccessor.MediaPath.valueOf(path[2].toUpperCase()));
            if (file == null || file.length() < 1 || !file.isFile()) throw new NotFoundException("Media not found");

            int length = (int) file.length();
            String type = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
            if (type.trim().isEmpty()) throw new NotFoundException("File corruption");

            if (MediaAccessor.VIDEO_EXTENSIONS.contains(type)) {
                exchange.getResponseHeaders().add("Content-Type", "Video/" + type);
            } else if (MediaAccessor.AUDIO_EXTENSIONS.contains(type)) {
                exchange.getResponseHeaders().add("Content-Type", "Audio/" + type);
            } else if (MediaAccessor.IMAGE_EXTENSIONS.contains(type)) {
                exchange.getResponseHeaders().add("Content-Type", "Image/" + type);
            } else throw new NotFoundException("File format not supported");

            if (method.equals("HEAD")) {
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
                return;
            }

            exchange.sendResponseHeaders(200, length);
            try (OutputStream outputStream = exchange.getResponseBody();
                 FileInputStream inputStream = new FileInputStream(file)) {

                int totalWrite = 0;
                byte[] buffer = new byte[1000000];
                while (totalWrite < length) {
                    int read = inputStream.read(buffer);
                    if (read == -1) break;

                    outputStream.write(buffer, 0, read);
                    totalWrite += read;
                }
                outputStream.flush();
            }
        } catch (NotFoundException e) {
            exchange.sendResponseHeaders(404, 0);
        }
        exchange.close();
    }
}
