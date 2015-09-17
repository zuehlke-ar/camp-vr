package com.zuehlke.vr.domain;

public class GpsPoint extends Node{
    private long timeStamp;
    private double accuracy;

    public GpsPoint(long timeStamp, double lat, double lon, double ele, double accuracy) {
        super(lat,lon);
        setEle(ele);
        this.timeStamp = timeStamp;
        this.accuracy = accuracy;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "GpsPoint{" +
                "timeStamp=" + timeStamp +
                ", latitude=" + getLat() +
                ", longitude=" + getLon() +
                ", elevation=" + getEle() +
                ", accuracy=" + accuracy +
                '}';
    }
}
