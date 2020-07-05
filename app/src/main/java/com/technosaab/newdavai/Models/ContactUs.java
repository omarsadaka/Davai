package com.technosaab.newdavai.Models;

/**
 * Created by manar on 1/16/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUs implements Parcelable
{


    @SerializedName("title")
    @Expose
    private int title;

    @SerializedName("userID")
    @Expose
    private String userID;

    @SerializedName("msg")
    @Expose
    private String msg;
  public static final Creator<ContactUs> CREATOR = new Creator<ContactUs>() {
      @Override
      public ContactUs createFromParcel(Parcel in) {
          return new ContactUs(in);
      }

      @Override
      public ContactUs[] newArray(int size) {
          return new ContactUs[size];
      }
  };

    public ContactUs(int title , String userId , String msg ) {
        this.title =title;
        this.userID = userId;
        this.msg = msg;

    }
    protected ContactUs(Parcel in) {
        title = in.readInt();
        userID = in.readString();
        msg = in.readString();
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(userID);
        dest.writeValue(msg);

    }

    public int describeContents() {
        return 0;
    }

}



