package com.example.android_social_media.model;

import java.util.Date;
import java.util.List;

public class ChatModel {

    private String id;
    private String message;
    private Date time;
    private String senderID;
    private List<String> uid;

    public ChatModel() {
    }

    public ChatModel(String id, String message, Date time, String senderID) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.senderID = senderID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public List<String> getUid(List<String> uids) {
        return uid;
    }

    public void setUid(List<String> uid) {
        this.uid = uid;
    }
}
