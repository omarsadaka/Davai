package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsImgModel {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("imgPath")
@Expose
private String imgPath;
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
public AdsImgModel() {
}

/**
* 
* @param updatedAt
* @param id
* @param v
* @param status
* @param createdAt
* @param imgPath
*/
public AdsImgModel(String id, String updatedAt, String createdAt, String imgPath, Integer v, Integer status) {
super();
this.id = id;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.imgPath = imgPath;
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

public String getImgPath() {
return imgPath;
}

public void setImgPath(String imgPath) {
this.imgPath = imgPath;
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