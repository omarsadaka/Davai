package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientServiceResponse {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("price")
@Expose
private String price;
@SerializedName("servicesID")
@Expose
private ServicesID servicesID;
@SerializedName("clientID")
@Expose
private String clientID;
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
public ClientServiceResponse() {
}

/**
* 
* @param updatedAt
* @param id
* @param v
* @param price
* @param status
* @param createdAt
* @param clientID
* @param servicesID
*/
public ClientServiceResponse(String id, String updatedAt, String createdAt, String price, ServicesID servicesID, String clientID, Integer v, Integer status) {
super();
this.id = id;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.price = price;
this.servicesID = servicesID;
this.clientID = clientID;
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

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

public ServicesID getServicesID() {
return servicesID;
}

public void setServicesID(ServicesID servicesID) {
this.servicesID = servicesID;
}

public String getClientID() {
return clientID;
}

public void setClientID(String clientID) {
this.clientID = clientID;
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

