package org.aut.dataAccessors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class MediaAccessor {
    private MediaAccessor() {
    }

    static void createDirectories() throws IOException {
        for (MediaPath path : MediaPath.values())
            if (!Files.isDirectory(path.value)) Files.createDirectories(path.value);
    }

    public static File getMedia(String fileId, MediaPath mediaPath) {
        try (Stream<Path> paths = Files.list(mediaPath.value)) {
            for (Path path : paths.toList()) {
                if (path.toString().contains(fileId)) return path.toFile();
            }
        } catch (IOException ignored) {
        }
        return new File("null");
    }

    public enum MediaPath {
        PROFILES(Path.of("./src/main/resources/profiles")),
        BACKGROUNDS(Path.of("./src/main/resources/backgrounds")),
        POSTS(Path.of("./src/main/resources/posts")),
        MESSAGES(Path.of("./src/main/resources/messages"));

        private final Path value;

        MediaPath(Path value) {
            this.value = value;
        }

        public Path value() {
            return value;
        }
    }
}
