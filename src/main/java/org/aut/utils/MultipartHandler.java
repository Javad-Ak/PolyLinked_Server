package org.aut.utils;

import org.aut.models.JsonSerializable;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class MultipartHandler {
    private MultipartHandler() {
    }

    public static <T extends JsonSerializable> void writeJson(OutputStream outputStream, T obj) throws IOException, NotAcceptableException {
        writeHeaders(outputStream, obj.getClass().getSimpleName() + "/json", obj.toJson().toString().getBytes().length);
        outputStream.write(obj.toJson().toString().getBytes());
        outputStream.flush();
    }

    public static void writeFromFile(OutputStream outputStream, File file) throws IOException, NotAcceptableException {
        int length = (int) file.length();
        writeHeaders(outputStream, file.getName() + "/file", length);
        if (length == 0) return;

        FileInputStream inputStream = new FileInputStream(file);
        int totalWrite = 0;
        byte[] buffer = new byte[100000];
        while (totalWrite < file.length()) {
            int read = inputStream.read(buffer);
            if (read == -1) break;

            outputStream.write(buffer, 0, read);
            totalWrite += read;
        }

        outputStream.flush();
        inputStream.close();
    }

    public static <T extends JsonSerializable> void writeMap(OutputStream outputStream, HashMap<T, File> map) throws NotAcceptableException, IOException {
        for (T obj : map.keySet()) {
            writeJson(outputStream, obj);
            writeFromFile(outputStream, map.get(obj));
        }
    }

    private static void writeHeaders(OutputStream outputStream, String type, int length) throws IOException, NotAcceptableException {
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", type);
        headers.put("Content-Length", length);

        outputStream.write(headers.toString().getBytes());
        outputStream.flush();
    }

    public static File readToFile(InputStream inputStream, Path path) throws IOException, NotAcceptableException {
        // path = directory + name without type
        JSONObject headers = getJson(inputStream);
        String[] type = headers.getString("Content-Type").split("/");
        int length = headers.getInt("Content-Length");
        if (length == 0) return new File("null");
        if (!type[1].equals("file")) throw new NotAcceptableException("Invalid Content-Type");

        File file = new File(path + type[0].substring(type[0].lastIndexOf('.')));
        FileOutputStream outputStream = new FileOutputStream(file);

        int remained = length;
        byte[] buffer = new byte[100000];
        while (remained > 0) {
            if (remained < 110000) {
                byte[] bytes = new byte[remained];
                int read = inputStream.read(bytes);
                if (read == -1) break;

                outputStream.write(bytes, 0, read);
                remained -= read;
            } else {
                int read = inputStream.read(buffer);
                if (read == -1) break;

                outputStream.write(buffer, 0, read);
                remained -= read;
            }
        }
        outputStream.flush();
        outputStream.close();
        return file;
    }

    public static <T extends JsonSerializable> JSONObject readJson(InputStream inputStream, Class<T> cls) throws IOException, NotAcceptableException {
        JSONObject headers = getJson(inputStream);
        String[] type = headers.getString("Content-Type").split("/");
        if (type.length < 1 || (!type[1].equals("json") || !cls.getSimpleName().equals(type[0])))
            throw new NotAcceptableException("Invalid Content-Type");

        return getJson(inputStream);
    }

    public static <T extends JsonSerializable> HashMap<JSONObject, File> readMap(InputStream inputStream, ArrayList<Path> paths, Class<T> cls) throws NotAcceptableException, IOException {
        HashMap<JSONObject, File> map = new HashMap<>();
        for (Path path : paths) {
            map.put(readJson(inputStream, cls), readToFile(inputStream, path));
            cls.
        }
        return map;
    }

    private static JSONObject getJson(InputStream inputStream) throws IOException, NotAcceptableException {
        StringBuilder res = new StringBuilder();
        int ch;
        while ((ch = inputStream.read()) != -1) {
            res.append((char) ch);
            if ((char) ch == '}') break;
        }
        if (res.isEmpty() || res.charAt(0) != '{' || res.charAt(res.length() - 1) != '}') {
            throw new NotAcceptableException("Invalid headers");
        }

        return new JSONObject(res.toString());
    }
}
