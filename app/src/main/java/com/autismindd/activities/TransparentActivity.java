package com.autismindd.activities;

import android.os.Bundle;

import com.autismindd.customui.OverlayBallsEnd;
import com.autismindd.dao.TaskPack;
import com.autismindd.dao.User;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.StaticInstance;


public class TransparentActivity extends BaseActivity {

    private TaskPack taskPack;
    private StaticInstance staticInstance;

    final static int ResultTag = 102;
    boolean musicControl = false;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticInstance = StaticInstance.getInstance();
        user = staticInstance.getUser();
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
        taskPack = staticInstance.getLevel();
        OverlayBallsEnd view = new OverlayBallsEnd(this, taskPack.getLevel());
        super.setContentView(view);

    }


    // Activity finish
    public void finishActivity() {
        musicControl = true;
        setResult(ResultTag);
        finish();
    }

    @Override
    public void onBackPressed() {

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
