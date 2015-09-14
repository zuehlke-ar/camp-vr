package com.zuehlke.vr.openStreetmap.json;

public class Photo {
    private String path;
    private double height;
    private double direction;

    public Photo path(final String path) {
        this.path = path;
        return this;
    }

    public Photo height(final double height) {
        this.height = height;
        return this;
    }

    public Photo direction(final double direction) {
        this.direction = direction;
        return this;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}
