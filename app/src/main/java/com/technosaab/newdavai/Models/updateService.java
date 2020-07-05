package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class updateService {

    @SerializedName("clientID")
    @Expose
    private String clientID;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("servicesID")
    @Expose
    private String servicesID;

    @SerializedName("services")
    @Expose
    private List<EmployeeID> emplist;

    public updateService() {
    }

    public updateService(String clientID, String price, String servicesID, List<EmployeeID> emplist) {
        this.clientID = clientID;
        this.price = price;
        this.servicesID = servicesID;
        this.emplist = emplist;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServicesID() {
        return servicesID;
    }

    public void setServicesID(String servicesID) {
        this.servicesID = servicesID;
    }

    public List<EmployeeID> getEmplist() {
        return emplist;
    }

    public void setEmplist(List<EmployeeID> emplist) {
        this.emplist = emplist;
    }
}
