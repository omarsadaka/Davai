package com.technosaab.newdavai.Models;

/**
 * Created by manar on 1/16/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingInfo implements Parcelable
{

    @SerializedName("clientID")
    @Expose
    private String clientID;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("services")
    @Expose
    private List<ServiceInfo> services;


  public static final Creator<BookingInfo> CREATOR = new Creator<BookingInfo>() {
      @Override
      public BookingInfo createFromParcel(Parcel in) {
          return new BookingInfo(in);
      }

      @Override
      public BookingInfo[] newArray(int size) {
          return new BookingInfo[size];
      }
  };

    public BookingInfo(String clientID, String dateTime, String userID, List<ServiceInfo> services) {
        this.clientID = clientID;
        this.dateTime = dateTime;
        this.userID = userID;
        this.services = services;

    }

    protected BookingInfo(Parcel in) {
        clientID = in.readString();
        dateTime = in.readString();
        userID = in.readString();
        //services = in.re;
    }


    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<ServiceInfo> getServices() {
        return services;
    }

    public void setServices(List<ServiceInfo> services) {
        this.services = services;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clientID);
        dest.writeValue(dateTime);
        dest.writeValue(userID);
        dest.writeValue(services);

    }

    public int describeContents() {
        return 0;
    }

}



