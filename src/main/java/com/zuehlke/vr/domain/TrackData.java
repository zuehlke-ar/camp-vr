package com.zuehlke.vr.domain;

import com.cedarsoftware.util.io.JsonWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackData {
    private List<Node> nodes = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();
    private Run run;

    public TrackData() {
//        Node node = new Node(47.499407, 8.722790);
//        Node node1 = new Node(47.497320, 8.721095);
//        Track track = new Track(node, node1);
//
//        nodes.add(node);
//        nodes.add(node1);
//        tracks.add(track);
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
//                .track(track)
//                .position(0.0)
//                .measurements(measurement0)
//                .photos(new Photo().path("data/img/photo0.png").direction(0).height(1));
//
//        Point point1 = new Point()
//                .timestamp(1000)
//                .track(track)
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

//    public static TrackData fromJson(String string) {
//        return (TrackData) JsonReader.jsonToJava(string);
//    }
//
//    public static TrackData fromFile(File file) throws IOException {
//        return fromJson(FileUtils.readFileToString(file));
//    }

    public String toJson() {
        Map<String, Object> args = new HashMap<>();
        args.put("TYPE", false);
        args.put("PRETTY_PRINT", true);

        return JsonWriter.objectToJson(this, args);
    }

    public void toFile(File file) throws IOException {
        FileUtils.writeStringToFile(file, toJson());
    }
}
