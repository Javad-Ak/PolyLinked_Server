package org.aut.utils;

import io.jsonwebtoken.lang.Classes;
import org.aut.models.JsonSerializable;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class RequestHandler {
    private RequestHandler() {
    }

    public static <T extends JsonSerializable> void writeJson(OutputStream outputStream, T obj) throws IOException {
        writeHeaders(outputStream, obj.getClass().getSimpleName() + "/json", obj.toJson().toString().getBytes().length);
        outputStream.write(obj.toJson().toString().getBytes());
        outputStream.flush();
    }

    public static void writeFile(OutputStream outputStream, File file) throws IOException {
        writeHeaders(outputStream, file.getName() + "/file", (int) file.length());

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[1000000];

            while (fis.read(bytes) != -1)
                outputStream.write(bytes);
        }
    }

    private static void writeHeaders(OutputStream outputStream, String type, int length) throws IOException {
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", type);
        headers.put("Content-Length", length);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write("--" + headers + "--\r\n");
        writer.flush();
//        writer.close();
    }

    public static File readFile(InputStream inputStream, String path) throws IOException, NotAcceptableException {
        JSONObject headers = readHeaders(inputStream);
        String[] type = headers.getString("Content-Type").split("/");
        int length = headers.getInt("Content-Length");

        if (!type[1].equals("file")) throw new NotAcceptableException("Invalid Content-Type");

        int dotIndex = type[0].lastIndexOf('.');
        File file = new File(path + type[0].substring(dotIndex + 1));

        FileOutputStream fos = new FileOutputStream(file);
    }

    public static <T extends JsonSerializable> JSONObject readJson(InputStream inputStream, Class<T> cls) throws IOException, NotAcceptableException {
        JSONObject headers = readHeaders(inputStream);
        String[] type = headers.getString("Content-Type").split("/");
        int length = headers.getInt("Content-Length");

        if (!type[1].equals("json") || !cls.getSimpleName().equals(type[0]))
            throw new NotAcceptableException("Invalid Content-Type");

        byte[] bytes = new byte[length];
        if (inputStream.read(bytes) < length) return null;

        return new JSONObject(bytes);
    }

    private static JSONObject readHeaders(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        if (line == null || !line.trim().startsWith("--") || !line.trim().endsWith("--"))
            throw new IOException("Unexpected end of stream");

        return new JSONObject(line);
    }
}
