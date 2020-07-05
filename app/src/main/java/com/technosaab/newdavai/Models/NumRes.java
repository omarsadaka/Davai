package com.technosaab.newdavai.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumRes {

@SerializedName("message")
@Expose
private Integer message;

public Integer getMessage() {
return message;
}

public void setMessage(Integer message) {
this.message = message;
}

}