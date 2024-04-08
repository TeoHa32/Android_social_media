package com.example.android_social_media.model;

public class User {
    private String name;
    private int resourceId;

    public User(String name, int resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
