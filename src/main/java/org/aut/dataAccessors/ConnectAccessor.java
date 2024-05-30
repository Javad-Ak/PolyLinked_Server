package org.aut.dataAccessors;

import org.aut.models.Connect;
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
                    ", accept_state TEXT NOT NULL" +
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
        statement.setString(3 , connect.getNote());
        statement.setString(4 , connect.getAcceptState());
        statement.executeUpdate();
        statement.close();
    }


    public synchronized static void deleteConnect(Connect connect) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM connects WHERE applicant_id = ? AND acceptor_id = ? AND  accept_state = ?");
        statement.setString(1, connect.getApplicantId());
        statement.setString(2, connect.getAcceptorId());
        statement.setString(3 , Connect.AcceptState.ACCEPTED.toString());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void updateConnect(Connect connect) throws SQLException {
       PreparedStatement statement = connection.prepareStatement("UPDATE connects SET accept_state = ? WHERE applicant_id = ? AND acceptor_id = ?");
       statement.setString(1, connect.getAcceptState()); //may need change to ACCEPTED if there is only one case for updating
       statement.setString(2, connect.getApplicantId());
       statement.setString(3, connect.getAcceptorId());
       statement.executeUpdate();
       statement.close();
    }

    public synchronized static ArrayList<Connect> getRejectedConnectsOf(String userId) throws SQLException, NotAcceptableException {
        ArrayList<Connect> acceptedConnects ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE (applicant_id = ? OR acceptor_id = ?) AND accept_state = ?");
        statement.setString(1, userId);
        statement.setString(2, userId);
        statement.setString(3, Connect.AcceptState.REJECTED.toString());
        acceptedConnects = getConnectListFromStatement(statement);
        statement.close();
        return acceptedConnects;

    }

    public synchronized static ArrayList<Connect> getWatingConnectsOf(String userId) throws SQLException, NotAcceptableException {
        ArrayList<Connect> acceptedConnects ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE acceptor_id = ? AND accept_state = ?");
        statement.setString(1, userId);
        statement.setString(2, Connect.AcceptState.WAITING.toString());
        acceptedConnects = getConnectListFromStatement(statement);
        statement.close();
        return acceptedConnects;

    }

//    public synchronized static Connect getWatingConnect(String user1Id , String user2Id ) throws SQLException , NotAcceptableException {
//        Connect connect = null;
//        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE ((acceptor_id = ? AND connects.applicant_id = ?) || (applicant_id = ? AND acceptor_id = ?)) AND accept_state = ?");
//        statement.setString(1, user1Id);
//        statement.setString(2, user2Id);
//        statement.setString(3, user1Id);
//        statement.setString(4, user2Id);
//        statement.setString(5, Connect.AcceptState.WAITING.toString());
//        ResultSet resultSet = statement.executeQuery();
//        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
//        if(jsonObject != null && !jsonObject.isEmpty()){
//            connect = new Connect(jsonObject);
//        }
//        statement.close();
//        return connect;
//    }

    public synchronized static Connect getWaitingConnectOfAcceptor(String applicantId , String acceptorId ) throws SQLException , NotAcceptableException {
        Connect connect = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE applicant_id = ? AND acceptor_id = ? AND accept_state = ?");
        statement.setString(1, applicantId);
        statement.setString(2, acceptorId);
        statement.setString(3, Connect.AcceptState.WAITING.toString());
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
//        System.out.println(jsonObject);
        if(jsonObject != null && !jsonObject.isEmpty()){
            System.out.println("hey 1.75");
            connect = new Connect(jsonObject);
            System.out.println("hey 1.8");
        }
        statement.close();
        return connect;
    }

    public synchronized static Connect getAcceptedConnect(String user1Id , String user2Id ) throws SQLException , NotAcceptableException {
        Connect connect = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM connects WHERE ((acceptor_id = ? AND connects.applicant_id = ?) || (applicant_id = ? AND acceptor_id = ?)) AND accept_state = ?");
        statement.setString(1, user1Id);
        statement.setString(2, user2Id);
        statement.setString(3, user1Id);
        statement.setString(4, user2Id);
        statement.setString(5, Connect.AcceptState.ACCEPTED.toString());
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject = JsonHandler.getFromResultSet(resultSet);
        if(jsonObject != null && !jsonObject.isEmpty()){
            connect = new Connect(jsonObject);
        }
        statement.close();
        return connect;
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
