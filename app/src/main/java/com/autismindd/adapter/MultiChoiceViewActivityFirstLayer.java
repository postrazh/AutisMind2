package com.autismindd.adapter;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autismindd.activities.AvatarUpdateActivity;
import com.autismindd.activities.BaseActivity;
import com.autismindd.activities.MultichoiceViewActivity;
import com.autismindd.customui.CustomAvatarView;
import com.autismindd.listener.MultiChoiceAdapterFirstLayerInterface;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.R;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.User;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RAFI on 10/3/2016.
 */

public abstract class MultiChoiceViewActivityFirstLayer extends BaseActivity {

    RelativeLayout rlAvatar, rlMultiChoiceBg, rlFirstLayerLamp_off;
    //    LinearLayout rlMultiChoiceBg;
    ImageButton ibtnAdd, ibtnSettings, ibtnStatistics, ibtnDelete, ibtnFloor, ibtnLamp, ibtDoll, ibtnBusket, ibtnBook;
    TextView tvFloorBg;
    //    GridView gvUser;
    boolean buttonVisibility = false;
    public User user;
    String avatarColor;
    StaticInstance staticInstance;
    ImageProcessing imageProcessing;
    MultiChoiceViewActivityFirstLayer activity;
    public CustomAvatarView customAvatarView;
    public UserInfo userInfo;
    public boolean isLamp = false;
    String lampSound = "sound_click_lamp.mp3";
    MediaPlayer mp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multichoice_view);
        activity = this;
        imageProcessing = new ImageProcessing(activity);

        rlMultiChoiceBg = (RelativeLayout) findViewById(R.id.rlMultiChoiceBg);
        rlAvatar = (RelativeLayout) findViewById(R.id.rlAvatar);
        ibtnAdd = (ImageButton) findViewById(R.id.ibtnAdd);
        ibtnSettings = (ImageButton) findViewById(R.id.ibtnSettings);
        ibtnStatistics = (ImageButton) findViewById(R.id.ibtnStatistics);
        ibtnDelete = (ImageButton) findViewById(R.id.ibtnDelete);
        ibtnFloor = (ImageButton) findViewById(R.id.ibtnFloor);
        ibtnLamp = (ImageButton) findViewById(R.id.ibtnLamp);
        ibtDoll = (ImageButton) findViewById(R.id.ibtDoll);
        tvFloorBg = (TextView) findViewById(R.id.tvFloorBg);
        rlFirstLayerLamp_off = (RelativeLayout) findViewById(R.id.rlFirstLayerLamp_off);
        ibtnBusket = (ImageButton) findViewById(R.id.ibtnBusket);
        ibtnBook = (ImageButton) findViewById(R.id.ibtnBook);

        userInfo = new UserInfo();
        staticInstance = StaticInstance.getInstance();
        user = staticInstance.getUser();

        avatarColor = userInfo.avatarLiteColor(user.getAvatar());
        rlMultiChoiceBg.setBackgroundColor(Color.parseColor(avatarColor));
        tvFloorBg.setBackgroundColor(Color.parseColor(userInfo.avatarDarkColor(user.getAvatar())));
        setImageBackground(user.getAvatar());

        customAvatarView = new CustomAvatarView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()));

        rlAvatar.addView(customAvatarView);
        customAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        gvUser = (GridView) findViewById(R.id.gvUserList);

        ibtDoll.setImageResource(userInfo.getAvatar(user.getAvatar(), user.getStars()));
        setLevelUpFlag();
        rlMultiChoiceBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rendomOutSideClicked(v);
            }
        });

        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstButtonClicked(v);
            }
        });
        ibtnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animanation.zoomOut(v);
                firstButtonClicked(v);
                Animanation.avatarWiseBookStartAnimation(ibtnBook,user);
            }
        });

        isLamp = SharedPreferenceValue.getLampFlag(MultiChoiceViewActivityFirstLayer.this);
        screenMode(isLamp);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondButtonClicked(v);

            }
        });

        ibtnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdButtonClicked(v);

            }
        });

        ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourthButtonClicked(v);

            }
        });


        //ibtnFloor, ibtnLamp, ibtDoll

        ibtnFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rendomOutSideClicked(v);


            }
        });
        ibtnLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rendomOutSideClicked(v);
                screenMode(isLamp);
                playSound(lampSound);


            }
        });
        ibtDoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animanation.zoomOut(v);
                Animanation.avatarStartAnimation(ibtDoll,user);
                rendomOutSideClicked(v);

            }
        });


    }

    public void screenMode(boolean isLampTem) {
        if (isLampTem) {
            rlFirstLayerLamp_off.setVisibility(View.VISIBLE);
            ibtnLamp.setImageResource(userInfo.getLamp(user.getAvatar(), isLampTem));
            isLamp = false;

        } else {
            rlFirstLayerLamp_off.setVisibility(View.GONE);
           // ibtnLamp.setImageResource(userInfo.getLamp(user.getAvatar(), isLampTem));
            isLamp = true;
        }
        ibtnLamp.setImageResource(userInfo.getLamp(user.getAvatar(), isLampTem));
        SharedPreferenceValue.setLampFlag(MultiChoiceViewActivityFirstLayer.this, isLampTem);
    }

    //this is the communication bridge between the Multichoice base adapter and the Activity
    //only the adapter that extends Multichoice Adapter calls these methods we made it just to be sure whocevers uses this mechanism has these functions
    public abstract void setMultiChoiceListener(MultiChoiceAdapterFirstLayerInterface.ControlMethods listener);

    public abstract void singleTapDone(FirstLayer key);

    public abstract void multiChoiceClear();

    public abstract void multiChoiceModeEnter(ArrayList<FirstLayer> FirstLayerList, boolean mode);

    public abstract void firstButtonClicked(View v);

    public abstract void secondButtonClicked(View v);

    public abstract void thirdButtonClicked(View v);

    public abstract void fourthButtonClicked(View v);

    public abstract void rendomOutSideClicked(View v);


    void setImageBackground(int avatar) {
        int res = -1;
        switch (avatar) {

            case StaticAccess.AVATAR_GIRL:

                ibtnBook.setVisibility(View.VISIBLE);
                ibtnBook.setImageResource(R.drawable.ic_floor_girl);
                ibtnBusket.setVisibility(View.INVISIBLE);

                res = userInfo.getLamp(avatar, isLamp);
                ibtnLamp.setImageResource(res);
                break;
            case StaticAccess.AVATAR_CAR:
                ibtnBook.setVisibility(View.VISIBLE);
                ibtnBook.setImageResource(R.drawable.ic_floor_car);
                ibtnBusket.setVisibility(View.INVISIBLE);

                res = userInfo.getLamp(avatar, isLamp);
                ibtnLamp.setImageResource(res);
                break;
            case StaticAccess.AVATAR_DINOSAUR:
                ibtnBook.setVisibility(View.VISIBLE);
                ibtnBook.setImageResource(R.drawable.ic_floor_unicorn);
                ibtnBusket.setVisibility(View.INVISIBLE);
                res = userInfo.getLamp(avatar, isLamp);
                ibtnLamp.setImageResource(res);
                break;
            case StaticAccess.AVATAR_HORSE:
                ibtnBook.setVisibility(View.VISIBLE);
                ibtnBook.setImageResource(R.drawable.ic_floor_rocket);
                ibtnBusket.setVisibility(View.INVISIBLE);
                res = userInfo.getLamp(avatar, isLamp);
                ibtnLamp.setImageResource(res);
                break;
            case StaticAccess.AVATAR_ROCKET:
                ibtnBook.setVisibility(View.VISIBLE);
                ibtnBook.setImageResource(R.drawable.ic_floor_dino);
                ibtnBusket.setVisibility(View.INVISIBLE);
                res = userInfo.getLamp(avatar, isLamp);
                ibtnLamp.setImageResource(res);
                break;
        }

    }

    // level up boolean set
    public void setLevelUpFlag() {

        int currentLevel = userInfo.getLevel(user.getStars());
        int tempValue = user.getStars() + 15;
        int expectLevel = userInfo.getLevel(tempValue);
        if (expectLevel > currentLevel) {
            staticInstance.setLevelUp(true);
        }

    }

    // play Sound for lamp click
    private void playSound(String song) {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
