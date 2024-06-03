package org.aut.models;

public interface MediaLinked extends JsonSerializable {
    String SERVER_ADDRESS = "https://localhost:8080/resources/";
    String getMediaId();
    String getMediaURL();
}
