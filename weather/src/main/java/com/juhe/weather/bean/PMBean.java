package com.juhe.weather.bean;

//该类封装了空气质量的相关信息
public class PMBean {

    private String aqi;  //空气质量指数
    private String quality;

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
