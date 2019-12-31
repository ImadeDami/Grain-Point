package com.zeathon.grainpoint_agent;

public class SpinnerModel {
    public SpinnerModel(String fName, String phoneNm, String crpTyp) {
        this.fName = fName;
        this.phoneNm = phoneNm;
        this.crpTyp = crpTyp;
    }

    private String fName, phoneNm, crpTyp;

    public SpinnerModel() {

    }

    public String getfName() {
        return fName;
    }

    public String getPhoneNm() {
        return phoneNm;
    }

    public String getCrpTyp() {
        return crpTyp;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setPhoneNm(String phoneNm) {
        this.phoneNm = phoneNm;
    }

    public void setCrpTyp(String crpTyp) {
        this.crpTyp = crpTyp;
    }

    @Override
    public String toString() {
        return phoneNm;
    }
}
