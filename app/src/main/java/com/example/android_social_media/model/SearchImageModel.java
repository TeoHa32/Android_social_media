package com.example.android_social_media.model;

public class SearchImageModel {
    private String id, mota, ngaydang, postImage, uid;

    public SearchImageModel(String id, String mota, String ngaydang, String postImage, String uid) {
        this.id = id;
        this.mota = mota;
        this.ngaydang = ngaydang;
        this.postImage = postImage;
        this.uid = uid;
    }



    public SearchImageModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getNgaydang() {
        return ngaydang;
    }

    public void setNgaydang(String ngaydang) {
        this.ngaydang = ngaydang;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
