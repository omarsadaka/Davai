package com.technosaab.newdavai.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientInfo implements Parcelable
{

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
    private int offDay;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("from")
    @Expose
    private int from;
    @SerializedName("to")
    @Expose
    private int to;
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

    @SerializedName("services")
    @Expose
    private List<ServiceModel> services;

    public final static Parcelable.Creator<ClientInfo> CREATOR = new Creator<ClientInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClientInfo createFromParcel(Parcel in) {
            return new ClientInfo(in);
        }

        public ClientInfo[] newArray(int size) {
            return (new ClientInfo[size]);
        }

    }
            ;

    protected ClientInfo(Parcel in) {
        this.brandName = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerName = ((String) in.readValue((String.class.getClassLoader())));
        this.vendorMobile = ((String) in.readValue((String.class.getClassLoader())));
        this.offDay = ((int) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.from = ((int) in.readValue((String.class.getClassLoader())));
        this.to = ((int) in.readValue((String.class.getClassLoader())));
        this.vendorAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.website = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerMobile = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.countryID = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryID = ((String) in.readValue((String.class.getClassLoader())));
        this.cityID = ((String) in.readValue((String.class.getClassLoader())));
        this.logo = ((String) in.readValue((String.class.getClassLoader())));
        this.cover = ((String) in.readValue((String.class.getClassLoader())));

    }

    /**
     * No args constructor for use in serialization
     *
     */
    public ClientInfo() {
    }

    /**
     *
     * @param logo
     * @param ownerName

     * @param categoryID
     * @param website
     * @param countryID
     * @param ownerMobile
     * @param cityID
     * @param vendorAddress
     * @param password
     * @param brandName
     * @param vendorMobile

     * @param cover

     * @param email

     * @param description
     * @param workingDays
     * @param ownerEmail
     */
    public ClientInfo( String brandName, String ownerName, String vendorMobile, int workingDays, String description,
                      int from , int to , String vendorAddress, String website, String ownerMobile, String email, String ownerEmail, String password, String countryID,
                      String categoryID, String cityID, String logo, String cover , List<ServiceModel> services) {
        super();
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
        this.services = services;

    }
    public ClientInfo(   String vendorMobile,  String description,
                     String vendorAddress, String website, String email,  String countryID,
                  String cityID, String logo, String cover ) {
        super();

        this.vendorMobile = vendorMobile;
        this.description = description;
        this.vendorAddress = vendorAddress;
        this.website = website;
        this.email = email;
        this.countryID = countryID;
        this.cityID = cityID;
        this.logo = logo;
        this.cover = cover;

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

    public int getOffDay() {
        return offDay;
    }

    public void setOffDay(int offDay) {
        this.offDay = offDay;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<ServiceModel> getServices() {
        return services;
    }

    public void setServices(List<ServiceModel> services) {
        this.services = services;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(brandName);
        dest.writeValue(ownerName);
        dest.writeValue(vendorMobile);
        dest.writeValue(offDay);
        dest.writeValue(description);
        dest.writeValue(from);
        dest.writeValue(to);
        dest.writeValue(vendorAddress);
        dest.writeValue(website);
        dest.writeValue(ownerMobile);
        dest.writeValue(email);
        dest.writeValue(ownerEmail);
        dest.writeValue(password);
        dest.writeValue(countryID);
        dest.writeValue(categoryID);
        dest.writeValue(cityID);
        dest.writeValue(logo);
        dest.writeValue(cover);

    }

    public int describeContents() {
        return 0;
    }

}