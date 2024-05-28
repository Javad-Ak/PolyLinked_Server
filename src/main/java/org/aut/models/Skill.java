package org.aut.models;

import java.util.Random;
import java.util.UUID;

public class Skill {
    private final String id;
    private final String profileId;
    private final String educationId;
    private final String name; // 60 chars

    public Skill(String profileId, String educationId, String name) {
        this.id = "skill" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);;
        this.profileId = profileId;
        this.educationId = educationId;
        this.name = name;
    }
}
