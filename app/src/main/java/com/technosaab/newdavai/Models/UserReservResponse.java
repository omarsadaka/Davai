package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReservResponse {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("dateTime")
@Expose
private String dateTime;
@SerializedName("clientID")
@Expose
private ReservClientId reservClientId;
@SerializedName("userID")
@Expose
private String userID;
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
public UserReservResponse() {
}

/**
* 
* @param updatedAt
* @param id
* @param v
* @param userID
* @param status
* @param dateTime
* @param createdAt
* @param reservClientId
*/
public UserReservResponse(String id, String updatedAt, String createdAt, String dateTime, ReservClientId reservClientId, String userID, Integer v, Integer status) {
super();
this.id = id;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.dateTime = dateTime;
this.reservClientId = reservClientId;
this.userID = userID;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ReservClientId getReservClientId() {
        return reservClientId;
    }

    public void setReservClientId(ReservClientId reservClientId) {
        this.reservClientId = reservClientId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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