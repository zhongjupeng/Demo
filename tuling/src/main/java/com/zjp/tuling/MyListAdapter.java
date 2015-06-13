package com.zjp.tuling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
public class MyListAdapter extends BaseAdapter{

    private Context context;
    private List<ListData1> lists;
    public MyListAdapter(Context context,List<ListData1> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout,null);
        ImageView imageView = (ImageView)view.findViewById(R.id.iv_list);
        TextView textView = (TextView)view.findViewById(R.id.tv_list);
        imageView.setImageResource(lists.get(position).getIm());
        textView.setText(lists.get(position).getStr());
        return view;
    }
}
