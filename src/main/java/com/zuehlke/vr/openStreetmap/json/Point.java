package com.zuehlke.vr.openStreetmap.json;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Point {
    private long timestamp;
    private int fromRef;
    private int toRef;
    private double position;
    private Map<String, Double> measurements;
    private List<Photo> photos;

    public Point timestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Point fromRef(final int fromRef) {
        this.fromRef = fromRef;
        return this;
    }

    public Point toRef(final int toRef) {
        this.toRef = toRef;
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

    public int getFromRef() {
        return fromRef;
    }

    public void setFromRef(int fromRef) {
        this.fromRef = fromRef;
    }

    public int getToRef() {
        return toRef;
    }

    public void setToRef(int toRef) {
        this.toRef = toRef;
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
