package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONException;
import org.json.JSONObject;

public class Connect {
    private final String applicantId;
    private final String acceptorId;
    private final String note;
    private final AcceptState acceptState;

    public Connect(String applicantId, String acceptorId, String note, AcceptState acceptState) throws NotAcceptableException {
        validateFields(applicantId, acceptorId, note);
        this.applicantId = applicantId;
        this.acceptorId = acceptorId;
        this.note = note;
        this.acceptState = acceptState;
    }

    public Connect(JSONObject json) throws NotAcceptableException {
        this.applicantId = json.getString("applicantId");
        this.acceptorId = json.getString("acceptorId");
        this.acceptState = AcceptState.valueOf(json.getString("acceptState"));
        this.note = json.getString("note");
        validateFields(applicantId, acceptorId, note);
        System.out.println("hey 1.76");
    }

    public String getAcceptorId() {
        return acceptorId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public String getNote() {
        return note;
    }

    public String getAcceptState() {
        return acceptState.value;
    }

    @Override
    public String toString() {
        return "{" +
                "applicantId:" + applicantId +
                ", acceptorId:" + acceptorId +
                ", acceptState:" + acceptState.toString() +
                ", note:" + note +
                "}";
    }

    public JSONObject toJSON() {
        return new JSONObject(toString());
    }

    public static void validateFields(String applicantId, String acceptorId, String note) throws NotAcceptableException {
        if (applicantId == null || acceptorId == null || note == null || note.length() >= 500) {
            throw new NotAcceptableException("Some fields are null");
        }
    }

    public enum AcceptState {
        ACCEPTED("ACCEPTED"),
        REJECTED("REJECTED"),
        WAITING("WAITING");


        private final String value;

        AcceptState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
