package com.autismindd.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.autismindd.R;

import com.autismindd.dao.User;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransparentFeedbackActivity extends BaseActivity implements View.OnClickListener {

    CircleImageView ivLeftCirculer, ivTopCirculer, ivRightCirculer, ivBottomCirculer, ivCenterCirculer;
    ImageView ivLeftNormal, ivTopNormal, ivRightNormal, ivBottomNormal, ivCenterNormal;

    MediaPlayer mp = new MediaPlayer();
    public static String IMAGE_PATH = "IMAGE";
    public static String SOUND_PATH = "SOUND";
    public static String ANIMATION = "ANIMATION";
    public static String CHOICE = "choice";

    String sound, image;
    int animation, choice;
    ImageProcessing imageProcessing;
    int pos=3000;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    User user;
    boolean musicControl=false;
    private StaticInstance staticInstance;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_feedback);
        staticInstance=StaticInstance.getInstance();
        user=staticInstance.getUser();
        if(user.getMusic()==0){
            if(!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
        findViewById();

        image = getIntent().getStringExtra(TransparentFeedbackActivity.IMAGE_PATH);
        sound = getIntent().getStringExtra(TransparentFeedbackActivity.SOUND_PATH);
        animation = getIntent().getIntExtra(TransparentFeedbackActivity.ANIMATION, 0);


        //0=rectangel, 1=circuler
        choice = getIntent().getIntExtra(TransparentFeedbackActivity.CHOICE, 0);
        imageProcessing = new ImageProcessing(this);

        if (sound != null)
            playSound(sound);
        showFeedBackAnimation(null, animation);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        handler=new Handler();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                mpSoundRelease();
                musicControl = true;
                finish();

            }
        }, pos);
    }


    @Override
    public void onClick(View v) {


    }

    //Playing sound
    private void playSound(String clipName) {
        if (clipName.length() > 0) {
            mpSoundRelease();
            mp = new MediaPlayer();
            try {
                mp.setDataSource(Environment.getExternalStorageDirectory() + StaticAccess.ANDROID_DATA + this.getPackageName() + StaticAccess.ANDROID_DATA_PACKAGE_SOUND + clipName);
                mp.prepare();
                mp.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // sound release created by reaz
    public void mpSoundRelease() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }


    private void findViewById() {
        ivLeftCirculer = (CircleImageView) findViewById(R.id.ivLeftCirculer);
        ivLeftNormal = (ImageView) findViewById(R.id.ivLeftNormal);
        ivTopCirculer = (CircleImageView) findViewById(R.id.ivTopCirculer);
        ivTopNormal = (ImageView) findViewById(R.id.ivTopNormal);
        ivRightCirculer = (CircleImageView) findViewById(R.id.ivRightCirculer);
        ivRightNormal = (ImageView) findViewById(R.id.ivRightNormal);
        ivBottomCirculer = (CircleImageView) findViewById(R.id.ivBottomCirculer);
        ivBottomNormal = (ImageView) findViewById(R.id.ivBottomNormal);
        ivCenterCirculer = (CircleImageView) findViewById(R.id.ivCenterCirculer);
        ivCenterNormal = (ImageView) findViewById(R.id.ivCenterNormal);


        ivLeftCirculer.setOnClickListener(this);
        ivLeftNormal.setOnClickListener(this);
        ivTopCirculer.setOnClickListener(this);
        ivTopNormal.setOnClickListener(this);
        ivRightCirculer.setOnClickListener(this);
        ivRightNormal.setOnClickListener(this);
        ivBottomCirculer.setOnClickListener(this);
        ivBottomNormal.setOnClickListener(this);
        ivCenterCirculer.setOnClickListener(this);
        ivCenterNormal.setOnClickListener(this);
    }

    //feedback Dialog show animation add by reaz
    private void showFeedBackAnimation(View view, int type) {
        if(type==0) {
            if (choice == 0) {
                //rectangle
                ivCenterNormal.setVisibility(View.VISIBLE);
                ivCenterNormal.setImageBitmap(imageProcessing.getImage(image));
                view = ivCenterNormal;
            } else {
                //circle
                ivCenterCirculer.setVisibility(View.VISIBLE);
                ivCenterCirculer.setImageBitmap(imageProcessing.getImage(image));
                view = ivCenterCirculer;
            }
        }else {
            switch (type) {
                case Animanation.shake1:

                    Animanation.shakeFeedBackAnimation(view);

                    break;
                case Animanation.shake2:
                    if (choice == 0) {
                        //rectangle
                        ivCenterNormal.setVisibility(View.VISIBLE);
                        ivCenterNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivCenterNormal;
                    } else {
                        //circle
                        ivCenterCirculer.setVisibility(View.VISIBLE);
                        ivCenterCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivCenterCirculer;
                    }
                    Animanation.shakeFeedBackAnimation2(view);
                    break;
                case Animanation.blink1:
                    if (choice == 0) {
                        //rectangle
                        ivCenterNormal.setVisibility(View.VISIBLE);
                        ivCenterNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivCenterNormal;
                    } else {
                        //circle
                        ivCenterCirculer.setVisibility(View.VISIBLE);
                        ivCenterCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivCenterCirculer;
                    }
                    Animanation.blinkFeedBackAnimation(view);
                    break;
                case Animanation.wiggleAnimation:
                    if (choice == 0) {
                        //rectangle
                        ivCenterNormal.setVisibility(View.VISIBLE);
                        ivCenterNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivCenterNormal;
                    } else {
                        //circle
                        ivCenterCirculer.setVisibility(View.VISIBLE);
                        ivCenterCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivCenterCirculer;
                    }
                    Animanation.wiggleAnimationFeedBackAnimation(view);
                    break;
                ///screenn related ones***************************************** ///////////////////////////////////////

                case Animanation.slideTopToBottom:
                    if (choice == 0) {
                        //rectangle
                        ivTopNormal.setVisibility(View.VISIBLE);
                        ivTopNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivTopNormal;
                    } else {
                        //circle
                        ivTopCirculer.setVisibility(View.VISIBLE);
                        ivTopCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivTopCirculer;
                    }
                    Animanation.topToBottomFeedBackAnimation(view);
                    break;

                case Animanation.slideBottomToTop:
                    if (choice == 0) {
                        //rectangle
                        ivBottomNormal.setVisibility(View.VISIBLE);
                        ivBottomNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivBottomNormal;
                    } else {
                        //circle
                        ivBottomCirculer.setVisibility(View.VISIBLE);
                        ivBottomCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivBottomCirculer;
                    }
                    Animanation.bottomToTopFeedBackAnimation(view);
                    break;

                case Animanation.slideLeftToRight:
                    if (choice == 0) {
                        //rectangle
                        ivLeftNormal.setVisibility(View.VISIBLE);
                        ivLeftNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivLeftNormal;
                    } else {
                        //circle
                        ivLeftCirculer.setVisibility(View.VISIBLE);
                        ivLeftCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivLeftCirculer;
                    }
                    Animanation.slideLeftToRightFeedBackAnimation(view);
                    break;
                case Animanation.slideRightToLeft:
                    if (choice == 0) {
                        //rectangle
                        ivRightNormal.setVisibility(View.VISIBLE);
                        ivRightNormal.setImageBitmap(imageProcessing.getImage(image));
                        view = ivRightNormal;
                    } else {
                        //circle
                        ivRightCirculer.setVisibility(View.VISIBLE);
                        ivRightCirculer.setImageBitmap(imageProcessing.getImage(image));
                        view = ivRightCirculer;
                    }
                    Animanation.slideRightToLeftFeedBackAnimation(view);
                    break;


            }
        }
    }


    @Override
    public void onBackPressed() {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("TransparentFeedback Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();

     if(!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }
    @Override
    public void onResume() {
        super.onResume();
       if(user.getMusic()>0){
            BackgroundMusicService.startMusic(activity);
        }
    }

}
