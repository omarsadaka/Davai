package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

    public class AllCountries {

        @SerializedName("ID")
        @Expose
        private Integer iD;
        @SerializedName("TITLE")
        @Expose
        private String tITLE;
        @SerializedName("titleAR")
        @Expose
        private String titleAR;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("Cities")
        @Expose
        private List<Object> cities = null;
        @SerializedName("Clients")
        @Expose
        private List<Object> clients = null;

        /**
         * No args constructor for use in serialization
         *
         */
        public AllCountries() {
        }

        /**
         *
         * @param tITLE
         * @param titleAR
         * @param cities
         * @param status
         * @param iD
         * @param clients
         */
        public AllCountries(Integer iD, String tITLE, String titleAR, Integer status, List<Object> cities, List<Object> clients) {
            super();
            this.iD = iD;
            this.tITLE = tITLE;
            this.titleAR = titleAR;
            this.status = status;
            this.cities = cities;
            this.clients = clients;
        }

        public Integer getID() {
            return iD;
        }

        public void setID(Integer iD) {
            this.iD = iD;
        }

        public String getTITLE() {
            return tITLE;
        }

        public void setTITLE(String tITLE) {
            this.tITLE = tITLE;
        }

        public String getTitleAR() {
            return titleAR;
        }

        public void setTitleAR(String titleAR) {
            this.titleAR = titleAR;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<Object> getCities() {
            return cities;
        }

        public void setCities(List<Object> cities) {
            this.cities = cities;
        }

        public List<Object> getClients() {
            return clients;
        }

        public void setClients(List<Object> clients) {
            this.clients = clients;
        }

    }

