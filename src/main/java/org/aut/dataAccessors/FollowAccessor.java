package org.aut.dataAccessors;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

public class FollowAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private FollowAccessor(){
    }
    public static void createFollowsTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS follows (" +
                    "follower_id TEXT NOT NULL" +
                    ", followed_id TEXT NOT NULL);");

        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addFollow (Follow follow)throws SQLException{
        PreparedStatement statement = connection.prepareStatement("INSERT INTO follows (follower_id , followed_id) " +
                "VALUES (?, ?);");
        statement.setNString(1 , follow.getFollower());
        statement.setNString(2 , follow.getFollowed());
        statement.executeUpdate();
        statement.close();
    }
    public synchronized static ArrayList<User> getFollowers(String id) throws SQLException{
        ArrayList<User> followers ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE followed_id = ? ORDER BY follower_id DESC;");
        statement.setNString(1 ,id);
       followers = getUserListFromStatement(statement);
        statement.close();
        return followers;
    }
    public synchronized static ArrayList<User> getFollowings(String id) throws SQLException{
        ArrayList<User> followings;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE follower_id = ? ORDER BY followed_id DESC;");
        statement.setNString(1 ,id);
       followings = getUserListFromStatement(statement);
        statement.close();
        return followings;
    }

    public static ArrayList<User> getUserListFromStatement (PreparedStatement statement) throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject ;
        while ((jsonObject = JsonHandler.getFromResultSet(resultSet)) != null ){
            users.add(new User(jsonObject));
        }
        resultSet.close();
        return users;
    }
}
