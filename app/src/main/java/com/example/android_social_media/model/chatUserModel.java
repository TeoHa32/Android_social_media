package com.example.android_social_media.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class chatUserModel {

    private String id,lastMessage;
    private List<String> uid;
    @ServerTimestamp
    private Date time;

//    private String id, lastMessage;
//    private List<String> uid;
//
//    private String time;


    public chatUserModel() {
    }

   // public chatUserModel(String id, String lastMessage, List<String> uid, Date time) {


//    public chatUserModel(String id, String lastMessage, List < String > uid, String time) {
//
//            this.id = id;
//            this.lastMessage = lastMessage;
//            this.uid = uid;
//            this.time = time;
//        }
//
//        public String getId () {
//            return id;
//        }
//
//        public void setId (String id){
//            this.id = id;
//        }
//
//        public String getLastMessage () {
//            return lastMessage;
//        }
//
//        public void setLastMessage (String lastMessage){
//            this.lastMessage = lastMessage;
//        }
//
//        public List<String> getUid () {
//            return uid;
//        }
//
//        public void setUid (List < String > uid) {
//            this.uid = uid;
//        }
//
//
//        public Date getTime () {
//            return time;
//        }
//
//        public void setTime (Date time){
//
//            public String getTime () {
//                return time;
//            }
//
//            public void setTime (String time){
//                this.time = time;
//            }
    //  }

    public chatUserModel(String id, String lastMessage, List<String> uid, Date time) {
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

    public void setTime(Date time) {
        this.time = time;
    }
}
