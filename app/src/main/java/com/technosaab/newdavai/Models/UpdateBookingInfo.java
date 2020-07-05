package com.technosaab.newdavai.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateBookingInfo {

    @SerializedName("bookingID")
    @Expose
    private String bookingID;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("services")
    @Expose
    private List<ServiceInfo> services;


    public static final Parcelable.Creator<BookingInfo> CREATOR = new Parcelable.Creator<BookingInfo>() {
        @Override
        public BookingInfo createFromParcel(Parcel in) {
            return new BookingInfo(in);
        }

        @Override
        public BookingInfo[] newArray(int size) {
            return new BookingInfo[size];
        }
    };

    public UpdateBookingInfo(String bookingID, String dateTime,  List<ServiceInfo> services) {
        this.bookingID = bookingID;
        this.dateTime = dateTime;
        this.services = services;

    }

    public UpdateBookingInfo(String bookingID, List<ServiceInfo> services) {
        this.bookingID = bookingID;
        this.services = services;
    }

    protected UpdateBookingInfo(Parcel in) {
        bookingID = in.readString();
        dateTime = in.readString();
        //services = in.re;
    }


    public String getClientID() {
        return bookingID;
    }

    public void setClientID(String clientID) {
        this.bookingID = clientID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<ServiceInfo> getServices() {
        return services;
    }

    public void setServices(List<ServiceInfo> services) {
        this.services = services;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bookingID);
        dest.writeValue(dateTime);
        dest.writeValue(services);

    }

    public int describeContents() {
        return 0;
    }
}
