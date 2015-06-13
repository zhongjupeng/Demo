package com.juhe.weather;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juhe.weather.adapter.MyAdapter;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityActivity extends Activity{

    private ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        getCities();
    }

    private void initView(){

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        listView = (ListView)findViewById(R.id.lv_city);

    }

    private void getCities(){

        JuheData.executeWithAPI(this,39,"http://v.juhe.cn/weather/citys",JuheData.GET,new Parameters(),new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                final List<String> list = new ArrayList<String>();
                Set<String> set = new HashSet<String>();
                try{
                    JSONObject json = new JSONObject(s);
                    int resultcode = json.getInt("resultcode");
                    int error_code = json.getInt("error_code");
                    if(resultcode == 200 && error_code == 0){
                        JSONArray jsonArray = json.getJSONArray("result");
                        for(int j=0;j<jsonArray.length();j++){
                            String city = jsonArray.getJSONObject(j).getString("city");
                            set.add(city);
                        }
                        list.addAll(set);
                        Collections.sort(list);
                        MyAdapter adapter = new MyAdapter(CityActivity.this,list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent();
                                intent.putExtra("city",list.get(position));
                                setResult(1,intent);
                                finish();
                            }
                        });

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {

            }
        });

    }

}
