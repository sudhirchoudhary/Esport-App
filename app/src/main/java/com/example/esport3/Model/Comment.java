package com.example.esport3.Model;

public class Comment {
    String commentId;
    String receiver;
    String sender;
    String comment;
    String username;
    String ImageURL;

    public Comment() {
    }

    public Comment(String commentId, String receiver, String sender, String comment,String username,String ImageURl) {
        this.commentId = commentId;
        this.receiver = receiver;
        this.sender = sender;
        this.comment = comment;
        this.username=username;
        this.ImageURL=ImageURl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
