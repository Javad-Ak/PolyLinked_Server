package org.aut.dataAccessors;

import org.aut.models.Connect;
import org.aut.models.Message;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

public class MessageAccessor {
    private static final Connection connection = DataBaseAccessor.getConnection();
    private MessageAccessor(){
    }

    static void createMessageTable() throws IOException {
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS messages (" +
                    "id TEXT NOT NULL PRIMARY KEY" +
                    ", senderId TEXT NOT NULL" +
                    ", receiverId TEXT NOT NULL" +
                    ", text TEXT NOT NULL" +
                    ", createDate BIGINT(20)" +
                    ", FOREIGN KEY (senderId , receiverId)" +
                    "REFERENCES users (userId , userId)" +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE " +
                    ");");
        } catch ( Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addMessage(Message message ) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (id , senderId , receiverId , text , createDate) " +
                "VALUES (?, ?, ? , ? ,?)");
        statement.setString( 1, message.getId() );
        statement.setString( 2, message.getSenderId());
        statement.setString(3 , message.getReceiverId());
        statement.setString(4 , message.getText());
        statement.setLong(5 , message.getCreateDate().getTime());
        statement.executeUpdate();
        statement.close();
    }
    public synchronized static void deleteMessage(String messageId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        statement.setString(1, messageId);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static boolean messageExists (String messageId) throws SQLException , NotAcceptableException , NotFoundException {
        Message message = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE id = ?");
        statement.setString(1, messageId);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }


    public synchronized static ArrayList<Message> getMessagesBetween(String user1 , String user2) throws SQLException, NotAcceptableException {
        ArrayList <Message> acceptedConnects ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE (senderId = ? AND receiverId = ?) OR (receiverId = ? AND senderId = ?) ORDER BY createDate DESC");
        statement.setString(1, user1);
        statement.setString(2, user2);
        statement.setString(3, user1);
        statement.setString(4, user2);
        acceptedConnects = getMessageListFromStatement(statement);
        statement.close();
        return acceptedConnects;

    }

    public synchronized static ArrayList<Message> getMessageListFromStatement(PreparedStatement statement) throws SQLException, NotAcceptableException {
        ArrayList<Message> messages = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject;
        while ((jsonObject = JsonHandler.getFromResultSet(resultSet)) != null) {
            messages.add(new Message(jsonObject));
        }
        resultSet.close();
        return messages;
    }

}
