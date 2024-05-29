import org.aut.models.Profile;
import org.aut.utils.RequestHandler;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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
    public void postProfile() throws Exception {
        Profile profile = new Profile("user719066ad-4efe-8f14", "aaa", "bbb", "ccc", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, true);
        File pic = new File("src/test/resources/profile.jpg");
        File bg = new File("src/test/resources/profile.jpg");


        URL url = URI.create("http://localhost:8080/profiles").toURL();
        HttpURLConnection con = getPostConnection(url);
        OutputStream out = con.getOutputStream();

//        RequestHandler.addJson(out, profile.toJson());
//        RequestHandler.addFile(out, pic);
//        RequestHandler.addFile(out, bg);
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("test result: " + con.getResponseCode());
        } else {
            System.out.println("Server returned HTTP code " + con.getResponseCode());
        }
        con.disconnect();
    }

    @Test
    @DisplayName("---- adding a user with httpClient")
    public void postUser_Java11() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new User("kasra@gmail.com", "ali7771222345", "Ali", "akbari", "ll").toString()))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response headers: " + response.headers().toString());

        client.close();
    }

    @Test
    @DisplayName("---- adding a user with URL")
    public void postUser_Java8() throws Exception {
        URL url = URI.create("http://localhost:8080/users").toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "mu"); // not needed

        con.setDoInput(true); // enables input stream, no need
        con.setDoOutput(true); // enables output stream
        OutputStream out = con.getOutputStream();

        JsonHandler.sendObject(con.getOutputStream(), new User("ali@gmail.com", "ali1222345", "Ali", "akbari", "k").toJson());
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("test result: " + con.getResponseCode());
        } else {
            System.out.println("Server returned HTTP code " + con.getResponseCode());
        }
        con.disconnect();
    }

    @Test
    @DisplayName("---- adding a user with httpClient")
    public void login() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        // Create a new HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/login"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "json")
                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject("{email: kasra@gmail.com, password:ali7771222345}").toString()))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() / 100 == 2) {
            System.out.println("test result: " + response.body());
        } else {
            System.out.println("Server returned HTTP code " + response.statusCode());
        }
        client.close();
    }

    private static HttpURLConnection getPostConnection(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjM3NTBkMTQtNDdjMC1iMjIwIiwiaWF0IjoxNzE2OTg1NjA2LCJleHAiOjE3MTc1ODU2MDZ9.ej7LwjDK9sO_9XXPJk5HxH6tSrw_2enANFDosy6yOr8\n" +
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjM3NTBkMTQtNDdjMC1iMjIwIiwiaWF0IjoxNzE2OTg1NjA2LCJleHAiOjE3MTc1ODU2MDZ9.ej7LwjDK9sO_9XXPJk5HxH6tSrw_2enANFDosy6yOr8");

        con.setDoOutput(true); // enables output stream
        return con;
    }
}
