package com.technosaab.newdavai.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorResponse {

@SerializedName("ID")
@Expose
private Integer iD;
@SerializedName("username")
@Expose
private String username;
@SerializedName("password")
@Expose
private String password;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("email")
@Expose
private String email;
@SerializedName("Country_ID")
@Expose
private Integer countryID;
@SerializedName("city_ID")
@Expose
private Integer cityID;
@SerializedName("location")
@Expose
private String location;
@SerializedName("brundName")
@Expose
private String brundName;
@SerializedName("registerCode")
@Expose
private Object registerCode;
@SerializedName("statusActive")
@Expose
private Integer statusActive;
@SerializedName("forgetPass")
@Expose
private Object forgetPass;
@SerializedName("img1")
@Expose
private String img1;
@SerializedName("img2")
@Expose
private String img2;
@SerializedName("img3")
@Expose
private String img3;
@SerializedName("img4")
@Expose
private String img4;
@SerializedName("img5")
@Expose
private String img5;
@SerializedName("img6")
@Expose
private String img6;
@SerializedName("img7")
@Expose
private String img7;
@SerializedName("img8")
@Expose
private String img8;
@SerializedName("img9")
@Expose
private String img9;
@SerializedName("img10")
@Expose
private String img10;
@SerializedName("description")
@Expose
private String description;
@SerializedName("street")
@Expose
private String street;
@SerializedName("district")
@Expose
private String district;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("message_Status")
@Expose
private Integer messageStatus;
@SerializedName("allow_Reseve")
@Expose
private Integer allowReseve;
@SerializedName("CategoryID")
@Expose
private Integer categoryID;
@SerializedName("from1")
@Expose
private Integer from1;
@SerializedName("to1")
@Expose
private Integer to1;
@SerializedName("from2")
@Expose
private Integer from2;
@SerializedName("to2")
@Expose
private Integer to2;
@SerializedName("imgPath")
@Expose
private Object imgPath;
@SerializedName("userType")
@Expose
private Integer userType;
@SerializedName("clientID")
@Expose
private Integer clientID;
@SerializedName("empType")
@Expose
private Integer empType;

/**
* No args constructor for use in serialization
* 
*/
public VendorResponse() {
}

/**
* 
* @param phone
* @param categoryID
* @param location
* @param countryID
* @param street
* @param clientID
* @param allowReseve
* @param password
* @param userType
* @param username
* @param description
* @param imgPath
* @param to1
* @param to2
* @param statusActive
* @param district
* @param messageStatus
* @param img2
* @param img1
* @param cityID
* @param registerCode
* @param iD
* @param from2
* @param empType
* @param email
* @param img10
* @param img9
* @param img7
* @param img8
* @param forgetPass
* @param img5
* @param from1
* @param img6
* @param brundName
* @param img3
* @param mobile
* @param img4
*/
public VendorResponse(Integer iD, String username, String password, String mobile, String email, Integer countryID, Integer cityID, String location, String brundName, Object registerCode, Integer statusActive, Object forgetPass, String img1, String img2, String img3, String img4, String img5, String img6, String img7, String img8, String img9, String img10, String description, String street, String district, String phone, Integer messageStatus, Integer allowReseve, Integer categoryID, Integer from1, Integer to1, Integer from2, Integer to2, Object imgPath, Integer userType, Integer clientID, Integer empType) {
super();
this.iD = iD;
this.username = username;
this.password = password;
this.mobile = mobile;
this.email = email;
this.countryID = countryID;
this.cityID = cityID;
this.location = location;
this.brundName = brundName;
this.registerCode = registerCode;
this.statusActive = statusActive;
this.forgetPass = forgetPass;
this.img1 = img1;
this.img2 = img2;
this.img3 = img3;
this.img4 = img4;
this.img5 = img5;
this.img6 = img6;
this.img7 = img7;
this.img8 = img8;
this.img9 = img9;
this.img10 = img10;
this.description = description;
this.street = street;
this.district = district;
this.phone = phone;
this.messageStatus = messageStatus;
this.allowReseve = allowReseve;
this.categoryID = categoryID;
this.from1 = from1;
this.to1 = to1;
this.from2 = from2;
this.to2 = to2;
this.imgPath = imgPath;
this.userType = userType;
this.clientID = clientID;
this.empType = empType;
}

public Integer getID() {
return iD;
}

public void setID(Integer iD) {
this.iD = iD;
}

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
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

public Integer getCountryID() {
return countryID;
}

public void setCountryID(Integer countryID) {
this.countryID = countryID;
}

public Integer getCityID() {
return cityID;
}

public void setCityID(Integer cityID) {
this.cityID = cityID;
}

public String getLocation() {
return location;
}

public void setLocation(String location) {
this.location = location;
}

public String getBrundName() {
return brundName;
}

public void setBrundName(String brundName) {
this.brundName = brundName;
}

public Object getRegisterCode() {
return registerCode;
}

public void setRegisterCode(Object registerCode) {
this.registerCode = registerCode;
}

public Integer getStatusActive() {
return statusActive;
}

public void setStatusActive(Integer statusActive) {
this.statusActive = statusActive;
}

public Object getForgetPass() {
return forgetPass;
}

public void setForgetPass(Object forgetPass) {
this.forgetPass = forgetPass;
}

public String getImg1() {
return img1;
}

public void setImg1(String img1) {
this.img1 = img1;
}

public String getImg2() {
return img2;
}

public void setImg2(String img2) {
this.img2 = img2;
}

public String getImg3() {
return img3;
}

public void setImg3(String img3) {
this.img3 = img3;
}

public String getImg4() {
return img4;
}

public void setImg4(String img4) {
this.img4 = img4;
}

public String getImg5() {
return img5;
}

public void setImg5(String img5) {
this.img5 = img5;
}

public String getImg6() {
return img6;
}

public void setImg6(String img6) {
this.img6 = img6;
}

public String getImg7() {
return img7;
}

public void setImg7(String img7) {
this.img7 = img7;
}

public String getImg8() {
return img8;
}

public void setImg8(String img8) {
this.img8 = img8;
}

public String getImg9() {
return img9;
}

public void setImg9(String img9) {
this.img9 = img9;
}

public String getImg10() {
return img10;
}

public void setImg10(String img10) {
this.img10 = img10;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getStreet() {
return street;
}

public void setStreet(String street) {
this.street = street;
}

public String getDistrict() {
return district;
}

public void setDistrict(String district) {
this.district = district;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public Integer getMessageStatus() {
return messageStatus;
}

public void setMessageStatus(Integer messageStatus) {
this.messageStatus = messageStatus;
}

public Integer getAllowReseve() {
return allowReseve;
}

public void setAllowReseve(Integer allowReseve) {
this.allowReseve = allowReseve;
}

public Integer getCategoryID() {
return categoryID;
}

public void setCategoryID(Integer categoryID) {
this.categoryID = categoryID;
}

public Integer getFrom1() {
return from1;
}

public void setFrom1(Integer from1) {
this.from1 = from1;
}

public Integer getTo1() {
return to1;
}

public void setTo1(Integer to1) {
this.to1 = to1;
}

public Integer getFrom2() {
return from2;
}

public void setFrom2(Integer from2) {
this.from2 = from2;
}

public Integer getTo2() {
return to2;
}

public void setTo2(Integer to2) {
this.to2 = to2;
}

public Object getImgPath() {
return imgPath;
}

public void setImgPath(Object imgPath) {
this.imgPath = imgPath;
}

public Integer getUserType() {
return userType;
}

public void setUserType(Integer userType) {
this.userType = userType;
}

public Integer getClientID() {
return clientID;
}

public void setClientID(Integer clientID) {
this.clientID = clientID;
}

public Integer getEmpType() {
return empType;
}

public void setEmpType(Integer empType) {
this.empType = empType;
}

}