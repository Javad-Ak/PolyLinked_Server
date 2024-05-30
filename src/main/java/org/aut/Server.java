package org.aut;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.aut.dataAccessors.DataBaseConnection;
import org.aut.httpHandlers.*;

/**
 * Server of PolyLinked - A simulation of LinkedIn
 *
 * @author AliReza Atharifard, MohammadJavad Akbari
 * @version 1.0
 */
public class Server {
    public static void main(String[] args) {
        try {
            DataBaseConnection.create();
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 8);

            server.createContext("/users", new UserHandler());
            server.createContext("/users/login", new LoginHandler());
            server.createContext("/follows", new FollowHandler());
            server.createContext("/profiles", new ProfileHandler());
            server.createContext("/connections", new ConnectHandler());

            server.setExecutor(Executors.newFixedThreadPool(8));
            server.start();
            Runtime.getRuntime().addShutdownHook(new DataBaseConnection("atExit"));

            System.out.println("Server is running (localhost:8080).");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}