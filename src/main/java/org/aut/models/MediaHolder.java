package org.aut.models;

import org.aut.utils.exceptions.NotAcceptableException;

import java.io.File;

public class MediaHolder {
    private final User user;
    private final File profile;
    private final MediaLinked mediaLinked;
    private final File MediFile;

    public MediaHolder(User user, File profile, MediaLinked mediaLinked, File mediFile) throws NotAcceptableException {
        if (user == null || (mediaLinked == null && mediFile == null)) throw new NotAcceptableException("Illegal args");

        this.user = user;
        this.profile = profile;
        this.mediaLinked = mediaLinked;
        MediFile = mediFile;
    }

    public User getUser() {
        return user;
    }

    public File getProfile() {
        return profile;
    }

    public MediaLinked getMediaLinked() {
        return mediaLinked;
    }

    public File getMediFile() {
        return MediFile;
    }
}
