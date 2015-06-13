package com.zjp.tuling;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class MyClickSpan extends ClickableSpan {
    String text;
    Context context;
    public MyClickSpan(Context context,String text) {
        super();
        this.text = text;
        this.context = context;
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#ffffff")); //设置链接的文本颜色
        ds.setUnderlineText(false); //去掉下划线
    }
    @Override
    public void onClick(View widget) {
        Toast.makeText(context,"Onclick",Toast.LENGTH_SHORT).show();

    }
}
