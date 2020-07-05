package com.technosaab.newdavai.Models;

public class ServiceInfo {

    private String servicesID;
    private String price;
    private String employeeID;

    public ServiceInfo() {
    }

    public ServiceInfo(String servicesID, String price, String employeeID) {
        this.servicesID = servicesID;
        this.price = price;
        this.employeeID = employeeID;
    }


    public String getServicesID() {
        return servicesID;
    }

    public void setServicesID(String servicesID) {
        this.servicesID = servicesID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
}
