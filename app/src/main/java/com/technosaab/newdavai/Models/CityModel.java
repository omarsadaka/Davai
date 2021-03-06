package com.technosaab.newdavai.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityModel {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("titleAr")
    @Expose
    private String titleAr;
    @SerializedName("titleEN")
    @Expose
    private String titleEN;
    @SerializedName("countryID")
    @Expose
    private String countryID;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("status")
    @Expose
    private Integer status;

    /**
     * No args constructor for use in serialization
     *
     */
    public CityModel() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param v
     * @param status
     * @param countryID
     * @param titleAr
     * @param createdAt
     * @param titleEN
     */
    public CityModel(String id, String updatedAt, String createdAt, String titleAr, String titleEN, String countryID, Integer v, Integer status) {
        super();
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.titleAr = titleAr;
        this.titleEN = titleEN;
        this.countryID = countryID;
        this.v = v;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}