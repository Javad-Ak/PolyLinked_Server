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
        System.out.println(Arrays.toString(path));
        if (!exchange.getRequestMethod().equals("GET") || path.length < 4) {
            exchange.sendResponseHeaders(405, 0);
            return;
        }

        try {
            File file = MediaAccessor.getMedia(path[3], MediaAccessor.MediaPath.valueOf(path[2].toUpperCase()));
            if (file == null) throw new NotFoundException("media not found");


            int length = (int) file.length();
            exchange.getResponseHeaders().add("Content-Type", file.getName().substring(file.getName().lastIndexOf(".") + 1));
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
    }
}
