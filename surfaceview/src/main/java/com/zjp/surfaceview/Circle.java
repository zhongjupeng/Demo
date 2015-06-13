package com.zjp.surfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Administrator on 2015/5/29.
 */
public class Circle extends Contanier {

    Paint paint = null;
    public Circle(){
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    public void onChilderView(Canvas canvas) {
        super.onChilderView(canvas);
        canvas.drawCircle(50,50,50,paint);
        Log.i("zjp", "drawCircle");

    }
}
