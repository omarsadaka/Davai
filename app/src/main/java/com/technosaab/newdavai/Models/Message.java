package com.technosaab.newdavai.Models;

public class Message {
    private String userImage;
    private String userMessage;
    private String vendorMessage;
    private int type;
    public static final int TYPE_MESSAGE_USER = 1;
    public static final int TYPE_MESSAGE_VENDOR = 2;

    public Message() {
    }

    public Message( String userMessage, String vendorMessage, int type) {
        this.userMessage = userMessage;
        this.vendorMessage = vendorMessage;
        this.type = type;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getVendorMessage() {
        return vendorMessage;
    }

    public void setVendorMessage(String vendorMessage) {
        this.vendorMessage = vendorMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
