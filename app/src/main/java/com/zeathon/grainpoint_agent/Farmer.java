package com.zeathon.grainpoint_agent;

public class Farmer {

    private String fullName;
    private String phone_number;
    private String idType;
    private String idNumber;
    private String bankName;
    private String bankAccount;
    private String bankVNum;
    private String farmSize;
    private String picture;
    private String gpsLocation;
    private String gender;
    private String state;
    private String lga;
    private String coopGroup;
    private String roleCGroup;
    private String cropFarmed;

    public Farmer(String fullName, String phone_number, String idType, String idNumber, String bankName, String bankAccount, String bankVNum, String farmSize, String picture, String gpsLocation, String gender, String state, String lga, String coopGroup, String roleCGroup, String cropFarmed) {
        this.fullName = fullName;
        this.phone_number = phone_number;
        this.idType = idType;
        this.idNumber = idNumber;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.bankVNum = bankVNum;
        this.farmSize = farmSize;
        this.picture = picture;
        this.gpsLocation = gpsLocation;
        this.gender = gender;
        this.state = state;
        this.lga = lga;
        this.coopGroup = coopGroup;
        this.roleCGroup = roleCGroup;
        this.cropFarmed = cropFarmed;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getIdType() {
        return idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankVNum() {
        return bankVNum;
    }

    public String getFarmSize() {
        return farmSize;
    }

    public String getPicture() {
        return picture;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public String getGender() {
        return gender;
    }

    public String getState() {
        return state;
    }

    public String getLga() {
        return lga;
    }

    public String getCoopGroup() {
        return coopGroup;
    }

    public String getRoleCGroup() {
        return roleCGroup;
    }

    public String getCropFarmed() {
        return cropFarmed;
    }
}
