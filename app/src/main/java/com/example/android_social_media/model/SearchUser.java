package com.example.android_social_media.model;

public class SearchUser {
    private String uid, name, username, profileImage;

    public SearchUser(String uid, String name, String username, String profileImage ) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.profileImage = profileImage;
    }

    public SearchUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}