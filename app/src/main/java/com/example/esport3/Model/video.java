package com.example.esport3.Model;

public class video {
    String Channel;
    String title;
    String ImageURL;
    String videoUrl;
    String videoId;
    String userid;
    String likes;
    String follower;

    public video() {
    }

    public video(String Channel, String title, String imageURL, String videoUrl, String videoId, String userid,String likes,String follower) {
        this.Channel=Channel;
        this.title = title;
        ImageURL= imageURL;
        this.videoUrl = videoUrl;
        this.videoId = videoId;
        this.userid = userid;
        this.likes=likes;
        this.follower=follower;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
