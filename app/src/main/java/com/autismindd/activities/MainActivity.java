package com.autismindd.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.autismindd.customui.CustomAnimation;
import com.autismindd.parser.FirstLayerJsonParser;
import com.autismindd.services.DownloadService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ArrowDownloadButton;
import com.autismindd.utilities.ConnectionManagerPromo;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.share.Share;
import com.autismindd.utilities.AppController;
import com.autismindd.utilities.ApplicationMode;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class MainActivity extends BaseActivity {

    private Animation animBlink, animBlink2, animBlink3, animBlink4;
    private ImageView animateImageView, largeCircleImageView, mediumCircleImageView, smallCircleImageView, ivFramAnim;
    private MediaPlayer mp;

    private String notifySong = "sd_token_end.mp3";
    private String tone = "sd.mp3";
    private String mFlag;
    boolean rememberMe = false;

    Date lastSynDate;
    Date currentDate;


    public String currentFileName = "TaskPacks";

    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;
    private ProgressDialog pDialogUnzip;
    MainActivity activity;
    ArrowDownloadButton downloadBtn;
    Share share;
    //file download status
    boolean isDownload = false;
    //JSONDownload for firstlayer id image  status
    boolean isJSONDownload = false;


    Handler h = new Handler();
    //    Handler h1 = new Handler();
    Handler smallCircleHandler1 = new Handler();
    Handler smallCircleHandler2 = new Handler();
    Handler smallCircleHandler3 = new Handler();
    Handler smallCircleHandler4 = new Handler();

    Runnable smallCircleHandlerRunable = new Runnable() {
        @Override
        public void run() {
            smallCircleImageView.setVisibility(View.VISIBLE);
            smallCircleImageView.startAnimation(animBlink);

        }
    };
    Runnable smallCircleHandler2Runable = new Runnable() {
        @Override
        public void run() {
            mediumCircleImageView.setVisibility(View.VISIBLE);
            mediumCircleImageView.startAnimation(animBlink2);

        }
    };
    Runnable smallCircleHandler3Runable = new Runnable() {
        @Override
        public void run() {

            largeCircleImageView.setVisibility(View.VISIBLE);
            largeCircleImageView.startAnimation(animBlink3);
//            animateImageView.setVisibility(View.VISIBLE);
        }
    };
    Runnable smallCircleHandler4Runable = new Runnable() {
        @Override
        public void run() {

            animateImageView.setVisibility(View.VISIBLE);
            animateImageView.startAnimation(animBlink4);
//            animateImageView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        mFlag = SharedPreferenceValue.getDownloadFlag(activity);


        animateImageView = (ImageView) findViewById(R.id.animateImageView);
        largeCircleImageView = (ImageView) findViewById(R.id.largeCircleImageView);
        mediumCircleImageView = (ImageView) findViewById(R.id.mediumCircleImageView);
        smallCircleImageView = (ImageView) findViewById(R.id.smallCircleImageView);
        ivFramAnim = (ImageView) findViewById(R.id.ivFramAnim);
        downloadBtn = (ArrowDownloadButton) findViewById(R.id.downloadBtn);

        animateImageView.setVisibility(View.INVISIBLE);
        largeCircleImageView.setVisibility(View.INVISIBLE);
        mediumCircleImageView.setVisibility(View.INVISIBLE);
        smallCircleImageView.setVisibility(View.INVISIBLE);
        ivFramAnim.setVisibility(View.INVISIBLE);
    /* currentDate = new Date(getFormattedDateFromTimestamp(System.currentTimeMillis()));
        lastSynDate = new Date(getFormattedDateFromTimestamp(SharedPreferenceValue.getDate(activity)));
    if (currentDate.after(lastSynDate)) {
            if (SharedPreferenceValue.getDate(activity) != 0) {
                // NextDate()
                if (SharedPreferenceValue.getInsert(activity)){
                    SharedPreferenceValue.setInsert(activity, false);
                    startService(new Intent(activity, DownloadService.class));
                }

            }
        }*/
        if (SharedPreferenceValue.getDate(activity) == 0) {
            // NextDate()
//                if (SharedPreferenceValue.getInsert(activity)) {
            if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                startService(new Intent(activity, DownloadService.class));
            }
        }
        if (!ApplicationMode.devMode) {
            animBlink = CustomAnimation.blink(1000);
            animBlink2 = CustomAnimation.blink(1000);
            animBlink3 = CustomAnimation.blink(1000);
            animBlink4 = CustomAnimation.blink(1000);

           /* smallCircleImageView.startAnimation(animBlink);
            smallCircleImageView.setVisibility(View.VISIBLE);*/

            playSound();
            smallCircleHandler1.postDelayed(smallCircleHandlerRunable, 1600);
            smallCircleHandler2.postDelayed(smallCircleHandler2Runable, 3200);
            smallCircleHandler3.postDelayed(smallCircleHandler3Runable, 4800);
            smallCircleHandler4.postDelayed(smallCircleHandler4Runable, 6400);
            h.postDelayed(r, 8200);

        } else {
            Intent mInSeond = new Intent(activity, WelcomeActivity.class);
            startActivity(mInSeond);
            finish();

        }

    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            largeCircleImageView.setVisibility(View.VISIBLE);
            animateImageView.setVisibility(View.VISIBLE);
            ivFramAnim.setVisibility(View.VISIBLE);
            ivFramAnim.setBackgroundResource(R.drawable.frame_anim_logo);
            AnimationDrawable anim = (AnimationDrawable) ivFramAnim.getBackground();
            anim.start();
        }
    };

    private void playSound() {
        mp = MediaPlayer.create(MainActivity.this, R.raw.splashsound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent mInSeond = new Intent(activity, WelcomeActivity.class);
                startActivity(mInSeond);
                finish();
            }
        });
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.start();


        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public void navBarHide(Activity activity, Dialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

 /*   public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds) {
        Date date = new Date();
        date.setTime(timestampInMilliSeconds);
        String formattedDate = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(date);
        return formattedDate;

    }*/

}
