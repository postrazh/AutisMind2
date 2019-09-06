package com.autismindd.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ibrar on 5/25/2016.
 */
public class DialogNavBarHide {

    public static int BOTTOM=0;
    public static int TOP=1;
    public static int LEFT=2;
    public static int RIGHT=3;


    public static void navBarHide(Activity activity , Dialog dialog){
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    public static  void  dialogAlignment( Dialog dialog,int choice){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        if(choice==BOTTOM)
            wlp.gravity = Gravity.BOTTOM;
        else if(choice==TOP)
            wlp.gravity = Gravity.TOP;
        else if(choice==LEFT)
            wlp.gravity = Gravity.LEFT;
        else if(choice==RIGHT)
            wlp.gravity = Gravity.RIGHT;
    }

}
