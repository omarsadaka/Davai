package com.technosaab.newdavai.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientServiceResponse2 {

    @SerializedName("servicesID")
    @Expose
    private String servicesID;
    @SerializedName("servicesAr")
    @Expose
    private String servicesAr;
    @SerializedName("servicesEN")
    @Expose
    private String servicesEN;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("employees")
    @Expose
    private List<Employee> employees = null;
    @SerializedName("price")
    @Expose
    private String price;

    /**
     * No args constructor for use in serialization
     *
     */
    public ClientServiceResponse2() {
    }

    /**
     *
     * @param logo
     * @param cover
     * @param price
     * @param servicesAr
     * @param servicesEN
     * @param brandName
     * @param employees
     * @param servicesID
     */
    public ClientServiceResponse2(String servicesID, String servicesAr, String servicesEN, String brandName, String logo, String cover, List<Employee> employees, String price) {
        super();
        this.servicesID = servicesID;
        this.servicesAr = servicesAr;
        this.servicesEN = servicesEN;
        this.brandName = brandName;
        this.logo = logo;
        this.cover = cover;
        this.employees = employees;
        this.price = price;
    }

    public String getServicesID() {
        return servicesID;
    }

    public void setServicesID(String servicesID) {
        this.servicesID = servicesID;
    }

    public String getServicesAr() {
        return servicesAr;
    }

    public void setServicesAr(String servicesAr) {
        this.servicesAr = servicesAr;
    }

    public String getServicesEN() {
        return servicesEN;
    }

    public void setServicesEN(String servicesEN) {
        this.servicesEN = servicesEN;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }




}