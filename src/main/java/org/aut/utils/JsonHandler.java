package org.aut.utils;

import org.aut.models.User;
import org.json.JSONObject;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static JSONObject getFromResultSet(ResultSet set) throws SQLException {
        JSONObject jsonObject = new JSONObject();
        if (set.next()) {
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++)
                jsonObject.put(set.getMetaData().getColumnName(i), set.getObject(i));//expected bug
        }
        set.close();
        return jsonObject.isEmpty() ? null : jsonObject;
    }
}
