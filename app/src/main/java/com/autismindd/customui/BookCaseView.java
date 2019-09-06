package com.autismindd.customui;

/**
 * Created by Probook 440 on 9/26/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.GridView;

import com.autismindd.R;

public class BookCaseView extends GridView {

    Context ctx;
    private Bitmap background;
    private int mShelfWidth;
    private int mShelfHeight;


    public BookCaseView(Context context, AttributeSet attributes) {
        super(context, attributes);
        ctx = context;
        this.setFocusableInTouchMode(true);
        this.setClickable(false);

        final Bitmap shelfBackground = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_shelve);
        setBackground(shelfBackground);
        this.setFocusable(true);
    }

    public void setBackground(Bitmap background) {
        this.background = background;

        mShelfWidth = dpToPx(180);//background.getWidth();
        mShelfHeight =dpToPx(180); //background.getHeight();

    }

    protected void onClick(int bookIndex) {
//        LibraryBook book = this.result.getItemAt(bookIndex);
//
//        this.selectedBook = book;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int count = getChildCount();

//        final int heightOfCell = getChildAt(0).getHeight();
//        int heightDp = pxToDp(getChildAt(0).getHeight());

         int top = count > 0 ? getChildAt(0).getTop() : 0;
         int shelfWidth = mShelfWidth;
         int shelfHeight = mShelfHeight;

         shelfWidth = count > 0 ? getChildAt(0).getWidth() : mShelfWidth;//as height and width are same
         shelfHeight = count > 0 ? getChildAt(0).getHeight() : mShelfHeight;

        final int width = getWidth();
        final int height = getHeight();
        final Bitmap background = this.background;

        //top=top+20;

        for (int x = 0; x < width; x += shelfWidth) {
            //drawing everything columnwise
            for (int y = top; y < height; y += shelfHeight) {
                if(y==top)
                    continue;
                else
                canvas.drawBitmap(background, x, y, null);
            }

            //This draws the top pixels of the shelf above the current one

           // Rect source = new Rect(0, shelfHeight+top, shelfWidth, shelfHeight);
            //Rect dest = new Rect(x, 0, x + shelfWidth, top);
         //   canvas.drawBitmap(background, source, dest, null);
        }


        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ( keyCode == KeyEvent.KEYCODE_BACK && this.selectedBook != null ) {
//            this.selectedBook = null;
//            invalidate();
//            return true;
//        }

        return false;
    }

  /*  public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
*/
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    //0=width  1=height
    public int getMesearement_of_screen(int choice) {
        WindowManager w =  (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);;
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

}