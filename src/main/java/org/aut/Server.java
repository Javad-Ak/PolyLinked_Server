package org.aut;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.Executors;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try {
            // dataBase file creation
            Path path = Paths.get("src/main/dataBase");
            Path file = Paths.get("src/main/dataBase/data.db");
            if (!Files.isDirectory(path)) Files.createDirectories(path);
            if (!Files.isRegularFile(file)) Files.createFile(file);

            // Server initialization
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 8);

//            server.createContext();

            server.setExecutor(Executors.newFixedThreadPool(8));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}