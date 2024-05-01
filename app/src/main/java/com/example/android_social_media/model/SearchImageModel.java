package com.example.android_social_media.model;

public class SearchImageModel {
    private String postId, postImage;

    public SearchImageModel(String postId, String postImage) {
        this.postId = postId;
        this.postImage = postImage;
    }

    public SearchImageModel() {
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
}
