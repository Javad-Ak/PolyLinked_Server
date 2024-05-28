package org.aut.models;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Education {
    private final String id;
    private final String userId;
    private final String institue; // 40 chars
    private final String field; // 40 chars
    private final Date start;
    private final Date end;
    private final int grade; // 0-100
    private final String activities; //   500 chars
    private final String about; // 1000 chars
    // + skills(5)

    public Education(String userId, String institute, String field, Date start, Date end, int grade, String activities, String about) {
        this.id = "edu" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        this.userId = userId;
        this.institue = institute;
        this.field = field;
        this.start = start;
        this.end = end;
        this.grade = grade;
        this.activities = activities;
        this.about = about;
    }
}
