package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityID {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("titleAr")
    @Expose
    private String titleAr;
    @SerializedName("titleEN")
    @Expose
    private String titleEN;

    /**
     * No args constructor for use in serialization
     *
     */
    public CityID() {
    }

    /**
     *
     * @param id
     * @param titleAr
     * @param titleEN
     */
    public CityID(String id, String titleAr, String titleEN) {
        super();
        this.id = id;
        this.titleAr = titleAr;
        this.titleEN = titleEN;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
