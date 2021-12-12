package com.example.esport3.Model;

public class User {
    private String id;
    private String username;
    private String ImageURL;
    private String status;
    private String search;
    private String phone;
    private String characterName;
    private String characterID;
    private double KD;
    private String adsress;
    private String server;
    private String state;
    private String season;
    private String tier;
    private String expi;
    private String assault;
    private String snipe;
    private String tactical;
    private String fragger;
    private String sniperol;
    private String igl;
    private String suppor;
    private String title;
    private String videoUrl;


    public User() {
    }

    public User(String id, String username, String imageURL, String status, String search, String phone, String characterName, String characterID, double KD, String adsress, String server, String state, String season, String tier, String expi, String assault, String snipe, String tactical, String fragger, String sniperol, String igl, String suppor,String title,String videoUrl) {
        this.id = id;
        this.username = username;
        ImageURL = imageURL;
        this.status = status;
        this.search = search;
        this.phone = phone;
        this.characterName = characterName;
        this.characterID = characterID;
        this.KD = KD;
        this.adsress = adsress;
        this.server = server;
        this.state = state;
        this.season = season;
        this.tier = tier;
        this.expi = expi;
        this.assault = assault;
        this.snipe = snipe;
        this.tactical = tactical;
        this.fragger = fragger;
        this.sniperol = sniperol;
        this.igl = igl;
        this.suppor = suppor;
        this.title=title;
        this.videoUrl=videoUrl;
    }


    public double getKD() {
        return KD;
    }

    public void setKD(double KD) {
        this.KD = KD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }




    public String getAdsress() {
        return adsress;
    }

    public void setAdsress(String adsress) {
        this.adsress = adsress;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getExpi() {
        return expi;
    }

    public void setExpi(String expi) {
        this.expi = expi;
    }

    public String getAssault() {
        return assault;
    }

    public void setAssault(String assault) {
        this.assault = assault;
    }

    public String getSnipe() {
        return snipe;
    }

    public void setSnipe(String snipe) {
        this.snipe = snipe;
    }

    public String getTactical() {
        return tactical;
    }

    public void setTactical(String tactical) {
        this.tactical = tactical;
    }

    public String getFragger() {
        return fragger;
    }

    public void setFragger(String fragger) {
        this.fragger = fragger;
    }

    public String getSniperol() {
        return sniperol;
    }

    public void setSniperol(String sniperol) {
        this.sniperol = sniperol;
    }

    public String getIgl() {
        return igl;
    }

    public void setIgl(String igl) {
        this.igl = igl;
    }

    public String getSuppor() {
        return suppor;
    }

    public void setSuppor(String suppor) {
        this.suppor = suppor;
    }
}
