import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import org.aut.models.User;
import org.aut.utils.JsonHandler;

@DisplayName("------ testing requests...")
public class RequestTest {
    @Test
    @DisplayName("---- adding a user")
    public void addUser() {
        try {
            URL url = new URL("http://localhost:8080/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "json"); // not needed

            con.setDoInput(true); // enables input stream, no need
            con.setDoOutput(true); // enables output stream
            OutputStream out = con.getOutputStream();

            JsonHandler.sendObject(con.getOutputStream(), new User("ali@gmail.com", "ali1222345", "Ali", "akbari").toJSON());
            out.close();

            if (con.getResponseCode() / 100 == 2) {
                System.out.println("test result: " + JsonHandler.getObject(con.getInputStream()));
            } else {
                System.out.println("Server returned HTTP code " + con.getResponseCode() + JsonHandler.getObject(con.getInputStream()));
            }
            con.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AssertionError(e);
        }
    }
}
