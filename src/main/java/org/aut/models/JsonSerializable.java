package org.aut.models;


import io.jsonwebtoken.lang.Classes;
import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public interface  JsonSerializable  {
    JSONObject toJson();
    static<T extends JsonSerializable> T fromJson(JSONObject jsonObject , Class<T> cls) throws NotAcceptableException {
        try {
            return Classes.getConstructor(cls, JSONObject.class).newInstance(jsonObject);
        } catch (Exception e){
            throw new NotAcceptableException("Json parse failure");
        }
    }


}
