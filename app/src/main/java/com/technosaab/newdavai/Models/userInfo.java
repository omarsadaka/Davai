package com.technosaab.newdavai.Models;

/**
 * Created by manar on 1/16/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class userInfo implements Parcelable
{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("gender")
    @Expose
    private int gender;

    @SerializedName("countryID")
    @Expose
    private String countryID;

    @SerializedName("cityID")
    @Expose
    private String cityID;

    @SerializedName("personalImg")
    @Expose
    private String personImg;


  public static final Creator<userInfo> CREATOR = new Creator<userInfo>() {
      @Override
      public userInfo createFromParcel(Parcel in) {
          return new userInfo(in);
      }

      @Override
      public userInfo[] newArray(int size) {
          return new userInfo[size];
      }
  };

    public userInfo(String email, String firstName, String lastName, String mobile, String password, int gender, String countryID, String cityID) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.password = password;
        this.gender = gender;
        this.countryID = countryID;
        this.cityID = cityID;
    }

    public userInfo(String email, String mobile, String countryID, String cityID) {
        this.email = email;
        this.mobile = mobile;
        this.countryID = countryID;
        this.cityID = cityID;
    }

    public userInfo(String email, String mobile, String countryID, String cityID, String personImg) {
        this.email = email;
        this.mobile = mobile;
        this.countryID = countryID;
        this.cityID = cityID;
        this.personImg = personImg;
    }

    public userInfo(String password) {
        this.password = password;
    }

    protected userInfo(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        mobile = in.readString();
        email = in.readString();
        password = in.readString();
        gender = in.readInt();
        countryID = in.readString();
        cityID = in.readString();


    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(mobile);
        dest.writeValue(password);
        dest.writeValue(gender);
        dest.writeValue(countryID);
        dest.writeValue(cityID);
    }

    public int describeContents() {
        return 0;
    }

}



