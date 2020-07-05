package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFavResponse {

@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("clientID")
@Expose
private String clientID;
@SerializedName("userID")
@Expose
private String userID;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("status")
@Expose
private Integer status;

/**
* No args constructor for use in serialization
* 
*/
public AddFavResponse() {
}

/**
* 
* @param id
* @param updatedAt
* @param userID
* @param v
* @param status
* @param createdAt
* @param clientID
*/
public AddFavResponse(Integer v, String updatedAt, String createdAt, String clientID, String userID, String id, Integer status) {
super();
this.v = v;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.clientID = clientID;
this.userID = userID;
this.id = id;
this.status = status;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
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

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

}