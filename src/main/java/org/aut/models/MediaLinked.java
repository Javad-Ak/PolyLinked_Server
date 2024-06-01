package org.aut.models;

import io.jsonwebtoken.lang.Classes;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

public interface MediaLinked extends JsonSerializable {
    String getMediaId();
}
