package org.aut.dataAccessors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class MediaAccessor {
    public static final Path PATH_TO_PROFILES = Path.of("./src/main/resources/profiles");
    public static final Path PATH_TO_BACKGROUNDS = Path.of("./src/main/resources/backgrounds");

    private MediaAccessor() {
    }

    static void createDirectories() throws IOException {
        if (!Files.isDirectory(PATH_TO_PROFILES)) Files.createDirectories(PATH_TO_PROFILES);
        if (!Files.isDirectory(PATH_TO_BACKGROUNDS)) Files.createDirectories(PATH_TO_BACKGROUNDS);
    }

    public static File getProfile(String userId) {
        try (Stream<Path> paths = Files.list(PATH_TO_PROFILES)) {
            for (Path path : paths.toList()) {
                if (path.toString().contains(userId)) return path.toFile();
            }
        } catch (IOException ignored) {
        }
        return new File("null");
    }

    public static File getBackGround(String userId) {
        try (Stream<Path> paths = Files.list(PATH_TO_BACKGROUNDS)) {
            for (Path path : paths.toList()) {
                if (path.toString().contains(userId)) return path.toFile();
            }
        } catch (IOException ignored) {
        }
        return new File("null");
    }
}
