package com.autismindd.customui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

import com.autismindd.R;

/**
 * Created by Sumon on 11/13/2016.
 */

public class AvatarUpdateView extends SurfaceView {
    private Paint circlePaint;
    private Paint strokePaint;
    private Bitmap bitmap;
    private Paint rectPaint;
    private TextPaint textPaint;
    private TextPaint textPaint2;
    private int starCount = 00;
    private String userName = "NAME";
    private String fillColorForUserRectangle;
    private Paint strokeRectPaint;
    boolean isDrawStar = true;

    //// star height width variable here
    int starWidth = 24;
    int starHeight = 24;


    /// for animation avatar
    Paint paint;

    Bitmap bm;
    int bm_offsetX, bm_offsetY;

    Path animPath;
    PathMeasure pathMeasure;
    float pathLength;

    int step;            //distance each step
    float distance;        //distance moved

    float[] pos;
    float[] tan;

    Matrix matrix;
    //// surface variables
    private SurfaceHolder surfaceHolder;
    private AvatarUpdateThread avatarUpdateThread;

    AvatarAnimationFininishedListener finish_listener;

    public AvatarUpdateView(Context context, Bitmap userPhoto, int starCount, String userName, String fillColorForUserRectangle) {
        super(context);
        this.bitmap = userPhoto;
        this.starCount = starCount;
        this.userName = userName;
        this.fillColorForUserRectangle = fillColorForUserRectangle;
        avatarUpdateThread = new AvatarUpdateThread(AvatarUpdateView.this);
        initView();
    }

    //this will only be used if new level is unlocked
    public AvatarUpdateView(Context context, Bitmap userPhoto, int starCount, String userName, String fillColorForUserRectangle, AvatarAnimationFininishedListener listener) {
        super(context);
        this.bitmap = userPhoto;
        this.starCount = starCount;
        this.userName = userName;
        this.fillColorForUserRectangle = fillColorForUserRectangle;
        avatarUpdateThread = new AvatarUpdateThread(AvatarUpdateView.this);
        finish_listener = listener;
        initView();
    }

    //// init paint and surface view
    private void initView() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(callback);
        setZOrderOnTop(true);    // necessary
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor(fillColorForUserRectangle));

        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(Color.parseColor(fillColorForUserRectangle));
        rectPaint.setAntiAlias(true);
        rectPaint.setStrokeWidth(3);

        strokePaint = new Paint();
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.FILL);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(3);

        textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);

        textPaint2 = new TextPaint();
        textPaint2.setStyle(Paint.Style.FILL);
        textPaint2.setColor(Color.WHITE);
        textPaint2.setTextSize(15);
        textPaint2.setAntiAlias(true);

        strokeRectPaint = new Paint();
        strokeRectPaint.setStyle(Paint.Style.STROKE);
        strokeRectPaint.setStrokeWidth(4);
        strokeRectPaint.setColor(Color.WHITE);
        strokeRectPaint.setAntiAlias(true);
        ///
        initAnimMaterials();
    }

    /// init star animation variables
    private void initAnimMaterials() {
        paint = new Paint();
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_star_yellow);
        bm = getResizedBitmap(bm, 200, 200);
        bm_offsetX = bm.getWidth() / 2;
        bm_offsetY = bm.getHeight() / 2;
        step = 50;
        distance = 0;
        pos = new float[2];
        tan = new float[2];

        matrix = new Matrix();
    }

    //// surface view callback
    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d("destroy", "create");
            boolean retry = true;
            avatarUpdateThread.setRunning(false);
            while (retry) {
                try {
                    avatarUpdateThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };


    //////////////////////////////////// Draw Thread ///////////////////////////////////
    /// redius and drawing x ,y positions
    float radius = ((getScreenWidth() / 100) * 5);

    float drawX = ((getScreenWidth() / 100) * 3);
    float drawY = ((getScreenHeight() / 100) * 3);


    /// main draw method
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawFromThread(Canvas canvas) {
        drawAvatar(canvas);
        drawText(canvas);
        //// code for animating star

        if (flyStar) {
            starX = (int) ((drawX + radius) + getWidth() / 13);
            starY = (int) ((drawY + radius) + 2);
            flyStarAnimation(canvas, getWidth() / 2, getHeight() / 2, starX, starY);


        } else {
            if (avatarUpdateThread != null) {
                avatarUpdateThread.setRunning(false);
                if (finish_listener != null) {
                    finish_listener.avatarAnimationFininished();
                }
            }
        }
    }

    /// method that used to draw avatar rect,circle,and bitmap
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawAvatar(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        canvas.drawRoundRect(drawX + radius, (drawY + radius) - radius / 2, (drawX + radius / 2) + getWidth() / 5,
                ((drawY + radius) - radius / 2) + radius, 5, 5, rectPaint);
        canvas.drawRoundRect(drawX + radius, (drawY + radius) - radius / 2, (drawX + radius / 2) + getWidth() / 5,
                ((drawY + radius) - radius / 2) + radius, 5, 5, strokeRectPaint);

        drawCircle(canvas, circlePaint, bitmap, drawX + radius, drawY + radius, radius);
        if (isDrawStar)
            drawStar(canvas);


        drawName(canvas);
    }
//////////////////////////////////// Draw Thread Ends ///////////////////////////////////


    //////////////////////// The avatar view controllers *************?//////////////////////////////////////
    // Name Draw
    private void drawName(Canvas canvas) {
        if (userName.length() > 10) {
            String uName = userName.substring(0, 10);
            canvas.drawText(uName + "...", ((drawX + radius) + getWidth() / 15), ((drawY + radius) + radius / 3), textPaint2);
        } else
            canvas.drawText(userName, ((drawX + radius) + getWidth() / 15), ((drawY + radius) + radius / 3), textPaint2);

    }

    // draw Star method
    private void drawStar(Canvas canvas) {
        int res = R.drawable.ic_star_yellow;
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, res);
        image = getResizedBitmap(image, starWidth, starHeight);
        canvas.drawBitmap(image, ((drawX + radius) + getWidth() / 13) - image.getWidth() / 2, ((drawY + radius) + 2) - image.getHeight(), rectPaint);

    }

    /// method for drawing points text
    private void drawText(Canvas canvas) {
        Log.e("From DrawText: ", String.valueOf(starCount));
        canvas.drawText(String.valueOf(starCount), ((drawX + radius) + getWidth() / 10), ((drawY + radius)), textPaint);

    }

    int circleStrokeDeviation = 4;
    int bitmapWidth = 84;
    int bitmapHeight = 84;

    //// method that draw avatar circle
    public void drawCircle(Canvas canvas, Paint paint, Bitmap bitmap, float x, float y, float radius) {

        canvas.drawCircle(x, y, radius + circleStrokeDeviation, strokePaint);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        bitmap = getResizedBitmap(bitmap, bitmapWidth, bitmapHeight);
        bitmap = getCircleBitmap(bitmap);
        canvas.drawCircle(x, y, radius, paint);
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, paint);
    }
    /// method for resizing bitmap
//////////////////////// The avatar view controllers end *************?//////////////////////////////////////


    ///// star animation from center to small star in avatar
    int drawCount = 0;

    /// animate star variable
    int starX, starY;
    ///////////////////////////////// FLY STAR CALCULATIONS START ***********************//////////
    boolean flyStar = false;
    int flyCount = 1;

    ///// method used for flaying star from activity
    public void flyStar(int flyCount) {
        avatarUpdateThread = new AvatarUpdateThread(AvatarUpdateView.this);
        Log.d("flyCount:", String.valueOf(flyCount));
        Log.d("starCount:", String.valueOf(starCount));
        if (flyCount > 0) {
            this.flyStar = true;
            starCount++;
        }
        this.flyCount = flyCount;
        initView();
        if (avatarUpdateThread != null) {
            avatarUpdateThread.setRunning(true);
            avatarUpdateThread.start();
        }
    }

    // method that animate the avatar
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flyStarAnimation(Canvas canvas, int fromX, int fromY, int toX, int toY) {

        animPath = new Path();
        animPath.moveTo(fromX, fromY);
        animPath.lineTo(toX, toY);
        animPath.close();

        pathMeasure = new PathMeasure(animPath, false);
        pathLength = pathMeasure.getLength();
        canvas.drawPath(animPath, paint);

        Log.e("Called", "flyStarAnimation");
/// used for animating star on a specific path calculating his path length
        if (distance < pathLength / 2) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            drawAvatar(canvas);
            pathMeasure.getPosTan(distance, pos, tan);

            matrix.reset();
            float degrees = (float) (Math.atan2(tan[1], tan[0]) * 0.0 / Math.PI);
            matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            matrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

            canvas.drawBitmap(bm, matrix, null);
            distance += step;
            if (bm.getWidth() > starWidth && bm.getHeight() > starHeight) {
                if (step < bm.getWidth() && step < bm.getHeight())
                    bm = getResizedBitmap(bm, bm.getWidth() - step, bm.getHeight() - step);
                bm_offsetX = bm.getWidth() / 2;
                bm_offsetY = bm.getHeight() / 2;
            } else {
                bm = getResizedBitmap(bm, starWidth, starHeight);
                bm_offsetX = bm.getWidth() / 2;
                bm_offsetY = bm.getHeight() / 2;
            }


        } else {
            distance = 0;
            drawCount++;
            starCount = starCount + 1;
            Log.e("CalledElse", "flyStarAnimation");
            Log.e("startCount: ", String.valueOf(starCount));
            if (drawCount == flyCount) {
                starCount--;
                flyStar = false;

            }
        }


    }
///////////////////////////////// FLY STAR CALCULATIONS START Ends***********************//////////


    //// method for calculating screenwidht and screen height////////////////////////////////
    private int getScreenWidth() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private int getScreenHeight() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        return screenHeight;
    }


    //////////////////////////**************BITMAP RESIZE METHODS  START HERE *********************************////////
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

    //// method that make a bitmap circular
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
    //////////////////////////**************BITMAP RESIZE METHODS  ENDS HERE *********************************////////



    /// variable used for overlapping method
    int width = (int) ((drawX + radius / 2) + getWidth() / 5), height = (int) (((drawY + radius) - radius / 2) + radius);

    ///////// for checking over lapping between to certain position by making two rectangle
    public boolean isViewOverlapping(int x, int y) {

        int xx = (int) (drawX);
        int yy = (int) (drawY);
        Rect rect1 = new Rect(xx, yy, xx + bitmapWidth, yy + bitmapHeight);

        Rect rect2 = new Rect(x, y, x + 20, y + 20);

        return rect1.intersect(rect2);
    }

    // interface for setting event when avatar animation is finished
    public interface AvatarAnimationFininishedListener {
        public void avatarAnimationFininished();
    }

}