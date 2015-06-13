package com.zjp.surfaceview;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Contanier {
    private float x=0;
    private float y=0;
    private List<Contanier> childerView;
    public Contanier(){
        childerView = new ArrayList<Contanier>();
    }


    public void Draw(Canvas canvas){
        canvas.save();
        canvas.translate(getX(),getY());
        onChilderView(canvas);
        for(Contanier c:childerView){
            c.Draw(canvas);
        }
        canvas.restore();

    }

    public void onChilderView(Canvas canvas){

    }

    public void addChilderView(Contanier childerview){
      childerView.add(childerview);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
