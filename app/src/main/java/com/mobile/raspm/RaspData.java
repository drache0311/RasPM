package com.mobile.raspm;


public class RaspData
{
    private double member_latitude;
    private double member_longitude;
    private int member_pm10;
    private int member_pm25;

    public double getMember_latitude() {
        return member_latitude;
    }

    public double getMember_longitude() {
        return member_longitude;
    }

    public int getMember_pm10() {
        return member_pm10;
    }
    public int getMember_pm25() {
        return member_pm25;
    }

    public void setMember_latitude(double member_latitude) {
        this.member_latitude = member_latitude;
    }

    public void setMember_longitude(double member_longitude) {
        this.member_longitude = member_longitude;
    }

    public void setMember_pm10(int member_pm10) {
        this.member_pm10 = member_pm10;
    }
    public void setMember_pm25(int member_pm25){
        this.member_pm25 = member_pm25;
    }
}



