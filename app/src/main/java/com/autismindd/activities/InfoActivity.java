package com.autismindd.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;

import java.lang.reflect.AccessibleObject;

/**
 * Created by RAFI on 1/19/2017.
 */

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    ImageButton ibtnBackInfo, ibtnLimbikaLink, ibtnIDAPPLink;
    TextView tvAutismindLink;
    InfoActivity activity;
    Intent intent;
   boolean musicControl=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        activity = this;

        findViewById();
    }

    void findViewById() {
        ibtnBackInfo = (ImageButton) findViewById(R.id.ibtnBackInfo);
        ibtnLimbikaLink = (ImageButton) findViewById(R.id.ibtnLimbikaLink);
        ibtnIDAPPLink = (ImageButton) findViewById(R.id.ibtnIDAPPLink);
        tvAutismindLink = (TextView) findViewById(R.id.tvAutismindLink);

        ibtnBackInfo.setOnClickListener(this);
        ibtnLimbikaLink.setOnClickListener(this);
        ibtnIDAPPLink.setOnClickListener(this);
        tvAutismindLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Animanation.zoomOut(v);
        switch (v.getId()) {
            case R.id.ibtnBackInfo:
                musicControl=true;
                 intent = new Intent(activity, WelcomeActivity.class);
                startActivity(intent);

                break;
            case R.id.ibtnLimbikaLink:
                musicControl=true;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.limbika.com")));

                break;
            case R.id.ibtnIDAPPLink:
                musicControl=true;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.idapp.es")));

                break;
            case R.id.tvAutismindLink:
                musicControl=true;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.autismind.com")));

                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        BackgroundMusicService.startMusic(activity);
    }


    @Override
    public void onStop() {
        super.onStop();
        if(!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }

}
