package com.zuehlke.vr.openStreetmap.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static com.zuehlke.vr.openStreetmap.util.Maps.entriesToMap;
import static com.zuehlke.vr.openStreetmap.util.Maps.entry;

public class TrackData {
    private List<Node> nodes = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();
    private Run run;

    public TrackData() {
//        nodes.add(new Node(0, 47.499407, 8.722790));
//        nodes.add(new Node(1, 47.497320, 8.721095));
//        tracks.add(new Track(0, 1));
//
//        Map<String, Double> measurement0 = Collections.unmodifiableMap(Stream.of(
//                entry("speed", 20.3),
//                entry("trackQuality", 100.0))
//                .collect(entriesToMap()));
//
//        Map<String, Double> measurement1 = Collections.unmodifiableMap(Stream.of(
//                entry("speed", 19.4),
//                entry("trackQuality", 85.4))
//                .collect(entriesToMap()));
//
//        Point point0 = new Point()
//                .timestamp(0)
//                .fromRef(0)
//                .toRef(1)
//                .position(0.0)
//                .measurements(measurement0)
//                .photos(new Photo().path("data/img/photo0.png").direction(0).height(1));
//
//        Point point1 = new Point()
//                .timestamp(1000)
//                .fromRef(0)
//                .toRef(1)
//                .position(0.9)
//                .measurements(measurement1)
//                .photos(new Photo().path("data/img/photo1.png").direction(0).height(1));
//
//        run = new Run()
//                .name("My test run")
//                .timestamp(0)
//                .points(point0, point1);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public static TrackData fromFile(File file) throws IOException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(FileUtils.readFileToString(file), TrackData.class);
    }

    public void toFile(File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileUtils.writeStringToFile(file, gson.toJson(this));
    }

    public static void main(String[] args) throws IOException {
        new TrackData().toFile(new File("data/json/test.json"));
    }
}
