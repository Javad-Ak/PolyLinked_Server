package org.aut.dataAccessors;

import org.aut.models.Connect;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

public class ConnectAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private ConnectAccessor() {
    }

    public static void createConnectTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS connects (" +
                    "applicant_id TEXT NOT NULL " +
                    ", acceptor_id TEXT NOT NULL" +
                    ", note TEXT NOT NULL" +
                    ", accept_state BOOLEAN NOT NULL" +
                    ", FOREIGN KEY (applicant_id , acceptor_id)" +
                    " REFERENCES users (userId, userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE " +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }
    public synchronized static void addConnect(Connect connect) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO connects (applicant_id , acceptor_id , note , accept_state) " +
                "VALUES (?, ?, ? , ?)");
        statement.setString( 1, connect.getApplicantId());
        statement.setString( 2, connect.getAcceptorId());
        statement.setBoolean(3 , connect.getAcceptState());
        statement.setString(4 , connect.getNote());
        statement.executeUpdate();
        statement.close();
    }


    public synchronized static void deleteConnect(Connect connect) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM connects WHERE applicant_id = ? AND acceptor_id = ?");
        statement.setString(1, connect.getApplicantId());
        statement.setString(2, connect.getAcceptorId());
        statement.close();
    }

    public synchronized static void acceptConnect(Connect connect) throws SQLException {
        // TODO implementation
    }

    public synchronized static ArrayList<Connect> getAcceptedConnect(String userId) throws SQLException, NotAcceptableException {
        ArrayList<Connect> acceptedConnects ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE (applicant_id = ? OR acceptor_id = ?) AND accept_state = true");
        statement.setString(1, userId);
        statement.setString(2, userId);
        acceptedConnects = getConnectListFromStatement(statement);
        statement.close();
        return acceptedConnects;

    }

    public synchronized static ArrayList<Connect> getUnacceptedConnect(String userId) throws SQLException, NotAcceptableException {
        ArrayList<Connect> acceptedConnects ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE applicant_id = ? AND accept_state = false");
        statement.setString(1, userId);
        acceptedConnects = getConnectListFromStatement(statement);
        statement.close();
        return acceptedConnects;

    }
    public synchronized static ArrayList<Connect> getConnectListFromStatement(PreparedStatement statement) throws SQLException, NotAcceptableException {
        ArrayList<Connect> connects = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject;
        while ((jsonObject = JsonHandler.getFromResultSet(resultSet)) != null) {
            connects.add(new Connect(jsonObject));
        }
        resultSet.close();
        return connects;
    }
}
