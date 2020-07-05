package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserByIdResponse {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("personalImg")
    @Expose
    private String personalImg;

    @SerializedName("countryID")
    @Expose
    private CountryID countryID;
    @SerializedName("cityID")
    @Expose
    private CityID cityID;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("status")
    @Expose
    private Integer status;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserByIdResponse() {
    }

    /**
     *
     * @param lastName
     * @param status
     * @param countryID
     * @param cityID
     * @param password
     * @param id
     * @param updatedAt
     * @param v
     * @param email
     * @param createdAt
     * @param gender
     * @param firstName
     * @param mobile
     */
    public UserByIdResponse(String id, String updatedAt, String createdAt, String firstName, String lastName, String mobile, String email, String password,
                            CountryID countryID, CityID cityID, Integer v, Integer gender, Integer status , String personalImg) {
        super();
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.countryID = countryID;
        this.cityID = cityID;
        this.v = v;
        this.gender = gender;
        this.status = status;
        this.personalImg = personalImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public CountryID getCountryID() {
        return countryID;
    }

    public void setCountryID(CountryID countryID) {
        this.countryID = countryID;
    }

    public CityID getCityID() {
        return cityID;
    }

    public void setCityID(CityID cityID) {
        this.cityID = cityID;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPersonalImg() {
        return personalImg;
    }

    public void setPersonalImg(String personalImg) {
        this.personalImg = personalImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public class CityID {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("titleAr")
        @Expose
        private String titleAr;
        @SerializedName("titleEN")
        @Expose
        private String titleEN;

        /**
         * No args constructor for use in serialization
         *
         */
        public CityID() {
        }

        /**
         *
         * @param id
         * @param titleAr
         * @param titleEN
         */
        public CityID(String id, String titleAr, String titleEN) {
            super();
            this.id = id;
            this.titleAr = titleAr;
            this.titleEN = titleEN;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitleAr() {
            return titleAr;
        }

        public void setTitleAr(String titleAr) {
            this.titleAr = titleAr;
        }

        public String getTitleEN() {
            return titleEN;
        }

        public void setTitleEN(String titleEN) {
            this.titleEN = titleEN;
        }

    }

    public class CountryID {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("titleAr")
        @Expose
        private String titleAr;
        @SerializedName("titleEN")
        @Expose
        private String titleEN;

        /**
         * No args constructor for use in serialization
         *
         */
        public CountryID() {
        }

        /**
         *
         * @param id
         * @param titleAr
         * @param titleEN
         */
        public CountryID(String id, String titleAr, String titleEN) {
            super();
            this.id = id;
            this.titleAr = titleAr;
            this.titleEN = titleEN;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitleAr() {
            return titleAr;
        }

        public void setTitleAr(String titleAr) {
            this.titleAr = titleAr;
        }

        public String getTitleEN() {
            return titleEN;
        }

        public void setTitleEN(String titleEN) {
            this.titleEN = titleEN;
        }

    }
}
