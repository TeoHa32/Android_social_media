package com.example.android_social_media.model;

public class chatUserModel {
    private String id,useruid, name, imgurl;

    public chatUserModel() {

    }

    public chatUserModel(String id, String useruid, String name, String imgurl) {
        this.id = id;
        this.useruid = useruid;
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
