package com.example.babarmustafa.chatapplication.User_Profile.postView;


/**
 * Created by muhammad imran on 05-Jan-17.
 */

public class PostObj {
    private String UserImage;
    private String userName;
    private String postTime;
    private String title;
    private String Desc;
    private String images;

    public PostObj() {
    }

    public PostObj(String userImage, String userName, String postTime, String title, String desc, String images) {
        UserImage = userImage;
        this.userName = userName;
        this.postTime = postTime;
        this.title = title;
        Desc = desc;
        this.images = images;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
