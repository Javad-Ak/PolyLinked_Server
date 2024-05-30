import org.aut.controllers.ConnectController;
import org.aut.controllers.FollowController;
import org.aut.controllers.UserController;
import org.aut.dataAccessors.DataBaseConnection;
import org.aut.dataAccessors.ProfileAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Connect;
import org.aut.models.Follow;
import org.aut.models.Profile;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@DisplayName("------ testing DataBase...")
public class DBTest {
    @Test
    @DisplayName("---- testing users table")
    public void UserTest() throws Exception {
        DataBaseConnection.create();
        String userId = null;
        try {
            User user = new User("reza@gmail.com", "ali1222345", "Ali", "akbari", "ll");
            UserAccessor.addUser(user);
            userId = user.getUserId();
            System.out.println("User added successfully");
        } catch (SQLException e) {
            System.out.println("User exists " + e.getMessage());
        }


        try{
            User user = UserAccessor.getUserById(userId != null ? userId : "user719066ad-4efe-8f14");
            UserAccessor.deleteUser(userId != null? userId : "user719066ad-4efe-8f14");
            System.out.println("User " + user.getEmail() +" deleted successfully");
        } catch (SQLException e){
            System.out.println("SQLException " + e.getMessage());
        } catch (NotFoundException e){
            System.out.println("User does not exist " + e.getMessage());
        }

        try {
            User user = UserAccessor.getUserByEmail("reza2@gmail.com");
            UserController.deleteUser(user);
            System.out.println("User " + user.getEmail()+ " deleted successfully");
        } catch ( SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (NotFoundException e) {
            System.out.println("User does not exist " + e.getMessage());
        }

        try {
            User newUser = new User("reza2@gmail.com", "ali1222345", "Ali", "akbari", "ll");

            UserAccessor.addUser(newUser);
            System.out.println("User added successfully");
            newUser.setFirstName("UpdatedAli");
            UserAccessor.updateUser(newUser);
            System.out.println("User " + newUser.getEmail() +" updated successfully");
        } catch (SQLException e){
            System.out.println("SQLException " + e.getMessage());
        }

    }

    @Test
    @DisplayName("---- testing follows table")
    public void FollowTest() throws Exception {
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

    @Test
    @DisplayName("---- testing profiles table")
    public void ProfileTest() throws Exception {
        DataBaseConnection.create();

        User user1 = new User("reza@gmail.com", "ali1222345", "Ali", "akbari", "ll");
        User user2 = new User("kasra@gmail.com", "ali1222345", "Ali", "akbari", "ll");
        try {
            UserAccessor.addUser(user1);
            UserAccessor.addUser(user2);
        } catch (SQLException e) {
            System.out.println("User exists" + e.getMessage());
        }

        ProfileAccessor.addProfile(new Profile(user1.getUserId(), "aaa", "bbb", "ccc", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, true));
        System.out.println("profile added successfully");

        ProfileAccessor.updateProfile(new Profile(user1.getUserId(), "updated", "bbb", "ccc", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, true));
        System.out.println("profile updated successfully");
    }

    @Test
    @DisplayName("---- testing connect table")
    public void ConnectTest() throws Exception {
        DataBaseConnection.create();

        User user1, user2;
        UserAccessor.addUser(user1 = new User("ali5@gmail.com", "ali1222345", "Ali", "akbari", ""));
        UserAccessor.addUser(user2 = new User("ali6@gmail.com", "ali1222345", "Alireza", "athari", ""));
        try {
            ConnectController.addConnect(new Connect(user1.getUserId(), user2.getUserId() , "this is connect note" , Connect.AcceptState.WAITING));
        } catch (NotAcceptableException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Connect added successfully");
    }
}
