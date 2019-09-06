package com.autismindd.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.autismindd.R;

/**
 * Created by ibrar on 1/29/2017.
 */

public class BackgroundMusicService extends Service {
    private static final String TAG = null;
    MediaPlayer player;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.app_bg_music);
        player.setLooping(true);
        player.setVolume(0.07f, 0.07f);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {

    }

    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }

    public static void startMusic(Context context) {
        Intent intent = new Intent(context, BackgroundMusicService.class);
        context.startService(intent);
        //PrefUtils.musicOnOff = 1;
    }

    public static void stopMusic(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BackgroundMusicService.class);
        context.stopService(intent);
        // PrefUtils.musicOnOff = 0;
    }


    public boolean isMusicServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (this.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
