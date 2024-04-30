package com.example.android_social_media.model;

public class SearchUser {
    private String UserID, name, username, profileImage;

    public SearchUser() {
    }

    public SearchUser(String userID, String name, String username, String profileImage) {
        UserID = userID;
        this.name = name;
        this.username = username;
        this.profileImage = profileImage;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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