import org.aut.controllers.FollowController;
import org.aut.dataAccessors.DataBaseConnection;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@DisplayName("------ testing DataBase...")
public class DBTest {
    @Test
    @DisplayName("---- adding a user")
    public void addUser() throws Exception {
        DataBaseConnection.create();
        try {
            UserAccessor.addUser(new User("reza@gmail.com", "ali1222345", "Ali", "akbari", "ll"));
        } catch (SQLException e) {
            System.out.println("User exists" + e.getMessage());
        }

        System.out.println("User added successfully");
    }

    @Test
    @DisplayName("---- adding a follow")
    public void addFollow() throws Exception {
        DataBaseConnection.create();
        User user1, user2;
        UserAccessor.addUser(user1 = new User("ali3@gmail.com", "ali1222345", "Ali", "akbari", ""));
        UserAccessor.addUser(user2 = new User("ali4@gmail.com", "ali1222345", "Alireza", "athari", ""));
        FollowController.addFollow(new Follow(user1.getUserId(), user2.getUserId()));
        try {
            FollowController.addFollow(new Follow(user1.getUserId(), user2.getUserId()));
        } catch (NotAcceptableException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Follow added successfully");
    }
}
