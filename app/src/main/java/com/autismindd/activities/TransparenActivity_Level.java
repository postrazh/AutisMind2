package com.autismindd.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.dao.GiftTbl;
import com.autismindd.dao.User;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ApplicationMode;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;

import java.io.IOException;

/**
 * Created by Macbook on 10/10/2016.
 */

public class TransparenActivity_Level extends BaseActivity {

    public static int LevelTransparentTag = 11000;
    RelativeLayout rlTransparentLevel;
    MediaPlayer mp;
    private String tone = "sd.wav";
    private String giftTapSound = "git_tap.mp3";
    private String giftSound = "gift_open_prize.mp3";
    private String counterSound = "counter.mp3";
    private String counterSoundEnd = "sound_counter_ends.mp3";
    int getStar, getResult;
    User user;
    StaticInstance staticInstance;
    Handler h = new Handler();
    TransparenActivity_Level activity;
    UserInfo userInfo;
    TextView tvCounter;
    private int time = 60;///counter time
    private ImageProcessing imageProcessing;
    private ImageView starOneIV, starTwoIV, starThreeIV, avatarIV;
    private LinearLayout pregressRelative;
    MediaPlayer mpCounter;
    boolean isMpCounterSound = false;
    boolean musicControl = false;
    IDatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_level);
        activity = this;
        rlTransparentLevel = (RelativeLayout) findViewById(R.id.rlTransparentLevel);
        pregressRelative = (LinearLayout) findViewById(R.id.pregressRelative);
        tvCounter = (TextView) findViewById(R.id.tvProgressCounter);
        starOneIV = (ImageView) findViewById(R.id.ivStarOne);
        starTwoIV = (ImageView) findViewById(R.id.ivStarTwo);
        starThreeIV = (ImageView) findViewById(R.id.ivStarThree);
        avatarIV = (ImageView) findViewById(R.id.ivAvaterCircular);
        imageProcessing = new ImageProcessing(this);
        staticInstance = StaticInstance.getInstance();
        databaseManager = new DatabaseManager(activity);
        user = staticInstance.getUser();
        userInfo = new UserInfo();
        getStar = getIntent().getExtras().getInt(StaticAccess.RESULT_STAR, -1);
        getResult = getIntent().getExtras().getInt(StaticAccess.RESULT_POINT, -1);
        // set user imageView Circular
        imageProcessing.setImageWith_loaderRound(avatarIV, user.getPic());
//        avatarIV.setImageBitmap(imageProcessing.getImage(user.getPic()));
        animationResultProgress(getResult);
//        sendMessage();
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
    }


    private void animationResultProgress(final int result) {
        count = 0;
//        resultProgress.startAnimating();

        this.resultTemp = result;
        h.postDelayed(r, time);

    }

    /*  int starCount = 0;
      int starTemp = -1;
  Runnable star=new Runnable() {
      @Override
      public void run() {
          count = count + 1;
          if(starCount<=userInfo.getStar(resultTemp)){

          }
      }
  };*/
    // its player Progress by result
    int count = 0;
    int resultTemp = -1;
    Runnable r = new Runnable() {
        @Override
        public void run() {
// 20-11
//            resultProgress.setProgress(count);

            count = count + 1;
            if (!isMpCounterSound) {
                counterSoundStart(isMpCounterSound);
            }

            if (count <= resultTemp) {
                if (count == 50) {
                    starOneIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_star_yellow));
                    playSoundAsset(tone, StaticAccess.TAG_SOUND_STAR);
                    Animanation.zoomInZoomOut(starOneIV);
                } else if (count == 75) {
                    starTwoIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_star_yellow));
                    playSoundAsset(tone, StaticAccess.TAG_SOUND_STAR);
                    Animanation.zoomInZoomOut(starTwoIV);
                } else if (count == 100) {
                    starThreeIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_star_yellow));
                    playSoundAsset(tone, StaticAccess.TAG_SOUND_STAR);
                    Animanation.zoomInZoomOut(starThreeIV);
                }
                if (count < 101)
                    tvCounter.setText(String.valueOf(count) + "%");

                h.postDelayed(r, time);//30

            } else {
                resultTemp = -1;
                Log.d("getting duration: ", String.valueOf(userInfo.getAnimDuration(getStar) + "total star: " + user.getStars()));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSoundAsset(counterSoundEnd, StaticAccess.TAG_SOUND_STAR_END);
                        isMpCounterSound = false;
                        musicControl = true;
                    }
                }, 800);
                rotateAnim(userInfo.getAnimDuration(getStar), pregressRelative);

//                customViewForStar.finishActivity();
                /*f(count<=50){
                   customViewForStar = new CustomViewForStar(activity, activity);
                    customViewForStar.drawAStar(1);
                    customstarRelativeLayout.addView(customViewForStar);
                }else if(count>=51&&count<=75){
                    customViewForStar = new CustomViewForStar(activity, activity);
                    customViewForStar.drawAStar(2);
                    customstarRelativeLayout.addView(customViewForStar);
                }
                else if(count>=100){
                    customViewForStar = new CustomViewForStar(activity, activity);
                    customViewForStar.drawAStar(3);
                    customstarRelativeLayout.addView(customViewForStar);
                }*/
                //Done working with progress
//                finishProgress();
                // zoomInAnimation(resultProgress);
            }

        }
    };
//20-11
  /*  public void finishProgress() {
        //progressRelativeLayout.removeAllViews();
        //progressRelativeLayout.setVisibility(View.GONE);
        int arrStar[] = UserInfo.returnVal(getStar);
        customViewForStar = new CustomViewForStar(TransparenActivity_Level.this, arrStar, TransparenActivity_Level.this);
        customstarRelativeLayout.addView(customViewForStar);
    }*/


    Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {
            musicControl = true;
            setResult(LevelTransparentTag);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        //back press wont work
    }

    void counterSoundStart(boolean isCounterSound) {
        if (!isCounterSound) {
            playSoundAssetCounter(counterSound);
            isMpCounterSound = true;
        }

    }

    int appearCount = 0;
    private GiftTbl giftBoxModel;
    private long giftBoxId;
    private int appearPoint;
    int currentStarPoint;
    int[] ranges;


    //// prevGiftBoxD shown or not check then get it and update it values by checking ranges
    //// appear point and appear count are use to check prev appear count and point

    public final int MAX_STAGE=4;
    public void finishTheActivity() {
        //check Level UP
        int prvStar = user.getStars() - getStar;
        int prvLevel = userInfo.getLevel(prvStar);
        int currentLevel = userInfo.getLevel(user.getStars());

      /*  if (ApplicationMode.devMode) {
            dialogGiftBox(userInfo.getGiftBox(user.getAvatar()));
        } else {*/
//        if (staticInstance.isLevelUp() && currentLevel > prvLevel) {

        giftBoxId = databaseManager.hasUserForGiftBox(user.getKey());
        if (giftBoxId > 0) { /// enter if giftBoxID>0 gift box appear for second and more time
            giftBoxModel = databaseManager.getGiftBoxByUserId(user.getKey());

            //last appeared giftbox point as in 15 or 50....
            appearPoint = giftBoxModel.getAppearPoint();
            // the count is like if 15 point then count=1
            appearCount = giftBoxModel.getAppearCount();
            //users stars
            currentStarPoint = user.getStars();
            //because we have only 5 stages
            if(appearCount<MAX_STAGE) {
                if (currentStarPoint > appearPoint) {
                    ranges = userInfo.getPointRange(appearCount);///getting ranges
                    if (currentStarPoint > ranges[0] && currentStarPoint <= ranges[1]) {
                        appearCount = appearCount + 1;
                        giftBoxModel = new GiftTbl();
                        giftBoxModel.setId(giftBoxId);
                        giftBoxModel.setUserId(user.getId());
                        giftBoxModel.setUserKey(user.getKey());
                        giftBoxModel.setAppearPoint(currentStarPoint);
                        giftBoxModel.setAppearCount(appearCount);
                        databaseManager.updateGiftTbl(giftBoxModel);

                        //Toast.makeText(this,"Update GiftboxId:" +String.valueOf(giftBoxId)
                             //   +"\nap Count:" +String.valueOf(appearCount)+
                              //  "\nap point:" +String.valueOf(appearPoint),Toast.LENGTH_LONG).show();

                        dialogGiftBox(userInfo.getGiftBox(user.getAvatar()));


                    } else {
                        h.postDelayed(finishRunnable, 500);
                    }
                } else {
                    h.postDelayed(finishRunnable, 500);
                }
            } else {
                h.postDelayed(finishRunnable, 500);
            }
        } else { /// first gift box appear time
            currentStarPoint = user.getStars();
            appearPoint = 0;
            appearCount = userInfo.getAppearLevel(currentStarPoint-getStar);

            if (currentStarPoint > appearPoint) {
                ranges = userInfo.getPointRange(appearCount);///getting ranges

                if(appearCount<MAX_STAGE) {
                if (currentStarPoint > ranges[0] && currentStarPoint <= ranges[1]) {
                    appearCount = appearCount + 1;
                    giftBoxModel = new GiftTbl();
                    giftBoxModel.setUserId(user.getId());
                    giftBoxModel.setUserKey(user.getKey());
                    giftBoxModel.setAppearPoint(currentStarPoint);
                    giftBoxModel.setAppearCount(appearCount);
                    databaseManager.insertGiftTbl(giftBoxModel);
                   /* Toast.makeText(this,"insert GiftboxId:" +String.valueOf(giftBoxId)
                            +"\nap Count:" +String.valueOf(appearCount)+
                            "\nap point:" +String.valueOf(appearPoint),Toast.LENGTH_LONG).show();*/
                    dialogGiftBox(userInfo.getGiftBox(user.getAvatar()));

                } else {
                    h.postDelayed(finishRunnable, 500);
                }
            } else {
                    h.postDelayed(finishRunnable, 500);
                }
            } else {
                h.postDelayed(finishRunnable, 500);
            }

        }

    }

    /// rotate animation for rotating the star contain dialog
    void rotateAnim(long duration, final View v) {

        RotateAnimation rotate = new RotateAnimation(0, 1440,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(duration);
        rotate.setFillAfter(true);
        //v.setAnimation(scal);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sendMessage();
                mpSoundReleaseCounter();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //progress goes after this animation 20-11
//                finishProgress();
                ZoomOut(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(rotate);
    }
    

    //// zoom out animation used for rotate animation
    public void ZoomOut(View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat("scaleY", 0.0f),
                PropertyValuesHolder.ofFloat("scaleX", 0.0f));
        scaleDown.setDuration(1000);
        scaleDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finishTheActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        scaleDown.start();
    }

    // Sound Play for Starting
    private void playSoundAsset(String song, final int flag) {
        mpSoundRelease();
        mp = new MediaPlayer();

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);

            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mpSoundRelease();
                    // for prize sound by reaz
                    if (flag == StaticAccess.TAG_SOUND_PRIZE) {
                        musicControl = true;
                    }
                    // for getting star by reaz
                    else if (flag == StaticAccess.TAG_SOUND_STAR) {
                        musicControl = true;
                        // play another sound
                    } else if (flag == StaticAccess.TAG_SOUND_PRIZE_TAP) {
                      /*  musicControl=true;
                        Intent intent = new Intent(activity, AvatarUpdateActivity.class);
                        intent.putExtra(StaticAccess.TAG_UPDATE_ACTIVITY, StaticAccess.TAG_UPDATE_LEVEL_ACTIVITY_FLAG);
                        startActivityForResult(intent, StaticAccess.TAG_AVATAR_UPDATE);*/
                    } else if (flag == StaticAccess.TAG_SOUND_STAR_END) {
                        musicControl = true;
                      /*  musicControl=true;
                        Intent intent = new Intent(activity, AvatarUpdateActivity.class);
                        intent.putExtra(StaticAccess.TAG_UPDATE_ACTIVITY, StaticAccess.TAG_UPDATE_LEVEL_ACTIVITY_FLAG);
                        startActivityForResult(intent, StaticAccess.TAG_AVATAR_UPDATE);*/
                    }
                  /* Intent levelIntent = new Intent(TransparenActivity_Level.this, TaskPackActivity.class);
                    levelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(levelIntent);*/
                }
            });

            mp.prepare();
            mp.start();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // sound release created by reaz
    public void mpSoundRelease() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    // Sound Play for Counter Progress
    private void playSoundAssetCounter(String song) {
        mpCounter = new MediaPlayer();
        if (mpCounter.isPlaying()) {
            mpCounter.stop();
        }

        try {
            mpCounter.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);
            mpCounter.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
          /*  mpCounter.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    mpSoundReleaseCounter();
                  *//* Intent levelIntent = new Intent(TransparenActivity_Level.this, TaskPackActivity.class);
                    levelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(levelIntent);*//*
                }
            });*/
            mpCounter.setLooping(true);
            mpCounter.prepare();
            mpCounter.start();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sound release for Counter Progress created by reaz
    public void mpSoundReleaseCounter() {
        if (mpCounter != null) {
            mpCounter.release();
            mpCounter = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        musicControl = false;
        if (resultCode == StaticAccess.TAG_AVATAR_UPDATE) {
            h.postDelayed(finishRunnable, 1500);
        }
    }


    // gift box  dialog
    private void dialogGiftBox(int giftBox) {

        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transparent_level_gift_box_layout);
        dialog.setCancelable(false);

        ImageView ivAvaterCircularGift = (ImageView) dialog.findViewById(R.id.ivAvaterCircularGift);
        ivAvaterCircularGift.setImageDrawable(ContextCompat.getDrawable(this, giftBox));
        playSoundAsset(giftSound, StaticAccess.TAG_SOUND_PRIZE);
        Animanation.zoomIn2(ivAvaterCircularGift);
//        Animanation.rotationAnfZoomAnimation(ivAvaterCircularGift, ivAvaterCircularGift); // comment by reaz 1/29/2017
        ivAvaterCircularGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpSoundRelease();
                musicControl = true;
                Intent intent = new Intent(activity, AvatarUpdateActivity.class);
                intent.putExtra(StaticAccess.TAG_UPDATE_ACTIVITY, StaticAccess.TAG_UPDATE_LEVEL_ACTIVITY_FLAG);
                startActivityForResult(intent, StaticAccess.TAG_AVATAR_UPDATE);

//                playSoundAsset(giftTapSound, StaticAccess.TAG_SOUND_PRIZE_TAP);
                dialog.dismiss();
            }
        });
   /*     h.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 4500);*/
        DialogNavBarHide.navBarHide(this, dialog);
    }

    //// method used for sending msg to broadcast receiver for starting star animation
    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        musicControl = true;
        Intent intent = new Intent(StaticAccess.TAG_BROADCAST_RECEIVER_FOR_STAR);
        // You can also include some extra data.
        intent.putExtra(StaticAccess.TAG_BROADCAST_RECEIVER_KEY, getString(R.string.broad_cast_msg));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user.getMusic() > 0) {
            BackgroundMusicService.startMusic(activity);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }
}
