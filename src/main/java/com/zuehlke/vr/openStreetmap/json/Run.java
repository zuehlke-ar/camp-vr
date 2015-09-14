package com.zuehlke.vr.openStreetmap.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Run {
    private String name;
    private long timestamp;
    private List<Point> points = new ArrayList<>();

    public Run name(final String name) {
        this.name = name;
        return this;
    }

    public Run timestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Run points(final List<Point> points) {
        this.points = points;
        return this;
    }

    public Run points(Point... points) {
        this.points = Arrays.asList(points);
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
