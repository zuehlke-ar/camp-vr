package com.zuehlke.vr.openStreetmap.json;

public class Track {
    private int fromRef;
    private int toRef;

    public Track(int fromRef, int toRef) {
        this.fromRef = fromRef;
        this.toRef = toRef;
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
}
