package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONException;
import org.json.JSONObject;

public class Connect {
    private final String applicantId;
    private final String acceptorId;
    private final String note;
    private boolean acceptState;

    Connect(String applicantId, String acceptorId, String note) throws NotAcceptableException {
        validateFields(applicantId, acceptorId, note);
        this.applicantId = applicantId;
        this.acceptorId = acceptorId;
        this.note = note;
        acceptState = false;
    }

    public Connect(JSONObject json) throws NotAcceptableException {
        validateFields(json.getString("applicantId"), json.getString("acceptorId"), json.getString("note"));
        applicantId = json.getString("applicantId");
        acceptorId = json.getString("acceptorId");
        acceptState = json.getBoolean("accepted");
        note = json.getString("note");
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

    public boolean getAcceptState() {
        return acceptState;
    }

    @Override
    public String toString() {
        return "{" +
                "applicantId:" + applicantId +
                ", user2ID:" + acceptorId +
                ", accepted:" + acceptState +
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
}
