package com.autismindd.customui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
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
import android.os.Build;
import android.os.Handler;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.autismindd.R;
import com.autismindd.activities.TaskPackActivity;

/**
 * Created by Probook 440 on 10/4/2016.
 */

public class CustomAvatarView extends View {
    private Paint circlePaint;
    private Context context;
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
    int count = 0;
    Handler h = new Handler();

    public CustomAvatarView(Context context, Bitmap userPhoto, int starCount, String userName, String fillColorForUserRectangle) {
        super(context);
        this.context = context;
        this.bitmap = userPhoto;
        this.starCount = starCount;
        this.userName = userName;
        this.fillColorForUserRectangle = fillColorForUserRectangle;
        initView();
    }

    private void initView() {
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
    }

    private int getScreenWidth() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private int getScreenHeight() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        return screenHeight;
    }

    float radius = ((getScreenWidth() / 100) * 5);

    float drawX = ((getScreenWidth() / 100) * 3);
    float drawY = ((getScreenHeight() / 100) * 3);


    int width = (int) ((drawX + radius / 2) + getWidth() / 5), height = (int) (((drawY + radius) - radius / 2) + radius);

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(drawX + radius, (drawY + radius) - radius / 2, (drawX + radius / 2) + getWidth() / 5,
                ((drawY + radius) - radius / 2) + radius, 5, 5, rectPaint);
        canvas.drawRoundRect(drawX + radius, (drawY + radius) - radius / 2, (drawX + radius / 2) + getWidth() / 5,
                ((drawY + radius) - radius / 2) + radius, 5, 5, strokeRectPaint);

        drawCircle(canvas, circlePaint, bitmap, drawX + radius, drawY + radius, radius);
        drawStar(canvas);
        drawText(canvas);

        drawName(canvas);

        width = (int) ((drawX + radius / 2) + getWidth() / 5);
        height = (int) (((drawY + radius) - radius / 2) + radius);

    }

    /// method that draw user name
    private void drawName(Canvas canvas) {
        if (userName.length() > 10) {
            String uName = userName.substring(0, 10);
            canvas.drawText(uName + "...", ((drawX + radius) + getWidth() / 15), ((drawY + radius) + radius / 3), textPaint2);
        } else
            canvas.drawText(userName, ((drawX + radius) + getWidth() / 15), ((drawY + radius) + radius / 3), textPaint2);

    }

    /// method that draw star
    private void drawStar(Canvas canvas) {
        int res = R.drawable.ic_star_yellow;
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, res);
        image = getResizedBitmap(image, 24, 24);
        canvas.drawBitmap(image, ((drawX + radius) + getWidth() / 13) - image.getWidth() / 2, ((drawY + radius) + 2) - image.getHeight(), rectPaint);
    }

    /// method that draw points text
    private void drawText(Canvas canvas) {

        canvas.drawText(String.valueOf(starCount), ((drawX + radius) + getWidth() / 10), ((drawY + radius)), textPaint);
    }

    int bitmapWidth = 84;
    int bitmapHeight = 84;
    int deviation = 4;

    //// method that draw avatar circle
    public void drawCircle(Canvas canvas, Paint paint, Bitmap bitmap, float x, float y, float radius) {

        canvas.drawCircle(x, y, radius + deviation, strokePaint);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        bitmap = getResizedBitmap(bitmap, bitmapWidth, bitmapHeight);
        bitmap = getCircleBitmap(bitmap);
        canvas.drawCircle(x, y, radius, paint);
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, paint);
    }

    /// image resized method
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

    // when star update that time star will be animate
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (count > 0) {
                count--;
                if (isDrawStar == true) {
                    isDrawStar = false;
                    invalidate();
                } else {
                    isDrawStar = true;
                    invalidate();
                }

                h.postDelayed(r, 200);
            } else {
                isDrawStar = true;
                invalidate();
            }
        }
    };

    /// method used to blink star effect
    public void blinkStar(int getStar) {
        count = getStar * 2;
        h.postDelayed(r, 0);
    }

    //will be initialized from oncreate
/*    float dX=0,dY=0;
    int lastAction;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                {
                    int x= (int) event.getX();
                    int y=(int) event.getY();
                    if(isViewOverlapping(x,y)){
                        Toast.makeText(context,"done",Toast.LENGTH_LONG).show();
                    }

                }

                break;

            default:
                return false;
        }
        return false;
    }*/
/// method that used to check touch postion and circle position are same or not by intersecting two rectangle
    public boolean isViewOverlapping(int x, int y) {

        int xx = (int) (drawX);
        int yy = (int) (drawY);
        Rect rect1 = new Rect(xx, yy, xx + bitmapWidth, yy + bitmapHeight);

        Rect rect2 = new Rect(x, y, x + 20, y + 20);

        return rect1.intersect(rect2);
    }

}
