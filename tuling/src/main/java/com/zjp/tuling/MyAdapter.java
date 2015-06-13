package com.zjp.tuling;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends BaseAdapter{

    private Context context;
    List<ListData> lists;
    private RelativeLayout layout;
    private int position;
    public MyAdapter(Context context,List<ListData> lists){
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
        this.position = position;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(lists.get(position).getFlag() == ListData.SEND){
            layout = (RelativeLayout)inflater.inflate(R.layout.right_layout,null);

        }else if(lists.get(position).getFlag() == ListData.RECEVE){
            layout = (RelativeLayout)inflater.inflate(R.layout.left_layout,null);
            CircleImageView imageView = (CircleImageView)layout.findViewById(R.id.iv);
            if(lists.get(position).getWho() == MainActivity.LZL){
                imageView.setImageResource(R.drawable.lzl);
            }else if (lists.get(position).getWho() == MainActivity.ZJY){
                imageView.setImageResource(R.drawable.zjy);
            }else if (lists.get(position).getWho() == MainActivity.CJK){
                imageView.setImageResource(R.drawable.cjk);
            }else if (lists.get(position).getWho() == MainActivity.FYJ){
                imageView.setImageResource(R.drawable.fyj);
            }
            else {
                imageView.setImageResource(R.drawable.ly);
            }

        }
        TextView textView = (TextView)layout.findViewById(R.id.tv);
        TextView textTime = (TextView)layout.findViewById(R.id.textTime);
        textTime.setText(lists.get(position).getTime());
        if(lists.get(position).getCode() == ListData.CODE_TEXT){
            textView.setText(lists.get(position).getContent());
        }else{
            textView.setText(lists.get(position).getContent() + "\n" + lists.get(position).getUrl());
        }
        return layout;
    }
}
