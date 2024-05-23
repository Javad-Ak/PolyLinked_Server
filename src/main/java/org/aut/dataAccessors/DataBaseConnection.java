package org.aut.dataAccessors;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.nio.file.*;
import java.sql.*;

public class DataBaseConnection extends Thread {
    private static final String directory = "src/main/dataBase";
    private static final String url = "jdbc:sqlite:./src/main/dataBase/data.db";
    private static final ArrayList<Connection> connections = new ArrayList<>();

    public DataBaseConnection(String name) {
        super(name);
    }

    @Override
    public void run() {
        closeConnections();
    }

    public static void create() throws IOException {
        Path path = Paths.get(directory);
        Path file = Paths.get(directory + "/data.db");
        if (!Files.isDirectory(path)) Files.createDirectories(path);
        if (!Files.isRegularFile(file)) Files.createFile(file);

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
//            TODO other tables
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id TEXT NOT NULL" +
                    ", email TEXT NOT NULL" +
                    ", password VARCHAR(20) NOT NULL" +
                    ", firstName VARCHAR(20) NOT NULL " +
                    ", lastName VARCHAR(40) NOT NULL" +
                    ", PRIMARY KEY (id, email));");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url);
            connections.add(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnections() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
