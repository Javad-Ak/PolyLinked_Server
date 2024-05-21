import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.*;

import org.aut.models.User;
import org.aut.utils.JsonHandler;

@DisplayName("------ testing users...")
public class UserTest {
    @Test
    @DisplayName("---- adding a user")
    public void testcase1() {
        try {
            URL url = new URL("http://localhost:8080/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "json"); // not needed

            con.setDoInput(true); // enables input stream, no need
            con.setDoOutput(true); // enables output stream
            JsonHandler.sendJsonObject(con.getOutputStream(), new User("ali@gmail.com" , "ali12345" , "Ali", "akbari").toJSON());

            if (con.getResponseCode() / 100 == 2) {
                System.out.println("test1 result: " + JsonHandler.getJsonObject(con.getInputStream()));
            } else {
                System.out.println("Server returned HTTP code " + con.getResponseCode());
            }
            con.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AssertionError(e);
        }
    }
}
