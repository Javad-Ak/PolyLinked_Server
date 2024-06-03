package org.aut.utils;

import org.aut.models.JsonSerializable;
import org.aut.models.MediaLinked;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MultipartHandler {
    private MultipartHandler() {
    }

    public static <T extends JsonSerializable> void writeJson(OutputStream outputStream, T obj) throws IOException, NotAcceptableException {
        writeHeaders(outputStream, obj.getClass().getSimpleName() + "/json", obj.toJson().toString().getBytes().length);
        outputStream.write(obj.toJson().toString().getBytes());
        outputStream.flush();
    }

    public static void writeFromFile(OutputStream outputStream, File file) throws IOException, NotAcceptableException {
        if (file == null || file.length() < 1) {
            writeHeaders(outputStream, "null/file", 0);
            return;
        }

        int length = (int) file.length();
        writeHeaders(outputStream, file.getName() + "/file", length);

        FileInputStream inputStream = new FileInputStream(file);
        int totalWrite = 0;
        byte[] buffer = new byte[100000];
        while (totalWrite < length) {
            int read = inputStream.read(buffer);
            if (read == -1) break;

            outputStream.write(buffer, 0, read);
            totalWrite += read;
        }

        outputStream.flush();
        inputStream.close();
    }

    public static <T extends MediaLinked> void writeMap(OutputStream outputStream, TreeMap<T, File> map) throws NotAcceptableException, IOException {
        for (T obj : map.keySet()) {
            writeJson(outputStream, obj);
            writeFromFile(outputStream, map.get(obj));
        }
    }

    public static <T extends MediaLinked> void writeObjectMap(OutputStream outputStream, TreeMap<User, T> map) throws NotAcceptableException, IOException {
        for (User user : map.keySet()) {
            writeJson(outputStream, user);
            writeJson(outputStream, map.get(user));
        }
    }

    public static <T extends JsonSerializable> void writeJsonArray(OutputStream outputStream, List<T> array) throws IOException, NotAcceptableException {
        for (T obj : array) writeJson(outputStream, obj);
    }

    private static void writeHeaders(OutputStream outputStream, String type, int length) throws IOException {
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
        if (length == 0) return null;
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

    public static <T extends JsonSerializable> T readJson(InputStream inputStream, Class<T> cls) throws IOException, NotAcceptableException {
        JSONObject headers = getJson(inputStream);
        String[] type = headers.getString("Content-Type").split("/");
        if (type.length < 1 || (!type[1].equals("json") || !cls.getSimpleName().equals(type[0])))
            throw new NotAcceptableException("Invalid Content-Type");

        return JsonSerializable.fromJson(getJson(inputStream), cls);
    }

    public static <T extends JsonSerializable> List<T> readJsonArray(InputStream inputStream, Class<T> cls, int count) throws IOException, NotAcceptableException {
        ArrayList<T> array = new ArrayList<>();
        for (int i = 1; i <= count; i++) array.add(readJson(inputStream, cls));
        return array;
    }

    public static <T extends MediaLinked> TreeMap<T, File> readMap(InputStream inputStream, Path dir, Class<T> cls, int count) throws NotAcceptableException, IOException {
        TreeMap<T, File> map = new TreeMap<>();
        for (int i = 1; i <= count; i++) {
            T obj = readJson(inputStream, cls);
            map.put(obj, readToFile(inputStream, Path.of(dir + "/" + obj.getMediaId())));
        }
        return map;
    }

    public static <T extends MediaLinked> TreeMap<User, T> readObjectMap(InputStream inputStream, Path dir, Class<T> cls, int count) throws NotAcceptableException, IOException {
        TreeMap<User, T> map = new TreeMap<>();
        for (int i = 1; i <= count; i++) {
            User user = readJson(inputStream, User.class);
            map.put(user, readJson(inputStream, cls));
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
            System.out.println(res);
            throw new NotAcceptableException("Invalid headers");
        }

        return new JSONObject(res.toString());
    }
}
