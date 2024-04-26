package com.example.android_social_media.model;

public class StoriesModel {

   private String imageUrl;
   private long startTime;
   private long endTime;
   private String storyId;
   private String userId;

    public StoriesModel(String imageUrl, long startTime, long endTime, String storyId, String userId) {
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.storyId = storyId;
        this.userId = userId;
    }

    public StoriesModel() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
