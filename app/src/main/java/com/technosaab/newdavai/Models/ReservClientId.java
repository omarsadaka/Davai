package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservClientId {

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

    /**
     * No args constructor for use in serialization
     *
     */
    public ReservClientId() {
    }

    /**
     *
     * @param id
     * @param logo
     * @param password
     * @param brandName
     */
    public ReservClientId(String id, String brandName, String password, String logo) {
        super();
        this.id = id;
        this.brandName = brandName;
        this.password = password;
        this.logo = logo;
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
}
