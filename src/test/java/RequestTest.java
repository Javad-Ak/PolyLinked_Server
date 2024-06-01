import org.aut.dataAccessors.MediaAccessor;
import org.aut.models.Message;
import org.aut.models.Post;
import org.aut.models.Profile;
import org.aut.utils.MultipartHandler;
import org.aut.dataAccessors.UserAccessor;
import org.aut.utils.exceptions.NotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import org.aut.models.User;
import org.aut.utils.JsonHandler;

@DisplayName("------ Testing requests...")
public class RequestTest {
    @Test
    @DisplayName("---- post")
    public void PostTest() throws Exception {
        // ##### POST
        Post post = new Post("user75930fcf-4bc1-9675", "ddd");
        File pic = new File("./in/prof1.jpg");

        HttpURLConnection con = (HttpURLConnection) URI.create("http://localhost:8080/posts").toURL().openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzU5MzBmY2YtNGJjMS05Njc1IiwiaWF0IjoxNzE3MTYzNzYzLCJleHAiOjE3MTc3NjM3NjN9.o58V5oeLY-CDuSm3ZmQMNDNXo8WVVfXyQUL_hiedt7s");

        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        MultipartHandler.writeJson(out, post);
        MultipartHandler.writeFromFile(out, pic);
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("test result: " + con.getResponseCode());
        } else {
            System.out.println("Server returned HTTP code " + con.getResponseCode());
        }
        con.disconnect();

        // ##### GET

        Path media = Path.of("./out/prof1");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + post.getPostId()))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzU5MzBmY2YtNGJjMS05Njc1IiwiaWF0IjoxNzE3MTYzNzYzLCJleHAiOjE3MTc3NjM3NjN9.o58V5oeLY-CDuSm3ZmQMNDNXo8WVVfXyQUL_hiedt7s")
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() / 100 == 2) {
            InputStream inputStream = response.body();
//            System.out.println(new String(inputStream.readAllBytes()));

            Post seeked = MultipartHandler.readJson(inputStream, Post.class);
            System.out.println(seeked);

            MultipartHandler.readToFile(inputStream, media);
            inputStream.close();
            System.out.println("test result: " + response.statusCode());
        } else {
            System.out.println("Server returned HTTP code " + response.statusCode());
        }
        client.close();

        // #### DELETE
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + post.getPostId()))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzU5MzBmY2YtNGJjMS05Njc1IiwiaWF0IjoxNzE3MTYzNzYzLCJleHAiOjE3MTc3NjM3NjN9.o58V5oeLY-CDuSm3ZmQMNDNXo8WVVfXyQUL_hiedt7s")
                .DELETE()
                .build();

        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        if (response2.statusCode() / 100 == 2) {
            System.out.println("test result: " + response2.statusCode());
        } else {
            System.out.println("Server returned HTTP code " + response2.statusCode());
        }
        client2.close();

    }


    @Test
    @DisplayName("---- message")
    public void MessageTest() throws Exception {
        // ##### POST
        Message message = new Message("user32734239-4c34-9d2f" , "user33374acb-40de-b57b", "jjj");
        File pic = new File("./in/prof2.png");

        HttpURLConnection con = (HttpURLConnection) URI.create("http://localhost:8080/messages" ).toURL().openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzI3MzQyMzktNGMzNC05ZDJmIiwiaWF0IjoxNzE3MjI3MjAzLCJleHAiOjE3MTc4MjcyMDN9.aaMsioZWmR81nQWLwL3gGBE_bE7e8e2iFgS-5U4PQDc");

        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        MultipartHandler.writeJson(out, message);
        MultipartHandler.writeFromFile(out, pic);
        out.close();

        if (con.getResponseCode() / 100 == 2) {
            System.out.println("POST test result: " + con.getResponseCode());
        } else {
            System.out.println("POST: Server returned HTTP code " + con.getResponseCode());
        }
        con.disconnect();

//         ##### GET
        ArrayList<Path> paths = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/" + "user32734239-4c34-9d2f" + "&" + "user33374acb-40de-b57b"))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzI3MzQyMzktNGMzNC05ZDJmIiwiaWF0IjoxNzE3MjI3MjAzLCJleHAiOjE3MTc4MjcyMDN9.aaMsioZWmR81nQWLwL3gGBE_bE7e8e2iFgS-5U4PQDc")
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() / 100 == 2) {
            InputStream inputStream = response.body();
            HashMap <Message , File > fullMessages ;
            int count = Integer.parseInt(response.headers().map().get("X-Total-Count").getFirst());
            System.out.println(count);
            fullMessages = MultipartHandler.readMap(inputStream , Path.of("./out") , Message.class , count );
            System.out.println(fullMessages);
//            System.out.println(new String(inputStream.readAllBytes()));


            inputStream.close();
//            System.out.println(new String(inputStream.readAllBytes()));
//            MultipartHandler.readMap(inputStream , )

            System.out.println("GET test result: " + response.statusCode());
        } else {
            System.out.println("GET: Server returned HTTP code " + response.statusCode());
        }
        client.close();

        // #### DELETE
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/" + message.getId()))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzI3MzQyMzktNGMzNC05ZDJmIiwiaWF0IjoxNzE3MjI3MjAzLCJleHAiOjE3MTc4MjcyMDN9.aaMsioZWmR81nQWLwL3gGBE_bE7e8e2iFgS-5U4PQDc")
                .DELETE()
                .build();

        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        if (response2.statusCode() / 100 == 2) {
            System.out.println("DELETE test result: " + response2.statusCode());
        } else {
            System.out.println("DELETE: Server returned HTTP code " + response2.statusCode());
        }
        client2.close();

    }

    @Test
    @DisplayName("---- get a user")
    public void getUser() throws Exception {
        Path pic = Path.of("./out/prof1");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/user75930fcf-4bc1-9675"))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzU5MzBmY2YtNGJjMS05Njc1IiwiaWF0IjoxNzE3MTYzNzYzLCJleHAiOjE3MTc3NjM3NjN9.o58V5oeLY-CDuSm3ZmQMNDNXo8WVVfXyQUL_hiedt7s")
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() / 100 == 2) {
            InputStream inputStream = response.body();
//            System.out.println(new String(inputStream.readAllBytes()));

            User user = MultipartHandler.readJson(inputStream, User.class);
            System.out.println(user);

            MultipartHandler.readToFile(inputStream, pic);
            inputStream.close();
            System.out.println("test result: " + response.statusCode());
        } else {
            System.out.println("Server returned HTTP code " + response.statusCode());
        }
        client.close();
    }

    @Test
    @DisplayName("---- posting a profile")
    public void postProfile() throws Exception {
        Profile profile = new Profile("user75930fcf-4bc1-9675", "ddd", "ddd", "jjj", Profile.Status.JOB_SEARCHER, Profile.Profession.ACTOR, 1);
        File pic = new File("./in/prof1.jpg");
        File bg = new File("./in/prof2.png");

        HttpURLConnection con = (HttpURLConnection) URI.create("http://localhost:8080/profiles").toURL().openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        con.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzU5MzBmY2YtNGJjMS05Njc1IiwiaWF0IjoxNzE3MTU1MDYzLCJleHAiOjE3MTc3NTUwNjN9.0RT8KOkyiSx99dVcMXO5K0cmqzfFua8wwYLIyVM67p8");

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

        HttpURLConnection con = (HttpURLConnection) URI.create("http://localhost:8080/profiles").toURL().openConnection();
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

            Profile profile = MultipartHandler.readJson(inputStream, Profile.class);
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
                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject("{email: ali@gmail.com, password: ali1222345}").toString()))
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

    @Test
    @DisplayName("---- update a user with httpClient")
    public void updateUser() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            User user = UserAccessor.getUserById("user769286ee-4627-ad72");
            user.setLastName("UpdatedAkbari");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/users"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNzY5Mjg2ZWUtNDYyNy1hZDcyIiwiaWF0IjoxNzE" +
                            "3MDExMTQyLCJleHAiOjE3MTc2MTExNDJ9.TaYK4qpCR6AKPDjma1IdOXVBVdV-MhfHbL10GkKubVg")
                    .PUT(HttpRequest.BodyPublishers.ofString(user.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response.statusCode());
            client.close();
        } catch (NotFoundException e) {
            System.out.println("User not found.");
        }
    }
}
