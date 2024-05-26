import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.aut.models.User;
import org.aut.utils.JsonHandler;

@DisplayName("------ Testing requests...")
public class RequestTest {
    @Test
    @DisplayName("---- adding a user with httpClient")
    public void addUser_New() throws Exception { // Using Httpclient is encouraged.
        try (HttpClient client = HttpClient.newHttpClient()) {
            // Create a new HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/users"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "json")
                    .POST(HttpRequest.BodyPublishers.ofString(new User("ali@gmail.com", "ali1222345", "Ali", "akbari", "").toString()))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code
            System.out.println("Response Code: " + response.statusCode());
            // Print the response status code
            System.out.println("Response headers: " + response.headers().toString());
            // Print the response body
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Test
    @DisplayName("---- adding a user with URL")
    public void addUser_Old() throws Exception {
        URL url = new URL("http://localhost:8080/users");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "json"); // not needed

        con.setDoInput(true); // enables input stream, no need
        con.setDoOutput(true); // enables output stream
        OutputStream out = con.getOutputStream();

        JsonHandler.sendObject(con.getOutputStream(), new User("ali@gmail.com", "ali1222345", "Ali", "akbari", "k").toJSON());
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("test result: " + JsonHandler.getObject(con.getInputStream()));
        } else {
            System.out.println("Server returned HTTP code " + con.getResponseCode() + JsonHandler.getObject(con.getInputStream()));
        }
        con.disconnect();
    }

    @Test
    @DisplayName("---- adding a user with httpClient")
    public void login() throws Exception { // Using Httpclient is encouraged.
        try (HttpClient client = HttpClient.newHttpClient()) {
            // Create a new HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/users/login"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "json")
                    .POST(HttpRequest.BodyPublishers.ofString(new JSONObject("{email: ali@gmail.com, password:ali1222345}").toString()))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code
            System.out.println("Response Code: " + response.statusCode());
            // Print the response status code
            System.out.println("Response headers: " + response.headers().toString());
            // Print the response body
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
