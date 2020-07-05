package com.technosaab.newdavai.Models;

/**
 * Created by manar on 1/15/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUser implements Parcelable
{
    @SerializedName("countryID")
    @Expose
    private String countryID;

    @SerializedName("cityID")
    @Expose
    private String cityID;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;

    public final static Creator<UpdateUser> CREATOR = new Creator<UpdateUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UpdateUser createFromParcel(Parcel in) {
            return new UpdateUser(in);
        }

        public UpdateUser[] newArray(int size) {
            return (new UpdateUser[size]);
        }

    }
            ;

    protected UpdateUser(Parcel in) {
        this.countryID=((String) in.readValue((String.class.getClassLoader())));
        this.cityID = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.mobile = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));

    }

    public UpdateUser(String countryID, String cityID, String email, String mobile, String password) {
        this.countryID = countryID;
        this.cityID = cityID;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public UpdateUser() {
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(countryID);
        dest.writeValue(cityID);
        dest.writeValue(email);
        dest.writeValue(mobile);
        dest.writeValue(password);
    }

    public int describeContents() {
        return 0;
    }

}
