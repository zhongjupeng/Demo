package com.zjp.layoutanimation;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;


public class MainActivity extends ListActivity {

    private ArrayAdapter<String> adapter;
    private LayoutAnimationController layoutAnimationController;
    private ScaleAnimation scaleAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String []{
                "Hello","zjp","how are you!"
        });
        getListView().setAdapter(adapter);

        //通过代码动态设置布局动画
//        scaleAnimation = new ScaleAnimation(0,1,0,1);
//        scaleAnimation.setDuration(1000);
//        layoutAnimationController = new LayoutAnimationController(scaleAnimation,0.5f);
//        getListView().setLayoutAnimation(layoutAnimationController);

    }

}
