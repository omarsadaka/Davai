package com.technosaab.newdavai.Models;

/**
 * Created by manar on 1/16/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFavourite implements Parcelable
{


    @SerializedName("clientID")
    @Expose
    private String client_ID;
    @SerializedName("userID")
    @Expose
    private String user_ID;
  public static final Creator<AddFavourite> CREATOR = new Creator<AddFavourite>() {
      @Override
      public AddFavourite createFromParcel(Parcel in) {
          return new AddFavourite(in);
      }

      @Override
      public AddFavourite[] newArray(int size) {
          return new AddFavourite[size];
      }
  };

    public AddFavourite(String client_ID , String user_ID  ) {
        this.client_ID =client_ID;
        this.user_ID = user_ID;

    }
    protected AddFavourite(Parcel in) {
        client_ID = in.readString();
        user_ID = in.readString();
    }


    public AddFavourite() {
    }

    public String getClient_ID() {
        return client_ID;
    }

    public void setClient_ID(String client_ID) {
        this.client_ID = client_ID;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(client_ID);
        dest.writeValue(user_ID);

    }

    public int describeContents() {
        return 0;
    }

}



