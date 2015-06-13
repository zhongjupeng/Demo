package com.juhe.weather;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.juhe.weather.bean.FutureWeatherBean;
import com.juhe.weather.bean.HoursWeatherBean;
import com.juhe.weather.bean.PMBean;
import com.juhe.weather.bean.WeatherBean;
import com.juhe.weather.service.WeatherService;
import com.juhe.weather.swiperefresh.PullToRefreshBase;
import com.juhe.weather.swiperefresh.PullToRefreshScrollView;
import java.util.Calendar;
import java.util.List;

public class WeatherActivity extends Activity {

    private Context mContext;
    private WeatherService mService;
    Intent intent;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private RelativeLayout rl_city;
    private TextView tv_city,  //当前显示城市
            tv_realease, //发布时间
            tv_now_weather,//现在的天气
            tv_today_temp,//今天的温度
            tv_now_temp,//现在的温度
            tv_aqi,//空气质量指数
            tv_quality,//空气质量
            tv_next_three,//3小时
            tv_next_six,//6小时
            tv_next_nine,//9小时
            tv_next_twelve,//12小时
            tv_next_fifteen,//15小时
            tv_next_three_temp,//3小时温差
            tv_next_six_temp,//6小时温差
            tv_next_nine_temp,//9小时温差
            tv_next_twelve_temp,//12小时温差
            tv_next_fifteen_temp,//15小时温差、
            tv_today_temp_a,//今天的温度a
            tv_today_temp_b,//今天的温度b
            tv_tommorrow,//明天
            tv_tommorrow_temp_a,//明天温度a
            tv_tommorrow_temp_b,//明天温度b
            tv_thirdday,//第三天
            tv_thirdday_temp_a,//第三天温度a
            tv_thirdday_temp_b,//第三天温度b
            tv_fourthday,//第四天
            tv_fourthday_temp_a,//第四天温度a
            tv_fourthday_temp_b,//第四天温度b
            tv_felt_air_temp,  //体感温度
            tv_humidity, //湿度
            tv_wind,  //风向、风力
            tv_uv_index,//紫外线强度
            tv_dress_index;//穿衣指数

    private ImageView iv_now_weather, //现在天气图标
                      iv_next_three,//三小时天气图标
                      iv_next_six,//六小时天气图标
                      iv_next_nine,//九小时天气图标
                      iv_next_twelve,//十二小时天气图标
                      iv_next_fifteen,//十五小时天气图标
                      iv_today_weather,//今天天气图标
                      iv_tommorrow,//明天天气图标
                      iv_thirdday,//后天天气图标
                      iv_fourthday;//大后天天气图标


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mContext = this;
        init();
        initService();
    }

    private void init(){
        mPullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.pull_refresh_scrollview);
        rl_city = (RelativeLayout)findViewById(R.id.rl_city);
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mService.getWeather();
            }
        });
        rl_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动城市选择列表
                startActivityForResult(new Intent(WeatherActivity.this,CityActivity.class),1);
            }
        });


        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_realease = (TextView)findViewById(R.id.tv_realease);
        tv_now_weather = (TextView)findViewById(R.id.tv_now_weather);
        tv_today_temp = (TextView)findViewById(R.id.tv_today_temp);
        tv_now_temp = (TextView)findViewById(R.id.tv_now_temp);
        tv_aqi = (TextView)findViewById(R.id.tv_aqi);
        tv_quality = (TextView)findViewById(R.id.tv_quality);
        tv_next_three = (TextView)findViewById(R.id.tv_next_three);
        tv_next_six = (TextView)findViewById(R.id.tv_next_six);
        tv_next_nine = (TextView)findViewById(R.id.tv_next_nine);
        tv_next_twelve = (TextView)findViewById(R.id.tv_next_twelve);
        tv_next_fifteen = (TextView)findViewById(R.id.tv_next_fifteen);
        tv_next_three_temp = (TextView)findViewById(R.id.tv_next_three_temp);
        tv_next_six_temp = (TextView)findViewById(R.id.tv_next_six_temp);
        tv_next_nine_temp = (TextView)findViewById(R.id.tv_next_nine_temp);
        tv_next_twelve_temp = (TextView)findViewById(R.id.tv_next_twelve_temp);
        tv_next_fifteen_temp = (TextView)findViewById(R.id.tv_next_fifteen_temp);
        tv_today_temp_a = (TextView)findViewById(R.id.tv_today_temp_a);
        tv_today_temp_b = (TextView)findViewById(R.id.tv_today_temp_b);
        tv_tommorrow = (TextView)findViewById(R.id.tv_tomorrow);
        tv_tommorrow_temp_a = (TextView)findViewById(R.id.tv_tomorrow_temp_a);
        tv_tommorrow_temp_b = (TextView)findViewById(R.id.tv_tomorrow_temp_b);
        tv_thirdday = (TextView)findViewById(R.id.tv_thirdday);
        tv_thirdday_temp_a = (TextView)findViewById(R.id.tv_thirdday_temp_a);
        tv_thirdday_temp_b = (TextView)findViewById(R.id.tv_thirdday_temp_b);
        tv_fourthday = (TextView)findViewById(R.id.tv_fourthday);
        tv_fourthday_temp_a = (TextView)findViewById(R.id.tv_fourthday_temp_a);
        tv_fourthday_temp_b = (TextView)findViewById(R.id.tv_fourthday_temp_b);
        tv_felt_air_temp = (TextView)findViewById(R.id.tv_felt_air_temp);
        tv_humidity = (TextView)findViewById(R.id.tv_humidity);
        tv_wind = (TextView)findViewById(R.id.tv_wind);
        tv_uv_index = (TextView)findViewById(R.id.tv_uv_index);
        tv_dress_index = (TextView)findViewById(R.id.tv_dressing_index);

        iv_now_weather = (ImageView)findViewById(R.id.iv_now_weather);
        iv_next_three = (ImageView)findViewById(R.id.iv_next_three);
        iv_next_six = (ImageView)findViewById(R.id.iv_next_six);
        iv_next_nine = (ImageView)findViewById(R.id.iv_next_nine);
        iv_next_twelve = (ImageView)findViewById(R.id.iv_next_twelve);
        iv_next_fifteen = (ImageView)findViewById(R.id.iv_next_fifteen);
        iv_today_weather = (ImageView)findViewById(R.id.iv_today_weather);
        iv_tommorrow = (ImageView)findViewById(R.id.iv_tomorrow);
        iv_thirdday = (ImageView)findViewById(R.id.iv_thirdday);
        iv_fourthday = (ImageView)findViewById(R.id.iv_fourthday);


    }

    private void initService(){
        intent = new Intent(mContext, WeatherService.class);
        startService(intent);
        bindService(intent,conn,Context.BIND_AUTO_CREATE);
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mService = ((WeatherService.WeatherServiceBinder)service).getService();
            mService.setCallBack(new WeatherService.OnParserCallBack() {
                @Override
                public void OnParserComplete(WeatherBean weatherBean, List<HoursWeatherBean> list, PMBean pmBean) {
                    mPullToRefreshScrollView.onRefreshComplete();
                    Log.i("zjp","stopRefresh");
                    if (list != null && list.size() >= 5) {
                        set3hForecast(list);
                    }

                    if (pmBean != null) {
                        setPMView(pmBean);
                    }

                    if (weatherBean != null) {
                        setWeatherViews(weatherBean);
                    }
                }
            });
            mService.getWeather();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService.removeCallBack();
        }
    };

    //显示城市查询接口内容
    private void setWeatherViews(WeatherBean bean){
        tv_city.setText(bean.getCity());
        tv_realease.setText(bean.getRelease());

        String[] tempArr = bean.getTemp().split("~");
        String temp_str_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
        String temp_str_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
        // 温度 8℃~16℃" ↑ ↓ °
        tv_today_temp.setText("↑ " + temp_str_a + "°   ↓" + temp_str_b + "°");
        tv_now_weather.setText(bean.getWeather_str());
        tv_now_temp.setText(bean.getNow_temp() + "°");
        iv_now_weather.setImageResource(getResources().getIdentifier("d"+bean.getWeather_id(),"drawable","com.juhe.weather"));

        tv_today_temp_a.setText(temp_str_a + "°");
        tv_today_temp_b.setText(temp_str_b + "°");
        List<FutureWeatherBean> futureWeatherBeanList = bean.getFutureList();
        if(futureWeatherBeanList!=null && futureWeatherBeanList.size()==3){
            Log.i("zjp","future");
            setFutureData(tv_tommorrow,iv_tommorrow,tv_tommorrow_temp_a,tv_tommorrow_temp_b,futureWeatherBeanList.get(0));
            setFutureData(tv_thirdday,iv_thirdday,tv_thirdday_temp_a,tv_thirdday_temp_b,futureWeatherBeanList.get(1));
            setFutureData(tv_fourthday,iv_fourthday,tv_fourthday_temp_a,tv_fourthday_temp_b,futureWeatherBeanList.get(2));
        }

        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        String prefixStr = null;
        if(time>=6 && time<18){
            prefixStr = "d";
        }else {
            prefixStr = "n";
        }
        tv_felt_air_temp.setText(bean.getNow_temp() + "°");
        iv_now_weather.setImageResource(getResources().getIdentifier(prefixStr+bean.getWeather_id(),"drawable","com.juhe.weather"));
        tv_humidity.setText(bean.getHumidity());
        tv_dress_index.setText(bean.getDressing_index());
        tv_uv_index.setText(bean.getUv_index());
        tv_wind.setText(bean.getWind());

        Log.i("zjp","setSuccessful");
    }

    //显示未来3天城市天气
    private void setFutureData(TextView tv_week, ImageView iv_weather, TextView tv_temp_a, TextView tv_temp_b, FutureWeatherBean bean) {
        tv_week.setText(bean.getWeek());
        iv_weather.setImageResource(getResources().getIdentifier("d" + bean.getWeather_id(), "drawable", "com.juhe.weather"));
        String[] tempArr = bean.getTemp().split("~");
        String temp_str_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
        String temp_str_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
        tv_temp_a.setText(temp_str_a + "°");
        tv_temp_b.setText(temp_str_b + "°");

    }


    //显示城市天气3小时预报

    private void set3hForecast(List<HoursWeatherBean> list){

        setHourseDate(tv_next_three,iv_next_three,tv_next_three_temp,list.get(0));
        setHourseDate(tv_next_six,iv_next_six,tv_next_six_temp,list.get(1));
        setHourseDate(tv_next_nine,iv_next_nine,tv_next_nine_temp,list.get(2));
        setHourseDate(tv_next_twelve,iv_next_twelve,tv_next_twelve_temp,list.get(3));
        setHourseDate(tv_next_fifteen,iv_next_fifteen,tv_next_fifteen_temp,list.get(4));

    }

    private void setHourseDate(TextView tv_time,ImageView iv_weather,TextView tv_temp,HoursWeatherBean bean){

        String presfixstr = null;
        tv_time.setText(bean.getTime() + "时");
        tv_temp.setText(bean.getTemp() + "°");
        int time = Integer.parseInt(bean.getTime());
        if(time>=6 && time<18){
            presfixstr = "d";
        }else {
            presfixstr = "n";
        }
        iv_weather.setImageResource(getResources().getIdentifier(presfixstr+bean.getWeather_id(),"drawable","com.juhe.weather"));
    }


    //显示空气质量
    private void setPMView(PMBean bean){
        tv_aqi.setText(bean.getAqi());
        tv_quality.setText(bean.getQuality());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == 1 && resultCode ==1){
           String city = data.getStringExtra("city");
           mService.getWeather(city);
       }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        //stopService(intent);
        Log.i("zjp","onDestory");
        super.onDestroy();
    }
}
