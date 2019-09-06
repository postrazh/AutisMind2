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

public class CustomVariousColorCircleStrokeAnimationView extends View {

    private Paint drawPaint;
    private float cx;
    private float cy;
    private boolean isTouch = false;
    private String initColor="#B30000FF";

    public CustomVariousColorCircleStrokeAnimationView(Context context, float cx, float cy) {
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
        drawPaint.setStrokeWidth(5);

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

                if (radius <= 100) {
                    radius = radius + step;
                    switch (radius) {
                        case 10:
                            drawPaint.setColor(Color.RED);
                            break;
                        case 20:
                            drawPaint.setColor(Color.CYAN);

                            break;
                        case 30:
                            drawPaint.setColor(Color.GRAY);

                            break;
                        case 40:
                            drawPaint.setColor(Color.GREEN);

                            break;
                        case 50:
                            drawPaint.setColor(Color.YELLOW);

                            break;
                        case 60:
                            drawPaint.setColor(Color.CYAN);

                            break;
                        case 70:
                            drawPaint.setColor(Color.BLACK);

                            break;
                        case 80:
                            drawPaint.setColor(Color.BLUE);

                            break;
                        case 90:
                            drawPaint.setColor(Color.RED);
                            break;
                        case 100:
                            drawPaint.setColor(Color.LTGRAY);
                            break;

                    }
                    invalidate();
                } else {
                    isTouch = false;
                    invalidate();
                }
            }
        }, 10);
    }
}
