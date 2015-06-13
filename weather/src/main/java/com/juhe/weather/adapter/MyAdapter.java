package com.juhe.weather.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juhe.weather.R;

import java.util.List;

public class MyAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> list;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context,List<String> list){
        this.mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = null;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.city_item_list,null);
        }else {
            view = convertView;
        }
        TextView tv_item_city = (TextView)view.findViewById(R.id.tv_item_city);
        tv_item_city.setText(getItem(position).toString());
        return view;
    }
}
