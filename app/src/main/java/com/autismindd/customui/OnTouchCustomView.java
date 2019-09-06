package com.autismindd.customui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

import com.autismindd.R;

/**
 * Created by Probook 440 on 10/17/2016.
 */
// its for only touch event
public class OnTouchCustomView extends View {
    private Paint drawPaint;
    private float cx;
    private float cy;
    private boolean isTouch = false;

    public OnTouchCustomView(Context context, float cx, float cy) {
        super(context);
        this.cx = cx;
        this.cy = cy;
        isTouch = true;
        init();
    }

    private void init() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setColor(getResources().getColor(R.color.transparent_blue));
        drawPaint.setStrokeWidth(5);

    }

    private int radius = 15;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isTouch) {
            canvas.drawCircle(cx, cy, radius, drawPaint);
            controlAnimation();
        }

    }

    private void controlAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (radius != 150) {
                    radius = radius + 15;
                    invalidate();
                } else {
                    isTouch = false;
                    invalidate();
                }
            }
        }, 10);
    }


}
