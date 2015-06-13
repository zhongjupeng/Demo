package com.juhe.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.juhe.weather.bean.FutureWeatherBean;
import com.juhe.weather.bean.HoursWeatherBean;
import com.juhe.weather.bean.PMBean;
import com.juhe.weather.bean.WeatherBean;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

//该类用于在后台解析获取到的天气数据
public class WeatherService extends Service{

    private WeatherServiceBinder binder = new WeatherServiceBinder();
    private boolean isRunning = false;
    private WeatherBean weatherBean = null;
    private PMBean pmBean = null;
    private List<HoursWeatherBean> list = null;
    private OnParserCallBack callBack;
    private final int REPEAT_MSG = 0x01;
    private final int CALLBACK_OK = 0x02;
    private final int CALLBACK_ERROR = 0x04;
    private String city;


    public interface OnParserCallBack{
        public void OnParserComplete(WeatherBean weatherBean,List<HoursWeatherBean> list ,PMBean pmBean);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.sendEmptyMessage(REPEAT_MSG);
        city = "北京";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REPEAT_MSG:
                    getWeather();
                    sendEmptyMessageDelayed(REPEAT_MSG,30*60*1000);
                    break;
                case CALLBACK_OK:
                    if(callBack!=null){
                        callBack.OnParserComplete(weatherBean,list,pmBean);
                    }
                    isRunning = false;
                    break;
                case CALLBACK_ERROR:

                    break;
            }
        }
    };

    public void setCallBack(OnParserCallBack callBack){
        this.callBack = callBack;
    }

    public void removeCallBack(){
        this.callBack = null;
    }

    public void getWeather(String city){
        this.city = city;
        getWeather();
    }
   //获得全国参数JSON返回值
   public void getWeather(){

       if (isRunning) {
           return;
       }
       isRunning = true;
       final CountDownLatch count = new CountDownLatch(3);
       Parameters parametersCitys = new Parameters();
       parametersCitys.add("cityname", city);
       parametersCitys.add("format", 2);
        JuheData.executeWithAPI(getApplicationContext(), 39, "http://v.juhe.cn/weather/index", JuheData.GET, parametersCitys, new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                try {
                    weatherBean = parserWeather(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                count.countDown();

            }

            @Override
            public void onFinish() {
                Log.i("zjp", "onFinish");
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {

            }
        });

        //获得城市天气3小时预报JSON数据
        Parameters parametersForecast3h = new Parameters();
        parametersForecast3h.add("cityname",city);
        JuheData.executeWithAPI(this,39,"http://v.juhe.cn/weather/forecast3h",JuheData.GET,parametersForecast3h,new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                Log.i("zjp","successful:"+s);
                try{
                    list = parser3hForecast(new JSONObject(s));
                }catch (Exception e){
                    e.printStackTrace();
                }
                count.countDown();

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {


            }
        });

        //获得空气质量
        Parameters parametersPM = new Parameters();
        parametersPM.add("city",city);
        JuheData.executeWithAPI(this,33,"http://web.juhe.cn:8080/environment/air/pm",JuheData.GET,parametersPM,new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                Log.i("zjp","successfulPM:"+s);
                try{
                   pmBean = parserPM(new JSONObject(s));
                }catch (Exception e){
                    e.printStackTrace();
                }
                count.countDown();

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Log.i("zjp", "FailurePM:" + s);
            }
        });

       new Thread(){
           @Override
           public void run() {
               super.run();
               try{
                   count.await();
                   mHandler.sendEmptyMessage(CALLBACK_OK);
               }catch (Exception e){
                   e.printStackTrace();
                   mHandler.sendEmptyMessage(CALLBACK_ERROR);
               }
           }
       }.start();

    }

   //解析城市查询接口
   private WeatherBean parserWeather(JSONObject json){
    WeatherBean bean = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    try{
        int code = json.getInt("resultcode");
        int error_code = json.getInt("error_code");

        if(code == 200 && error_code == 0){
            JSONObject jsonResult = json.getJSONObject("result");
            bean = new WeatherBean();

            //解析today
            JSONObject jsonToday = jsonResult.getJSONObject("today");
            bean.setTemp(jsonToday.getString("temperature"));
            bean.setWeather_str(jsonToday.getString("weather"));
            bean.setWeather_id(jsonToday.getJSONObject("weather_id").getString("fa"));
            bean.setCity(jsonToday.getString("city"));
            bean.setWind(jsonToday.getString("wind"));
            bean.setDressing_index(jsonToday.getString("dressing_advice"));
            bean.setUv_index(jsonToday.getString("uv_index"));

            //解析sk
            JSONObject jsonSk = jsonResult.getJSONObject("sk");
            bean.setNow_temp(jsonSk.getString("temp"));
            //bean.setWind(jsonSk.getString("wind_direction")+"、"+jsonSk.getString("wind_strength"));
            bean.setHumidity(jsonSk.getString("humidity"));
            bean.setRelease(jsonSk.getString("time") + "发布");

            //解析future
            JSONArray jsonArray = jsonResult.getJSONArray("future");
            List<FutureWeatherBean> futureWeatherBeanList = new ArrayList<FutureWeatherBean>();
            Date date = new Date(System.currentTimeMillis());
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonFuture = jsonArray.getJSONObject(i);
                FutureWeatherBean futureWeatherBean = new FutureWeatherBean();
                Date datef = sdf.parse(jsonFuture.getString("date"));
                if(!datef.after(date)){
                    continue;
                }
                futureWeatherBean.setWeek(jsonFuture.getString("week"));
                futureWeatherBean.setTemp(jsonFuture.getString("temperature"));
                futureWeatherBean.setWeather_id(jsonFuture.getJSONObject("weather_id").getString("fa"));


                futureWeatherBeanList.add(futureWeatherBean);
                if(futureWeatherBeanList.size()==3){
                    break;
                }
            }

            bean.setFutureList(futureWeatherBeanList);
            Log.i("zjp", "futureListsize:" + futureWeatherBeanList.size());
        }else {
            Toast.makeText(getApplicationContext(), "WEATHER_ERROR", Toast.LENGTH_SHORT).show();
        }
    }catch (Exception e){
        e.printStackTrace();
    }

    return bean;

}


    //解析城市天气3小时预报
    private List<HoursWeatherBean> parser3hForecast(JSONObject json){

        List<HoursWeatherBean> list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date(System.currentTimeMillis());
        try{
            int resulecode = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if(resulecode == 200 && error_code ==0){
                list = new ArrayList<HoursWeatherBean>();
                JSONArray jsonResult = json.getJSONArray("result");
                for(int i=0;i<jsonResult.length();i++){
                    HoursWeatherBean bean = new HoursWeatherBean();
                    JSONObject jsonObject = jsonResult.getJSONObject(i);
                    Date date1 = sdf.parse(jsonObject.getString("sfdate"));
                    if(!date1.after(date)){
                        continue;
                    }
                    bean.setTemp(jsonObject.getString("temp1"));
                    bean.setWeather_id(jsonObject.getString("weatherid"));
                    Calendar c = Calendar.getInstance();
                    c.setTime(date1);
                    bean.setTime(c.get(Calendar.HOUR_OF_DAY)+"");
                    list.add(bean);
                    if(list.size()==5){
                        break;
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(),"HOURES_ERROR",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;

    }


    //解析空气质量
    private PMBean parserPM(JSONObject json){
        PMBean pmBean = null;
        try{
            int resultcode = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if(resultcode == 200 && error_code == 0){
                pmBean = new PMBean();
                pmBean.setAqi(((JSONObject) json.getJSONArray("result").get(0)).getString("AQI"));
                pmBean.setQuality(((JSONObject)json.getJSONArray("result").get(0)).getString("quality"));
            }else{
                Toast.makeText(getApplicationContext(),"PM_ERROR",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return pmBean;
    }


    //获得该服务对象
    public class WeatherServiceBinder extends Binder{
        public WeatherService getService(){
            return WeatherService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.i("zjp","Service Destory");
        super.onDestroy();
    }
}
