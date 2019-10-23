package com.mobile.raspm;

public class PersonalData
{
    private String member_cityName;
    private String member_pm10Value;
    private String member_pm25Value;
    private int member_display;

    public String getMember_cityName() {
        return member_cityName;
    }

    public String getMember_pm10Value() {
        return member_pm10Value;
    }

    public String getMember_pm25Value() { return member_pm25Value; }
    public int getMember_display(){ return member_display; }

    public void setMember_cityName(String member_cityName) {
        this.member_cityName = member_cityName;
    }

    public void setMember_pm10Value(String member_pm10Value) {
        this.member_pm10Value = member_pm10Value;
    }

    public void setMember_pm25Value(String member_pm25Value) {
        this.member_pm25Value = member_pm25Value;
    }
    public void setMember_display(int member_display){
        this.member_display=member_display;
    }
}


