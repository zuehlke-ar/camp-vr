package com.zuehlke.vr.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Point {
    private long timestamp;
    private Track track;
    private double position;
    private Map<String, Double> measurements;
    private List<Photo> photos;

    public Point timestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Point track(final Track track) {
        this.track = track;
        return this;
    }

    public Point position(final double position) {
        this.position = position;
        return this;
    }

    public Point measurements(final Map<String, Double> measurements) {
        this.measurements = measurements;
        return this;
    }

    public Point photos(final List<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Point photos(Photo... photos) {
        this.photos = Arrays.asList(photos);
        return this;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public Map<String, Double> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, Double> measurements) {
        this.measurements = measurements;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
