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

public class CustomViewForGiftBox extends View {
    private Paint paint;
    private Context context;
    private Bitmap giftBitmap;
    private PlayGiftBoxSoundInterface listner;
    private int middleStarX;
    private int posY;
    private float circleRadius;
    private Paint circlePaint;
    private Paint circleStrokePaint;

    public CustomViewForGiftBox(Context context, int res, PlayGiftBoxSoundInterface listner) {
        super(context);
        this.context = context;
        this.giftBitmap = BitmapFactory.decodeResource(getResources(), res);
        this.listner = listner;
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


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        middleStarX = getWidth() / 2;
        posY = getHeight() / 2;
        circleRadius = getWidth() / 4;
        /// draw big circle calling
        drawBigCircle(canvas, middleStarX, posY, circleRadius);
        /// draw user bitmap method calling
        drawUserBitmap(canvas, middleStarX, posY);


    }

    /// draw user bitmap method
    private void drawUserBitmap(Canvas canvas, int middleStarX, int posY) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
//        circleStrokePaint.setStrokeWidth(10f);
//        canvas.drawCircle(middleStarX, posY / 2, 100, circleStrokePaint);
        giftBitmap = getResizedBitmap(giftBitmap, 284, 284);
        giftBitmap = getCircleBitmap(giftBitmap);
        canvas.drawBitmap(giftBitmap, middleStarX - giftBitmap.getWidth() / 2, (posY / 2) - giftBitmap.getHeight() / 2, bitmapPaint);
    }


    /// big circle draw method
    private void drawBigCircle(Canvas canvas, int middleStarX, int posY, float radius) {
        circleStrokePaint.setStrokeWidth(50f);
        canvas.drawCircle(middleStarX, posY, radius + 5, circleStrokePaint);
        canvas.drawCircle(middleStarX, posY, radius, circlePaint);
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
        listner.finishTheActivity();
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

    public interface PlayGiftBoxSoundInterface {
        public void playPring();

        public void finishTheActivity();
    }

}
