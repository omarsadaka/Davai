package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ratting {

    @SerializedName("clientID")
    @Expose
    private String clientID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("rate")
    @Expose
    private Float rate;

    @SerializedName("comment")
    @Expose
    private String comment;

    public Ratting(String clientID, String userID, Float rate , String comment) {
        this.clientID = clientID;
        this.userID = userID;
        this.rate = rate;
        this.comment =comment;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
