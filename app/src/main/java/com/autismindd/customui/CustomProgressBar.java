package com.autismindd.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Probook 440 on 12/6/2016.
 */

public class CustomProgressBar extends View {
    private Context context;

    private Paint paint;
    private Paint strokePaint;
    private Canvas mCanvas;
    private int res;
    private int percentage;
    private Bitmap avatarBitmap;
    private int height;
    private int width;
    private int posX;
    private int posY;
    private int radius;
    private String barColor;
    private String circleColor;
    private Paint circlePaint;

    public CustomProgressBar(Context context, int res, int percentage) {
        super(context);
        this.context = context;
        this.res = res;
        this.percentage = percentage;
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(barColor));

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStrokeWidth(7f);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor(circleColor));


        avatarBitmap = BitmapFactory.decodeResource(getResources(), res);
        avatarBitmap = getCircleBitmap(getResizedBitmap(avatarBitmap, 100, 100));
        radius = avatarBitmap.getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        width = getWidth();
        height = getHeight();
        posX = (width / 100) * percentage;
        posY = height / 2;
        drawEveryThing();


    }

    private int maxVal = 100;
    private int minVal = 5;

    private void drawEveryThing() {
        if (avatarBitmap != null) {

            if (percentage == maxVal) {
                mCanvas.drawRect(0, 0, width, posY + posY, paint);

            } else

                mCanvas.drawRect(0, 0, posX, posY + posY, paint);

            mCanvas.drawCircle(posX, posY, radius + 10, strokePaint);
            mCanvas.drawCircle(posX, posY, radius + 10, circlePaint);
            mCanvas.drawBitmap(avatarBitmap, posX - avatarBitmap.getWidth() / 2, posY - avatarBitmap.getHeight() / 2, circlePaint);

        }

    }

    public void setProgress(int percentage, int res, String barColor, String circleColor) {

        this.percentage = percentage;
        this.res = res;
        this.barColor = barColor;
        this.circleColor = circleColor;
        init();
        if (percentage < minVal) {
            this.percentage = minVal;
        }
        invalidate();
    }

    public void setProgressInPercent(float percentage) {
        this.percentage = (int) percentage;
        if (percentage < minVal) {
            this.percentage = minVal;
        }
        invalidate();
    }

    /*  image resize and get circular method*/
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

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
