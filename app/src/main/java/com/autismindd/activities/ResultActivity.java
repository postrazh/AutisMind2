package com.autismindd.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.autismindd.dao.ErrorUserLog;
import com.autismindd.dao.Star;
import com.autismindd.dao.TaskPack;
import com.autismindd.dao.TimeStatistics;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.pojo.ErrorLogModel;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.ApplicationMode;
import com.autismindd.utilities.ArrowDownloadButton;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.R;
import com.autismindd.customui.CustomViewForStar;
import com.autismindd.dao.User;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RAFI on 10/4/2016.
 */

public class ResultActivity extends BaseActivity {
    StaticInstance staticInstance;
    ArrayList<ErrorLogModel> errorLogModelList;

    ResultActivity activity;
    MediaPlayer mp;
    private String tone = "sd.mp3";
    private String Yes = "sd_v.mp3";
    private String victory = "sd_v.mp3";
    private static long TIME_STAY;
    IDatabaseManager databaseManager;
    User user;
    TaskPack level;
    int fLayerID, levelID, pvUserStar, getStar = 0;
    Long userKey;
    ErrorUserLog errorUserLog;
    long errorLogId = -1;
    int totalTaskNumber, result, errorLevelSize;
    RelativeLayout rlProgressIDStar, rlResult;
    ImageView imgYes;
    ArrowDownloadButton resultProgress;
    CustomViewForStar customViewForStar;
    Star star;
    UserInfo userInfo;
    ArrayList<ErrorUserLog> prvErrorArray;
    int prvStar = 0;

    int prvPoint = 0;
    long getDTSec;
    long getRT;
    private Handler reslutHandler;
    private String levelSound;
    boolean musicControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results);
        activity = this;
        reslutHandler = new Handler();
        databaseManager = new DatabaseManager(activity);
        staticInstance = StaticInstance.getInstance();
        rlResult = (RelativeLayout) findViewById(R.id.rlResult);

        totalTaskNumber = getIntent().getExtras().getInt(StaticAccess.TAG_FINAL_TASK_ID);
        getDTSec = getIntent().getExtras().getLong(StaticAccess.TAG_DT_MILL_SEC, -1);
        getRT = getIntent().getExtras().getLong(StaticAccess.TAG_RT_MILL_SEC, -1);
        errorLogModelList = new ArrayList<>();
        errorLogModelList = staticInstance.getErrorLogModelsList();

        userInfo = new UserInfo();
        imgYes = (ImageView) findViewById(R.id.imgYes);
        resultProgress = (ArrowDownloadButton) findViewById(R.id.resultProgress);
        rlProgressIDStar = (RelativeLayout) findViewById(R.id.rlProgressIDStar);
        user = staticInstance.getUser();
        userKey = user.getKey();

        level = staticInstance.getLevel();
        fLayerID = level.getFirstLayerTaskID();
        levelID = level.getLevel();
        rlResult.setBackgroundColor(Color.parseColor(userInfo.avatarLiteColor(user.getAvatar())));
        res = userInfo.getlevelWiseResultGif(levelID);
        levelSound = userInfo.getLevelWiseResultSound(levelID);
        playSoundAsset(levelSound, StaticAccess.TAG_SOUND_OPEN);
        imgYes.setVisibility(View.VISIBLE);
        new AsyncResult().execute();

        imgYes.setVisibility(View.VISIBLE);
        imgYes.setImageResource(res[0]);
        imageChangeRunnable.run();
        // check user Music on or off
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
    }

    int counter = 0;
    int[] res;
    Runnable imageChangeRunnable = new Runnable() {
        @Override
        public void run() {
            counter++;

            if (counter == 1) {
                Log.e("counter if: ", String.valueOf(counter));
                imgYes.setVisibility(View.VISIBLE);
                imgYes.setImageResource(res[1]);
            } else {
                Log.e("counter else: ", String.valueOf(counter));
                imgYes.setVisibility(View.VISIBLE);
                imgYes.setImageResource(res[0]);
                counter = 0;

            }
            reslutHandler.postDelayed(imageChangeRunnable, 200);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
        if (reslutHandler != null) {
            reslutHandler.removeCallbacks(imageChangeRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (reslutHandler != null) {
            reslutHandler.removeCallbacks(imageChangeRunnable);
        }
    }

    class AsyncResult extends AsyncTask<String, String, String> {
        int progress = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... f_url) {

            updateUserWithErrorLog();
            //set previous userStar
            staticInstance.clearPvUserStar();
            staticInstance.setPvUserStar(pvUserStar);

            //level wise get error size

            errorLevelSize = errorLogModelList.size();
            Log.d("result", "errorLogSize :" + String.valueOf(errorLevelSize));

            //calculation part
            result = StaticAccess.PERCENT_HUNDRED - Math.round((errorLevelSize * StaticAccess.PERCENT_HUNDRED) / totalTaskNumber);
            Log.d("result", "result :" + String.valueOf(result));

            getStar = userInfo.getStar(result);
            Log.d("result", "Star :" + String.valueOf(getStar));


            prvStar = databaseManager.getLevelWiseStar(userKey, fLayerID, levelID);

            prvPoint = databaseManager.getLevelWisePoint(userKey, fLayerID, levelID);
            if (result > prvPoint) {
                // delete Level Star
                databaseManager.deleteLevelWiseStar(user.getKey(), fLayerID, levelID);

                // Insert Level Star
                star = new Star();

                star.setUserId(userKey);
                star.setFirstLayerTaskId(fLayerID);
                star.setLevelTaskPackId(levelID);
                star.setLevelStar(getStar);
                star.setLevelPoint(result);
                star.setLevelDT(System.currentTimeMillis() - getDTSec);
                star.setLevelRT(getRT);
                star.setLevelErrorPoint(StaticAccess.PERCENT_HUNDRED - result);
                databaseManager.insertStar(star);

                //static Instance userClear and Update
                // user Update part
                user.setLastLevelID(levelID);
                user.setPoint(result);
                // levelstar wise user FirstLayerId update

                user.setFirstLayerTaskID(fLayerID);

                user.setStars(databaseManager.getTotalStar(userKey));
                user.setUserState(false);

                //update user
                userKey = databaseManager.updateUser(user);

                //UserState all time set false because its check only selected user
                user.setUserState(false);
                staticInstance.clearUser();
                staticInstance.setUser(user);

            }


            return null;
        }


        /**
         * After completing background task
         * Dismiss the progress dialog
         **/

        @Override
        protected void onPostExecute(String file_url) {
            musicControl = true;
            Intent in = new Intent(ResultActivity.this, TransparentActivity.class);
            startActivityForResult(in, TransparentActivity.ResultTag);

        }
    }


    // sound release created by reaz
    public void mpSoundRelease() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    // Sound Play for Starting
    private void playSoundAsset(String song, int flag) {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            if (flag == StaticAccess.TAG_SOUND_OPEN) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {


                    }
                });
            } else if (flag == StaticAccess.TAG_SOUND_STAR_FINISH) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mpSoundRelease();
                        musicControl = true;
                        Intent levelIntent = new Intent(activity, TaskPackActivity.class);
                        levelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(levelIntent);
                        finish();
                    }
                });
            }

            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // insertion or update by user ErrorLog Table
    void updateUserWithErrorLog() {
        prvErrorArray = new ArrayList<>();
        prvErrorArray = databaseManager.errorLogByKeyFIdAndLevel(userKey, fLayerID, levelID);
        if (errorLogModelList.size() < prvErrorArray.size()) {
            if (errorLogModelList.size() > 0) {
                for (int i = 0; i < errorLogModelList.size(); i++) {
                    boolean isRemoved = false;
                    Long userKey2 = errorLogModelList.get(i).getUserKey();
                    int lastLevel2 = errorLogModelList.get(i).getLevelID();
                    int fLayerID2 = errorLogModelList.get(i).getFirstLayerId();
                    long taskID2 = errorLogModelList.get(i).getTaskId();
                    isRemoved = databaseManager.deleteErrorUserLogByUserKey(userKey2, fLayerID2, lastLevel2);

                    errorUserLog = new ErrorUserLog();
                    errorUserLog.setUserId(userKey2);
                    errorUserLog.setLevelTaskPackId(lastLevel2);
                    errorUserLog.setFirstLayerTaskId(fLayerID2);
                    errorUserLog.setErrorTaskCount(taskID2);

                    databaseManager.insertErrorUserLogByUserKey(errorUserLog);

                }

            } else {
                boolean isRemoved = databaseManager.deleteErrorUserLogByUserKey(userKey, fLayerID, levelID);

            }
        }


    }

    // its player Progress by result
    int count = 0;
    int resultTemp = -1;
    Runnable r = new Runnable() {
        @Override
        public void run() {

            resultProgress.setProgress(count);
            count = count + 1;

            if (count <= resultTemp) {
                h.postDelayed(r, 30);

            } else {
                resultProgress.setVisibility(View.GONE);
                resultTemp = -1;

                int arrStar[] = UserInfo.returnVal(getStar);
                rlProgressIDStar.addView(customViewForStar);
            }

        }
    };
    Handler h = new Handler();

    private void animationResultProgress(final int result) {

        count = 0;
        resultProgress.setVisibility(View.VISIBLE);
        resultProgress.startAnimating();
        this.resultTemp = result;
        h.postDelayed(r, 100);

    }

    public long errorScreenDisplayTime() {
        if (!ApplicationMode.devMode) {
            TIME_STAY = 15000;
        } else {
            TIME_STAY = 6000;
        }
        return TIME_STAY;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        musicControl = false;
        if (resultCode == TransparentActivity.ResultTag) {

            //take all data and send it to level
            musicControl = true;
            Intent intent = new Intent(activity, TaskPackActivity.class);
            //
//            if (getStar > prvStar) {
            // flag one means going level activity with Star and Point
            intent.putExtra(StaticAccess.RESULT_FLAG, 1);
            intent.putExtra(StaticAccess.RESULT_POINT, result);
            intent.putExtra(StaticAccess.RESULT_STAR, getStar);
            intent.putExtra(StaticAccess.PRV_STAR, prvStar);
            intent.putExtra(StaticAccess.CUR_LEVEL, levelID);
           /* } else {
                intent.putExtra(StaticAccess.RESULT_FLAG, -1);
                intent.putExtra(StaticAccess.RESULT_POINT, -1);
                intent.putExtra(StaticAccess.RESULT_STAR, -1);
                intent.putExtra(StaticAccess.PRV_STAR, -1);
                intent.putExtra(StaticAccess.CUR_LEVEL, -1);
            }*/
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (user.getMusic() > 0) {
            BackgroundMusicService.startMusic(activity);
        }
    }


}
