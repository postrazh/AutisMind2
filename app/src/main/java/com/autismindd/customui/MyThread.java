package com.autismindd.customui;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Probook 440 on 12/8/2016.
 */

public class MyThread extends Thread {
    ProgressBarSurfaceView progressBarSurfaceView;


    public MyThread(ProgressBarSurfaceView progressBarSurfaceView) {
        this.progressBarSurfaceView = progressBarSurfaceView;
    }

    public static void setRunning(boolean runnings) {
        ProgressBarSurfaceView.running = runnings;
    }

    @Override
    public void run() {
        Log.e("running: ", String.valueOf( ProgressBarSurfaceView.running));
        while ( ProgressBarSurfaceView.running) {

            Log.e("running to2: ", String.valueOf( ProgressBarSurfaceView.running));
            Canvas canvas = progressBarSurfaceView.getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (progressBarSurfaceView.getHolder()) {
                    Log.e("from draw Thread: ", "from thread draw method");
                    progressBarSurfaceView.drawEveryThing(canvas);
                }
                progressBarSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
