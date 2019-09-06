package com.autismindd.customui;

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
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;

import com.autismindd.R;

/**
 * Created by Probook 440 on 10/6/2016.
 */

public class CustomViewForStar extends View {
    private Paint paint;
    private int starCount;
    private Context context;
    private static long TIME_STAY = 800;
    private PlayStarSoundInterface listener;
    /// variable for draw circle , circle stroke, bitmap,textpaint
    private Paint circlePaint;
    private Paint circleStrokePaint;
    private float circleRadius = 0;
    private Bitmap userBitmap;
    private TextPaint textPaint;
    private Rect rect = new Rect();
    private int count;

    public CustomViewForStar(Context context, PlayStarSoundInterface listener) {
        super(context);
        this.context = context;
        this.userBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_girl);
        this.listener = listener;
        initPaint();
    }

    /// paint initialization
    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circleStrokePaint = new Paint();
        circleStrokePaint.setStyle(Paint.Style.STROKE);
        circleStrokePaint.setAntiAlias(true);
        circleStrokePaint.setColor(Color.parseColor("#9D9D9D"));
        textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.STROKE);

    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    //// screen height width calculation
    DisplayMetrics lDisplayMetrics = getResources().getDisplayMetrics();
    int widthPixels = lDisplayMetrics.widthPixels;
    int heightPixels = lDisplayMetrics.heightPixels;
    int firstStarX = widthPixels / 3;// first star position
    int middleStarX = widthPixels / 2;//middle star position
    int lastStarX = widthPixels - widthPixels / 3;/// last star position
    int halfHeight = (heightPixels / 100) * 50;
    int posY = halfHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        posY = getHeight() / 2;
        circleRadius = getWidth() / 4;

        /// draw big circle calling
        drawBigCircle(canvas, middleStarX, posY, circleRadius);
        /// draw user bitmap method calling
        drawUserBitmap(canvas, middleStarX, posY);


        rect.set(firstStarX, getHeight() - getHeight() / 3, firstStarX + getWidth() / 3, (getHeight() - getHeight() / 3) + getHeight() / 5);
        drawRectText(String.valueOf(count), canvas, rect);


        //// star draw section
        drawAllStar(canvas, firstStarX, posY, 1, getWhiteGreyBitmap());
        drawAllStar(canvas, middleStarX, posY, 1, getWhiteGreyBitmap());
        drawAllStar(canvas, lastStarX, posY, 1, getWhiteGreyBitmap());

        ////// star draw star count wise
        if (starCount == 1) {
            drawAllStar(canvas, firstStarX, posY, starCount, getYellowBitmap());

        }
        if (starCount == 2) {
            drawAllStar(canvas, firstStarX, posY, starCount, getYellowBitmap());
            drawAllStar(canvas, middleStarX, posY, starCount, getYellowBitmap());
        }
        if (starCount == 3) {
            drawAllStar(canvas, firstStarX, posY, starCount, getYellowBitmap());
            drawAllStar(canvas, middleStarX, posY, starCount, getYellowBitmap());
            drawAllStar(canvas, lastStarX, posY, starCount, getYellowBitmap());

        }

    }

    /// draw user bitmap method
    private void drawUserBitmap(Canvas canvas, int middleStarX, int posY) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        circleStrokePaint.setStrokeWidth(10f);
        canvas.drawCircle(middleStarX, posY / 2, 100, circleStrokePaint);
        userBitmap = getResizedBitmap(userBitmap, 84, 84);
        userBitmap = getCircleBitmap(userBitmap);
        canvas.drawBitmap(userBitmap, middleStarX - userBitmap.getWidth() / 2, (posY / 2) - userBitmap.getHeight() / 2, bitmapPaint);
    }


    /// big circle draw method
    private void drawBigCircle(Canvas canvas, int middleStarX, int posY, float radius) {
        circleStrokePaint.setStrokeWidth(50f);
        canvas.drawCircle(middleStarX, posY, radius + 5, circleStrokePaint);
        canvas.drawCircle(middleStarX, posY, radius, circlePaint);
    }


    /// method that draw text
    private void drawRectText(String countText, Canvas canvas, Rect r) {
        String text = countText + "%";
        textPaint.setTextSize(150);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();
        textPaint.setColor(Color.parseColor("#9D9D9D"));
//        canvas.drawRect(r, textPaint);
        int numOfChars = textPaint.breakText(text, true, width, null);
        int start = (text.length() - numOfChars) / 2;
        canvas.drawText(text, start, start + numOfChars, r.exactCenterX(), r.exactCenterY(), textPaint);
    }


    //// start draw controll method
    public void drawAStar(int which) {
        this.starCount = which;
        listener.playPring();
        invalidate();
    }

    public void increaseCounter(int count) {
        this.count = count;
        this.invalidate();
    }

    /// draw all star
    private void drawAllStar(Canvas canvas, int posX, int posY, int starCount, Bitmap bitmap) {

        Paint paintStar = new Paint();
        paintStar.setAntiAlias(true);
        switch (starCount) {
            case 1:
                drawStar(canvas, posX, posY, paintStar, bitmap);

                break;
            case 2:
                drawStar(canvas, posX, posY, paintStar, bitmap);
                break;
            case 3:
                drawStar(canvas, posX, posY, paintStar, bitmap);
        }

    }

    private void drawStar(Canvas canvas, int posX, int posY, Paint paintStar, Bitmap bitmap) {
//        myStarShape.setStar(posX, posY, radius, innerRadius, numberOfPoint);
//        canvas.drawPath(myStarShape.getPath(), paintStar);
        canvas.drawBitmap(bitmap, posX - bitmap.getWidth() / 2, posY - bitmap.getHeight() / 2, paintStar);
    }

    private Bitmap getWhiteGreyBitmap() {

        int res = R.drawable.ic_star_grey;
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, res);
        image = getResizedBitmap(image, 148, 148);
        return image;
    }

    private Bitmap getYellowBitmap() {

        int res = R.drawable.ic_star_yellow;
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, res);
        image = getResizedBitmap(image, 148, 148);
        return image;
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

    public void finishActivity() {
        listener.finishTheActivity();
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

    public interface PlayStarSoundInterface {
        public void playPring();

        public void finishTheActivity();
    }

}
