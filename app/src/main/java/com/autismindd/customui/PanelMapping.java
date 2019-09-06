package com.autismindd.customui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;


public class PanelMapping extends View implements OnTouchListener {

    private Canvas mCanvas;
    MyPaths mMyPaths;
    Context context;
    DoubleTapListener doubleTapListener;
    GestureDetector gestureDetector;
    GestureListener gestureListener;

    private int defaultColor = Color.parseColor("#7d5fbcd3"),// default color
            backColor = Color.TRANSPARENT,// Color.TRANSPARENT
            selectedColor = Color.parseColor("#7dff5555");// Color.RED

    private Bitmap mBitmap, bitLetter, bitLetter1, bitPic, bitPic1;


    private Boolean isTouched = false;
    private int currentItemId, selectedItemId;
    private float currentXPos = 20, currentYPos = 20, selectedXPos = 20, selectedYPos = 20, width = 20,
            height = 20;

    int screenHeight, screenWidth;

    private ArrayList<MyPaths> paths = new ArrayList<MyPaths>();
    private ArrayList<MyPaths> pathsX = new ArrayList<MyPaths>();
    private ArrayList<MyPaths> total = new ArrayList<MyPaths>();


    private ArrayList<Integer> mCorrectIDList = new ArrayList<Integer>();
    private Coordinate coordinate;
    private float x;
    private float y;

    private Paint getPaintFormat() {
        Paint mPaint = new Paint();
        // mPaint.setAntiAlias(true);
        // mPaint.setDither(true);
        mPaint.setColor(defaultColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        // mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShadowLayer(0, 0, 0, 0);

        // mPaint.set
//		mPaint.setStrokeWidth(selectedFontSize);
        return mPaint;
    }

    GETClickValue listener=null;

    public PanelMapping(Context context, int screenHeight, int screenWidth, GETClickValue listener) {
        super(context);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        coordinate = new Coordinate();
        gestureDetector = new GestureDetector(context,new GestureListener());
        this.listener=listener;

        mCanvas = new Canvas();

        Path mPath = new Path();
        mMyPaths = new MyPaths(mPath, getPaintFormat());
        paths.add(mMyPaths);
        pathsX.add(mMyPaths);
        total.add(mMyPaths);


    }

    public PanelMapping(Context context) {
        super(context);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);

        mCanvas = new Canvas();

        Path mPath = new Path();
        mMyPaths = new MyPaths(mPath, getPaintFormat());
        paths.add(mMyPaths);
        pathsX.add(mMyPaths);
        total.add(mMyPaths);
        coordinate = new Coordinate();
        gestureListener=new GestureListener();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backColor);

        if (mBitmap == null)

            mBitmap = Bitmap.createBitmap(screenWidth,
                    screenHeight,
                    Bitmap.Config.ARGB_8888);

        canvas.drawBitmap(mBitmap, 0, 0, new Paint());
        for (MyPaths p : pathsX) {

            // p.mPaint.setColor(selectedColor);
            // Log.i("pppp", "ppp");
            canvas.drawPath(p.mPath, getPaintFormat());
        }
        this.mCanvas = canvas;

    }


    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        // mMyPaths.mPath.reset();
        mMyPaths.mPath.moveTo(x, y);

        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mMyPaths.mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

        }
    }

    private void touch_up() {

        Canvas c = new Canvas(mBitmap);
        Paint paint = getPaintFormat();
        paint.setColor(selectedColor);
        paint.setMaskFilter(new EmbossMaskFilter(new float[]{1, 1, 1}, 0.2f,
                10, 8.2f));
        c.drawLine(selectedXPos, selectedYPos,
                currentXPos, currentYPos, paint);
        resetPaint();
    }

    // reset
    public void resetPaint() {
//		mPaint = getPaintFormat();
        pathsX.clear();
        paths.clear();
        total.clear();
        Path mPath = new Path();
        mMyPaths = new MyPaths(mPath, getPaintFormat());
        paths.add(mMyPaths);
        pathsX.add(mMyPaths);
        total.add(mMyPaths);
    }
boolean isfingerDown=false;
    @Override
    public synchronized boolean onTouch(View arg0, MotionEvent event) {
       // gestureListener.onDoubleTapEvent(event);
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                touch_start(x, y);

                if(isfingerDown==false)
                if(listener!=null) {
                    listener.startXYPanelPoint(x, y);
                    isfingerDown=true;
                }
               // Toast.makeText(context, "StartPoint: x, y = " + String.valueOf(x) + " , " + String.valueOf(y), Toast.LENGTH_SHORT).show();
                coordinate.setStartX(x);
                coordinate.setStartY(y);

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                touch_move(x, y);
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                touch_up();
//				 resetPain();
                // touch_up();
                coordinate.setEndX(x);
                coordinate.setEndY(y);

                if(isfingerDown)
                    if(listener!=null) {
                        listener.endXYPanelPoint(x, y);
                        isfingerDown=false;

                    }

                invalidate();


                break;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }


    public String getStartXY() {
        String startXY = String.valueOf(coordinate.getStartX()) + " , " + String.valueOf(coordinate.getStartY());
        return startXY;
    }

    public String getEndXY() {
        String enXY = String.valueOf(coordinate.getEndX()) + " , " + String.valueOf(coordinate.getEndY());
        return enXY;
    }

    public void setDoubleTapListener(DoubleTapListener doubleTapListener) {
        this.doubleTapListener = doubleTapListener;
    }

    public interface DoubleTapListener {
        void onDoubleTap(float x, float y);
    }

    public interface GETClickValue{
        void startXYPanelPoint(float x, float y);
        void endXYPanelPoint(float x, float y);

        void singleTap(float x, float y);
        void doubleTap(float x, float y);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (listener != null) {
                listener.singleTap(x, y);
            }
            return super.onSingleTapConfirmed(e);
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            float x = e.getX();
            float y = e.getY();

            if (listener != null) {
                listener.doubleTap(x, y);
            }

            return true;
        }
    }

}
