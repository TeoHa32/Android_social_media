package com.example.android_social_media.model;

public class comment {
    private String comment;
    private String publisher;

    public comment() {
    }

    public comment(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
