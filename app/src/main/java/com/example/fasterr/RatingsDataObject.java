package com.example.fasterr;

public class RatingsDataObject {
    String rating;
    String review;
    String uname;
    String uid;
    String date;

    RatingsDataObject(){}
    public RatingsDataObject(String rating, String review, String uname, String date) {
        this.rating = rating;
        this.review = review;
        this.uname = uname;
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
