package com.autismindd.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.autismindd.utilities.Animanation;

/**
 * Created by Probook 440 on 1/22/2017.
 */

public class LockerCustomView extends View {
    private Paint paint;
    private Bitmap bitmapSun;
    private Bitmap resizedBitmap;

    private float circlePosX = 0, circlePosY = 0;
    private int circleRadius = 10;

    public LockerCustomView(Context context, int res, float cx, float cy, int radius) {
        super(context);
        bitmapSun = BitmapFactory.decodeResource(getResources(), res);
        this.circlePosX = cx;
        this.circlePosY = cy;
        this.circleRadius = radius;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.YELLOW);
//        Log.e("getLayoutParams: ", "width: " + getWidth() + " height: " + getHeight());
        resizedBitmap = getResizedBitmap(bitmapSun, circleRadius, circleRadius);
        canvas.drawBitmap(resizedBitmap, circlePosX , circlePosY, paint);

    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width <= 1 ? 10 : width, height <= 1 ? 10 : height, matrix, false);
        //  bm.recycle();
        bm = null;
        return resizedBitmap;
    }
}
