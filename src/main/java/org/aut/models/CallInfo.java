package org.aut.models;

import org.aut.utils.exceptions.NotFoundException;
import org.json.JSONObject;
import java.util.Date;

public class CallInfo {
    private final String userId; // foreign key
    private final String emailAddress; // foreign key
    private final String mobileNumber; // valid 40 chars
    private final String homeNumber;
    private final String workNumber;
    private final String Address; // 220
    private final Date birthDay;
    private final PrivacyPolitics privacyPolitics;
    private final String socialMedia;


    public CallInfo(String userId, String emailAddress, String mobileNumber, String homeNumber, String workNumber, String address, Date birthDay, PrivacyPolitics privacyPolitics, String socialMedia) throws NotFoundException {
        validateFields(userId, emailAddress, mobileNumber, homeNumber, workNumber, address, socialMedia);
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.homeNumber = homeNumber;
        this.workNumber = workNumber;
        this.Address = address;
        this.birthDay = birthDay;
        this.privacyPolitics = privacyPolitics;
        this.socialMedia = socialMedia;
    }

    public CallInfo(JSONObject jsonObject) {
        userId = jsonObject.getString("userId");
        emailAddress = jsonObject.getString("emailAddress");
        mobileNumber = jsonObject.getString("mobileNumber");
        homeNumber = jsonObject.getString("homeNumber");
        workNumber = jsonObject.getString("workNumber");
        Address = jsonObject.getString("Address");
        birthDay = new Date(jsonObject.getLong("birthDay"));
        privacyPolitics = PrivacyPolitics.valueOf(jsonObject.getString("privacyPolitics"));
        socialMedia = jsonObject.getString("socialMedia");
    }

    @Override
    public String toString() {
        return '{' +
                "userId: " + userId +
                ", emailAddress: " + emailAddress +
                ", mobileNumber: " + mobileNumber +
                ", homeNumber: " + homeNumber +
                ", workNumber: " + workNumber +
                ", Address: " + Address +
                ", birthDay: " + birthDay.getTime() +
                ", privacyPolitics: " + privacyPolitics +
                ", socialMedia: " + socialMedia +
                '}';
    }

    private void validateFields(String userId, String emailAddress, String mobileNumber, String homeNumber, String workNumber, String address, String socialMedia) throws NotFoundException {
        if (userId == null || emailAddress == null ||
                (mobileNumber != null && !mobileNumber.matches("^[0-9]{1,40}$")) ||
                (workNumber != null && !workNumber.matches("^[0-9]{1,40}$")) ||
                (homeNumber != null && !homeNumber.matches("^[0-9]{1,40}$")) ||
                (address != null && address.length() > 40) ||
                (socialMedia != null && socialMedia.length() > 40)
        ) throw new NotFoundException("Illegal args");
    }

    public enum PrivacyPolitics {
        ONLY_ME("ONLY_ME"), MY_CONNECTIONS("MY_CONNECTIONS"), FURTHER_CONNECTIONS("FURTHER_CONNECTIONS"), EVERYONE("EVERYONE");

        private final String value;

        PrivacyPolitics(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
