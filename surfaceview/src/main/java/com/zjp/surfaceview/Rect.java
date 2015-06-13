package com.zjp.surfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Administrator on 2015/5/29.
 */
public class Rect  extends Contanier{

    Paint paint = null;
    public Rect(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
}

    @Override
    public void onChilderView(Canvas canvas) {
        super.onChilderView(canvas);
        canvas.drawRect(0,0,100,100,paint);
        Log.i("zjp","drawRect");
        this.setX(this.getX() + 5);
    }
}
