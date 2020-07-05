package com.technosaab.newdavai.Models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse {

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
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("imgPath")
    @Expose
    private String imgPath;
    @SerializedName("status")
    @Expose
    private Integer status;

    /**
     * No args constructor for use in serialization
     *
     */
    public CategoryResponse() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param v
     * @param status
     * @param titleAr
     * @param createdAt
     * @param imgPath
     * @param titleEN
     */
    public CategoryResponse(String id, String updatedAt, String createdAt, String titleAr, String titleEN, Integer v, String imgPath, Integer status) {
        super();
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.titleAr = titleAr;
        this.titleEN = titleEN;
        this.v = v;
        this.imgPath = imgPath;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}