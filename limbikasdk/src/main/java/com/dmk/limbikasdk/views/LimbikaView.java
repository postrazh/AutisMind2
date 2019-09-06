package com.dmk.limbikasdk.views;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.dmk.limbikasdk.R;
import com.dmk.limbikasdk.db.Database;
import com.dmk.limbikasdk.pojo.LimbikaViewItemValue;
import com.dmk.limbikasdk.utilities.ImageProcessing;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class LimbikaView extends View {

    //text alignment constants
    public static final int ALIGN_CENTER = 101;
    public static final int ALIGN_LEFT = 102;
    public static final int ALIGN_RIGHT = 103;
    public static final int TEXT_TOP = 104;
    public static final int TEXT_CENTER = 105;
    public static final int TEXT_BOTTOM = 106;
    //constant for defining the time duration between the click that can be considered as double-tap
    static final int MAX_DURATION = 800;
    private static final float TOUCH_TOLERANCE = 4;
    final int MINIMUM_SIZE = 25;
    //Amit start
    final int cornerRadius = 10;
    private final int POSITIVE_TAG = 200;
    private final int NEGATIVE_TAG = 201;
    private final int SHOW_BALLS = 203;
    private final int HIDE_BALLS = 204;
    public Context context;
    public Bitmap mBitmap = null;
    //defaults
    public int canvasHeight = 256;  //80 is the minimum
    //Amit end
    public int canvasWidth = 256;
    //holds the key of target
    public long targetKey = -1;
    Point[] points = new Point[4];
    Point point1, point3;
    Point point2, point4;
    Point startMovePoint;
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = -1;
    boolean isCircleView = false;
    int rotation = 0;

    int someShittyColorr = -32888;

    int backgroundColor = someShittyColorr;//some color
    Drawable backgroundDrawable = null;
    boolean isRestoring = false;
    float dX, dY;
    //Listeners vars
    DoubleTapListener doubleTapListener;
    RotationListener rotationListener;
    SingleTapListener singleTapListener;
    LongTapListener longTapListener;
    GestureDetector gestureDetector;
    DragListener dragListener;
    Rect viewBounds = null;
    int width, height;
    WindowManager wm;
    int xCenter;
    int yCenter;
    LimbikaViewItemValue limbikaViewItemValue;
    DragFinishListner dragFinishListner;
    EraseListner eraseListner;
    ImageProcessing imageProcessing;
    int textAlignment = ALIGN_CENTER;//align center by default
    float posX, posY;
    //this one is used when restoring the view, this one is
    float sleft, sright, stop, sbottom;
    int savedDrawable = -1;
    String blob = null;
    boolean shouldRotate = true;
    Database helper;
    SQLiteDatabase db;
    //this holds the value of right and lft in onDraw
    float gRight;
    float gBottom;
    float gLeft;
    float gTop;
    boolean playmode = false;
    int clickCount = 0;
    //variable for storing the time of first click
    long startTime;
    //variable for calculating the total time
    long duration;
    //openApp Package name added by reaz
    String openApp;
    //every limbika view must contain its droptargets
    LinkedHashMap<Long, LimbikaView> dropTargetsMap = new LinkedHashMap<>();
    boolean alreadyDropped = false;
    //use this one to color and make rectangle select/deselect RED RECTANGLE
    Paint rectangleBorder_paint = new Paint();
    //Round Corner
    ///////////////////////// Outer Rect Round ****************/////////
    int pixels_for_roundcorner = 20;
    boolean round_corner_shape = false;
    Paint paintfullBackground;
    ///////////////////////// Outer Border Round ****************/////////
    boolean borderVisible = false;
    // int pixels_for_roundcorner_border = 0;
    Paint border_paint = new Paint();
    int outerBorderColor = Color.TRANSPARENT;
    int outerBorderSize = 0;
    /////////////////////// CUSTOM TYPEFACE **********//////////////////
    Typeface typefaceCustom = null;
    /////////////////////////******** auto play openApp*********///////////////
    int autoPlay;
    int soundDalay;
    private double deviation = .5;  /* deviation used for balancing relative height and width*/
    // variable to know what ball is being dragged
    private Paint paint;
    private Canvas canvas;
    private int image = -1;
    private Bitmap imageBitmap = null;
    private ArrayList<ColorBall> colorballs = new ArrayList<ColorBall>();
    // array that holds the balls
    private int balID = -1;
    private LimbikaView limbikaView;
    private String type;
    private String text;
    private Typeface typeFace;
    private int textColor = -15066598; // default color black Set
    private int textSize = -1;
    private int circleColor = someShittyColorr;
    private int borderColor = Color.TRANSPARENT;
    private Long key;
    private boolean isBorderVisible = true; //border is visible by default
    //Amit finish
    private int textType = TEXT_CENTER; //center text is the default value
    private int tag = -1;
    private LimbikaView dropTarget;
    private boolean isDraggable = true;
    private DropTargetListener dropTargetListener = null;
    //initial position of this view before user drops it on the parent view
    //these values will keep track of the last position of the view
    private float childInitX = 0;
    private float childInitY = 0;
    private float mX, mY;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private String result;
    private String itemSoundClip;
    private String openUrl;
    private long navigateTo;
    private int allowDragDrop;
    private long dragDropTarget;
    private int fontType;
    private int showBalls = -1;
    private boolean isChildView = false;
    private String imagePath = "";
    private Paint mostOuterRectPaint;
    private boolean isMostOuterRect = false;
    private boolean isStartAnimation = false;
    private int whichAnimation;

    public LimbikaView(Context context) {
        super(context);
        paint = new Paint();
        canvas = new Canvas();
        init(context);
        imageProcessing = new ImageProcessing(context.getPackageName());
    }

    public LimbikaView(Context context, Long key) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        this.key = key;
        init(context);
        imageProcessing = new ImageProcessing(context.getPackageName());
    }

    public LimbikaView(Context context, Long key, boolean playmode) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        this.key = key;
        init(context);
        this.playmode = playmode;
        imageProcessing = new ImageProcessing(context.getPackageName());
    }

/*    int closeApp = -1;

    public void setCloseApp(int closeApp) {
        this.closeApp = closeApp;
    }*/

    public LimbikaView(Context context, Long key, String type) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        this.key = key;
        this.type = type;
        //  setLayoutParams(new RelativeLayout.LayoutParams(layout_width, layout_height));
        init(context);
        imageProcessing = new ImageProcessing(context.getPackageName());
    }


    public LimbikaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        canvas = new Canvas();
        init(context);
    }

    public LimbikaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        canvas = new Canvas();
        init(context);
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderVisible(boolean isBorderVisible) {
        this.isBorderVisible = isBorderVisible;
    }

    public void setTextPosition(int TEXT_TYPE) {
        this.textType = TEXT_TYPE;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(Long targetkey) {
        this.targetKey = targetkey;
    }

    // Amit Start
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        //super.setBackgroundColor(color);
    }

    public void setBackground(Drawable background) {
        this.backgroundDrawable = background;
        super.setBackground(background);
    }

    public void setDropTargetsMap(LinkedHashMap<Long, LimbikaView> map) {
        dropTargetsMap = map;
    }

    /**
     * restores the view if it exists
     **/
    public void onResume(LimbikaViewItemValue limbikaViewItemValue) {

        //Modified by RAFI
        if (limbikaViewItemValue != null) {
            this.limbikaViewItemValue = limbikaViewItemValue;

            WindowManager w = wm;
            Display d = w.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
// since SDK_INT = 1;
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;
// includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
                try {
                    screenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                    screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
// includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    screenWidth = realSize.x;
                    screenHeight = realSize.y;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }

            //get height & width in points
            float scaleFactor = metrics.density;

            //calculate the amount of density independent pixels there are for a certain height or width.
            float widthDp = screenWidth / scaleFactor;
            float heightDp = screenHeight / scaleFactor;

            //to know the exact dimensions of a device in inches, you can work that out too, using the metrics method for how many pixels
            // there are per inch of the screen.
            float widthDpi = metrics.xdpi;
            float heightDpi = metrics.ydpi;

            //work out how many inches the device is.
            float widthInches = screenWidth / widthDpi;
            float heightInches = screenHeight / heightDpi;


            float x = limbikaViewItemValue.getX() * screenWidth / 100;
            float y = limbikaViewItemValue.getY() * screenHeight / 100;

            Log.d("Limbika-on-resume", "X is at " + limbikaViewItemValue.getX() + "% == " + x + " px");
            Log.d("Limbika-on-resume", "Y is at " + limbikaViewItemValue.getY() + "% == " + y + " px");


            int rotation = limbikaViewItemValue.getRotation();
            int circleColor = limbikaViewItemValue.getCircleColor();
            int borderColor = limbikaViewItemValue.getBorderColor();
            int textColor = limbikaViewItemValue.getTextColor();
            int backgroundColor = limbikaViewItemValue.getBackgroundColor();
            int isCircleView = limbikaViewItemValue.getIsCircleView();
            int textSize = limbikaViewItemValue.getTextSize();
            int roundedCorner = limbikaViewItemValue.getCornerRound();
            //result = limbikaViewItemValue.getResult();
            //String itemSound = limbikaViewItemValue.getItemSound();
            int itemFontType = limbikaViewItemValue.getFontTypeFace();
            int fontTypeAlign = limbikaViewItemValue.getFontAlign();
            //String openURL = limbikaViewItemValue.getOpenURL();
            // String getOpenApp = limbikaViewItemValue.getOpenApp();
            // int getCloseApp = limbikaViewItemValue.getCloseApp();
            // navigateTo = limbikaViewItemValue.getNavigateTo();
            //allowDragDrop = limbikaViewItemValue.getAllowDragDrop();
            //dragDropTarget = limbikaViewItemValue.getDragDropTarget();*/
            int getAutoPlay = limbikaViewItemValue.getAutoPlay();
            int getSoundDelay = limbikaViewItemValue.getSoundDelay();
            int getBorderPixel = limbikaViewItemValue.getBorderPixel();

            sleft = (limbikaViewItemValue.getLeft() * screenWidth) / 100;
            sright = (limbikaViewItemValue.getRight() * screenWidth) / 100;

            stop = (limbikaViewItemValue.getTop() * screenHeight) / 100;
            sbottom = (limbikaViewItemValue.getBottom() * screenHeight) / 100;


            // px implementation
            int width = Float.valueOf((limbikaViewItemValue.getWidth() * screenWidth / 100)).intValue();
            int height = Float.valueOf((limbikaViewItemValue.getHeight() * screenHeight / 100)).intValue();
            Log.d("Limbika-on-steroids", "Expected relWidth value " + width + " px  && height " + height + " px");

            int drawable = limbikaViewItemValue.getDrawable();

            Long key = limbikaViewItemValue.getKey();
            String userText = limbikaViewItemValue.getUserText();
            imagePath = limbikaViewItemValue.getImagePath();
            imageBitmap = imageProcessing.getImage(limbikaViewItemValue.getImagePath());
            type = limbikaViewItemValue.getType();


            posX = x;
            posY = y;

            Log.d("Limbika reg OnResume()", "x=" + x + " y= " + y);


            //  childInitY =  new Double(x).intValue();
            //childInitY = new Double(y).intValue();
            setX(posX);
            setY(posY);

            setRotation(rotation);
            setCircleColor(circleColor);

            if (userText != null) {
                setText(userText);
            }

            setTextColor(textColor);
            setTextSize(textSize);
            setBackgroundColor(backgroundColor);
            setCircleView(isCircleView == 1);
            dropTarget = limbikaViewItemValue.getDropTarget();


            //setItemSound(itemSound);      //added by reaz
            setFontType(itemFontType);// added by reaz
            //setOpenUrl(openURL);// added by reaz
            setLimbikaTextAlignment(fontTypeAlign);
            // added by reaz
            //setOpenApp(getOpenApp);
            //setCloseApp(getCloseApp);
            //setNavigateTo(getnavigateTo);
            setLimbikaAutoPlay(getAutoPlay);
            setLimbikaDelay(getSoundDelay);
//            setOuterBorderSize(getBorderPixel);

//            setLimbikaResult(result);
            if (borderColor != Color.TRANSPARENT) {
                outerBorderColor(borderColor, true, getBorderPixel);
            }


            if (roundedCorner == 1) {
                roundCorner(true);
            }

            if (image == -1)
                setImage(drawable);

            canvasHeight = height;
            canvasWidth = width;
            Log.d("Limbika-on-steroids", "Returned dimensions " + canvasWidth + "px canvasHeight=" + canvasHeight);

            isRestoring = true;
            // this is for negative coordinates where we loss left and top position
            /*if (x < 0) {
                sleft = posX-(colorballs.get(0).getWidthOfBall());
            }
            if (y < 0) {
                stop = posY-(colorballs.get(0).getHeightOfBall());
            }*/
            invalidate();

        } else {

            posX = 0;
            posY = 0;

            // setting the start point for the balls
            point1 = new Point();
            point1.x = 50;
            point1.y = 20;


            point2 = new Point();
            point2.x = 150;
            point2.y = 20;

            point3 = new Point();
            point3.x = 150;
            point3.y = 120;

            point4 = new Point();
            point4.x = 50;
            point4.y = 120;
        }

    }

    /**
     * saves current view state and position to database
     ***/
    public void saveViewState() {
        if (key != null) {
            //Modified by RAFI
            Rect myViewRect = new Rect();
            getGlobalVisibleRect(myViewRect);
            //getX and this x returns same with 1 pixel deviation
            float x = myViewRect.left;//100%
            float y = getY();//100%

            WindowManager w = wm;
            Display d = w.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
            // since SDK_INT = 1;
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
                try {
                    screenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                    screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    screenWidth = realSize.x;
                    screenHeight = realSize.y;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }


            //get height & width in points
            float scaleFactor = metrics.density;

            //calculate the amount of density independent pixels there are for a certain height or width.
            float widthDp = screenWidth / scaleFactor;
            float heightDp = screenHeight / scaleFactor;

            //to know the exact dimensions of a device in inches, you can work that out too, using the metrics method for how many pixels
            // there are per inch of the screen.
            float widthDpi = metrics.xdpi;
            float heightDpi = metrics.ydpi;

            //work out how many inches the device is.
            float widthInches = screenWidth / widthDpi;
            float heightInches = screenHeight / heightDpi;


            float widthPixels = widthInches * widthDpi;
            //covert inches to points for better accuracy.
            //1 point = 1/72 inches
            float widthPoints = widthInches / 72;
            float heighPoints = heightInches / 72;


            Log.d("Limbika-on-steroids", "x=" + screenWidth + " 100%  y= " + screenHeight + " widthPixels =" + widthPixels);

            //mir fix
            double relX = ((getX() / screenWidth) * 100);
            double relY = ((getY() / screenHeight) * 100);

            Log.d("Limbika-on-steroids", "X is at " + relX + "% == " + x + " px");
            Log.d("Limbika-on-steroids", "Y is at " + relY + "% == " + y + " px");

            double ex = relX / 100 * screenWidth;

            Log.d("Limbika-on-steroids", "Expected X value " + ex + " px");

            int left = getLeft();
            int right = canvasWidth + colorballs.get(0).getWidthOfBall();
            int top = getTop();
            int bottom = canvasHeight + colorballs.get(0).getHeightOfBall();


            float relLeft = (left * 100) / screenWidth;
            float relRight = (right * 100) / screenWidth;
            float relTop = (top * 100) / screenHeight;
            float relBottom = (bottom * 100) / screenHeight;


            //convert current view height & width to dp
            int canvasWidthDp = pxToDp(canvasWidth);//new Float(canvasWidth / scaleFactor).intValue();
            int canvasHeightDp = pxToDp(canvasHeight); //new Float(canvasHeight / scaleFactor).intValue();


            //pixel implementation
            double relWidth = ((canvasWidth * 100) / screenWidth) + deviation;
            double relHeight = ((canvasHeight * 100) / screenHeight) + deviation;

            //dp
            //int relWidth = new Float((canvasWidthDp * 100) / widthDp).intValue();
            //int relHeight =new Float ((canvasHeightDp * 100) / heightDp).intValue();

            Log.d("Limbika-on-steroids", "relWidth " + relWidth + " % & HEight = " + relHeight + "%");

            Log.d("Limbika-on-steroids", "saved dimensions " + canvasWidth + "px canvasHeight=" + canvasHeight);

            limbikaViewItemValue = new LimbikaViewItemValue();
            limbikaViewItemValue.setX((float) relX);
            limbikaViewItemValue.setY((float) relY);
            limbikaViewItemValue.setRotation(rotation);
            limbikaViewItemValue.setKey(key);
            limbikaViewItemValue.setIsCircleView(isCircleView ? 1 : 0);
            limbikaViewItemValue.setCircleColor(circleColor);
            limbikaViewItemValue.setUserText(text);
            limbikaViewItemValue.setTextColor(textColor);
            limbikaViewItemValue.setTextSize(textSize);
            limbikaViewItemValue.setBorderColor(outerBorderColor);
            limbikaViewItemValue.setBackgroundColor(this.backgroundColor != someShittyColorr ? this.backgroundColor : Color.TRANSPARENT);
            limbikaViewItemValue.setDrawable(image);
            limbikaViewItemValue.setWidth((float) relWidth);
            limbikaViewItemValue.setHeight((float) relHeight);
            limbikaViewItemValue.setLeft(relLeft);
            limbikaViewItemValue.setRight(relRight);
            limbikaViewItemValue.setTop(relTop);
            limbikaViewItemValue.setBottom(relBottom);
            limbikaViewItemValue.setImagePath(imagePath);
            limbikaViewItemValue.setType(type);
            limbikaViewItemValue.setLimbikaView(limbikaView);
            limbikaViewItemValue.setDropTarget(dropTarget);
            //limbikaViewItemValue.setBorderColor(outerBorderColor);
            //limbikaViewItemValue.setOuterBorderSize(outerBorderSize);
            limbikaViewItemValue.setCornerRound(round_corner_shape == true ? 1 : 0);
            //limbikaViewItemValue.setResult(result);
            //limbikaViewItemValue.setItemSound(itemSoundClip);
            // Toast.makeText(context, itemSoundClip, Toast.LENGTH_SHORT).show();
            limbikaViewItemValue.setFontTypeFace(fontType);
            //limbikaViewItemValue.setOpenURL(openUrl);
            limbikaViewItemValue.setFontAlign(textAlignment);
            // limbikaViewItemValue.setOpenApp(openApp);
            //limbikaViewItemValue.setCloseApp(closeApp);
            //limbikaViewItemValue.setNavigateTo(navigateTo);
            //limbikaViewItemValue.setAllowDragDrop(allowDragDrop);
            //limbikaViewItemValue.setDragDropTarget(dragDropTarget);
            limbikaViewItemValue.setAutoPlay(autoPlay);
            limbikaViewItemValue.setSoundDelay(soundDalay);
            limbikaViewItemValue.setBorderPixel(outerBorderSize);


            //drag and drop in play mode
            if (getLimbikaViewItemValue().getDropTarget() != null) {
                mergeWith(getLimbikaViewItemValue().getDropTarget(), 0, 0);
//                Toast.makeText(getContext(), "merge:notNull", Toast.LENGTH_SHORT).show();
            } else {

                //mir calculate if it was dropped on a target
                //**this part is done to cover the fact if the drop target is null for any view
                // suppose a view can be used for only double tap(no target is fixed ex: close app button) then this part is executed **//
                long keys = -1;
                for (Map.Entry<Long, LimbikaView> itemValue : dropTargetsMap.entrySet()) {
                    LimbikaView v = itemValue.getValue();
                    if (isViewOverlapping(v, LimbikaView.this))
                        keys = v.getLimbikaViewItemValue().getKey();

                }
                if (keys != -1)
                    dropTargetListener.onViewDroppedonWrongTarge(LimbikaView.this, keys);

                childInitX = posX;//new Float(posX).intValue();
                childInitY = posY;//new Float(posY).intValue();
                if (showBalls == HIDE_BALLS) {
                    animateTo(childInitX, childInitY);
                }
            }


            //drag finish listener
            if (dragFinishListner != null)
                dragFinishListner.onDragFinish(limbikaViewItemValue);
        }
    }

    public LimbikaViewItemValue getLimbikaViewItemValue() {
        return limbikaViewItemValue;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLimbikaTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void setImage(int res) {
        this.image = res;
    }

    public void setImage(Bitmap bitmap) {
        this.imageBitmap = bitmap;
        imagePath = imageProcessing.imageSave(bitmap);
    }

    public void setCircleView(boolean bol) {
        this.isCircleView = bol;
    }

    // added by reaz
    public void setItemSound(String itemSoundClip) {
        this.itemSoundClip = itemSoundClip;
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

    public Bitmap getBitmapImage() {
        Resources r = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(r, image);
        return bitmap;
    }

    public Bitmap getSavedBitmapImage() {

        Resources r = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(r, savedDrawable);
        return bitmap;
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

    public void showPositiveTag() {
        tag = POSITIVE_TAG;
        invalidate();//invoke onDraw
    }

    public void showNegativeTag() {
        tag = NEGATIVE_TAG;
        invalidate();//invoke onDraw
    }

    /**
     * hides tag
     **/
    public void hideTag() {
        this.tag = -1;
        invalidate();
    }

    public void showBalls() {
        this.showBalls = SHOW_BALLS;
        invalidate();
    }

    public void hideBalls() {
        this.showBalls = HIDE_BALLS;
        invalidate();
    }

    //drag and drop tick/cross
    public void drawTag(Canvas canvas) {
        int res = tag == POSITIVE_TAG ? R.drawable.tag_greentick : R.drawable.tag_redx;

        Resources r = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(r, res);
        bitmap = getResizedBitmap(bitmap, 128, 128);

        Bitmap circle = getCircleBitmap(bitmap);

        canvas.drawBitmap(circle, getWidth() / 2 - bitmap.getWidth() / 2,
                getHeight() / 2 - bitmap.getHeight() / 2, paint);
    }

    // Drawable d = context.getResources().getDrawable(res);
    public void drawImage(Canvas canvas, int res, Bitmap drawableBitmap, float xCenter, float yCenter) {


        Resources r = getResources();
        Bitmap bitmap = drawableBitmap != null ? drawableBitmap : BitmapFactory.decodeResource(r, res);
        mBitmap = bitmap;

        imageBitmap = bitmap;
        int newWidth, newHeight;
////
////
//        newWidth = canvasWidth-colorballs.get(0).getWidthOfBall();
//        newHeight = canvasHeight-colorballs.get(0).getHeightOfBall();


        newWidth = canvasWidth;
        newHeight = canvasHeight;

        bitmap = getResizedBitmap(bitmap, newWidth <= 1 ? MINIMUM_SIZE : newWidth, newHeight <= 1 ? MINIMUM_SIZE : newHeight);

        Bitmap circle = null;
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        if (isCircleView) {
            circle = getCircleBitmap(bitmap);
        } else if (!isCircleView && round_corner_shape) {
            bitmap = getRoundedCornerBitmap(bitmap, pixels_for_roundcorner);
        }

        canvas.drawBitmap(isCircleView ? circle : bitmap, xCenter, yCenter, paint);
        if (isCircleView)
            circle.recycle();
        else
            bitmap.recycle();
    }

    public Bitmap getSavedBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(blob, options);
    }

    private void saveBitmap(Bitmap bitmap) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/limbikasdk/";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, key + ".png");

        FileOutputStream fOut;
        try {

            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            blob = file_path + key + ".png";

            Log.d("Bitmapsave", "Bitmap  saved");
        } catch (Exception e) {
            Log.d("Bitmapsave", "Bitmap NOT saved " + e.toString());
            e.printStackTrace();
        }


    }

    public void setDoubleTapListener(DoubleTapListener doubleTapListener) {
        this.doubleTapListener = doubleTapListener;
    }

    public void setRotationListener(RotationListener rotationListener) {
        this.rotationListener = rotationListener;
    }

    public void setSingleTapListener(SingleTapListener singleTapListener) {
        this.singleTapListener = singleTapListener;
    }

    public void setLongTapListener(LongTapListener longTapListener) {
        this.longTapListener = longTapListener;
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public void setDragFinishListener(DragFinishListner dragFinishListener) {
        this.dragFinishListner = dragFinishListener;
    }

    public void setEraseListner(EraseListner eraseListner) {
        this.eraseListner = eraseListner;
    }

    public void setChildView(boolean childView) {
        isChildView = childView;
    }

    public void setDropTarget(LimbikaView dropTarget) {
        //dropTarget.sendViewToBack();
        this.dropTarget = dropTarget;
    }

    public void setDropTargetListener(DropTargetListener dropTargetListener) {
        this.dropTargetListener = dropTargetListener;
    }

    /**
     * Turn dragging on & off # defualt is on
     ***/
    public void setDraggable(boolean draggable) {
        isDraggable = draggable;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    /**
     * Merges view with parent view
     ***/
    public void mergeWith(LimbikaView parent, int x, int y) {

        //check if parent view is bigger than child view
        int childArea = this.getWidth() * this.getHeight();
        int parentArea = parent.getWidth() * parent.getHeight();
        //   if(parentArea>=childArea)


        //merging thes views
        LimbikaViewItemValue parentItemValue = parent.getLimbikaViewItemValue();
        float parentX;
        float parentY;

        if (parentItemValue == null) {
            parentX = 0;
            parentY = 0;
        } else {

            //parents position
            parentX = parentItemValue.getX();
            parentY = parentItemValue.getY();
        }
        //child position
        int childX = new Float(getX() + .5).intValue();
        int childY = new Float(getY() + .5).intValue();


        //  Rect parentRect = new Rect(parentItemValue.getLeft(), parentItemValue.getTop(), parentItemValue.getRight(), parentItemValue.getBottom());
        Rect childRect = new Rect(getLeft(), getTop(), getRight(), getBottom());

        //check if child view is inside parent view
        //  if(childRect.left>parentRect.right || childRect.right<parentRect.left || childRect.bottom>parentRect.top || childRect.top<parentRect)

        boolean isDragged = false;

        //to hold the key of drop target other than its own
        long key = -1;
        //if it overlapped with
        if (isViewOverlapping(parent, LimbikaView.this)) {

            if (dropTargetListener != null)
                isDragged = true;
            alreadyDropped = true;
            //Toast.makeText(getContext(), "(Droppabble child key:" + key + ",dropTarget key:" + dropTarget.key + ")", Toast.LENGTH_LONG).show();

            // change by sumon
            //  always restore view position during drag and drop
            childInitX = getX();//new Float(posX).intValue();
            childInitY = getY();//new Float(posY).intValue();

            if (showBalls == HIDE_BALLS)
                animateTo(childInitX, childInitY);
            isDraggable = false;
        } else {
            alreadyDropped = false;
            //check the overlapping in every drop target
            for (Map.Entry<Long, LimbikaView> itemValue : dropTargetsMap.entrySet()) {
                LimbikaView v = itemValue.getValue();
                if (isViewOverlapping(v, LimbikaView.this))
                    key = v.getLimbikaViewItemValue().getKey();
            }
            //change by sumon
            //  always restore view position during drag and drop
            childInitX = posX;//new Float(posX).intValue();
            childInitY = posY;//new Float(posY).intValue();

            if (showBalls == HIDE_BALLS)
                animateTo(childInitX, childInitY);
        }


        if (isDragged) {
            dropTargetListener.onViewDropped(LimbikaView.this, dropTarget.key);
        } else {
            dropTargetListener.onViewDroppedonWrongTarge(LimbikaView.this, key);
        }
        //child view not within parent
//          Toast.makeText(getContext(), "Error: Child view not within parent bounds animateTo(" + childInitX + "," + childInitY + ")", Toast.LENGTH_LONG).show();

        // }
    }

    private boolean isViewOverlapping(View firstView, View secondView) {

        final int[] location = new int[2];
        //mir fix to avoiad overlapping issue of drop target and dropaable
        firstView.getLocationInWindow(location);
        Rect rect1 = new Rect(location[0] + colorballs.get(0).getWidthOfBall(), location[1] + colorballs.get(0).getHeightOfBall(),
                (location[0] + colorballs.get(0).getWidthOfBall()) + (firstView.getWidth() - colorballs.get(0).getWidthOfBall()),
                location[1] + colorballs.get(0).getHeightOfBall() + firstView.getHeight() - colorballs.get(0).getHeightOfBall());

        secondView.getLocationInWindow(location);
        Rect rect2 = new Rect(location[0] + colorballs.get(0).getWidthOfBall(), location[1] + colorballs.get(0).getHeightOfBall(),
                (location[0] + colorballs.get(0).getWidthOfBall()) + (secondView.getWidth() - colorballs.get(0).getWidthOfBall()),
                location[1] + colorballs.get(0).getHeightOfBall() + secondView.getHeight() - colorballs.get(0).getHeightOfBall());

        return rect1.intersect(rect2);
    }

    public void drag(MotionEvent event, View view) {
        int X = (int) event.getX();
        int Y = (int) event.getY();


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on

                //     if (singleTapListener != null) {
                //    singleTapListener.onSingleTap(this, limbikaViewItemValue);
                //   }
                clickCount++;

                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                startMovePoint = new Point(X, Y);

                //change balls to selected state
                for (ColorBall ball : colorballs) {
                    if (ball.getID() == 2) {
                        ball.setBitmap(R.drawable.icon_resize);
                        ball.setX(isRestoring ? (int) (sright - colorballs.get(0).getWidthOfBall()) : (int) gRight);
                        ball.setY(isRestoring ? (int) (sbottom - colorballs.get(0).getHeightOfBall()) : (int) gBottom);

                    } else if (ball.getID() == 3) {

                        ball.setBitmap(R.drawable.icon_delete);
                        ball.setX(isRestoring ? (int) (sright - colorballs.get(0).getWidthOfBall()) : (int) gRight);
                        ball.setY((int) gTop);

                    } else if (ball.getID() == 0)
                        ball.setBitmap(R.drawable.ic_rotate);
                    else {
                        ball.setBitmap(R.drawable.ic_circle_default);
                        ball.setX((int) gLeft);
                        ball.setY(isRestoring ? (int) (sbottom - colorballs.get(0).getHeightOfBall()) : (int) gBottom);
                    }
                }

                // a ball
                if (points[0] == null) {
                    //initialize rectangle.
                    points[0] = new Point();
                    points[0].x = X;
                    points[0].y = Y;

                    points[1] = new Point();
                    points[1].x = X;
                    points[1].y = Y + canvasHeight;

                    points[2] = new Point();
                    points[2].x = (int) sright - colorballs.get(0).getWidthOfBall();
                    points[2].y = (int) sbottom - colorballs.get(0).getHeightOfBall();

                    points[3] = new Point();
                    points[3].x = X + canvasWidth;
                    points[3].y = Y;

                    balID = 3;
                    groupId = 2;
                    // declare each ball with the ColorBall class
                    for (Point pt : points) {
                        colorballs.add(new ColorBall(getContext(), R.drawable.ic_circle, pt));
                    }
                } else {


                    //resize rectangle
                    balID = -1;
                    groupId = -1;
                    for (int i = colorballs.size() - 1; i >= 0; i--) {
                        ColorBall ball = colorballs.get(i);
                        // check if inside the bounds of the ball (circle)
                        // get the center for the ball
                        int centerX = ball.getX() + ball.getWidthOfBall();
                        int centerY = ball.getY() + ball.getHeightOfBall();
                        paint.setColor(Color.CYAN);
                        // calculate the radius from the touch to the center of the
                        // ball
                        double radCircle = Math
                                .sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y)
                                        * (centerY - Y)));

                        if (radCircle < ball.getWidthOfBall()) {

                            balID = ball.getID();
                            if (balID == 1 || balID == 3) {
                                groupId = 2;
                            } else {
                                groupId = 1;
                            }


                            invalidate();
                            break;
                        }
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball
                //todo enable drag
                if (playmode) {
                    isRestoring = true;
                } else
                    isRestoring = false;
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                width = size.x;
                height = size.y;

                final int MINIMUM_SIZE = 80; //change by sumon


                if (isEnabled()) // only when view is enabled & balls are shown


//                    if (X > MINIMUM_SIZE && Y > MINIMUM_SIZE) // don't do anything is expanding will go to negative side

                    if (balID > -1 && showBalls != HIDE_BALLS) {


                        if (balID == 2) {
                            /// bringToFront();
                            //
                            colorballs.get(balID).setX(X);
                            colorballs.get(balID).setY(Y);

                        }
                        // paint.setColor(Color.GREEN);
                        Rect rect = new Rect();

                        if (groupId == 1) {
                            colorballs.get(1).setX(colorballs.get(0).getX());
                            colorballs.get(1).setY(colorballs.get(2).getY());
                            colorballs.get(3).setX(colorballs.get(2).getX());
                            colorballs.get(3).setY(colorballs.get(0).getY());
                            //   rect.set(colorballs.get(0).getX(), colorballs.get(2).getY(), colorballs.get(2).getX(), colorballs.get(0).getY());

                            Log.d("LimbikaView", "GROUP 1");
                        } else {

                            colorballs.get(0).setX(colorballs.get(1).getX());
                            colorballs.get(0).setY(colorballs.get(3).getY());
                            colorballs.get(2).setX(colorballs.get(3).getX());
                            colorballs.get(2).setY(colorballs.get(1).getY());
                            Log.d("LimbikaView", "GROUP 2");

                        }

                        rect.set(colorballs.get(1).getX(), colorballs.get(3).getY(), colorballs.get(3).getX(), colorballs.get(1).getY());

                        canvasWidth = rect.width();
                        canvasHeight = rect.height();

                        //  double diagonal = Math.sqrt((canvasHeight*canvasHeight + canvasWidth*canvasWidth));


                        xCenter = rect.centerX() - rect.width() / 2;
                        yCenter = rect.centerX() - rect.height() / 2;

                        getLayoutParams().height = canvasHeight + colorballs.get(0).getHeightOfBall();
                        getLayoutParams().width = canvasWidth + colorballs.get(0).getWidthOfBall();

                        if (canvasWidth < MINIMUM_SIZE || canvasHeight < MINIMUM_SIZE) {

                            //disable view
                            setVisibility(View.GONE);
                            eraseListner.onErase(limbikaViewItemValue);
                        }
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        invalidate();

                    } else {

                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        shouldRotate = true;

                        if (dragListener != null)
                            dragListener.onDrag(event.getRawX() + dX, event.getRawY() + dY);


                        //dragging
                        //only drag child views
                        if (isDraggable) {//this must be a child view
                            setX(event.getRawX() + dX);
                            setY(event.getRawY() + dY);
                        }


                        if (false/*startMovePoint!=null*/) { //dont move bitch!
                            paint.setColor(Color.CYAN);
                            int diffX = X - startMovePoint.x;
                            int diffY = Y - startMovePoint.y;


                            startMovePoint.x = X;
                            startMovePoint.y = Y;
                            colorballs.get(0).addX(diffX);
                            colorballs.get(1).addX(diffX);
                            colorballs.get(2).addX(diffX);
                            colorballs.get(3).addX(diffX);
                            colorballs.get(0).addY(diffY);
                            colorballs.get(1).addY(diffY);
                            colorballs.get(2).addY(diffY);
                            colorballs.get(3).addY(diffY);
                            if (groupId == 1) {
                                canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                        paint);
                            } else {
                                canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                        paint);
                            }
                            invalidate();
                        }
                    }


                break;

            case MotionEvent.ACTION_UP:
                //rotate button clicked

                if (isEnabled() && showBalls != HIDE_BALLS) // only when controls are visible
                    if (balID == 0) {
                        if (rotation == 360/*max rotation*/)
                            rotation = -45;//

                        //   if (shouldRotate) {
                        rotation += 45;//rotate by 45 degrees each time

                        ///rotat animation by sumon start from here///
                        if (rotation >= 360) {

                            rotation = -45;
                        } else {
                            view.animate()
                                    .rotation(rotation)
                                    .setDuration(400)
                                    .start();
                        }
                        ///rotat animation by sumon Finish  here///
                        setRotation(rotation);
                        // }

                        if (rotationListener != null) {
                            rotationListener.onRotate(rotation);

                        }
                    }


                if (isEnabled() && showBalls != HIDE_BALLS)//only when view is enabled & controls are visible
                    //cancel button clicked
                    if (balID == 3) {
                        //disable view
                        setVisibility(View.GONE);

                        if (eraseListner != null)
                            eraseListner.onErase(limbikaViewItemValue);

                    } else if (balID == 3) {
                        //disable view
                        setVisibility(View.GONE);

                        if (eraseListner != null)
                            eraseListner.onErase(limbikaViewItemValue);

                    }

                saveViewState();
                // touch drop - just do things here after dropping
                requestLayout();


                //change balls to un-selected state
                if (isEnabled()) // only when view is enabled

                    for (ColorBall ball : colorballs) {

                        if (ball.getID() == 2)
                            ball.setBitmap(R.drawable.icon_resize);
                        else if (ball.getID() == 3)
                            ball.setBitmap(R.drawable.icon_delete);
                        else if (ball.getID() == 0)
                            ball.setBitmap(R.drawable.ic_rotate);
                        else
                            ball.setBitmap(R.drawable.ic_circle_default);
                    }


                if (dropTarget != null || getLimbikaViewItemValue().getDropTarget() != null || isChildView) {//must be child view animate back too riginal position


                    if (dropTargetListener != null)
                        if (!alreadyDropped)
                            dropTargetListener.onViewDropped(LimbikaView.this, -1l);

                    if (showBalls == HIDE_BALLS)
                        animateTo(childInitX, childInitY);

                }
                break;

            default:
                break;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private Paint getPaintFormat() {
        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShadowLayer(0, 0, 0, 0);
        mPaint.setStrokeWidth(18);
        return mPaint;
    }

    public void animateTo(final float amountToMoveRight, final float amountToMoveDown) {
        if (amountToMoveDown > -1)
            setY(amountToMoveDown);
        if (amountToMoveRight > -1)
            setX(amountToMoveRight);
    }

    //sends limbika view to the back
    public void sendViewToBack() {
        View child = LimbikaView.this;
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    private void init(Context context) {

        this.context = context;

        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        helper = new Database(getContext());
        db = helper.getWritableDatabase();

        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        gestureDetector = new GestureDetector(context, new GestureListener());


        mPath = new Path();
        mBitmap = Bitmap.createBitmap(canvasWidth,
                canvasHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);


        //position of the rectangle
        int X = (int) posX;
        int Y = (int) posY;

        //initialize rectangle.
        points[0] = new Point();
        points[0].x = X;
        points[0].y = Y;

        points[1] = new Point();
        points[1].x = X;
        points[1].y = Y + canvasHeight;

        points[2] = new Point();
        points[2].x = X + canvasWidth;
        points[2].y = Y + canvasHeight;

        points[3] = new Point();
        points[3].x = X + canvasWidth;
        points[3].y = Y;

        balID = 2;
        groupId = 1;

        //changed by sumon // 8/17/2016  start from here
        // declare each ball with the ColorBall class
//        for (Point pt : points) {
//            colorballs.add(new ColorBall(getContext(), R.drawable.ic_circle_default, pt));
//        }
        colorballs.add(new ColorBall(getContext(), R.drawable.ic_rotate, points[0]));
        colorballs.add(new ColorBall(getContext(), R.drawable.ic_circle_default, points[1]));
        colorballs.add(new ColorBall(getContext(), R.drawable.icon_resize, points[2]));
        colorballs.add(new ColorBall(getContext(), R.drawable.icon_delete, points[3]));
        //changed by sumon // 8/17/2016  finish  here

        //intializing paints
        paintfullBackground = new Paint();
        paintfullBackground.setAntiAlias(true);
        paintfullBackground.setColor(Color.TRANSPARENT);
        paintfullBackground.setStyle(Paint.Style.FILL);
        paintfullBackground.setAntiAlias(true);
        paintfullBackground.setDither(true);

        border_paint = new Paint();
        border_paint.setAntiAlias(true);
        border_paint.setColor(Color.TRANSPARENT);//changed by sumon
//        border_paint.setColor(Color.BLACK);
        border_paint.setStyle(Paint.Style.STROKE);
        border_paint.setAntiAlias(true);
        border_paint.setDither(true);
        mostOuterRectPaint = new Paint();
        mostOuterRectPaint.setStyle(Paint.Style.STROKE);
        mostOuterRectPaint.setAntiAlias(true);


    }

    public void changeSelection(boolean state) {
        if (rectangleBorder_paint == null) {
            rectangleBorder_paint = new Paint();

        }
        if (state) {
            borderColor = Color.parseColor("#ff37abc8");
            limbikaView.setEnabled(true);
        } else {
            borderColor = Color.TRANSPARENT;
        }

        rectangleBorder_paint.setColor(borderColor);
        rectangleBorder_paint.setStrokeWidth(10);
        invalidate();

    }

    public void changeSelection(boolean state, int selectedColor) {
        if (rectangleBorder_paint == null)
            rectangleBorder_paint = new Paint();

        rectangleBorder_paint.setColor(state ? selectedColor : Color.parseColor("#000555"));
        invalidate();
    }

    //use this to change the rectanguler color to anything
    public void changeBorderColor(int color) {
        if (rectangleBorder_paint == null)
            rectangleBorder_paint = new Paint();

        saveViewState();
        rectangleBorder_paint.setColor(color);
    }

    // the method that draws the balls
    // @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        getLayoutParams().width = canvasWidth + colorballs.get(0).getWidthOfBall();
        getLayoutParams().height = canvasHeight + colorballs.get(0).getHeightOfBall();


        requestLayout();


        float left, top, right, bottom;
        left = sleft;
        top = stop;


        right = getWidth() - colorballs.get(0).getWidthOfBall();
        bottom = getHeight() - colorballs.get(0).getHeightOfBall();


        gRight = right;
        gBottom = bottom;


        for (int i = 1; i < points.length; i++) {
            left = left > points[i].x ? points[i].x : left;
            top = top > points[i].y ? points[i].y : top;
            right = right < points[i].x ? points[i].x : right;
            bottom = bottom < points[i].y ? points[i].y : bottom;
        }
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5);

        //draw stroke
        //change by mir
        rectangleBorder_paint.setStyle(Paint.Style.STROKE);
        if (borderColor == Color.TRANSPARENT)
            rectangleBorder_paint.setColor(Color.TRANSPARENT);
        else
            rectangleBorder_paint.setColor(borderColor);


        paint.setStrokeWidth(2);
        //this is the outer rectangle with fill paint
        //modify by sumon // 10/20/2016
        if (round_corner_shape && !isCircleView)
            canvas.drawRoundRect(left + colorballs.get(0).getWidthOfBall() / 2, top + colorballs.get(0).getHeightOfBall() / 2, canvas.getWidth() - colorballs.get(0).getWidthOfBall() / 2, canvas.getHeight() - colorballs.get(0).getHeightOfBall() / 2, pixels_for_roundcorner, pixels_for_roundcorner, paintfullBackground);
        else {
            if (backgroundColor != someShittyColorr && !isCircleView) {
                paintfullBackground.setColor(backgroundColor);
                canvas.drawRect(left + colorballs.get(0).getWidthOfBall() / 2, top + colorballs.get(0).getHeightOfBall() / 2, canvas.getWidth() - colorballs.get(0).getWidthOfBall() / 2, canvas.getHeight() - colorballs.get(0).getHeightOfBall() / 2, paintfullBackground);
            } else if (backgroundColor != someShittyColorr && isCircleView) {
                paintfullBackground.setColor(Color.TRANSPARENT);
                circleColor = backgroundColor;
            }
        }

// checking if ismostouterrect is enable
        if (isMostOuterRect) {
            canvas.drawRect(left + colorballs.get(0).getWidthOfBall() / 2, top + colorballs.get(0).getHeightOfBall() / 2, canvas.getWidth() - colorballs.get(0).getWidthOfBall() / 2, canvas.getHeight() - colorballs.get(0).getHeightOfBall() / 2, mostOuterRectPaint);

        }
        //if there is border then draw it areond outer rect
        if (borderVisible) {
            if (round_corner_shape) {
                /// new one by sumon
                canvas.drawRoundRect(left + colorballs.get(0).getWidthOfBall() / 2, top + colorballs.get(0).getHeightOfBall() / 2, canvas.getWidth() - colorballs.get(0).getWidthOfBall() / 2, canvas.getHeight() - colorballs.get(0).getHeightOfBall() / 2,
                        pixels_for_roundcorner, pixels_for_roundcorner, border_paint);
            } else {
                canvas.drawRect(left + colorballs.get(0).getWidthOfBall() / 2, top + colorballs.get(0).getHeightOfBall() / 2, canvas.getWidth() - colorballs.get(0).getWidthOfBall() / 2, canvas.getHeight() - colorballs.get(0).getHeightOfBall() / 2, border_paint);
            }
        }

        //draw red rect only when view is enabled
        if (isBorderVisible)// only show border when user has enabled it
            if (isEnabled())
                if (bottom > MINIMUM_SIZE && right > MINIMUM_SIZE)

                    if (isRestoring) {

                        if (playmode)
                            rectangleBorder_paint.setColor(Color.TRANSPARENT);
                        // change by sumon
                        canvas.drawRect(
                                sleft + colorballs.get(0).getWidthOfBall() / 1.5f,
                                stop + colorballs.get(0).getHeightOfBall() / 1.5f,
                                gRight + colorballs.get(0).getWidthOfBall() / 3,
                                gBottom + colorballs.get(0).getHeightOfBall() / 3, rectangleBorder_paint);

                    } else
                        canvas.drawRect(
                                left + colorballs.get(0).getWidthOfBall() / 1.5f,
                                top + colorballs.get(0).getWidthOfBall() / 1.5f,
                                right + colorballs.get(0).getWidthOfBall() / 3,
                                bottom + colorballs.get(0).getWidthOfBall() / 3, rectangleBorder_paint);


        //fill the rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT/*parseColor("#55DB1255")*/);
        paint.setStrokeWidth(0);

        if (bottom > MINIMUM_SIZE && right > MINIMUM_SIZE)

            canvas.drawRect(
                    left + colorballs.get(0).getWidthOfBall() / 2,
                    top + colorballs.get(0).getWidthOfBall() / 2,
                    right + colorballs.get(2).getWidthOfBall() / 2,
                    bottom + colorballs.get(2).getWidthOfBall() / 2, paint);

        //new val change by sumon
        paint.setStyle(Paint.Style.FILL);

        if (circleColor != someShittyColorr && circleColor != 0)
            paint.setColor(circleColor);
        else {
            //new val change by sumon
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
        }

        //new val change by sumon
        paint.setStrokeWidth(2);


        float xCenter = ((left + colorballs.get(0).getWidthOfBall() / 2) + (right + colorballs.get(2).getWidthOfBall() / 2)) / 2;

        float yCenter = ((top + colorballs.get(0).getWidthOfBall()) / 2 + (bottom + colorballs.get(2).getWidthOfBall() / 2)) / 2;

        //for restoring text state
        float sxCenter = (sleft + sright) / 2;
        float syCenter = (stop + sbottom) / 2;


        //modify by sumon
        //draw circle on restoring and editor mode
        if (bottom > MINIMUM_SIZE && right > MINIMUM_SIZE) {
            if (isCircleView && image == -1 && imageBitmap == null)
                if (isRestoring) {
                    if (canvasHeight > canvasWidth) {
                        canvas.drawCircle(sxCenter, syCenter, (canvasWidth / 2) - canvasWidth / 20, paint);

                    } else {
                        canvas.drawCircle(sxCenter, syCenter, (canvasHeight / 2) - canvasHeight / 20, paint);
                    }

                } else {
                    if (canvasHeight > canvasWidth) {
                        canvas.drawCircle(xCenter, yCenter, (canvasWidth / 2) - canvasWidth / 20, paint);
                    } else {
                        canvas.drawCircle(xCenter, yCenter, (canvasHeight / 2) - canvasHeight / 20, paint);
                    }
                }
        }
        //draw the corners
        BitmapDrawable bitmap = new BitmapDrawable();
        // draw the balls on the canvas
        paint.setColor(Color.BLUE);

        paint.setTextSize(18);
        paint.setStrokeWidth(0);

        if (bottom > MINIMUM_SIZE && right > MINIMUM_SIZE)
            if (image != -1 || imageBitmap != null)
                if (isRestoring)
                    // change by sumon
                    drawImage(canvas, image, imageBitmap, sleft < left ? sleft : left + colorballs.get(0).getWidthOfBall() / 2, stop < top ? stop : top + colorballs.get(0).getHeightOfBall() / 2);
                else
                    // change by sumon
                    drawImage(canvas, image, imageBitmap, sleft < left ? sleft : left + colorballs.get(0).getWidthOfBall() / 2, stop < top ? stop : top + colorballs.get(0).getHeightOfBall() / 2);

        //draw balls only when view is enabled
        if (bottom > MINIMUM_SIZE && right > MINIMUM_SIZE)

            if (isEnabled() && showBalls != HIDE_BALLS)

                for (int i = 0; i < colorballs.size(); i++) {
                    ColorBall ball = colorballs.get(i);


                    //  draw balls onn correct position
                    if (i == 1)
                        canvas.drawBitmap(ball.getBitmap(), left, isRestoring ? sbottom - colorballs.get(0).getHeightOfBall() : bottom,
                                paint);
                    else if (i == 3)
                        canvas.drawBitmap(ball.getBitmap(), isRestoring ? sright - colorballs.get(0).getWidthOfBall() : right, top,
                                paint);
                    else if (i == 2)
                        canvas.drawBitmap(ball.getBitmap(), isRestoring ? sright - colorballs.get(0).getWidthOfBall() : right, isRestoring ? sbottom - colorballs.get(0).getHeightOfBall() : bottom,
                                paint);
                    else
                        canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                                paint);


                    if (ball.getID() == 0)
                        canvas.drawText("", ball.getX(), ball.getY(), paint);
                    //   else
                    //  canvas.drawText("" + (i + 1), ball.getX(), ball.getY(), paint);

                }

//  we dont want to show tags for now
        //draw tags
//        if (tag != -1)
//            drawTag(canvas);


        //************* THIS PORTION IS DRAWING THE TEXT ***//////////////////////////
        if (bottom > MINIMUM_SIZE && right > MINIMUM_SIZE)

            if (!TextUtils.isEmpty(text))
                if (isCircleView)
                    drawDigit(canvas, canvasHeight > canvasWidth ? canvasWidth / 16 : canvasHeight / 16
                            , isRestoring ? sxCenter : xCenter, isRestoring ? syCenter : yCenter, Color.BLACK, text);
                else
                    drawDigit(canvas, canvasHeight > canvasWidth ? canvasWidth / 16 : canvasHeight / 16
                            , isRestoring ? sxCenter : xCenter, isRestoring ? syCenter : yCenter, Color.BLACK, text);
        //todo assistive touch
        gRight = right;
        gLeft = left;
        gTop = top;
        gBottom = bottom;


    }

    private void drawDigit(Canvas canvas, int textSize, float cX, float cY, int color, String text) {

        float realTextSize;
        TextPaint tempTextPaint = new TextPaint();
        tempTextPaint.setAntiAlias(true);
        tempTextPaint.setStyle(Paint.Style.FILL);
        if (typefaceCustom != null)
            tempTextPaint.setTypeface(typefaceCustom);

        if (textColor != -15066598)
            tempTextPaint.setColor(textColor);
        else
            tempTextPaint.setColor(color);

        if (this.textSize != -1) {
            /// use to convert int to px
            // by default textpaint take textsize in pixel. so we have to convert it to pixel.
            realTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.textSize, getResources().getDisplayMetrics());
            tempTextPaint.setTextSize(realTextSize);
        } else {
            realTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 18, getResources().getDisplayMetrics());

            tempTextPaint.setTextSize(realTextSize);

        }
        tempTextPaint.setTextAlign(Paint.Align.LEFT);


        int padding = colorballs.get(0).getWidthOfBall();
        StaticLayout sl;
        if (canvasWidth > canvasHeight) {
            int width = (canvasWidth >
                    canvasHeight ? ((canvasWidth) < 0 ? 1 : (canvasWidth)) : ((canvasHeight) < 0 ? 1 : (canvasHeight)));
            sl = new StaticLayout(text, tempTextPaint, isCircleView ? (width / 2) : width,
                    textAlignment == ALIGN_CENTER ? Layout.Alignment.ALIGN_CENTER : textAlignment == ALIGN_LEFT ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE, 1, 1, true);
        } else {
            int w = (canvasHeight >
                    canvasWidth ? ((canvasWidth) < 0 ? 1 : (canvasWidth)) : ((canvasHeight) < 0 ? 1 : (canvasHeight)));
            sl = new StaticLayout(text, tempTextPaint, isCircleView ? (w / 2) : w,
                    textAlignment == ALIGN_CENTER ? Layout.Alignment.ALIGN_CENTER : textAlignment == ALIGN_LEFT ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE, 1, 1, true);
        }


        //draw circle within circle bounds
        int diameter = canvasHeight > canvasWidth ? canvasWidth : canvasHeight;
        int radius = diameter / 2;
        int hyp = (int) Math.sqrt(radius * radius + radius * radius);
        //rect inside circle
        Rect circleBounds = new Rect(
                (int) cX - hyp / 2,
                (int) cY - hyp / 2,
                (int) cX + hyp / 2,
                (int) cY + hyp / 2);
        if (isCircleView) {

            sl = new StaticLayout(text, tempTextPaint, circleBounds.width(),
                    textAlignment == ALIGN_CENTER ? Layout.Alignment.ALIGN_CENTER : textAlignment == ALIGN_LEFT ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE, 1, 1, true);


        }


        canvas.save();


        //calculate X and Y coordinates - In this case we want to draw the text in the
        //center of canvas so we calculate
        //text height and number of lines to move Y coordinate to center.

        //***********CALCUALTIONS********************///
        float textHeight = sl.getHeight();//getTextHeight(text, tempTextPaint);

        int numberOfTextLines = sl.getLineCount();

        float alignCenter = 0;
        if (numberOfTextLines > 1)
            alignCenter = cY - (textHeight / 2); //+ (colorballs.get(0).getHeightOfBall() / 2));
        else {
            double tempHeight = (float) textHeight / 1.8;
            float h = (float) tempHeight;
            alignCenter = cY - h;

        }

        int alignTop = colorballs.get(0).getHeightOfBall() / 2;
        float alignBottom = gBottom - alignTop * 2 - ((numberOfTextLines * textHeight) / 2);

        //text will be drawn from left
        float textXCoordinate = cX - 30;
        if (isCircleView)
            //canvas.translate(circleBounds.left, circleBounds.top);
            canvas.translate(circleBounds.left, ((cY + cY) / 2) - (sl.getHeight() / 2));

        else
            canvas.translate(cX - canvasWidth / 2, textType == TEXT_TOP ? alignTop : textType == TEXT_CENTER ? alignCenter : alignBottom);


        int lastLineYposition = sl.getLineBottom(sl.getLineCount() - 1);
        if (isCircleView)
            Log.d("Circle-Text", "final line is at Y=" + lastLineYposition + " bounds Bottom=" + circleBounds.bottom + " Bottom-padding=" + (circleBounds.bottom - padding));


        sl.draw(canvas);
        canvas.restore();


    }

    /**
     * @return text height
     */
    private float getTextHeight(String text, Paint paint) {

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drag(event, LimbikaView.this);
        gestureDetector.onTouchEvent(event);

        if (isEnabled())
            gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setLimbikaView(LimbikaView limbikaView) {
        this.limbikaView = limbikaView;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    //0=width  1=height
    public int getMesearement_of_screen(int choice) {
        WindowManager w = wm;
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

        if (choice == 1)
            return screenHeight;

        return screenWidth;
    }

    public void roundCorner(boolean shape) {
        if (paintfullBackground == null)
            paintfullBackground = new Paint();

        paintfullBackground.setAntiAlias(true);
        if (backgroundColor == someShittyColorr)
            paintfullBackground.setColor(Color.TRANSPARENT);
        else paintfullBackground.setColor(backgroundColor);

        paintfullBackground.setStyle(Paint.Style.FILL);

        round_corner_shape = shape;
        //pixels_for_roundcorner = px;
        saveViewState();

        invalidate();
    }

    public void outerBorderColor(int color, boolean visiblility, int borderPX) {
        if (border_paint == null)
            border_paint = new Paint();

        //outerBorderSize = px;
        outerBorderColor = color;
        border_paint.setColor(color);
        border_paint.setStyle(Paint.Style.STROKE);
        if (borderPX == 0) {
            outerBorderSize = 20;
        } else {
            outerBorderSize = borderPX;
        }
        border_paint.setStrokeWidth(outerBorderSize / 3);

        borderVisible = visiblility;
        //pixels_for_roundcorner_border = outerBorderSize;
        saveViewState();
        invalidate();
    }

    //////////////************ Center x, y Alignment ********** *********
    public void alignIt_withMeCenterY(LimbikaView lm) {

        float current_y = getY() + canvasHeight / 2;
        float Y_2nd = current_y - lm.canvasHeight / 2;
        lm.setY(Y_2nd);
        lm.invalidate();
    }

    public void alignIt_withMeCenterX(LimbikaView lm) {

        float current_x = getX() + canvasWidth / 2;
        float X_2nd = current_x - lm.canvasWidth / 2;
        lm.setX(X_2nd);
        lm.invalidate();
    }

    ///both of the views center x,y will be the same /////////
    public void alignIt_fullCenter(LimbikaView lm) {
        float current_y = getY() + canvasHeight / 2;
        float Y_2nd = current_y - lm.canvasHeight / 2;
        lm.setY(Y_2nd);
        float current_x = getX() + canvasWidth / 2;
        float X_2nd = current_x - lm.canvasWidth / 2;
        lm.setX(X_2nd);
        lm.invalidate();
    }

    //////////// Horizontal Alignments***********/////////////////
    public void alignIt_withMeRightEnd_X(LimbikaView lm) {
        float x = (getX() + canvasWidth) - (lm.canvasWidth);
        lm.setX(x);
        lm.invalidate();

    }

    public void alignIt_withMeLeftEnd_X(LimbikaView lm) {
        float x = (getX());
        lm.setX(x);
        lm.invalidate();
    }

    //////////// Vertical Alignments***********/////////////////
    public void alignIt_withMeBottom_Y(LimbikaView lm) {
        float y = (getY() + canvasHeight) - (lm.canvasHeight);
        lm.setY(y);
        lm.invalidate();

    }

    public void alignIt_withMeTop_Y(LimbikaView lm) {
        float y = getY();
        lm.setY(y);
        lm.invalidate();
    }

    //method created by sumon //  8/17/2016
    public void hideRectSelectedLine(boolean state) {
        if (rectangleBorder_paint == null)
            rectangleBorder_paint = new Paint();

        if (state) {
            borderColor = Color.TRANSPARENT;
            rectangleBorder_paint.setColor(borderColor);
            rectangleBorder_paint.setStrokeWidth(10);
            // hideBalls();
            limbikaView.setEnabled(false);
        }

        invalidate();

    }

    //new Functions for aligning in center of screen
    public void alignIt_centerscreenX() {
        float width = getMesearement_of_screen(0);
        float center_x = width / 2;
        float current_x = center_x - (canvasWidth + colorballs.get(0).getWidthOfBall()) / 2;
        setX(current_x);
        invalidate();
        /*Toast.makeText(context, String.valueOf(width)+" center:"+String.valueOf(width)+
                " current:"+String.valueOf(current_x), Toast.LENGTH_SHORT).show();*/
    }

    public void alignIt_centerscreenY() {
        float height = getMesearement_of_screen(1);
        float center_y = height / 2;
        float current_y = center_y - (canvasHeight + colorballs.get(0).getHeightOfBall()) / 2;
        setY(current_y);
        invalidate();

        //Toast.makeText(context, String.valueOf(height), Toast.LENGTH_SHORT).show();
    }

    public void alignIt_LeftOfScreen() {

        float new_x = 0 - colorballs.get(0).getWidthOfBall() / 2;

        setX(new_x);
        invalidate();
    }

    public void alignIt_RightOfScreen() {

        float new_x = getMesearement_of_screen(0) - (canvasWidth + colorballs.get(0).getWidthOfBall() / 2);

        setX(new_x);
        invalidate();
    }

    public void alignIt_topOfScreen() {

        float new_y = 0 - colorballs.get(0).getWidthOfBall() / 2;
        setY(new_y);
        invalidate();
    }

    public void alignIt_bottomOfScreen() {

        float height = getMesearement_of_screen(1);

        float new_y = height - (canvasHeight + colorballs.get(0).getHeightOfBall() / 2);

        setY(new_y);
        invalidate();
    }

    public void alignItCenterOfScreen() {
        float width = getMesearement_of_screen(0);
        float center_x = width / 2;
        float current_x = center_x - (canvasWidth + colorballs.get(0).getWidthOfBall()) / 2;
        setX(current_x);
        float height = getMesearement_of_screen(1);
        float center_y = height / 2;
        float current_y = center_y - (canvasHeight + colorballs.get(0).getHeightOfBall()) / 2;
        setY(current_y);
        invalidate();

    }

    public void setCustomTypeFace(Typeface tf) {

        typefaceCustom = tf;
    }

    /////new method for aligning in top,bottom,left,right,of screen by sumon///////

    /////////////////////// added reaz **********//////////////////
    public void setFontType(int fontType) {
        this.fontType = fontType;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    /////////////////////////********add setter OpenAPP*********///////////////
    public void setOpenApp(String openApp) {
        this.openApp = openApp;
    }

    public void setNavigateTo(Long navigateTo) {
        this.navigateTo = navigateTo;
    }

    public void setLimbikaAutoPlay(int autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setLimbikaDelay(int soundDalay) {
        this.soundDalay = soundDalay;
    }

    //LISTENERS
    public interface DropTargetListener {
        void onViewDropped(LimbikaView view, Long dropTargetKey);

        void onViewDroppedonWrongTarge(LimbikaView view, Long dropTargetKey);
    }

    public interface SingleTapListener {
        void onSingleTap(View v, LimbikaViewItemValue limbikaViewItemValue);
    }

    public interface LongTapListener {
        void onLongTap(View v);
    }

//    public void setLimbikaResult(String result) {
//        this.result = result;
//    }

    public interface DragListener {
        void onDrag(float x, float y);
    }

    public interface DoubleTapListener {
        void onDoubleTap(float x, float y, LimbikaViewItemValue key);

        void onDoubleTap_2(LimbikaViewItemValue key);
    }

    public interface RotationListener {
        void onRotate(int rotation);//current rotation in degrees
    }

    public interface DragFinishListner {
        void onDragFinish(LimbikaViewItemValue limbikaViewItemValue);
    }

    public interface EraseListner {
        void onErase(LimbikaViewItemValue limbikaViewItemValue);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (singleTapListener != null) {
                singleTapListener.onSingleTap(LimbikaView.this, limbikaViewItemValue);

                //added by sumon for animation
                if (playmode && isStartAnimation) {
                    selectAnimation(whichAnimation);
                }
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (longTapListener != null)
                longTapListener.onLongTap(LimbikaView.this);
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            if (doubleTapListener != null) {
                doubleTapListener.onDoubleTap(x, y, limbikaViewItemValue);
            }

            if (doubleTapListener != null) {
                doubleTapListener.onDoubleTap_2(limbikaViewItemValue);
            }

            return true;
        }
    }

    private void selectAnimation(int whichAnimation) {
        switch (whichAnimation) {
            case 0:
                break;
            case 1:
                zoomOut(LimbikaView.this);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
        }
    }

    long lineShowTime = 3000;

    public void drawMostOuterRectangle(int color) {
        isMostOuterRect = true;
        mostOuterRectPaint.setColor(color);
        mostOuterRectPaint.setStrokeWidth(10);
        invalidate();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isMostOuterRect = false;
                mostOuterRectPaint.setColor(Color.TRANSPARENT);
                invalidate();
            }
        }, lineShowTime);
    }

    // method used to shape image roundedrectagle
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // zoomout animation method by sumon
    public void zoomOut(final View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat("scaleX", .90f),
                PropertyValuesHolder.ofFloat("scaleY", .90f));
        scaleDown.setDuration(200);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.setRepeatCount(1);

        scaleDown.start();
    }

    // zoomIn animation method by sumon
   /* public void zoomIn(final View v) {

    }*/

    // use to call doubletaplistner HELPER ANIMATION CONTROLS IN LOADTASK
    public void startOnTapAnimation(int whichAnimation) {
        isStartAnimation = true;
        this.whichAnimation = whichAnimation;

    }

    public void call_doubleTap() {
        saveViewState();
        doubleTapListener.onDoubleTap(0, 0, getLimbikaViewItemValue());
    }

    @Override
    protected void onDetachedFromWindow() {
       /* if(imageBitmap!=null)
            imageBitmap.recycle();*/
        super.onDetachedFromWindow();

    }
}