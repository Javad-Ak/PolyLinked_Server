import org.aut.controllers.ConnectController;
import org.aut.controllers.FollowController;
import org.aut.controllers.UserController;
import org.aut.dataAccessors.ProfileAccessor;
import org.aut.dataAccessors.DataBaseAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Connect;
import org.aut.models.Follow;
import org.aut.models.Profile;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

@DisplayName("------ testing DataBase...")
public class DBTest {
    @Test
    @DisplayName("---- testing users table")
    public void UserTest() throws Exception {
        DataBaseAccessor.create();
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
        DataBaseAccessor.create();
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
        DataBaseAccessor.create();

        User user1 = new User("reza@gmail.com", "ali1222345", "Ali", "akbari", "ll");
        User user2 = new User("kasra@gmail.com", "ali1222345", "Ali", "akbari", "ll");
        try {
            UserAccessor.addUser(user1);
            UserAccessor.addUser(user2);
        } catch (SQLException e) {
            System.out.println("User exists" + e.getMessage());
        }

        Profile prof = new Profile(user1.getUserId(), "aaa", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, 1);
        ProfileAccessor.addProfile(prof);
        System.out.println("profile added successfully");

        ProfileAccessor.updateProfile(new Profile(user1.getUserId(), "updated", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, 1));
        System.out.println("profile updated successfully");

        Profile p2 = ProfileAccessor.getProfile("user9227641a-49d4-ac0c");
        System.out.println("profile fetched success fully: " + p2);
    }

    @Test
    @DisplayName("---- testing connect table")
    public void ConnectTest() throws Exception {
        DataBaseAccessor.create();
        try {
            ConnectController.addConnect(new Connect("user90059c8e-4be7-8764", "user241374d1-464e-863e" , "this is connect note" , Connect.AcceptState.WAITING));
        } catch (NotAcceptableException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Connect added successfully");
    }
}
