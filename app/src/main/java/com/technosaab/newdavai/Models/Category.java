package com.technosaab.newdavai.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

@SerializedName("ID")
@Expose
private Integer iD;
@SerializedName("title")
@Expose
private String title;
@SerializedName("titleAR")
@Expose
private String titleAR;
@SerializedName("imgPath")
@Expose
private String imgPath;
@SerializedName("status")
@Expose
private Integer status;
@SerializedName("Clients")
@Expose
private List<Object> clients = null;
@SerializedName("reserveUsers")
@Expose
private List<Object> reserveUsers = null;
@SerializedName("SubCategories")
@Expose
private List<Object> subCategories = null;

/**
* No args constructor for use in serialization
* 
*/
public Category() {
}

/**
* 
* @param title
* @param subCategories
* @param titleAR
* @param reserveUsers
* @param status
* @param imgPath
* @param iD
* @param clients
*/
public Category(Integer iD, String title, String titleAR, String imgPath, Integer status, List<Object> clients, List<Object> reserveUsers, List<Object> subCategories) {
super();
this.iD = iD;
this.title = title;
this.titleAR = titleAR;
this.imgPath = imgPath;
this.status = status;
this.clients = clients;
this.reserveUsers = reserveUsers;
this.subCategories = subCategories;
}

public Integer getID() {
return iD;
}

public void setID(Integer iD) {
this.iD = iD;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getTitleAR() {
return titleAR;
}

public void setTitleAR(String titleAR) {
this.titleAR = titleAR;
}

public String getImgPath() {
return imgPath;
}

public void setImgPath(String imgPath) {
this.imgPath = imgPath;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public List<Object> getClients() {
return clients;
}

public void setClients(List<Object> clients) {
this.clients = clients;
}

public List<Object> getReserveUsers() {
return reserveUsers;
}

public void setReserveUsers(List<Object> reserveUsers) {
this.reserveUsers = reserveUsers;
}

public List<Object> getSubCategories() {
return subCategories;
}

public void setSubCategories(List<Object> subCategories) {
this.subCategories = subCategories;
}

}