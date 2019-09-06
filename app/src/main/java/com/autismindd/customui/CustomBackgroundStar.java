package com.autismindd.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

import java.util.Random;

/**
 * Created by Probook 440 on 11/22/2016.
 */

public class CustomBackgroundStar extends View {

    private int lastColor = Color.BLACK;
    private final Random random = new Random();
    private final Paint paint = new Paint();
    private final int radius = 25;
    private final Handler handler = new Handler();
    private CustomShape customShape;

    public CustomBackgroundStar(Context context) {
        super(context);
        customShape = new CustomShape();

    }


    private final Runnable updateCircle = new Runnable() {
        @Override
        public void run() {
            lastColor = random.nextInt(3);
            switch (lastColor) {
                case 1:
                    lastColor = Color.YELLOW;
                    break;
                case 2:
                    lastColor = Color.GRAY;
                    break;
                case 3:
                    lastColor = Color.RED;
                    break;


            }
            paint.setColor(lastColor);
            invalidate();
            handler.postDelayed(this, 400);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.post(updateCircle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(updateCircle);
    }

    private float starRadius = 10;
    private float starInnerRadius = 25;
    private int numOfPoints = 6;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRendomStars(canvas, getWidth());
    }

    private void drawRendomStars(Canvas canvas, int width) {
       for (int i = 0; i < width; i = i + width /50) {
        customShape.setStar(random.nextInt(canvas.getWidth() - radius / 2 ) + radius / 2f, random.nextInt(canvas.getHeight() - radius / 2 ) + radius / 2f, starRadius, starInnerRadius, numOfPoints);

        customShape.setPaint(paint);
            canvas.drawPath(customShape.getPath(), customShape.getPaint());
      }
    }

}
