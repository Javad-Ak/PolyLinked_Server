import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.net.*;

import org.aut.models.User;
import org.aut.utils.JsonHandler;

public class UnitTest {
    @Test
    @DisplayName("----test 1 : Adding a user")
    public void testcase1() {
        try {
            URL url = new URL("http://localhost:8080/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json"); // not needed

            con.setDoInput(true); // enables input stream, no need
            con.setDoOutput(true); // enables output stream
            JsonHandler.sendJsonObject(con.getOutputStream(), new User("Ali", "a1234").toJSON());

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
