package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartChatResponse {

@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("userID")
@Expose
private String userID;
@SerializedName("clientID")
@Expose
private String clientID;
@SerializedName("title")
@Expose
private String title;
@SerializedName("_id")
@Expose
private String id;

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

public String getUserID() {
return userID;
}

public void setUserID(String userID) {
this.userID = userID;
}

public String getClientID() {
return clientID;
}

public void setClientID(String clientID) {
this.clientID = clientID;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

}