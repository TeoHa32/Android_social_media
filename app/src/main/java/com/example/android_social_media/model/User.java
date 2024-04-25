package com.example.android_social_media.model;

public class User {
    private String UserID;
    private String username;
    private String name;
    private String profileImage;

    public User() {
    }

    public User(String userID, String username, String name, String profileImage) {
        UserID = userID;
        this.username = username;
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
