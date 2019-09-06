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
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Dimension;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.autismindd.R;

import java.util.ArrayList;

/**
 * Created by RAFI on 9/29/2016.
 */

public class LevelView extends View {

    float radius, cx, cy; /// to hold circle center x and center y
    private Canvas mCanvas; /// to store the canvas in ondraw method
    private Paint strokePaint;///(circle stroke paint )
    private int strokeRadius = 2;///circle stroke radius
    private int defaultRadius = 55;/// default all circle radius
    private float x, y;/// setting touch position on onTouch event method
    WhichViewClicked whichViewClickedListner;/// our interface referance to setin different method value, called in constructor
    int position = -1;/// postion  that  hot circle  position  by clicking them, used in overlap and sOverlapping() method and checkOverLaping() method
    int[] starCount; /// to hold level star that a user got called on constructor and draw star method
    String circleColor;/// set the ciicle color and assign in constructor and used in draw star method
    String circleTouchColor;/// on touch set circle color used in constructor(but not used in any other method)
    int resource; /// to store the avatar resourcen used in default constructor
    Bitmap image;// bitmap avater  image used in ondraw Mehtod
    Paint paint; // paint variable used in initViewMethod
    //    ******************* avatar animate variable end here *****************
    // angle for drawing circle in the exact position used in getCircleLocation method
    private int rightBottomAngle = 105;
    private int rightUpperAngle = 135;
    private int leftTopCenterAngle = 165;
    private int rightTopCenterAngle = 195;
    private int leftUpperAngle = 225;
    private int leftBottomAngle = 255;
    Handler h = new Handler(); /// handler to set position used in overlap method
    MediaPlayer mp;///mp reference used in play sound method
    Bitmap bigAvaterImage;/// big avatar bitmap used to draw avater in ondraw method
    private Bitmap lockBitmap = getCircleBitmap(getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_locker_level),
            90, 90));/// lock bitmap resized and get circled used in ondraw method
    private Bitmap unLockBitmap = getCircleBitmap(getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_unlocked),
            90, 90));/// unlock bitmap resized and get circled used in ondraw method

    private float[] draggedLocation;
    private String defaultAllCircleColor = "#FFFFFF"; //// default all circle color
    //// circle click control variable
    private boolean isCircleClickControlEnabled = false;
    private int[] points; /// array that hold total point per level

    private int permitIndex; // check permit index


    // to recognize which star color
    public LevelView(Context context, WhichViewClicked whichViewClickedListner, int[] starCount, String circleColor, String
            circleTouchColor, int resource, int[] points) {
        super(context);
        this.whichViewClickedListner = whichViewClickedListner;
        this.starCount = starCount;
        this.circleTouchColor = circleTouchColor;
        this.circleColor = circleColor;
        this.resource = resource;
        this.points = points;
        initMyView();
        permitIndex = getPlayAble();
    }


    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public LevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //// initialization all paints and other variables
    public void initMyView() {
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

    }

    //// method that calculate screen width
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    //// method that calculate screen height
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    /// pading used to draw the big circle
    float returnTenPercentPaddingWidth() {
        float width = (float) getWidth();
        return (10 * width) / 100;
    }

    ///// method that return circles location using angle
    private float[] getCircleLocation(int angle) {
        float[] location = new float[2];
        float radian = (float) Math.toRadians(angle);

        location[0] = (float) (cx + radius * Math.sin(radian));
        location[1] = (float) (cy + radius * Math.cos(radian));

        return location;
    }

    boolean animate = false;// for animating avatar

    private int lockerOpen;/// for checking is the locker is open or not

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        this.mCanvas = canvas;
        Paint p = new Paint();
        //draw big circle
        drawBigCircle(canvas, p);

        lockerOpen = lastStar(); /// getting the unlock level by checking stars

        // bitmap
//        int res = resource;
        Resources r = getResources();
        image = BitmapFactory.decodeResource(r, resource);
        bigAvaterImage = image;
        bigAvaterImage = getResizedBitmap(bigAvaterImage, 200, 200);
        image = getResizedBitmap(image, 100, 100);

        Bitmap bitmap = getCircleBitmap(image);
        p.setStrokeWidth(6);

        p.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);


        /////****************************Most Left Circle Draw and other control here **************************/////
        //left circle bottom
        float[] leftBottomCircle = getCircleLocation(leftBottomAngle);
        if (position == 0) { /// worked when click on a specific circle and set position =0

            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, leftBottomCircle[0], leftBottomCircle[1], defaultRadius);/// draw left bottom circle

        } else {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            if (lockerOpen > -1 && lockerOpen == 0 && isCircleClickControlEnabled) { /// if has any unlock level
                if (makeItDisappear) {
                    drawDisAppearCircle(canvas, p, leftBottomCircle[0], leftBottomCircle[1]);
                } else {
                    drawCircle(canvas, p, unLockBitmap, leftBottomCircle[0], leftBottomCircle[1], defaultRadius);
                    whichViewClickedListner.getUnlockCircleInfo(leftBottomCircle[0], leftBottomCircle[1], defaultRadius);
                }
            } else
                drawCircle(canvas, p, lockBitmap, leftBottomCircle[0], leftBottomCircle[1], defaultRadius); ///drawing the left bottom circle

        }
//// draw star above left circle bottom if has any star
        drawStar(mCanvas, leftBottomCircle[0], leftBottomCircle[1], starCount[0], getYellowBitmap(),
                points[0]);
        if (isDragged && position == 0) { /// called when percent shown in circle and go to play on that level
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, leftBottomCircle[0], leftBottomCircle[1], defaultRadius);/// draw left bottom circle
        }


        /////****************************Left Top Circle Draw and other control here **************************/////
        //left top circle position
        float[] leftTopCircle = getCircleLocation(leftUpperAngle);
        if (position == 1) {//// called when position set to 1

            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, leftTopCircle[0], leftTopCircle[1], defaultRadius); /// draw left top circle

        } else {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            if (lockerOpen > -1 && lockerOpen == 1 && isCircleClickControlEnabled) {
                /// checking if has any unlock level
                if (makeItDisappear) {
                    drawDisAppearCircle(canvas, p, leftTopCircle[0], leftTopCircle[1]);
                } else {
                    drawCircle(canvas, p, unLockBitmap, leftTopCircle[0], leftTopCircle[1], defaultRadius);
                    whichViewClickedListner.getUnlockCircleInfo(leftTopCircle[0], leftTopCircle[1], defaultRadius);
                }
            } else
                drawCircle(canvas, p, lockBitmap, leftTopCircle[0], leftTopCircle[1], defaultRadius); ///draw unlock bitmap to the left to circle
        }

////method that draw star int left top circle with yellow star bitmap
        drawStar(mCanvas, leftTopCircle[0], leftTopCircle[1], starCount[1], getYellowBitmap(), points[1]);
        if (isDragged && position == 1) {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, leftTopCircle[0], leftTopCircle[1], defaultRadius); /// draw left top circle
        }


        /////****************************Most Top-Center Circle Draw and other control here **************************/////
        // draw top center circle
        float[] locationCenterTop = getCircleLocation(rightTopCenterAngle);
        if (position == 2) { /// checking position

            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, locationCenterTop[0], locationCenterTop[1], defaultRadius);
        } else {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            if (lockerOpen > -1 && lockerOpen == 2 && isCircleClickControlEnabled) { /// checking if playable
                if (makeItDisappear) {
                    drawDisAppearCircle(canvas, p, locationCenterTop[0], locationCenterTop[1]);
                } else {
                    drawCircle(canvas, p, unLockBitmap, locationCenterTop[0], locationCenterTop[1], defaultRadius);
                    whichViewClickedListner.getUnlockCircleInfo(locationCenterTop[0], locationCenterTop[1], defaultRadius);// methdo that pass unlock circle position

                }
            } else
                drawCircle(canvas, p, lockBitmap, locationCenterTop[0], locationCenterTop[1], defaultRadius);/// drawing lock bitmap

        }
/// drawing star int top center
        drawStar(mCanvas, locationCenterTop[0], locationCenterTop[1], starCount[2], getYellowBitmap(),
                points[2]);
        if (isDragged && position == 2) {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, locationCenterTop[0], locationCenterTop[1], defaultRadius);
        }


        /////****************************Most top-right Circle Draw and other control here **************************/////

///// top center right circle with stars
        float[] locationLeftCenterTop = getCircleLocation(leftTopCenterAngle);
        if (position == 3) {

            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, locationLeftCenterTop[0], locationLeftCenterTop[1], defaultRadius); /// drawing circle top right circle
            //animateAvatar(canvas);
        } else {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            if (lockerOpen > -1 && lockerOpen == 3 && isCircleClickControlEnabled) { //checking the unlock circle and draw it and pass its info through the method
                if (makeItDisappear) {
                    drawDisAppearCircle(canvas, p, locationLeftCenterTop[0], locationLeftCenterTop[1]);
                } else {
                    drawCircle(canvas, p, unLockBitmap, locationLeftCenterTop[0], locationLeftCenterTop[1], defaultRadius);
                    whichViewClickedListner.getUnlockCircleInfo(locationLeftCenterTop[0], locationLeftCenterTop[1], defaultRadius);

                }
            } else
                drawCircle(canvas, p, lockBitmap, locationLeftCenterTop[0], locationLeftCenterTop[1], defaultRadius);// draw top right circle with lock bitmap

        }
/// draw star to center right circle above
        drawStar(mCanvas, locationLeftCenterTop[0], locationLeftCenterTop[1], starCount[3],
                getYellowBitmap(), points[3]);
        if (isDragged && position == 3) {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, locationLeftCenterTop[0], locationLeftCenterTop[1], defaultRadius); /// drawing circle top right circle
        }


        /////****************************Most bottom-right Circle Draw and other control here **************************/////

        //right 2nd top circle
        float[] rightTopCircle = getCircleLocation(rightUpperAngle);
        if (position == 4) {
            //animateAvatar(canvas);
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, rightTopCircle[0], rightTopCircle[1], defaultRadius);

        } else {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            if (lockerOpen > -1 && lockerOpen == 4 && isCircleClickControlEnabled) {
                if (makeItDisappear) {
                    drawDisAppearCircle(canvas, p, rightTopCircle[0], rightTopCircle[1]);
                } else {
                    drawCircle(canvas, p, unLockBitmap, rightTopCircle[0], rightTopCircle[1], defaultRadius);
                    whichViewClickedListner.getUnlockCircleInfo(rightTopCircle[0], rightTopCircle[1], defaultRadius);
                }
            } else
                drawCircle(canvas, p, lockBitmap, rightTopCircle[0], rightTopCircle[1], defaultRadius);// drawing lock bitmap to the center 2nd right position

        }
//// draw star center 2nd right circle above
        drawStar(mCanvas, rightTopCircle[0], rightTopCircle[1], starCount[4], getYellowBitmap(), points[4]);
        if (isDragged && position == 4) {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, rightTopCircle[0], rightTopCircle[1], defaultRadius);
        }


        /////**************************** bottom-right Circle Draw and other control here **************************/////
        //draw right bottom circle
        float[] rightBottomLocation = getCircleLocation(rightBottomAngle);
        if (position == 5) {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, rightBottomLocation[0], rightBottomLocation[1], defaultRadius);

        } else {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            if (lockerOpen > -1 && lockerOpen == 5 && isCircleClickControlEnabled) {
                if (makeItDisappear) {
                    drawDisAppearCircle(canvas, p, rightBottomLocation[0], rightBottomLocation[1]);
                } else {
                    drawCircle(canvas, p, unLockBitmap, rightBottomLocation[0], rightBottomLocation[1], defaultRadius);
                    whichViewClickedListner.getUnlockCircleInfo(rightBottomLocation[0], rightBottomLocation[1], defaultRadius);
                }
            } else
                drawCircle(canvas, p, lockBitmap, rightBottomLocation[0], rightBottomLocation[1], defaultRadius);// drawing right bottom circle

        }
        ///drawing star to the right bottom circle above
        drawStar(mCanvas, rightBottomLocation[0], rightBottomLocation[1], starCount[5], getYellowBitmap(), points[5]);
        if (isDragged && position == 5) {
            p.setColor(Color.parseColor(defaultAllCircleColor));
            drawCircle(canvas, p, bitmap, rightBottomLocation[0], rightBottomLocation[1], defaultRadius);
        }

    }


    /// big circle default color
    private String bigCircleColor = "#80FFFFFF";

    //method that draw the big circle
    private void drawBigCircle(Canvas canvas, Paint p) {
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor(bigCircleColor));
        p.setAntiAlias(true);
        cx = getWidth() / 2;
        cy = getHeight();
        radius = getWidth() / 2 - returnTenPercentPaddingWidth();
        canvas.drawCircle(cx, cy, radius, p);
    }

    /// method that drawcircle by given position (used in on draw method)
    public void drawCircle(Canvas canvas, Paint paint, Bitmap bitmap, float x, float y, int radius) {
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(x, y, radius, paint);
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, null);


    }

    ///
    private void drawDisAppearCircle(Canvas canvas, Paint paint, float cx, float cy) {
        strokePaint = new Paint();
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(4);
        canvas.drawCircle(cx, cy, defaultRadius + strokeRadius, strokePaint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(circleColor));
        paint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, defaultRadius, paint);
    }

    /// font-size to draw percent text in proper way
    private float fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, getResources().getDisplayMetrics());
    private int fontWeight = 7; /// text font weight
    private String percent = "%";/// additional text

    /// method that draw text  with a circle called from drawStar method by checking if the level played before
    public void drawCircleWithText(Canvas canvas, Paint paint, String text, float x, float y, int radius) {
        strokePaint = new Paint();
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(4);
        canvas.drawCircle(x, y, radius + strokeRadius, strokePaint);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(x, y, radius, paint);
        drawText(canvas, x, y, text, fontSize, Color.WHITE, fontWeight);
    }

    // method that draw text called from draw circle with text
    private void drawText(Canvas canvas, float x, float y, String text, float size, int color, float boldWidth) {

        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(color);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(size);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(boldWidth);
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();
        canvas.drawText(text + percent, x, y + textOffset, textPaint);
    }


    /// method that draw 3 star called from on draw by giving its required value
    private void drawStar(Canvas canvas, float x, float y, int starCount, Bitmap starBitmap,
                          int point) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        switch (starCount) {
            case 1:

                drawLeftStar(canvas, x, y, paint, starBitmap);
                // for not getting star drawn area
                drawMiddleStar(canvas, x, y, paint, getWhiteGreyBitmap());
                drawRightStar(canvas, x, y, paint, getWhiteGreyBitmap());
                /// percent draw area
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor(circleColor));
                drawCircleWithText(canvas, paint, String.valueOf(point), x, y, defaultRadius);// draw text with circle
                break;
            case 2:
                drawLeftStar(canvas, x, y, paint, starBitmap);
                drawMiddleStar(canvas, x, y, paint, starBitmap);
                /// for not getting star draw area
                drawRightStar(canvas, x, y, paint, getWhiteGreyBitmap());
                /// percent draw area
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor(circleColor));
                drawCircleWithText(canvas, paint, String.valueOf(point), x, y, defaultRadius);
                break;
            case 3:
                drawLeftStar(canvas, x, y, paint, starBitmap);
                drawMiddleStar(canvas, x, y, paint, starBitmap);
                drawRightStar(canvas, x, y, paint, starBitmap);
                /// percent draw area
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor(circleColor));
                drawCircleWithText(canvas, paint, String.valueOf(point), x, y, defaultRadius);

                break;
        }
    }

    //method that draw left star
    private void drawLeftStar(Canvas canvas, float x, float y, Paint paint, Bitmap bitmap) {

        canvas.drawBitmap(bitmap, (x - getWidth() / 18) - bitmap.getWidth() / 2, (y - getHeight() / 7) - bitmap.getHeight() / 2, paint);

    }

    //method that draw right star
    private void drawRightStar(Canvas canvas, float x, float y, Paint paint, Bitmap bitmap) {

        canvas.drawBitmap(bitmap, (x + getWidth() / 18) - bitmap.getWidth() / 2, (y - getHeight() / 7) - bitmap.getHeight() / 2, paint);

    }

    //method that draw middle star method
    private void drawMiddleStar(Canvas canvas, float x, float y, Paint paint, Bitmap bitmap) {
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, (y - getHeight() / 7) - bitmap.getHeight() / 2, paint);

    }

    //method that return grey bitmap
    private Bitmap getWhiteGreyBitmap() {

        int res = R.drawable.ic_star_grey;
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, res);
        image = getResizedBitmap(image, 48, 48);
        return image;
    }

    /// method that return yellow bitmap
    private Bitmap getYellowBitmap() {

        int res = R.drawable.ic_star_yellow;
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, res);
        image = getResizedBitmap(image, 48, 48);
        return image;
    }

    /// method that resized a bitmap by giving new width and height
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

    /// method that make a image circular
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

    /// method that check if two rectangle intersect or not called from overlap image and overlap method called when we touch a circle
    private boolean isViewOverlapping(int x, int y, float[] circleLocation) {

        int circleLocationX = (int) circleLocation[0];
        int circleLocationY = (int) circleLocation[1];
        Rect rect1 = new Rect(circleLocationX, circleLocationY, circleLocationX + defaultRadius, circleLocationY + defaultRadius);

        Rect rect2 = new Rect(x, y, x + 20, y + 20);

        return rect1.intersect(rect2);
//      return  (rect1.contains(rect2)) || (rect2.contains(rect1));
    }

    private boolean isDragged = false;

    ///// method used for another activity level overlap
    public boolean checkOverLaping(float x, float y, int width, int height) {

        ArrayList<float[]> locationList = new ArrayList<>();

        locationList.add(getCircleLocation(leftBottomAngle));

        locationList.add(getCircleLocation(leftUpperAngle));

        locationList.add(getCircleLocation(rightTopCenterAngle));

        locationList.add(getCircleLocation(leftTopCenterAngle));

        locationList.add(getCircleLocation(rightUpperAngle));

        locationList.add(getCircleLocation(rightBottomAngle));

        for (int i = 0; i < locationList.size(); i++) {

            float[] circleLocation = locationList.get(i);
            Rect rect1 = new Rect((int) circleLocation[0], (int) circleLocation[1], (int) circleLocation[0] + defaultRadius, (int)
                    circleLocation[1] + defaultRadius);

            Rect rect2 = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
            if (rect1.intersect(rect2) && isCircleClickControlEnabled) {
                //position = i;
                isDragged = true;
                Log.e("This is the shit", "yes");
                if (i <= (getPlayAble())) {
                    position = i;
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isTouch == false) {
                                whichViewClickedListner.viewPosition(position);/// setting circle position
                                isTouch = true;
                            }
                        }
                    }, 300);

                    if (soundplayed == false) {
                        whichViewClickedListner.playsound(0);
                        soundplayed = true;
                    }
                    animX = rect1.left;
                    animY = rect1.top;
                    whichViewClickedListner.getAnimationPos(animX, animY); // setting anim position
                    invalidate();
                    return true;
                }
            }
        }
        return false;

    }

    private float animX, animY; /// where to animate
    private boolean isTouch = false; //// for checking if touch is true or not // used to control one time cirlce click
    private boolean isAnimate = false; /// used to control one time animation
    boolean soundplayed = false; /// used to control one time sound palyed

    // method that checking is touch position and circle postion are intersect by creating two rectangle
    public void isOverlapping(int x, int y) { /// <code>not used method off for client issue, may be need in future</code>
//        int position = -1;
        for (int i = 0; i < getPlayAble() + 1; i++) {/// check if the touched cirlce is playable or not
            switch (i) {
                case 0:
                    // left bottom circle
                    float[] leftBottomCircleLocation = getCircleLocation(leftBottomAngle);
                    if (isViewOverlapping(x, y, leftBottomCircleLocation)) {
                        position = 0;
                        animX = x;
                        animY = y;
                    }

                    break;
                case 1:
                    // left upper circle
                    float[] leftUpperCircleLocation = getCircleLocation(leftUpperAngle);
                    if (isViewOverlapping(x, y, leftUpperCircleLocation)) {
                        position = 1;
                        animX = x;
                        animY = y;
                    }
                    break;
                case 2:
                    //center left upper top circle
                    float[] topRightCircleLocation = getCircleLocation(rightTopCenterAngle);
                    if (isViewOverlapping(x, y, topRightCircleLocation)) {
                        position = 2;
                        animX = x;
                        animY = y;
                    }
                    Log.d("from overlap", "pos: 2");

                    break;
                case 3:
                    //center right upper top circle
                    float[] topLeftCircleLocation = getCircleLocation(leftTopCenterAngle);
                    if (isViewOverlapping(x, y, topLeftCircleLocation)) {
                        position = 3;
                        animX = x;
                        animY = y;
                    }
                    Log.d("from overlap", "pos: 3");
                    break;
                case 4:
                    //right right upper circle
                    float[] rightUpperCircleLocation = getCircleLocation(rightUpperAngle);
                    if (isViewOverlapping(x, y, rightUpperCircleLocation)) {
                        position = 4;
                        animX = x;
                        animY = y;
                    }
                    break;
                case 5:
                    // right bottom circle
                    float[] rightBottomCircleLocation = getCircleLocation(rightBottomAngle);
                    if (isViewOverlapping(x, y, rightBottomCircleLocation)) {
                        position = 5;
                        animX = x;
                        animY = y;

                    }
                    break;
                default:
                    break;
            }
        }
        if (position != -1) {
            if (isAnimate == false) {
                whichViewClickedListner.getAnimationPos(animX, animY);
                isAnimate = true;
            }

            if (soundplayed == false) {
                whichViewClickedListner.playsound(0);
                soundplayed = true;
            }
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isTouch == false) {
                        whichViewClickedListner.viewPosition(position);
                        isTouch = true;
                    }
                }
            }, 300);
        }
    }

    ////central method that control circle click event
    public void enabledCircleClick(boolean enableCircleClick) {
        this.isCircleClickControlEnabled = enableCircleClick;
        invalidate();
    }

    // helper method that give us the next level that is playable
    public int getPlayAble() {
        int count = 0;
        for (int i = 0; i < starCount.length; i++) {

            if (starCount[i] != 0) {
                count++;

            }

        }
        return count;
    }

    //this method will return the last available star which can indicate the unlocker screen
    public int lastStar() {
        int last = -1;
        for (int i = 0; i < starCount.length; i++) {

            if (starCount[i] == 0) {
                last = i;
                break;
            }
        }

        return last;
    }

    /// media player release int onDetachedFromWindow  method
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mp != null)
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = null;
            }
    }

    /// on touch event method that give us x y position
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
//                if (isCircleClickControlEnabled) { ////<code> commented by sumon for client issue</code>
//                    isOverlapping((int) x, (int) y);
//                }
//                invalidate();
                break;
        }
        return true;
    }

    private boolean makeItDisappear = false;/// that used to controll unlocker bitmap disappear

    /// method that used for making unlock bitmap disappear
    public void makeUnlockDisAppear(boolean makeDisAppear) {
        this.makeItDisappear = makeDisAppear;
        invalidate();
    }


    // whoever implement this
    // if position=5 must check which circle
    public interface WhichViewClicked {
        public void viewPosition(int labelID);// seting circle postion

        public void playsound(int avatar);/// play sound on touch

        public void getAnimationPos(float x, float y);// set animaton positn

        public void getUnlockCircleInfo(float cx, float cy, int radius);///setting unlock level information
    }

}