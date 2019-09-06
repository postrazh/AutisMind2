package com.autismindd.activities;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.autismindd.listener.MultichoiceAdapterInterface;
import com.autismindd.R;
import com.autismindd.dao.User;
import com.autismindd.utilities.SharedPreferenceValue;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Macbook on 28/09/2016.
 */

//extend this activity when using multi choice mode listener

public abstract class MultichoiceViewActivity extends BaseActivity {

    RelativeLayout rlUserLstBg;
    ImageButton ibtnAdd, ibtnSettings, ibtnStatistics, ibtnDelete, ibtnSync, ibtnFloor, ibtnLamp, ibtDoll;
    GridView gvUser;
    RelativeLayout rlUserListBulb_off;
    boolean isLamp = false;
    String lampSound = "sound_click_lamp.mp3";
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        rlUserLstBg = (RelativeLayout) findViewById(R.id.rlUserLstBg);
        ibtnAdd = (ImageButton) findViewById(R.id.ibtnAdd);
        ibtnSettings = (ImageButton) findViewById(R.id.ibtnSettings);
        ibtnStatistics = (ImageButton) findViewById(R.id.ibtnStatistics);
        ibtnDelete = (ImageButton) findViewById(R.id.ibtnDelete);
        ibtnSync = (ImageButton) findViewById(R.id.ibtnSync);
        ibtnFloor = (ImageButton) findViewById(R.id.ibtnFloor);
        ibtnLamp = (ImageButton) findViewById(R.id.ibtnLamp);
        ibtDoll = (ImageButton) findViewById(R.id.ibtDoll);
        rlUserListBulb_off = (RelativeLayout) findViewById(R.id.rlUserListBulb_off);
        gvUser = (GridView) findViewById(R.id.gvUserList);
        isLamp = SharedPreferenceValue.getLampFlag(MultichoiceViewActivity.this);
        screenMode(isLamp);
        rlUserLstBg.setOnClickListener(new View.OnClickListener() {
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

        ibtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fifthButtonClicked(v);

            }
        });


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
                rendomOutSideClicked(v);

            }
        });


    }

    public void screenMode(boolean isLampTem) {
        if (isLampTem) {
            rlUserListBulb_off.setVisibility(View.VISIBLE);
            ibtnLamp.setImageResource(R.drawable.ic_lamp_dream);
            isLamp = false;

        } else {
            rlUserListBulb_off.setVisibility(View.GONE);
            ibtnLamp.setImageResource(R.drawable.ic_lamp);
            isLamp = true;

        }
        SharedPreferenceValue.setLampFlag(MultichoiceViewActivity.this, isLampTem);
    }

    //this is the communication bridge between the Multichoice base adapter and the Activity
    //only the adapter that extends Multichoice Adapter calls these methods we made it just to be sure whocevers uses this mechanism has these functions
    public abstract void setMultichoiceListener(MultichoiceAdapterInterface.ControlMethods listener);

    public abstract void singleTapDone(long key, User user);

    public abstract void singleTapModeOn(long key);

    public abstract void multiChoiceClear();

    public abstract void multiChoiceModeEnter(ArrayList<User> userlist, boolean mode);

    public abstract void firstButtonClicked(View v);

    public abstract void secondButtonClicked(View v);

    public abstract void thirdButtonClicked(View v);

    public abstract void fourthButtonClicked(View v);

    public abstract void fifthButtonClicked(View v);

    public abstract void rendomOutSideClicked(View v);

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
