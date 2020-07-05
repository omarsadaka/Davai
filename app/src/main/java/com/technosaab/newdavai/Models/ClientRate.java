package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientRate {

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
private String clientID;
@SerializedName("userID")
@Expose
private UserID userID;
@SerializedName("rate")
@Expose
private Integer rate;
@SerializedName("comment")
@Expose
private String comment;
@SerializedName("__v")
@Expose
private Integer v;

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

public String getClientID() {
return clientID;
}

public void setClientID(String clientID) {
this.clientID = clientID;
}

public UserID getUserID() {
return userID;
}

public void setUserID(UserID userID) {
this.userID = userID;
}

public Integer getRate() {
return rate;
}

public void setRate(Integer rate) {
this.rate = rate;
}

public String getComment() {
return comment;
}

public void setComment(String comment) {
this.comment = comment;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

}


