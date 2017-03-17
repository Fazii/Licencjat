package com.nowakowski.krzysztof95.navigationdrawerapp;


public class ListItem {

    private int event_id;
    private String event_title;
    private String event_author;
    private String event_time;
    private String event_desc;
    private double event_lat;
    private double event_lng;

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_author() {
        return event_author;
    }

    public void setEvent_author(String event_author) {
        this.event_author = event_author;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_date) {
        this.event_time = event_time;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public double getEvent_lat() {
        return event_lat;
    }

    public void setEvent_lat(double event_lat) {
        this.event_lat = event_lat;
    }

    public double getEvent_lng() {
        return event_lng;
    }

    public void setEvent_lng(double event_lng) {
        this.event_lng = event_lng;
    }

}
