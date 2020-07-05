package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employee {

        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("id")
        @Expose
        private String id;

        /**
         * No args constructor for use in serialization
         *
         */
        public Employee() {
        }

        /**
         *
         * @param id
         * @param fullname
         */
        public Employee(String fullname, String id) {
            super();
            this.fullname = fullname;
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }