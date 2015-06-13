package com.zjp.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/5/29.
 */
public class View extends SurfaceView implements SurfaceHolder.Callback{

    private Contanier contanier;
    private Rect rect;
    private Circle circle;
    private Timer timer = null;
    private TimerTask timerTask = null;
    public View(Context context) {
        super(context);
        contanier = new Contanier();
        rect = new Rect();
        circle = new Circle();
        rect.addChilderView(circle);
        contanier.addChilderView(rect);
        getHolder().addCallback(this);
    }

    private void startTime(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Draw();
            }
        };
        timer.schedule(timerTask,100,100);   //每100毫秒就执行一次
    }

    private void stopTime(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    private void Draw(){
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        contanier.Draw(canvas);
        getHolder().unlockCanvasAndPost(canvas);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startTime();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopTime();
    }
}
