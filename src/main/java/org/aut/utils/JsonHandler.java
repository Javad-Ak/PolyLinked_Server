package org.aut.utils;

import org.json.JSONObject;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonHandler {
    private JsonHandler() {
    }

    public static JSONObject getObject(InputStream inp) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
        StringBuilder res = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null)
            res.append(line);

        reader.close();
        return new JSONObject(res.toString());
    }

    public static void sendObject(OutputStream out, JSONObject obj) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(obj.toString());
        writer.flush();
        writer.close();
    }

    public static JSONObject getFromResultSet(ResultSet set) throws SQLException {
        JSONObject jsonObject = new JSONObject();
        if (set.next()) {
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++)
                jsonObject.put(set.getMetaData().getColumnName(i), set.getObject(i));//expected bug
        }
        return jsonObject.isEmpty() ? null : jsonObject;
    }
}
