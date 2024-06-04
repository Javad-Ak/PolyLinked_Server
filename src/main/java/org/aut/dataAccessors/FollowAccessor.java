package org.aut.dataAccessors;

import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.JsonHandler;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class FollowAccessor {
    private static final Connection connection = DataBaseAccessor.getConnection();

    private FollowAccessor() {
    }

    static void createFollowsTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS follows (" +
                    "follower_id TEXT NOT NULL" +
                    ", followed_id TEXT NOT NULL" +
                    ", FOREIGN KEY (follower_id, followed_id)" +
                    " REFERENCES users (userId, userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");

        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }


    public synchronized static void addFollow(Follow follow) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO follows (follower_id , followed_id) " +
                "VALUES (?, ?);");
        statement.setString(1, follow.getFollower_id());
        statement.setString(2, follow.getFollowed_id());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void deleteFollow(Follow follow) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM follows WHERE follower_id = ? AND followed_id = ?");
        statement.setString(1, follow.getFollowed_id());
        statement.setString(2, follow.getFollowed_id());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized static ArrayList<User> getFollowers(String id) throws SQLException, NotAcceptableException , NotFoundException {
        ArrayList<Follow> follows;
        ArrayList<User> followers = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE followed_id = ?");
        statement.setString(1, id);
        follows = getFollowsListFromStatement(statement);
        for (Follow follow : follows) {
            followers.add(UserAccessor.getUserById(follow.getFollower_id()));
        }
        statement.close();
        followers.sort(Comparator.comparing(User::getFirstName));
        return followers;
    }

    public synchronized static ArrayList<User> getFollowings(String id) throws SQLException, NotAcceptableException , NotFoundException {
        ArrayList<Follow> follows ;
        ArrayList<User> followings = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE follower_id = ? ");
        statement.setString(1, id);
        follows = getFollowsListFromStatement(statement);
        for (Follow follow : follows) {
            followings.add(UserAccessor.getUserById(follow.getFollowed_id()));
        }
        statement.close();
        followings.sort(Comparator.comparing(User::getFirstName));
        return followings;
    }

    public synchronized static boolean followExists(Follow follow) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE follower_id = ? AND followed_id = ?");
        statement.setString(1, follow.getFollower_id());
        statement.setString(2, follow.getFollowed_id());
        ResultSet resultSet = statement.executeQuery();
        boolean res = resultSet.next();
        statement.close();
        return res;
    }

    public synchronized static ArrayList<Follow> getFollowsListFromStatement(PreparedStatement statement) throws SQLException, NotAcceptableException {
        ArrayList<Follow> follows = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        JSONObject jsonObject;
        while ((jsonObject = JsonHandler.getFromResultSet(resultSet)) != null) {
            System.out.println(jsonObject);
            follows.add(new Follow(jsonObject));
        }
        resultSet.close();
        return follows;
    }
}
