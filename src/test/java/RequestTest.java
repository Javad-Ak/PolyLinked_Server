import org.aut.models.Profile;
import org.aut.utils.MultipartHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

import org.aut.models.User;
import org.aut.utils.JsonHandler;


@DisplayName("------ Testing requests...")
public class RequestTest {
    @Test
    @DisplayName("---- posting a profile")
    public void postProfile() throws Exception {
        Profile profile = new Profile("user719066ad-4efe-8f14", "ddd", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, 1);
        File pic = new File("./in/prof1.jpg");
        File bg = new File("./in/prof2.png");

        HttpURLConnection con = getPostConnection(URI.create("http://localhost:8080/profiles").toURL());
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzE5MDY2YWQtNGVmZS04ZjE0IiwiaWF0IjoxNzE3MDc5MzI3LCJleHAiOjE3MTc2NzkzMjd9.Wf5S2mrgrofUvs8GZeXRH31X8WcKq0ozvfzi1_mTeEY");

        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        MultipartHandler.writeJson(out, profile);
        MultipartHandler.writeFromFile(out, pic);
        MultipartHandler.writeFromFile(out, bg);
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("test result: " + con.getResponseCode());
        } else {
            System.out.println("Server returned HTTP code " + con.getResponseCode());
        }
        con.disconnect();
    }

    @Test
    @DisplayName("---- posting a profile")
    public void putProfile() throws Exception {
        Profile profile = new Profile("user719066ad-4efe-8f14", "ddd", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, 1);
        File bg = new File("./in/prof1.jpg");
        File pic = new File("./in/prof2.png");

        HttpURLConnection con = getPostConnection(URI.create("http://localhost:8080/profiles").toURL());
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzE5MDY2YWQtNGVmZS04ZjE0IiwiaWF0IjoxNzE3MDc5MzI3LCJleHAiOjE3MTc2NzkzMjd9.Wf5S2mrgrofUvs8GZeXRH31X8WcKq0ozvfzi1_mTeEY");

        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        MultipartHandler.writeJson(out, profile);
        MultipartHandler.writeFromFile(out, pic);
        MultipartHandler.writeFromFile(out, bg);
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("test result: " + con.getResponseCode());
        } else {
            System.out.println("Server returned HTTP code " + con.getResponseCode());
        }
        con.disconnect();
    }

    @Test
    @DisplayName("---- posting a profile")
    public void getProfile() throws Exception {
        Path pic = Path.of("./out/prof1");
        Path bg = Path.of("./out/prof2");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/profiles/user719066ad-4efe-8f14"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "multipart/form-data")
                .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzE5MDY2YWQtNGVmZS04ZjE0IiwiaWF0IjoxNzE3MDc5MzI3LCJleHAiOjE3MTc2NzkzMjd9.Wf5S2mrgrofUvs8GZeXRH31X8WcKq0ozvfzi1_mTeEY")
                .GET()
                .build();

        // Send the request and get the response
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() / 100 == 2) {
            InputStream inputStream = response.body();
//            System.out.println(new String(inputStream.readAllBytes()));

            Profile profile = new Profile(MultipartHandler.readJson(inputStream, Profile.class));
            System.out.println(profile);

            MultipartHandler.readToFile(inputStream, pic);
            MultipartHandler.readToFile(inputStream, bg);
            inputStream.close();

            System.out.println("test result: " + response.statusCode());
        } else {
            System.out.println("Server returned HTTP code " + response.statusCode());
        }
        client.close();

//        HttpURLConnection con = getPostConnection(URI.create("http://localhost:8080/profiles/user719066ad-4efe-8f14").toURL());
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Content-Type", "multipart/form-data");
//        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzE5MDY2YWQtNGVmZS04ZjE0IiwiaWF0IjoxNzE3MDc5MzI3LCJleHAiOjE3MTc2NzkzMjd9.Wf5S2mrgrofUvs8GZeXRH31X8WcKq0ozvfzi1_mTeEY");
//
//        InputStream inputStream = con.getInputStream();
//        if (con.getResponseCode() / 100 == 2) {
//            System.out.println(new String(inputStream.readAllBytes()));
//
////            Profile profile = new Profile(MultipartHandler.readJson(inputStream, Profile.class));
////            System.out.println(profile);
////
////            MultipartHandler.readToFile(inputStream, pic);
////            MultipartHandler.readToFile(inputStream, bg);
//            inputStream.close();
//
//            System.out.println("test result: " + con.getResponseCode());
//        } else {
//            System.out.println("Server returned HTTP code " + con.getResponseCode());
//        }
//        con.disconnect();
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
        con.setRequestProperty("Content-Type", "json"); // not needed

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
                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject("{email: reza@gmail.com, password: ali1222345}").toString()))
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
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjM3NTBkMTQtNDdjMC1iMjIwIiwiaWF0IjoxNzE2OTg1NjA2LCJleHAiOjE3MTc1ODU2MDZ9.ej7LwjDK9sO_9XXPJk5HxH6tSrw_2enANFDosy6yOr8 eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjM3NTBkMTQtNDdjMC1iMjIwIiwiaWF0IjoxNzE2OTg1NjA2LCJleHAiOjE3MTc1ODU2MDZ9.ej7LwjDK9sO_9XXPJk5HxH6tSrw_2enANFDosy6yOr8");

        con.setDoOutput(true); // enables output stream
        return con;
    }
}
