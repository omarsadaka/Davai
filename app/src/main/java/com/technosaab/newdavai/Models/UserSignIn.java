package com.technosaab.newdavai.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSignIn {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    /*  public final static Parcelable.Creator<userInfo> CREATOR = new Creator<userInfo>() {


          @SuppressWarnings({
                  "unchecked"
          })
          public userInfo createFromParcel(Parcel in) {
              return new userInfo(in);
          }

          public userInfo[] newArray(int size) {
              return (new userInfo[size]);
          }

      }
              ;*/
    public static final Parcelable.Creator<UserSignIn> CREATOR = new Parcelable.Creator<UserSignIn>() {
        @Override
        public UserSignIn createFromParcel(Parcel in) {
            return new UserSignIn(in);
        }

        @Override
        public UserSignIn[] newArray(int size) {
            return new UserSignIn[size];
        }
    };

    public UserSignIn(String email  , String password) {
        this.email =email;
        this.password = password;
    }
    protected UserSignIn(Parcel in) {
        email = in.readString();
        password = in.readString();
    }


    public UserSignIn() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(password);
    }

    public int describeContents() {
        return 0;
    }
}
