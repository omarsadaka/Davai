package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteResponse {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("clientID")
    @Expose
    private ClientID clientID;
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
    public FavouriteResponse() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param v
     * @param userID
     * @param status
     * @param createdAt
     * @param clientID
     */
    public FavouriteResponse(String id, String updatedAt, String createdAt, ClientID clientID, String userID, Integer v, Integer status ) {
        super();
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.clientID = clientID;
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

    public ClientID getClientID() {
        return clientID;
    }

    public void setClientID(ClientID clientID) {
        this.clientID = clientID;
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
