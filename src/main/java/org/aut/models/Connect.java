package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;
import org.json.JSONException;
import org.json.JSONObject;

public class Connect {
    private final String applicant_id;
    private final String acceptor_id;
    private final String note;
    private final AcceptState accept_state;

    public Connect(String applicant_id, String acceptor_id, String note, AcceptState accept_state) throws NotAcceptableException {
        validateFields(applicant_id, acceptor_id, note);
        this.applicant_id = applicant_id;
        this.acceptor_id = acceptor_id;
        this.note = note;
        this.accept_state = accept_state;
    }

    public Connect(JSONObject json) throws NotAcceptableException {
        try {
            this.applicant_id = json.getString("applicant_id");
            this.acceptor_id = json.getString("acceptor_id");
            this.accept_state = AcceptState.valueOf(json.getString("accept_state"));
            this.note = json.getString("note");
        } catch (JSONException e) {
            throw new NotAcceptableException("Wrong jsonObject");
        }
        validateFields(applicant_id, acceptor_id, note);
    }

    public String getAcceptor_id() {
        return acceptor_id;
    }

    public String getApplicant_id() {
        return applicant_id;
    }

    public String getNote() {
        return note;
    }

    public String getAccept_state() {
        return accept_state.value;
    }

    @Override
    public String toString() {
        return "{" +
                "applicant_id:" + applicant_id +
                ", acceptor_id:" + acceptor_id +
                ", accept_state:" + accept_state.toString() +
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
