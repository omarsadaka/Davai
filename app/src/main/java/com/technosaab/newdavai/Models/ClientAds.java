package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientAds {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("clientID")
    @Expose
    private String clientID;
    @SerializedName("imgPath")
    @Expose
    private String imgPath;


    public ClientAds(String description, String clientID, String imgPath) {
        this.description = description;
        this.clientID = clientID;
        this.imgPath = imgPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
