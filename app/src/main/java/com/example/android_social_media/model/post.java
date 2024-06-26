package com.example.android_social_media.model;

public class post {
    private  String postId;
    private String postImage;
    private String description;
    private String publisher;
    private String dateTime;
    public post() {
    }
    public post(String postId, String postImage, String description, String publisher, String datetime) {
        this.postId = postId;
        this.postImage = postImage;
        this.description = description;
        this.publisher = publisher;
        this.dateTime=datetime;
    }

    public String getDatetime() {
        return dateTime;
    }
    public void setDatetime(String datetime) {
        this.dateTime = datetime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
