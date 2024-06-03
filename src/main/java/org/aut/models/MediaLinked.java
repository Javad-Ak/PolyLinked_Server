package org.aut.models;

import org.jetbrains.annotations.NotNull;


public interface MediaLinked extends JsonSerializable, Comparable<MediaLinked> {
    String SERVER_ADDRESS = "https://localhost:8080/resources/";

    String getMediaId();

    String getMediaURL();

    default int compareTo(@NotNull MediaLinked mediaLinked) {
        return 0;
    }
}
