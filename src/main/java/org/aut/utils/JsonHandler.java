package org.aut.utils;

import org.json.JSONObject;
import java.io.*;

public class JsonHandler {
    private JsonHandler() {
    }

    public static JSONObject getJsonObject(InputStream body) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(body));
        StringBuilder res = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            res.append(line);
        }
        body.close();
        return new JSONObject(res.toString());
    }

    public static void sendJsonObject(OutputStream body, JSONObject obj) throws IOException {
        body.write(obj.toString().getBytes());
        body.flush();
        body.close();
    }
}
