package com.technosaab.newdavai.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsResponse {

@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("description")
@Expose
private String description;
@SerializedName("clientID")
@Expose
private String clientID;
@SerializedName("imgPath")
@Expose
private String imgPath;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("status")
@Expose
private Integer status;

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

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getClientID() {
return clientID;
}

public void setClientID(String clientID) {
this.clientID = clientID;
}

public String getImgPath() {
return imgPath;
}

public void setImgPath(String imgPath) {
this.imgPath = imgPath;
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