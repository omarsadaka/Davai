package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartChatMessage {


@SerializedName("userID")
@Expose
private String userID;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StartChatMessage(String userID, String clientID, String title) {
        this.userID = userID;
        this.clientID = clientID;
        this.title = title;
    }

    @SerializedName("clientID")

@Expose
private String clientID;
@SerializedName("title")
@Expose
private String title;



public String getUserID() {
return userID;
}

public void setUserID(String userID) {
this.userID = userID;
}

public String getClientID() {
return clientID;
}

public void setClientID(String clientID) {
this.clientID = clientID;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}



}