import org.aut.controllers.FollowController;
import org.aut.dataAccessors.DataBaseConnection;
import org.aut.dataAccessors.FollowAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Follow;
import org.aut.models.User;
import org.aut.utils.exceptions.PermissionDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@DisplayName("------ testing DataBase...")
public class DBTest {
    @Test
    @DisplayName("---- adding a user")
    public void addUser() throws Exception {
        DataBaseConnection.create();
        UserAccessor.addUser(new User("ali@gmail.com" , "ali1222345" , "Ali", "akbari"));
        System.out.println("User added successfully");
    }
    @Test
    @DisplayName("---- adding a follow")
    public void addFollow() throws Exception {
        DataBaseConnection.create();
        User user1 , user2;
        UserAccessor.addUser(user1 = new User("ali1@gmail.com" , "ali1222345" , "Ali", "akbari"));
        UserAccessor.addUser(user2 = new User("ali2@gmail.com" , "ali1222345" , "Alireza", "athari"));
        FollowController.addFollow(new Follow (user1.getId() , user2.getId()));
        try{
            FollowController.addFollow(new Follow (user1.getId() , user2.getId()));
        } catch (PermissionDeniedException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Follow added successfully");
    }
}
