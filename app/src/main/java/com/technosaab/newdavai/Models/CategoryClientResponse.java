package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryClientResponse {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("brandName")
@Expose
private String brandName;
@SerializedName("ownerName")
@Expose
private String ownerName;
@SerializedName("VendorMobile")
@Expose
private String vendorMobile;
@SerializedName("workingDays")
@Expose
private String workingDays;
@SerializedName("description")
@Expose
private String description;
@SerializedName("workingHours")
@Expose
private String workingHours;
@SerializedName("VendorAddress")
@Expose
private String vendorAddress;
@SerializedName("website")
@Expose
private String website;
@SerializedName("ownerMobile")
@Expose
private String ownerMobile;
@SerializedName("email")
@Expose
private String email;
@SerializedName("OwnerEmail")
@Expose
private String ownerEmail;
@SerializedName("password")
@Expose
private String password;
@SerializedName("countryID")
@Expose
private String countryID;
@SerializedName("categoryID")
@Expose
private String categoryID;
@SerializedName("cityID")
@Expose
private String cityID;
@SerializedName("logo")
@Expose
private String logo;
@SerializedName("cover")
@Expose
private String cover;
@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("userType")
@Expose
private Integer userType;
@SerializedName("status")
@Expose
private Integer status;

    @SerializedName("totalRate")
    @Expose
    private String totalRate;


/**
* No args constructor for use in serialization
* 
*/
public CategoryClientResponse() {
}

/**
* 
* @param logo
* @param ownerName
* @param status
* @param categoryID
* @param website
* @param countryID
* @param ownerMobile
* @param cityID
* @param vendorAddress
* @param password
* @param brandName
* @param userType
* @param vendorMobile
* @param id
* @param updatedAt
* @param v
* @param cover
* @param workingHours
* @param email
* @param createdAt
* @param description
* @param workingDays
* @param ownerEmail
*/
public CategoryClientResponse(String id, String updatedAt, String createdAt, String brandName, String ownerName, String vendorMobile, String workingDays,
                              String description, String workingHours, String vendorAddress, String website, String ownerMobile, String email, String ownerEmail,
                              String password, String countryID, String categoryID, String cityID, String logo, String cover, Integer v, Integer userType, Integer status , String totalRate) {
super();
this.id = id;
this.updatedAt = updatedAt;
this.createdAt = createdAt;
this.brandName = brandName;
this.ownerName = ownerName;
this.vendorMobile = vendorMobile;
this.workingDays = workingDays;
this.description = description;
this.workingHours = workingHours;
this.vendorAddress = vendorAddress;
this.website = website;
this.ownerMobile = ownerMobile;
this.email = email;
this.ownerEmail = ownerEmail;
this.password = password;
this.countryID = countryID;
this.categoryID = categoryID;
this.cityID = cityID;
this.logo = logo;
this.cover = cover;
this.v = v;
this.userType = userType;
this.status = status;
this.totalRate = totalRate;
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

public String getBrandName() {
return brandName;
}

public void setBrandName(String brandName) {
this.brandName = brandName;
}

public String getOwnerName() {
return ownerName;
}

public void setOwnerName(String ownerName) {
this.ownerName = ownerName;
}

public String getVendorMobile() {
return vendorMobile;
}

public void setVendorMobile(String vendorMobile) {
this.vendorMobile = vendorMobile;
}

public String getWorkingDays() {
return workingDays;
}

public void setWorkingDays(String workingDays) {
this.workingDays = workingDays;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getWorkingHours() {
return workingHours;
}

public void setWorkingHours(String workingHours) {
this.workingHours = workingHours;
}

public String getVendorAddress() {
return vendorAddress;
}

public void setVendorAddress(String vendorAddress) {
this.vendorAddress = vendorAddress;
}

public String getWebsite() {
return website;
}

public void setWebsite(String website) {
this.website = website;
}

public String getOwnerMobile() {
return ownerMobile;
}

public void setOwnerMobile(String ownerMobile) {
this.ownerMobile = ownerMobile;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getOwnerEmail() {
return ownerEmail;
}

public void setOwnerEmail(String ownerEmail) {
this.ownerEmail = ownerEmail;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getCountryID() {
return countryID;
}

public void setCountryID(String countryID) {
this.countryID = countryID;
}

public String getCategoryID() {
return categoryID;
}

public void setCategoryID(String categoryID) {
this.categoryID = categoryID;
}

public String getCityID() {
return cityID;
}

public void setCityID(String cityID) {
this.cityID = cityID;
}

public String getLogo() {
return logo;
}

public void setLogo(String logo) {
this.logo = logo;
}

public String getCover() {
return cover;
}

public void setCover(String cover) {
this.cover = cover;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

public Integer getUserType() {
return userType;
}

public void setUserType(Integer userType) {
this.userType = userType;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }
}