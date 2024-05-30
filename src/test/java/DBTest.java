import org.aut.controllers.FollowController;
import org.aut.dataAccessors.DataBaseAccessor;
import org.aut.dataAccessors.ProfileAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Follow;
import org.aut.models.Profile;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@DisplayName("------ testing DataBase...")
public class DBTest {
    @Test
    @DisplayName("---- testing users table")
    public void UserTest() throws Exception {
        DataBaseAccessor.create();
        try {
            UserAccessor.addUser(new User("reza@gmail.com", "ali1222345", "Ali", "akbari", "ll"));
        } catch (SQLException e) {
            System.out.println("User exists" + e.getMessage());
        }

        System.out.println("User added successfully");
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

        Profile prof = new Profile(user1.getUserId(), "aaa", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, true);
        ProfileAccessor.addProfile(prof);
        System.out.println("profile added successfully");

        ProfileAccessor.updateProfile(new Profile(user1.getUserId(), "updated", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, true));
        System.out.println("profile updated successfully");

        Profile p2 = ProfileAccessor.getProfile("user9227641a-49d4-ac0c");
        System.out.println("profile fetched success fully: " + p2);
    }
}
