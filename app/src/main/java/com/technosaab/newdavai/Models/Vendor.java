package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Vendor {

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
    @SerializedName("offDay")
    @Expose
    private Integer offDay;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("from")
    @Expose
    private Integer from;

    @SerializedName("to")
    @Expose
    private Integer to;
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
    private CountryID countryID;
    @SerializedName("categoryID")
    @Expose
    private CategoryID categoryID;
    @SerializedName("cityID")
    @Expose
    private CityID cityID;
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
    public Vendor() {
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

     * @param email
     * @param createdAt
     * @param description
     * @param workingDays
     * @param ownerEmail
     */
    public Vendor(String id, String updatedAt, String createdAt, String brandName, String ownerName, String vendorMobile, Integer workingDays, String description,
                  Integer from, Integer to ,String vendorAddress, String website, String ownerMobile, String email, String ownerEmail, String password,
                  CountryID countryID, CategoryID categoryID, CityID cityID, String logo, String cover, Integer v, Integer userType, Integer status ,String totalRate) {
        super();
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.brandName = brandName;
        this.ownerName = ownerName;
        this.vendorMobile = vendorMobile;
        this.offDay = workingDays;
        this.description = description;
        this.from = from;
        this.to = to;
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



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOffDay() {
        return offDay;
    }

    public void setOffDay(Integer offDay) {
        this.offDay = offDay;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
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

    public CountryID getCountryID() {
        return countryID;
    }

    public void setCountryID(CountryID countryID) {
        this.countryID = countryID;
    }

    public CategoryID getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(CategoryID categoryID) {
        this.categoryID = categoryID;
    }

    public CityID getCityID() {
        return cityID;
    }

    public void setCityID(CityID cityID) {
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


