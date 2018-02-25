package com.voxelgameslib.dependencies;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import org.bukkit.plugin.java.JavaPlugin;

public class Dependencies extends JavaPlugin {

    private List<Dependency> dependencies = new ArrayList<>();

    @Override
    public void onLoad() {
        try {
            FileSystem fileSystem = FileSystems.newFileSystem(getFile().toPath(), getClass().getClassLoader());
            Stream<Path> walk = Files.walk(fileSystem.getPath("/"), 5);
            walk.filter(path -> path.startsWith("/META-INF/maven")).filter(path -> path.endsWith("pom.properties")).forEach(path -> {
                Dependency dependency;
                try (InputStream stream = Files.newInputStream(path)) {
                    Properties properties = new Properties();
                    properties.load(stream);

                    dependency = new Dependency(
                        properties.getProperty("groupId", "undefined"),
                        properties.getProperty("artifactId", "undefined"),
                        properties.getProperty("artifactId", "undefined"),
                        new Date(0));

                    dependencies.add(dependency);
                } catch (IOException ex) {
                    getLogger().warning("Error while reading file " + path);
                    ex.printStackTrace();
                    return;
                }

                try {
                    Optional<String> dateLine = Files.readAllLines(path).stream()
                        .filter(l -> l.startsWith("#"))
                        .filter(l -> !l.startsWith("#G"))
                        .filter(l -> !l.startsWith("#C"))
                        .findFirst();

                    if (!dateLine.isPresent()) {
                        getLogger().warning("Couldn't figure out compile date for dependency " + dependency);
                        return;
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYYY");
                    try {
                        dependency.setCompileTime(dateFormat.parse(dateLine.get().replace("#", "")));
                    } catch (ParseException e) {
                        getLogger().warning("Error while parsing compile time date " + dateLine.get().replace("#", ""));
                        e.printStackTrace();
                    }
                } catch (IOException ex) {
                    getLogger().warning("Error while reading compile date for dependency " + dependency);
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        dependencies.sort(Comparator.comparing(Dependency::getGroupId).thenComparing(Dependency::getArtifactId));
        getLogger().info("Loaded " + dependencies.size() + " dependencies!");
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }
}
