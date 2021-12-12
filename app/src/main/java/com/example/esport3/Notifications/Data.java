package com.example.esport3.Notifications;

public class Data {
    private String data;
    private int icon;
    private String body;
    private String title;
    private String sented;

    public Data() {
    }

    public Data(String data, int icon, String body, String title, String sented) {
        this.data = data;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}
