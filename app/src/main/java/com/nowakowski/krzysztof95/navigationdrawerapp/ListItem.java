package com.nowakowski.krzysztof95.navigationdrawerapp;

/**
 * Created by Krzysztof on 2017-03-02.
 */

public class ListItem {

    private String id;
    private String book_id;
    private String author;
    private String date;
    private String comment;
    private double lat;
    private double lng;


    public void setId(String id) {
        this.id = id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return book_id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCommentDate() {
        return date;
    }

    public String getBookComment() {
        return comment;
    }
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
