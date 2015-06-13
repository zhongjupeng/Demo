package com.zjp.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    private ImageView iv;
    private RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root= (RelativeLayout)findViewById(R.id.rlayout);
        iv = (ImageView)findViewById(R.id.ivmage);
        root.setOnTouchListener(new View.OnTouchListener() {
            float mCurdistant;
            float mLastdistant = -1;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.i("zjp","ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //单点触控，移动图片
                        if(event.getPointerCount()==1){
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)iv.getLayoutParams();
                            layoutParams.leftMargin = (int)event.getX();
                            layoutParams.topMargin = (int)event.getY();
                            iv.setLayoutParams(layoutParams);
                        }


                        //多点触控，缩放图片
                        if(event.getPointerCount()==2){
                            float mOffx = event.getX(0)-event.getX(1);
                            float mOffy = event.getY(0)-event.getY(1);
                            mCurdistant = (float)Math.sqrt(mOffx*mOffx+mOffy*mOffy);
                        if (mLastdistant<0){
                            mLastdistant = mCurdistant;
                        }else {
                            if (mLastdistant - mCurdistant<20){
                                //图片放大
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)iv.getLayoutParams();
                                layoutParams.width = (int)(iv.getWidth()*1.1f);
                                layoutParams.height = (int)(iv.getHeight()*1.1f);
                                iv.setLayoutParams(layoutParams);
                                mLastdistant = mCurdistant;
                            }else if(mCurdistant - mLastdistant<20){
                                //图片缩小
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)iv.getLayoutParams();
                                layoutParams.width = (int)(iv.getWidth()*0.9f);
                                layoutParams.height = (int)(iv.getHeight()*0.9f);
                                iv.setLayoutParams(layoutParams);
                                mLastdistant = mCurdistant;
                            }
                        }

                        }


                        break;

                    case MotionEvent.ACTION_UP:
                        Log.i("zjp","ACTION_UP");
                        break;
                }

                return true;   //返回true在ACTION_DOWN后能继续后续的ACTION_UO ACTION_MOVE
            }
        });




    }


}
