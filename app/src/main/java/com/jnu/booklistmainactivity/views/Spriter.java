package com.jnu.booklistmainactivity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.jnu.booklistmainactivity.R;

public class Spriter {
    float x,y;
    int drawableResourceId;
    float direction;
    Context context;

    public Spriter(Context context)
    {
        this.context=context;
    }
    public void move(float maxHeight, float maxWidth)
    {
        if(Math.random()<0.05)
        {
            setDirection((float) (Math.random()*2*Math.PI));
        }
        x= (float) (x+30*Math.cos(direction));
        y= (float) (y+30*Math.sin(direction));
        if(x<0)x+=maxWidth;
        if(x>maxWidth)x-=maxWidth;
        if(y<0)y+=maxHeight;
        if(y>maxHeight)y-=maxHeight;

    }
    public void draw(Canvas canvas)
    {
        Bitmap bitmap=((BitmapDrawable)context.getResources().getDrawable(R.drawable.spriter)).getBitmap();
        Paint mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        canvas.drawBitmap(bitmap, getX(), getY(), mBitPaint);
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

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    public void setDrawableResourceId(int drawableResourceId) {
        this.drawableResourceId = drawableResourceId;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public boolean isTouched(float touchedX, float touchedY) {
        Log.i("test",""+(touchedX-x)*(touchedX-x)+(touchedY-y)*(touchedY-y));
        return (touchedX-x)*(touchedX-x)+(touchedY-y)*(touchedY-y)<=20000;//平方距离小于等于400就认为击中了
    }
}
