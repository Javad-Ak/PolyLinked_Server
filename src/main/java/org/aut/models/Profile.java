package org.aut.models;

import org.json.JSONObject;

import java.util.Date;

public class Profile {
    private final String userId; // same as user id -> foreign key
    private final String bio; // 220 ch
    private final String pathToPic; // 400*400, 1 mb
    private final String pathToBG; // 1584*396
    private final String country; // 60 ch
    private final String city;  // 60 ch
    private final Status status;
    private final Profession profession;
    // + Skills(relatively), Educations, CallInfo

    public Profile(String id, String bio, String pathToPic, String pathToBG, String country, String city, Status status, Profession profession) {
        validateFields(bio, country, city);

        this.userId = id;
        this.bio = bio;
        this.pathToPic = pathToPic;
        this.pathToBG = pathToBG;
        this.country = country.toUpperCase();
        this.city = city.toUpperCase();
        this.status = status;
        this.profession = profession;
    }

    public Profile(JSONObject profile) {
        this.userId = profile.getString("id");
        this.bio = profile.getString("bio");
        this.pathToPic = profile.getString("pathToPic");
        this.pathToBG = profile.getString("pathToBG");
        this.country = profile.getString("country");
        this.city = profile.getString("city");
        this.status = Status.valueOf(profile.getString("status"));
        this.profession = Profession.valueOf(profile.getString("profession"));
    }

    @Override
    public String toString() {
        return '{' +
                "id: " + userId +
                ", bio: " + bio +
                ", pathToPic: " + pathToPic +
                ", pathToBG: " + pathToBG +
                ", country: " + country.toUpperCase() +
                ", city: " + city.toUpperCase() +
                ", status: " + status +
                ", profession: " + profession +
                '}';
    }

    public JSONObject toJson() {
        return new JSONObject(toString());
    }

    private void validateFields(String bio, String country, String city) {
        if ((bio != null && bio.length() > 220) ||
                (country != null && !country.matches("(?i)^[a-z]{0,60}$")) ||
                (city != null && city.matches("(?i)^[a-z]{0,60}$")))

            throw new RuntimeException("invalid arguments");
    }

    public enum Status {
        RECRUITER("RECRUITER"), SERVICE_PROVIDER("SERVICE_PROVIDER"), JOB_SEARCHER("JOB_SEARCHER");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Profession {
        DOCTOR("DOCTOR"),
        NURSE("NURSE"),
        TEACHER("TEACHER"),
        ENGINEER("ENGINEER"),
        LAWYER("LAWYER"),
        ACCOUNTANT("ACCOUNTANT"),
        ARCHITECT("ARCHITECT"),
        SCIENTIST("SCIENTIST"),
        SOFTWARE_DEVELOPER("SOFTWARE_DEVELOPER"),
        DENTIST("DENTIST"),
        PHARMACIST("PHARMACIST"),
        PILOT("PILOT"),
        VETERINARIAN("VETERINARIAN"),
        CHEF("CHEF"),
        JOURNALIST("JOURNALIST"),
        POLICE_OFFICER("POLICE_OFFICER"),
        FIREFIGHTER("FIREFIGHTER"),
        ELECTRICIAN("ELECTRICIAN"),
        PLUMBER("PLUMBER"),
        MECHANIC("MECHANIC"),
        GRAPHIC_DESIGNER("GRAPHIC_DESIGNER"),
        MARKETING_MANAGER("MARKETING_MANAGER"),
        HR_MANAGER("HR_MANAGER"),
        FINANCIAL_ANALYST("FINANCIAL_ANALYST"),
        SOCIAL_WORKER("SOCIAL_WORKER"),
        PSYCHOLOGIST("PSYCHOLOGIST"),
        THERAPIST("THERAPIST"),
        MUSICIAN("MUSICIAN"),
        ACTOR("ACTOR"),
        WRITER("WRITER"),
        LIBRARIAN("LIBRARIAN"),
        ECONOMIST("ECONOMIST"),
        PHYSICAL_THERAPIST("PHYSICAL_THERAPIST"),
        OCCUPATIONAL_THERAPIST("OCCUPATIONAL_THERAPIST"),
        RADIOLOGIST("RADIOLOGIST"),
        SURGEON("SURGEON"),
        ANESTHESIOLOGIST("ANESTHESIOLOGIST"),
        CONSULTANT("CONSULTANT"),
        ENTREPRENEUR("ENTREPRENEUR"),
        PROJECT_MANAGER("PROJECT_MANAGER"),
        REAL_ESTATE_AGENT("REAL_ESTATE_AGENT"),
        SALES_MANAGER("SALES_MANAGER"),
        INTERIOR_DESIGNER("INTERIOR_DESIGNER"),
        CIVIL_ENGINEER("CIVIL_ENGINEER"),
        MECHANICAL_ENGINEER("MECHANICAL_ENGINEER"),
        DATA_SCIENTIST("DATA_SCIENTIST"),
        BIOLOGIST("BIOLOGIST"),
        CHEMIST("CHEMIST"),
        URBAN_PLANNER("URBAN_PLANNER"),
        ENVIRONMENTAL_SCIENTIST("ENVIRONMENTAL_SCIENTIST"),
        COMPUTER_ENGINEER("COMPUTER_ENGINEER"),
        PROGRAMMER("PROGRAMMER"),
        WEB_DEVELOPER("WEB_DEVELOPER"),
        NETWORK_ENGINEER("NETWORK_ENGINEER"),
        SYSTEMS_ANALYST("SYSTEMS_ANALYST"),
        CYBERSECURITY_ANALYST("CYBERSECURITY_ANALYST"),
        DATABASE_ADMINISTRATOR("DATABASE_ADMINISTRATOR"),
        UX_UI_DESIGNER("UX_UI_DESIGNER"),
        BUSINESS_ANALYST("BUSINESS_ANALYST"),
        IT_MANAGER("IT_MANAGER");

        private final String value;

        Profession(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
