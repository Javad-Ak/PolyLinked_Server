package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Education {
    private final String id;
    private final String userId;
    private final String institute; // 40 chars
    private final String field; // 40 chars
    private final Date start;
    private final Date end;
    private final int grade; // 0-100
    private final String activities; //   500 chars
    private final String about; // 1000 chars
    // + skills(5)

    public Education(String userId, String institute, String field, Date start, Date end, int grade, String activities, String about) throws NotAcceptableException {
        validateFields(institute, field, start, end, grade, activities, about);

        this.id = "edu" + new Random().nextInt(99999) + UUID.randomUUID().toString().substring(10, 23);
        this.userId = userId;
        this.institute = institute;
        this.field = field;
        this.start = start;
        this.end = end;
        this.grade = grade;
        this.activities = activities;
        this.about = about;
    }

    public Education(JSONObject jsonObject) {
        id = jsonObject.getString("id");
        userId = jsonObject.getString("user_id");
        institute = jsonObject.getString("institute");
        field = jsonObject.getString("field");
        start = new Date(jsonObject.getLong("start"));
        end = new Date(jsonObject.getLong("end"));
        grade = jsonObject.getInt("grade");
        activities = jsonObject.getString("activities");
        about = jsonObject.getString("about");
    }

    @Override
    public String toString() {
        return "{" +
                ", id:" + id +
                ", userId:" + userId +
                ", institute:" + institute +
                ", field:" + field +
                ", start:" + start.getTime() +
                ", end:" + end.getTime() +
                ", grade:" + grade +
                ", activities:" + activities +
                ", about:" + about +
                '}';
    }

    private void validateFields(String institute, String field, Date start, Date end, int grade, String activities, String about) throws NotAcceptableException {
        if (institute == null || institute.isEmpty() || field == null || field.isEmpty() ||
                start == null || end == null || start.after(end) ||
                activities == null || activities.isEmpty() || about == null || about.isEmpty() ||
                grade < 0 || grade > 100 ||
                institute.length() > 40 || field.length() > 40 || activities.length() > 500 || about.length() > 1000
        ) throw new NotAcceptableException("Illegal args");
    }
}
