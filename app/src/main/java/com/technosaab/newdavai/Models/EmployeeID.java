package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeID {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("fullname")
@Expose
private String fullname;

/**
* No args constructor for use in serialization
* 
*/
public EmployeeID() {
}

/**
* 
* @param id
* @param fullname
*/
public EmployeeID(String id, String fullname) {
super();
this.id = id;
this.fullname = fullname;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getFullname() {
return fullname;
}

public void setFullname(String fullname) {
this.fullname = fullname;
}

}