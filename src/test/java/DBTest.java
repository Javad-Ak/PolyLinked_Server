import org.aut.dataAccessors.DataBaseConnection;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@DisplayName("------ testing requests...")
public class DBTest {
    @Test
    @DisplayName("---- adding a user")
    public void addUser() throws Exception {
        DataBaseConnection.create();
        UserAccessor.addUser(new User("ali@gmail.com" , "ali1222345" , "Ali", "akbari"));
        System.out.println("User added successfully");
    }
}
