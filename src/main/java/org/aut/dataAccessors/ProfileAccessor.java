package org.aut.dataAccessors;

import org.aut.models.CallInfo;
import org.aut.models.Profile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private ProfileAccessor() {
    }

    static void createTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS profiles (" +
                    "userId TEXT NOT NULL" +
                    ", bio TEXT" +
                    ", pathToPic TEXT" +
                    ", pathToBG TEXT" +
                    ", country TEXT" +
                    ", city TEXT" +
                    ", status TEXT" +
                    ", profession TEXT" +
                    ", notify BOOLEAN" +
                    ", FOREIGN KEY (userId)" +
                    " REFERENCES users (userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addProfile(Profile profile) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO profiles (userId, bio, pathToPic, pathToBG, country, city, status, profession, notify) " +
                "VALUES (?,?,?,?,?,?,?,?,?);");

        statement.setString(1, profile.getUserId());
        statement.setString(2, profile.getBio());
        statement.setString(3, profile.getPathToPic());
        statement.setString(4, profile.getPathToBG());
        statement.setString(5, profile.getCountry());
        statement.setString(6, profile.getCity());
        statement.setString(7, profile.getStatus());
        statement.setString(8, profile.getProfession());
        statement.setBoolean(9, profile.getNotify());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void updateProfile(Profile profile) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE profiles SET bio = ?, pathToPic = ?, pathToBG = ?, " +
                "country = ?, city = ?, status = ?, profession = ?, notify = ? WHERE userId = ?;");

        statement.setString(1, profile.getBio());
        statement.setString(2, profile.getPathToPic());
        statement.setString(3, profile.getPathToBG());
        statement.setString(4, profile.getCountry());
        statement.setString(5, profile.getCity());
        statement.setString(6, profile.getStatus());
        statement.setString(7, profile.getProfession());
        statement.setBoolean(8, profile.getNotify());
        statement.setString(9, profile.getUserId());

        statement.executeUpdate();
        statement.close();
    }
}
