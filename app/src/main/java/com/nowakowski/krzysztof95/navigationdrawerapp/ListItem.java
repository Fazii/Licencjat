package com.nowakowski.krzysztof95.navigationdrawerapp;


public class ListItem {

    private String user_id;
    private String user_name;
    private String user_avatar;
    private String event_id;
    private String event_title;
    private String event_author;
    private String event_time;
    private String event_start_time;
    private String event_desc;
    private String event_tag;
    private String join_time;
    private double event_lat;
    private double event_lng;
    private int joined_num;

    String getUser_id() {
        return user_id;
    }

    void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    String getUser_avatar() {
        return user_avatar;
    }

    void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    String getEvent_id() {
        return event_id;
    }

    void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    String getEvent_title() {
        return event_title;
    }

    void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    String getEvent_author() {
        return event_author;
    }

    void setEvent_author(String event_author) {
        this.event_author = event_author;
    }

    String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_date) {
        this.event_time = event_time;
    }

    String getEvent_start_time() {
        return event_start_time;
    }

    void setEvent_start_time(String event_start_time) {this.event_start_time = event_start_time;}

    String getEvent_desc() {
        return event_desc;
    }

    void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_tag() {
        return event_tag;
    }

    public void setEvent_tag(String event_tag) {
        this.event_tag = event_tag;
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }

    double getEvent_lat() {
        return event_lat;
    }

    void setEvent_lat(double event_lat) {
        this.event_lat = event_lat;
    }

    double getEvent_lng() {
        return event_lng;
    }

    void setEvent_lng(double event_lng) {
        this.event_lng = event_lng;
    }

    int getJoined_num() {
        return joined_num;
    }

    public void setJoined_num(int joined_num) {
        this.joined_num = joined_num;
    }
}
