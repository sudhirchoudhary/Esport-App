package com.example.esport3.Model;

public class Likes {
    String sender;
    String reciver;
    String info;

    public Likes() {
    }

    public Likes(String sender, String reciver, String info) {
        this.sender = sender;
        this.reciver = reciver;
        this.info = info;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
