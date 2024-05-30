package org.aut.dataAccessors;

import org.aut.models.CallInfo;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CallInfoAccessor {
    private static final Connection connection = DataBaseAccessor.getConnection();

    private CallInfoAccessor() {
    }

    static void createUserTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS callInfo (" +
                    "userId TEXT NOT NULL" +
                    ", email TEXT PRIMARY KEY NOT NULL" +
                    ", mobileNumber TEXT" +
                    ", homeNumber TEXT" +
                    ", workNumber TEXT" +
                    ", Address TEXT" +
                    ", birthDay BIGINT" +
                    ", privacyPolitics TEXT" +
                    ", socialMedia TEXT" +
                    ", FOREIGN KEY (userId, email)" +
                    " REFERENCES users (userId, email)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addCallInfo(CallInfo callInfo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO callInfo(userId, email, mobileNumber, homeNumber, workNumber, Address, birthDay, privacyPolitics, socialMedia) " +
                "VALUES (?,?,?,?,?,?,?,?,?);");

        statement.setString(1, callInfo.getUserId());
        statement.setString(2, callInfo.getEmail());
        statement.setString(3, callInfo.getMobileNumber());
        statement.setString(4, callInfo.getHomeNumber());
        statement.setString(5, callInfo.getWorkNumber());
        statement.setString(6, callInfo.getAddress());
        statement.setLong(7, callInfo.getBirthDay());
        statement.setString(8, callInfo.getPrivacyPolitics());
        statement.setString(9, callInfo.getSocialMedia());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void updateCallInfo(CallInfo callInfo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE callInfo SET email=?, mobileNumber=?, homeNumber=?, workNumber=?, Address=?, birthDay=?, privacyPolitics=?, socialMedia=? WHERE userId=?;");


        statement.setString(1, callInfo.getEmail());
        statement.setString(2, callInfo.getMobileNumber());
        statement.setString(3, callInfo.getHomeNumber());
        statement.setString(4, callInfo.getWorkNumber());
        statement.setString(5, callInfo.getAddress());
        statement.setLong(6, callInfo.getBirthDay());
        statement.setString(7, callInfo.getPrivacyPolitics());
        statement.setString(8, callInfo.getSocialMedia());
        statement.setString(9, callInfo.getUserId());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void deleteCallInfo(CallInfo callInfo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM callInfo WHERE userId = ?;");
        statement.setString(1, callInfo.getUserId());

        statement.executeUpdate();
        statement.close();
    }
}
