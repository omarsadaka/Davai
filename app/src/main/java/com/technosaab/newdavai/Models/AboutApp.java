package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutApp {

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
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("typeFor")
    @Expose
    private Integer typeFor;
    @SerializedName("__v")
    @Expose
    private Integer v;

    /**
     * No args constructor for use in serialization
     *
     */
    public AboutApp() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param v
     * @param titleAr
     * @param createdAt
     * @param typeFor
     * @param titleEN
     * @param type
     */
    public AboutApp(String id, String updatedAt, String createdAt, String titleAr, String titleEN, Integer type, Integer typeFor, Integer v) {
        super();
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.titleAr = titleAr;
        this.titleEN = titleEN;
        this.type = type;
        this.typeFor = typeFor;
        this.v = v;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTypeFor() {
        return typeFor;
    }

    public void setTypeFor(Integer typeFor) {
        this.typeFor = typeFor;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}