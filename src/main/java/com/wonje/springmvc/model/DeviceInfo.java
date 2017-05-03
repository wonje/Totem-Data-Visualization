package com.wonje.springmvc.model;


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
