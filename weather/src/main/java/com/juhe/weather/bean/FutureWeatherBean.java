package com.juhe.weather.bean;

//该类封装了未来三天的天气情况
public class FutureWeatherBean {

    private String week;  //时间
    private String Weather_id; //天气id
    private String temp; //温度
    private String date;  //日期


    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeather_id() {
        return Weather_id;
    }

    public void setWeather_id(String weather_id) {
        Weather_id = weather_id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
