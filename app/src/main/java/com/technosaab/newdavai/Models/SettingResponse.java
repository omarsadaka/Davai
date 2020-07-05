package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingResponse {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("value")
@Expose
private String value;
@SerializedName("key")
@Expose
private String key;
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

public String getValue() {
return value;
}

public void setValue(String value) {
this.value = value;
}

public String getKey() {
return key;
}

public void setKey(String key) {
this.key = key;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

}