package com.autismindd.customui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

/**
 * Created by Probook 440 on 10/18/2016.
 */

public class CustomCircleStrokeAnimation extends View {
    private Paint drawPaint;
    private float cx;
    private float cy;
    private boolean isTouch = false;
    private String initColor="#B30000FF";

    public CustomCircleStrokeAnimation(Context context, float cx, float cy) {
        super(context);
        this.cx = cx;
        this.cy = cy;
        isTouch = true;
        init();
    }

    // paint initialization
    private void init() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(Color.parseColor(initColor));
        drawPaint.setStrokeWidth(3);

    }

    // variable that assign radius and step;
    private int radius = 10;
    private int step = 10;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isTouch) {
            canvas.drawCircle(cx, cy, radius, drawPaint);
            controlAnimation();
        }
    }

    /// use to control animation
    private void controlAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (radius != 100) {
                    radius = radius + step;
                    invalidate();
                } else {
                    isTouch = false;
                    invalidate();
                }
            }
        }, 10);
    }

}
