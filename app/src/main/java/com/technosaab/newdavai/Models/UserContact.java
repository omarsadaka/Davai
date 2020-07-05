package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserContact {

@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("title")
@Expose
private String title;
@SerializedName("userID")
@Expose
private String userID;
@SerializedName("msg")
@Expose
private String msg;
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
public UserContact() {
}

/**
* 
* @param id
* @param updatedAt
* @param userID
* @param v
* @param title
* @param status
* @param createdAt
* @param msg
*/
public UserContact(Integer v, String updatedAt, String createdAt, String title, String userID, String msg, String id, Integer status) {
super();
this.v = v;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.title = title;
this.userID = userID;
this.msg = msg;
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

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getUserID() {
return userID;
}

public void setUserID(String userID) {
this.userID = userID;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
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