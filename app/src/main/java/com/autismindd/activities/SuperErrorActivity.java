package com.autismindd.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.dao.Task;
import com.autismindd.dao.TaskPack;
import com.autismindd.dao.User;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.pojo.ErrorLogModel;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.ApplicationMode;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.TypeFace_MY;
import com.autismindd.utilities.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SuperErrorActivity extends BaseActivity {

    RelativeLayout rlBackGroungError;
    ImageView ivError;
    TextView tvErrorMessage;
    ImageProcessing imageProcessing;
    SuperErrorActivity activity;
    String getErrorImagePath, getErrorText, getErrorSound = "";
    int getColor, getMode;
    private static long TIME_STAY, TARGET, taskID;
    MediaPlayer mp;
    private ArrayList<ErrorLogModel> errorLogModelList;
    StaticInstance staticInstance;
    User user;
    TaskPack level;
    int getTutorialMode = 0;
    boolean musicControl = false;
    private Handler errorImageHandler;
    UserInfo userInfo;
    List<Integer> shuffleValueArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_super_error);
        activity = this;
        imageProcessing = new ImageProcessing(activity);
        userInfo = new UserInfo();
        errorLogModelList = new ArrayList<>();
        shuffleValueArray = new ArrayList<>();
        staticInstance = StaticInstance.getInstance();
        errorLogModelList = staticInstance.getErrorLogModelsList();
        errorImageHandler = new Handler();
        user = staticInstance.getUser();
        level = staticInstance.getLevel();

        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }

        getErrorImagePath = getIntent().getExtras().getString(StaticAccess.TAG_PLAY_ERROR_IMAGE);
        getErrorText = getIntent().getExtras().getString(StaticAccess.TAG_PLAY_ERROR_TEXT);
        getColor = getIntent().getExtras().getInt(StaticAccess.TAG_PLAY_ERROR_COLOR);
        getMode = getIntent().getExtras().getInt(StaticAccess.TAG_TASK_MODE_KEY);
        //get Tutorial Mode
        getTutorialMode = getIntent().getExtras().getInt(StaticAccess.TAG_TASK_TUTORIAL);
        TARGET = getIntent().getExtras().getLong(StaticAccess.TARGET_KEY, -1);
        getErrorSound = getIntent().getExtras().getString(StaticAccess.TAG_PLAY_ERROR_SOUND);
        taskID = getIntent().getExtras().getInt(StaticAccess.TAG_PLAY_ERROR_TASK_ID, -1);
        rlBackGroungError = (RelativeLayout) findViewById(R.id.rlError);
        ivError = (ImageView) findViewById(R.id.ivError);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);

//        res = userInfo.getlevelWiseSupperErrorGIF(level.getLevel());
        // error Text
    /*    if (getErrorText.length() > 0 && getErrorText != null) {
            tvErrorMessage.setText(getErrorText);
        } else {
            tvErrorMessage.setText(getResources().getString(R.string.emptyErrorMsg));
        }*/

        // error Image
       /* if (getErrorImagePath.length() > 0 && getErrorImagePath != null) {
            imageProcessing.setImageWith_loader(ivError, getErrorImagePath);
        } else {
            ivError.setImageResource(R.drawable.img_women);
        }*/

        shuffleValueArray = userInfo.getShuffleList();
        ivError.setImageResource(userInfo.getlevelWiseSupperErrorGIF(shuffleValueArray.get(0)));
        tvErrorMessage.setText(get40CharPerLineString(userInfo.getLevelWiseSupperErrorString(shuffleValueArray.get(0), activity)));
        tvErrorMessage.setTypeface(TypeFace_MY.getRancho(activity));
/*        ivError.setImageResource(res[0]);
        errorImageHandler.postDelayed(imageChangeRunnable, 0);*/
//        imageChangeRunnable.run();


        // error color
     /*   if (getColor != 0) {
            rlBackGroungError.setBackgroundColor(getColor);
        } else {
            rlBackGroungError.setBackgroundColor(getResources().getColor(R.color.redLight));
        }*/
        rlBackGroungError.setBackgroundColor(getResources().getColor(R.color.white));
       /* if (getErrorSound != null && getErrorSound.length() > 0) {
            playSound(getErrorSound);
        } else {

        }*/
        playSoundAsset(userInfo.getLevelWiseSupperErrorSound(shuffleValueArray.get(0)));
        setArrayList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (errorImageHandler != null) {
                    errorImageHandler.removeCallbacks(imageChangeRunnable);
                }
                musicControl = true;
                Intent returnIntent = new Intent();
                returnIntent.putExtra(StaticAccess.TAG_PLAY_ERROR_RESPONSE, StaticAccess.TAG_PLAY_ERROR_RESPONSE);
//                setArrayList();
                if (TARGET != -1)
                    returnIntent.putExtra(StaticAccess.TARGET_KEY, TARGET);
                if (getMode == StaticAccess.TAG_TASK_GENERAL_MODE) {
                    setResult(StaticAccess.TAG_TASK_GENERAL_MODE, returnIntent);
                } else if (getMode == StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE) {
                    setResult(StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE, returnIntent);
                } else if (getMode == StaticAccess.TAG_TASK_ASSISTIVE_MODE) {
                    setResult(StaticAccess.TAG_TASK_ASSISTIVE_MODE, returnIntent);
                }
                mpSoundRelease();
                finish();
            }
        }, errorScreenDisplayTime());

    }

    public long errorScreenDisplayTime() {
        if (!ApplicationMode.devMode) {
            TIME_STAY = 5000;
        } else {
            TIME_STAY = 8000;
        }
        return TIME_STAY;
    }

    private void playSound(String getErrorSound) {
        if (getErrorSound.length() > 0) {
            mpSoundRelease();
            mp = new MediaPlayer();
            try {
                mp.setDataSource(Environment.getExternalStorageDirectory() + "/Android/Data/" + activity.getPackageName() + "/Sound/" + getErrorSound);
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // default sound set
        }
    }

    // Sound Play for Starting
    private void playSoundAsset(String song) {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

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

    // set ErrorLog without tutorial Tag
    void setArrayList() {
        if (getTutorialMode == 0) {
            if (!staticInstance.isTaskIdArray(taskID)) {
                ErrorLogModel errorLogModel = new ErrorLogModel();
//                errorLogModel.setUser(user);
                errorLogModel.setUserKey(user.getKey());
                errorLogModel.setTaskId(taskID);
                errorLogModel.setLevelID(level.getLevel());
                errorLogModel.setFirstLayerId(level.getFirstLayerTaskID());
//            errorLogModelList.add(errorLogModel);
                staticInstance.setErrorLogModelList(errorLogModel);
            } else {
                for (int i = 0; i < errorLogModelList.size(); i++) {
                    if (errorLogModelList.get(i).getTaskId() == taskID) {
                        ErrorLogModel errorLogModel = new ErrorLogModel();
//                        errorLogModel.setUser(user);
                        errorLogModel.setUserKey(user.getKey());
                        errorLogModel.setTaskId(taskID);
                        errorLogModel.setLevelID(level.getLevel());
                        errorLogModel.setFirstLayerId(level.getFirstLayerTaskID());
//                  errorLogModelList.add(errorLogModel);
                        staticInstance.setErrorLogModelList(errorLogModel);
                    }
                }
            }
        } else {

        }


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

        if (errorImageHandler != null) {
            errorImageHandler.removeCallbacks(imageChangeRunnable);
        }
    }

    //  for animation Error Image
    int counter = 0;
    int[] res;
    Runnable imageChangeRunnable = new Runnable() {
        @Override
        public void run() {
            counter++;
            Log.e("reaz", String.valueOf(counter));
            if (counter == 1) {
                Log.e("counter if: ", String.valueOf(counter));
                ivError.setVisibility(View.VISIBLE);
                ivError.setImageResource(res[1]);
            } else {
                Log.e("counter else: ", String.valueOf(counter));
                ivError.setVisibility(View.VISIBLE);
                ivError.setImageResource(res[0]);
                counter = 0;

            }
            errorImageHandler.postDelayed(imageChangeRunnable, 100);
        }
    };

    public String get40CharPerLineString(String text){

        String tenCharPerLineString = "";
        while (text.length() > 40) {

            String buffer = text.substring(0, 40);
            tenCharPerLineString = tenCharPerLineString + buffer + "\n";
            text = text.substring(40);
        }

        tenCharPerLineString = tenCharPerLineString + text.substring(0);
        return tenCharPerLineString;
    }
}
