package com.autismindd.customui;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Probook 440 on 12/8/2016.
 */

public class AvatarUpdateThread extends Thread {
    AvatarUpdateView avatarUpdateView;
    private boolean running = false;

    public AvatarUpdateThread(AvatarUpdateView avatarUpdateView) {
        this.avatarUpdateView = avatarUpdateView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        while (running) {
            Canvas canvas = avatarUpdateView.getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (avatarUpdateView.getHolder()) {
                    avatarUpdateView.drawFromThread(canvas);
                }
                avatarUpdateView.getHolder().unlockCanvasAndPost(canvas);
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
