package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientID {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("cover")
    @Expose
    private String cover;

    @SerializedName("totalRate")
    @Expose
    private String totalRate;

    /**
     * No args constructor for use in serialization
     *
     */
    public ClientID() {
    }

    /**
     *
     * @param id
     * @param logo
     * @param cover
     * @param password
     * @param brandName
     */
    public ClientID(String id, String brandName, String password, String logo, String cover , String totalRate) {
        super();
        this.id = id;
        this.brandName = brandName;
        this.password = password;
        this.logo = logo;
        this.cover = cover;
        this.totalRate = totalRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

}
