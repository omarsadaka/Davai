package com.technosaab.newdavai.Models;

import java.util.List;

public class ServiceModel {

    private String servicesID;
    private int price =0;
    private List<String> emp;

    public ServiceModel(String serviceId, String serviceName, int servicePrice, List<String> serviceEmployees) {
        this.servicesID = serviceId;
//        this.serviceName = serviceName;
        this.price = servicePrice;
        this.emp = serviceEmployees;
    }

    public ServiceModel() {
    }

    public String getServiceId() {
        return servicesID;
    }

    public void setServiceId(String serviceId) {
        this.servicesID = serviceId;
    }

//    public String getServiceName() {
//        return serviceName;
//    }
//
//    public void setServiceName(String serviceName) {
//        this.serviceName = serviceName;
//    }

    public int getServicePrice() {
        return price;
    }

    public void setServicePrice(int servicePrice) {
        this.price = servicePrice;
    }

    public List<String> getServiceEmployees() {
        return emp;
    }

    public void setServiceEmployees(List<String> serviceEmployees) {
        this.emp = serviceEmployees;
    }
}
