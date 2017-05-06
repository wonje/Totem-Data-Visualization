package com.wonje.springmvc.model;


import java.util.Arrays;
import java.util.List;

/**
 * Created by wonje on 5/1/17.
 */
public class DeviceInfo {

    private String totemDevice;

    private long timeStamp;

    private String date;

    private double amp;

    private double volt;

    public DeviceInfo(String totemDevice, long timeStamp, String date, double amp, double volt){
        this.totemDevice = totemDevice;
        this.timeStamp = timeStamp;
        this.date = date;
        this.amp = amp;
        this.volt = volt;
    }

    public DeviceInfo(String rowToParse){
        List<String> columns = Arrays.asList(rowToParse.split(","));
        if (columns.size() < 2) {
            throw new IllegalArgumentException("Parsing error");
        }

        this.totemDevice = columns.get(0);
        this.date = columns.get(2);
        this.timeStamp = Long.valueOf(columns.get(3)).longValue();
        this.amp = Double.valueOf(columns.get(4)).doubleValue();
        this.volt = Double.valueOf(columns.get(5)).doubleValue();
    }

    public String getTotemDevice() {
        return totemDevice;
    }

    public void setTotemDevice(String totemDevice) {
        this.totemDevice = totemDevice;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public double getAmp(){
        return amp;
    }

    public void setAmp(double amp){
        this.amp = amp;
    }

    public double getVolt(){
        return volt;
    }

    public void setVolt(double volt){
        this.volt = volt;
    }

    @Override
    public String toString() {
        return "DeviceInfo [totemdevice=" + getTotemDevice() + ", timestamp=" + getTimeStamp()
                + ", date=" + getDate() + ", amp=" + getAmp() + ", volt=" + getVolt()
                + "]";
    }

}
