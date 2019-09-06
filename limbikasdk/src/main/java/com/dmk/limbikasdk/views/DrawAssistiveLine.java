package com.dmk.limbikasdk.views;

/**
 * Created by Macbook on 19/07/2016.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class DrawAssistiveLine extends View {
    Paint paint = new Paint();

    float startx, starty, stopx, stopy;
    private int paintAlpha = 255;
    public DrawAssistiveLine(Context context) {
        super(context);
    }

    //call this method with the two limbika view and it will draw the line
    public void DrawAssistiveLine(LimbikaView lm1, LimbikaView lm2, boolean right) {
        startx = lm1.getX() + lm1.getWidth() / 2;
        starty = lm1.getY() + lm1.getHeight() / 2;
        stopx = lm2.getX() + lm2.getWidth() / 2;
        stopy = lm2.getY() + lm2.getHeight() / 2;

        DrawTheLine(startx, starty, stopx, stopy, right);

    }


    public void DrawTheLine(float startx, float starty, float stopx, float stopy, boolean right) {


        this.startx = startx;
        this.starty = starty;
        this.stopx = stopx;
        this.stopy = stopy;

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        paint.setStrokeWidth(50);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAlpha(0x80);

        paintAlpha=Math.round((float)50/100*255);



        if (right)
            paint.setColor(Color.parseColor("#66BB6A"));


        else
        paint.setColor(Color.RED);

        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {

        Path p = new Path();
        p.moveTo(startx, starty);
        p.lineTo(stopx, stopy);

        p.moveTo(stopx, stopy);
        p.lineTo(startx, starty);
        paint.setAlpha(paintAlpha);

        canvas.drawLine(startx, starty, stopx, stopy, paint);

    }

}

