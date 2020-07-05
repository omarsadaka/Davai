package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingServiceResponse {

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
private Integer price;
@SerializedName("servicesID")
@Expose
private ServicesID servicesID;
@SerializedName("bookingID")
@Expose
private BookingID bookingID;
@SerializedName("employeeID")
@Expose
private EmployeeID employeeID;


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
public BookingServiceResponse() {
}

/**
* 
* @param updatedAt
* @param id
* @param v
* @param employeeID
* @param price
* @param bookingID
* @param status
* @param createdAt
* @param servicesID
*/
public BookingServiceResponse(String id, String updatedAt, String createdAt, Integer price, ServicesID servicesID, BookingID bookingID,
                              EmployeeID employeeID , Integer v, Integer status) {
super();
this.id = id;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.price = price;
this.servicesID = servicesID;
this.bookingID = bookingID;
this.employeeID = employeeID;
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

public Integer getPrice() {
return price;
}

public void setPrice(Integer price) {
this.price = price;
}

public ServicesID getServicesID() {
return servicesID;
}

public void setServicesID(ServicesID servicesID) {
this.servicesID = servicesID;
}

public BookingID getBookingID() {
return bookingID;
}

public void setBookingID(BookingID bookingID) {
this.bookingID = bookingID;
}

public EmployeeID getEmployeeID() {
return employeeID;
}

public void setEmployeeID(EmployeeID employeeID) {
this.employeeID = employeeID;
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