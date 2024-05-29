package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;

public class Skill {
    private final String skillId;
    private final String userId;
    private final String educationId;
    private final String text; // 60 chars

    public Skill(String profileId, String educationId, String text) throws NotAcceptableException {
        if (text == null || text.length() > 40) throw new NotAcceptableException("Invalid Arguments");

        this.skillId = "skill" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        this.userId = profileId;
        this.educationId = educationId;
        this.text = text;
    }

    public Skill(JSONObject jsonObject) {
        this.skillId = jsonObject.getString("skillId");
        this.userId = jsonObject.getString("profile_id");
        this.educationId = jsonObject.getString("education_id");
        this.text = jsonObject.getString("name");
    }

    public String getSkillId() {
        return skillId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEducationId() {
        return educationId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return '{' +
                "skillId:" + skillId +
                ", profileId:" + userId +
                ", educationId:" + educationId +
                ", name:" + text +
                '}';
    }
}
