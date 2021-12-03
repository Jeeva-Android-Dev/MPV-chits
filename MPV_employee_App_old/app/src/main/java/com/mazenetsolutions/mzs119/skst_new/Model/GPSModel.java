package com.mazenetsolutions.mzs119.skst_new.Model;

/**
 * Created by admin on 03/10/2016.
 */
public class GPSModel {

    String name;
    String lat;
    String lon;
    String datee;
    String Id;
    String time;
    String type;

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
