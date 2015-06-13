package com.juhe.weather.bean;

import java.util.List;

//该类封装了当天的天气情况及一些详细信息
public class WeatherBean {
    private String city; //当前城市
    private String release;//发布时间
    private String weather_id; //天气id,用于确定当时的天气状况，以便于显示对应的图片
    private String weather_str; //天气
    private String temp;  //当天温度
    private String now_temp;//当天实时温度
    private String felt_temp; //体感温度
    private String humidity; //湿度
    private String wind;//风力、风向
    private String uv_index;//紫外线强度
    private String dressing_index;//穿衣指数

    private List<FutureWeatherBean> futureList;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }

    public String getWeather_str() {
        return weather_str;
    }

    public void setWeather_str(String weather_str) {
        this.weather_str = weather_str;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getNow_temp() {
        return now_temp;
    }

    public void setNow_temp(String now_temp) {
        this.now_temp = now_temp;
    }

    public String getFelt_temp() {
        return felt_temp;
    }

    public void setFelt_temp(String felt_temp) {
        this.felt_temp = felt_temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getUv_index() {
        return uv_index;
    }

    public void setUv_index(String uv_index) {
        this.uv_index = uv_index;
    }

    public String getDressing_index() {
        return dressing_index;
    }

    public void setDressing_index(String dressing_index) {
        this.dressing_index = dressing_index;
    }

    public List<FutureWeatherBean> getFutureList() {
        return futureList;
    }

    public void setFutureList(List<FutureWeatherBean> futureList) {
        this.futureList = futureList;
    }
}
