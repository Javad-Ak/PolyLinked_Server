package org.aut.models;

import org.aut.utils.exceptions.PermissionDeniedException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;

public class Skill {
    private final String id;
    private final String profileId;
    private final String educationId;
    private final String name; // 60 chars

    public Skill(String profileId, String educationId, String name) throws PermissionDeniedException {
        if (name == null || name.isEmpty()) throw new PermissionDeniedException("Invalid Arguments");

        this.id = "skill" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        this.profileId = profileId;
        this.educationId = educationId;
        this.name = name;
    }

    public Skill(JSONObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.profileId = jsonObject.getString("profile_id");
        this.educationId = jsonObject.getString("education_id");
        this.name = jsonObject.getString("name");
    }

    @Override
    public String toString() {
        return '{' +
                "id:" + id +
                ", profileId:" + profileId +
                ", educationId:" + educationId +
                ", name:" + name +
                '}';
    }
}
