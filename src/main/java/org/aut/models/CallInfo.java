package org.aut.models;

import java.util.Date;

public class CallInfo {
    private final String userId; // foreign key
    private final String emailAddress; // valid
    private final String mobileNumber; // valid 40 chars
    private final String homeNumber;
    private final String workNumber;
    private final String Address; // 220
    private final Date birthDay;
    private final PrivacyPolitics privacyPolitics;
    private final String socialMedia;


    public CallInfo(String userId, String emailAddress, String mobileNumber, String homeNumber, String workNumber, String address, Date birthDay, PrivacyPolitics privacyPolitics, String socialMedia) {
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
