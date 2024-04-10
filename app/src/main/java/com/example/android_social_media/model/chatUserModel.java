package com.example.android_social_media.model;

import java.util.Date;
import java.util.List;

public class chatUserModel {
    private String id, lastMessage, name, profileIamge;
    private List<String> uid;
    private String time;

    public chatUserModel() {
    }

    public chatUserModel(String id, String lastMessage, List<String> uid, String time) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.uid = uid;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getUid() {
        return uid;
    }

    public void setUid(List<String> uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileIamge() {
        return profileIamge;
    }

    public void setProfileIamge(String profileIamge) {
        this.profileIamge = profileIamge;
    }
}
