package org.aut.httpHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.aut.dataAccessors.MediaAccessor;
import org.aut.utils.exceptions.NotFoundException;

import java.io.*;
import java.util.Arrays;

public class ResourceHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String method = exchange.getRequestMethod();
        if (!(method.equals("GET") || method.equals("HEAD")) || path.length < 4) {
            exchange.sendResponseHeaders(405, 0);
            return;
        }

        try {
            File file = MediaAccessor.getMedia(path[3], MediaAccessor.MediaPath.valueOf(path[2].toUpperCase()));
            if (file == null) throw new NotFoundException("Media not found");


            int length = (int) file.length();
            String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (type.trim().isEmpty()) throw new NotFoundException("File corruption");

            if (MediaAccessor.VIDEO_EXTENSIONS.contains(type)) {
                exchange.getResponseHeaders().add("Content-Type", "Video/" + type);
            } else if (MediaAccessor.AUDIO_EXTENSIONS.contains(type)) {
                exchange.getResponseHeaders().add("Content-Type", "Audio/" + type);
            } else if (MediaAccessor.IMAGE_EXTENSIONS.contains(type)) {
                exchange.getResponseHeaders().add("Content-Type", "Image/" + type);
            } else throw new NotFoundException("File format not supported");

            exchange.sendResponseHeaders(200, length);
            if (method.equals("HEAD")) return;

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
    }
}
