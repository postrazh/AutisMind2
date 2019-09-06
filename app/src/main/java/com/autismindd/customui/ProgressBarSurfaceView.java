package com.autismindd.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Probook 440 on 12/8/2016.
 */

public class ProgressBarSurfaceView extends SurfaceView {
    private SurfaceHolder surfaceHolder;
    private MyThread myThread;
    private Paint paint;
    private Paint strokePaint;
    private int res;
    private int percentage;
    private Bitmap avatarBitmap;
    private int posX;
    private int posY;
    private int radius;
    private String barColor;
    private String circleColor;
    private Paint circlePaint;

    public static volatile boolean running = false;

    public ProgressBarSurfaceView(Context context) {
        super(context);
        //init();
        myThread = new MyThread(ProgressBarSurfaceView.this);
    }

    public ProgressBarSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myThread = new MyThread(ProgressBarSurfaceView.this);

    }

    public ProgressBarSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myThread = new MyThread(ProgressBarSurfaceView.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        myThread = new MyThread(ProgressBarSurfaceView.this);

    }
    //////////////////////*****************  PAINT INITIALIZATION START HERE ****************///////////////////////////

    public void init() {

        myThread = new MyThread(this);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(callback);
        setZOrderOnTop(true);    // necessary
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        ////////
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(barColor));

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.parseColor(barColor));
        strokePaint.setStrokeWidth(5f);


        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor(circleColor));


        avatarBitmap = BitmapFactory.decodeResource(getResources(), res);


    }
    //////////////////////*****************  PAINT INITIALIZATION END HERE ****************///////////////////////////



    //////////////////////*****************  SURFACE CALLBACK START HERE ****************///////////////////////////

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d("create", "create");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            running=false;
        }
    };
    //////////////////////*****************  SURFACE CALLBACK END HERE ****************///////////////////////////



    //////////////////////*****************  DRAWING AREAS ARE START HERE ****************///////////////////////////


    private int width;
    private int height;
    private int speed = 5;
    private int maxVal = 100;
    private int minVal = 5;

    public void drawEveryThing(Canvas canvas) {

        width = getWidth();
        height = getHeight();
//        avatarBitmap = getCircleBitmap(getResizedBitmap(avatarBitmap, height - imgDeviation, height - imgDeviation));
        avatarBitmap = getCircleBitmap(avatarBitmap);
        radius = height;
        posX = (width / 100) * percentage;
        posY = height / 2;

        drawBar(canvas);

    }

    int deviation = 2;
    int imgDeviation = 4;

    private void drawBar(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if (avatarBitmap != null) {

            if (percentage == maxVal) {
                canvas.drawRect(0, 0, speed, height, paint);

            } else
                canvas.drawRect(0, 0, speed, height, paint);

            canvas.drawCircle(speed, posY, radius / 2 - deviation, strokePaint);
            canvas.drawCircle(speed, posY, radius / 2 - deviation, circlePaint);
            canvas.drawBitmap(avatarBitmap, speed - avatarBitmap.getWidth() / 2, posY - avatarBitmap.getHeight() / 2, circlePaint);

        }
        if (speed <= posX) {
            speed = speed + 30;

        } else {
            running=false;
            Log.e("progressView: ", "speed: " + speed + " posX: " + posX + " posY: " + posY);

        }
    }

    //////////////////////*****************  DRAWING AREAS ARE END HERE ****************///////////////////////////



    //////////////////////*****************  PROGRESS SET METHOD ARE START HERE ****************///////////////////////////

    public void setProgress(int percentage, int res, String barColor, String circleColor) {
        Log.e("setProgress() ", "progress 1st time: ");
        this.percentage = percentage;
        this.res = res;
        this.barColor = barColor;
        this.circleColor = circleColor;
        if (percentage < minVal) {
            this.percentage = minVal;
        }
        init();
        if (myThread != null) {
            running=true;
            myThread.start();


        }
    }

    public void setProgressInPercent(float percentage) {
        Log.e("setProgressInPercent() ", "progress 1st time: setProgressInPercent(float percentage)");
        this.percentage = (int) percentage;
        posX = (int) ((width / 100) * percentage);
        if (percentage < minVal) {
            this.percentage = minVal;
        }
        init();
        if (myThread != null) {
            running=true;
            myThread.start();

        }
    }

    //////////////////////*****************  PROGRESS SET METHOD ARE END HERE ****************///////////////////////////
    

    //////////////////////*****************  BITMAP RESIZED METHOD ARE START HERE ****************///////////////////////////
    /*  image resize and get circular method*/
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.e("Progress: ", String.valueOf(height)+"," +String.valueOf(width));

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Log.e("New Progress: ", String.valueOf(scaleHeight)+"," +String.valueOf(scaleWidth));
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
    //////////////////////*****************  BITMAP RESIZED METHOD ARE END HERE ****************///////////////////////////
}
