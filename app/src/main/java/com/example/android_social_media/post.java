package com.example.android_social_media;

public class post {
    private int id_userImg_post;
    private int id_image_post;
    private String name;
    private int id_heart;
    private int id_comment;
    private int id_share;

    public post(int id_userImg_post, int id_image_post, String name, int id_heart, int id_comment, int id_share) {
        this.id_userImg_post = id_userImg_post;
        this.id_image_post = id_image_post;
        this.name = name;
        this.id_heart = id_heart;
        this.id_comment = id_comment;
        this.id_share = id_share;
    }

    public int getId_userImg_post() {
        return id_userImg_post;
    }

    public void setId_userImg_post(int id_userImg_post) {
        this.id_userImg_post = id_userImg_post;
    }

    public int getId_image_post() {
        return id_image_post;
    }

    public void setId_image_post(int id_image_post) {
        this.id_image_post = id_image_post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId_heart() {
        return id_heart;
    }

    public void setId_heart(int id_heart) {
        this.id_heart = id_heart;
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public int getId_share() {
        return id_share;
    }

    public void setId_share(int id_share) {
        this.id_share = id_share;
    }
}
