package org.aut.dataAccessors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MediaAccessor {
    private static final Path PATH_TO_PROFILES = Path.of("./src/main/resources/profiles");
    private static final Path PATH_TO_BACKGROUNDS = Path.of("./src/main/resources/backgrounds");

    private MediaAccessor() {
    }

    static void createDirectories() throws IOException {
        if (!Files.isDirectory(PATH_TO_PROFILES)) Files.createDirectories(PATH_TO_PROFILES);
        if (!Files.isDirectory(PATH_TO_BACKGROUNDS)) Files.createDirectories(PATH_TO_BACKGROUNDS);
    }

    public static File getProfile(String userId) {
        File file = new File(PATH_TO_PROFILES.toString() + '/' + userId + ".jpg");
        return file.isFile() ? file : null;
    }

    public static File getBackGround(String userId) {
        File file = new File(PATH_TO_BACKGROUNDS.toString() + '/' + userId + ".jpg");
        return file.isFile() ? file : null;
    }
}
