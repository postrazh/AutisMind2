package com.autismindd.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenu;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.autismindd.adapter.TaskPackAdapter;
import com.autismindd.customui.AvatarUpdateView;
import com.autismindd.customui.CustomAvatarView;
import com.autismindd.customui.LockerCustomView;
import com.autismindd.customui.OnTouchCustomView;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.customui.CustomViewForStar;
import com.autismindd.customui.LevelView;
import com.autismindd.dao.Star;
import com.autismindd.dao.TaskPack;
import com.autismindd.dao.User;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.utilities.ArrowDownloadButton;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

// TaskPackActivity > PlayerActivity > ResultActivity >TaskPackActivity > Transparent_LevelActivity > Check User Avatar Update
// if (user avatar Update GiftBox show > AvatarUpdateActivity) oR >TaskPackActivity


public class TaskPackActivity extends BaseActivity implements LevelView.WhichViewClicked, View.OnTouchListener, AvatarUpdateView.AvatarAnimationFininishedListener {

    TaskPackActivity activity;
    private IDatabaseManager databaseManager;
    public ArrayList<TaskPack> taskPacks = new ArrayList<>();
    RelativeLayout rlLeveView, rlAvatar;
    int getId;
    private TaskPack taskPack;
    LevelView levelView;
    ProgressDialog pDialog;
    StaticInstance staticInstance;
    //    RelativeLayout rlAvatar;
    User user;
    TaskPack level;
    //    CustomAvatarView customAvatarView;
    ImageProcessing imageProcessing;

    TextView tvTaskPackName;
    ImageButton ibtnBackPack;
    RelativeLayout rlProgressIDStar, rlTransParent;
    ArrowDownloadButton resultProgress;
    int getResultFlag = -1;
    int cur_level = -1;
    int getStar = -1, getResult = -1, prvStar = -1;
    MediaPlayer mp;
    private String tone = "sd.mp3";
    UserInfo userInfo;
    AvatarUpdateView avatarUpdateView;
    ImageView centerImage;
    int draggableResourceId = 0;
    private Handler lockerHandler;
    public ArrayList<Integer> points;
    public boolean musicControl = false;
    private boolean isAppClosePressed = false; /// used for checking if user cancel playing task
    public ArrayList<Star> stars;

    //handling avatar view  click
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Toast.makeText(TaskPackAc4tivity.this,"done",Toast.LENGTH_LONG).show();
        Log.e("add1", "Heeres");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (avatarUpdateView.isViewOverlapping(x, y)) {
                    musicControl = true;
                    Intent intent = new Intent(activity, AvatarUpdateActivity.class);
                    intent.putExtra(StaticAccess.TAG_UPDATE_ACTIVITY, StaticAccess.TAG_UPDATE_ACTIVITY_FLAG);
                    startActivityForResult(intent, StaticAccess.TAG_AVATAR_UPDATE);
                }

                break;

            default:
                return false;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pack);
        activity = this;
        lockerHandler = new Handler();
        points = new ArrayList<>();
        stars = new ArrayList<>();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(StaticAccess.TAG_BROADCAST_RECEIVER_FOR_STAR));


        imageProcessing = new ImageProcessing(activity);
        staticInstance = StaticInstance.getInstance();
        ibtnBackPack = (ImageButton) findViewById(R.id.ibtnBackPack);
        tvTaskPackName = (TextView) findViewById(R.id.tvTaskPackName);


        user = staticInstance.getUser();
        level = staticInstance.getLevel();
        staticInstance.clearErrorArray();
        staticInstance.clearLevel();
        userInfo = new UserInfo();


       /* if (!ApplicationMode.devMode) {
            ibtnBackPack.setVisibility(View.INVISIBLE);
        }*/

        if (getIntent().getExtras() != null) {

            getResultFlag = getIntent().getExtras().getInt(StaticAccess.RESULT_FLAG, -1);
            getStar = getIntent().getExtras().getInt(StaticAccess.RESULT_STAR, -1);
            getResult = getIntent().getExtras().getInt(StaticAccess.RESULT_POINT, -1);
            prvStar = getIntent().getExtras().getInt(StaticAccess.PRV_STAR, -1);
            cur_level = getIntent().getExtras().getInt(StaticAccess.CUR_LEVEL, -1);
            whereItCameFrom = 1;
            // when someone press on Close App
            isAppClosePressed = getIntent().getBooleanExtra(StaticAccess.TAG_DETECT_APP_CLOSED_BUTTON_PRESSED, false);
            if (isAppClosePressed) {
                whereItCameFrom = 4;
            }
            Log.e("getResult: ", String.valueOf(getResult));
        }
        databaseManager = new DatabaseManager(this);
        getId = staticInstance.getFirstLayer().getFirstLayerTaskID();
        tvTaskPackName.setText(staticInstance.getFirstLayer().getName());
        resultProgress = (ArrowDownloadButton) findViewById(R.id.taskPackProgress);
        rlProgressIDStar = (RelativeLayout) findViewById(R.id.rlProgressIDStar);
        rlTransParent = (RelativeLayout) findViewById(R.id.rlTransParent);

        rlLeveView = (RelativeLayout) findViewById(R.id.rlLeveView);
        rlAvatar = (RelativeLayout) findViewById(R.id.rlAvatar);

        centerImage = (ImageView) findViewById(R.id.centerimageView);

        centerImage.setOnTouchListener(this);


     /*  asynTask for getStar Level Wise and Result Value Equation for
      TransParent_level_Activity And show Gift Box condition wise*/
        new AsyncGetStar().execute();


        ibtnBackPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lockerHandler != null) {
                    mpSoundRelease();
                    lockerHandler.removeCallbacks(lockerRunable);
                }
                musicControl = true;
                Intent intent = new Intent(activity, FirstLayerActivity.class);
                startActivity(intent);
                Animanation.zoomOut(v);
                finish();
            }
        });
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }


    }


    //use this method to play the sound
    public void playSound(String soundFileName) {

        MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(TaskPackActivity.this, R.raw.sd_faces_pop);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


            }
        });
        // mp.prepare();
        mp.start();


    }

    @Override
    public void playsound(int avatar) {

    }

    @Override
    public void getAnimationPos(float x, float y) {
//        OnTouchCustomView customCircleStrokeAnimation = new OnTouchCustomView(activity, x, y);
//        rlLeveView.addView(customCircleStrokeAnimation);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (lockerHandler != null) {
            mpSoundRelease();
            lockerHandler.removeCallbacks(lockerRunable);
        }
        musicControl = true;
        Intent intent = new Intent(activity, FirstLayerActivity.class);
        startActivity(intent);
        finish();
    }

    int arrStar[]; /// star on level
    int tempArrStar[];// temp array star on level

    class AsyncGetStar extends AsyncTask<String, String, String> {
        int levelOneStar = 0, levelTwoStar = 0, levelThreeStar = 0, levelFourStar = 0, levelFiveStar = 0, levelSixStar = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getResources().getString(R.string.Loading_msg));
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
            DialogNavBarHide.navBarHide(activity, pDialog);

        }


        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

//            databaseManager.deleteLevelWiseStar(user.getKey(), 5, 3);
            taskPacks = (ArrayList<TaskPack>) databaseManager.listTaskPacksFirstLayerID(getId);
            stars = new ArrayList<>();
            stars = databaseManager.getLayerLevelWiseStar(user.getKey(), getId);
            if (stars != null)
                for (int i = 0; i < stars.size(); i++) {
                    if (stars.get(i).getLevelTaskPackId() == 0) {
                        levelOneStar = stars.get(i).getLevelStar();
                    }
                    if (stars.get(i).getLevelTaskPackId() == 1) {
                        levelTwoStar = stars.get(i).getLevelStar();
                    }
                    if (stars.get(i).getLevelTaskPackId() == 2) {
                        levelThreeStar = stars.get(i).getLevelStar();
                    }
                    if (stars.get(i).getLevelTaskPackId() == 3) {
                        levelFourStar = stars.get(i).getLevelStar();
                    }
                    if (stars.get(i).getLevelTaskPackId() == 4) {
                        levelFiveStar = stars.get(i).getLevelStar();
                    }
                    if (stars.get(i).getLevelTaskPackId() == 5) {
                        levelSixStar = stars.get(i).getLevelStar();
                    }

                }
            points = databaseManager.getLevelWisePoint(user.getKey(), getId);
           /* levelOneStar = databaseManager.getLevelWiseStar(user.getKey(), getId, StaticAccess.LEVEL_ONE);
            levelTwoStar = databaseManager.getLevelWiseStar(user.getKey(), getId, StaticAccess.LEVEL_TWO);
            levelThreeStar = databaseManager.getLevelWiseStar(user.getKey(), getId, StaticAccess.LEVEL_THREE);
            levelFourStar = databaseManager.getLevelWiseStar(user.getKey(), getId, StaticAccess.LEVEL_FOUR);
            levelFiveStar = databaseManager.getLevelWiseStar(user.getKey(), getId, StaticAccess.LEVEL_FIVE);
            levelSixStar = databaseManager.getLevelWiseStar(user.getKey(), getId, StaticAccess.LEVEL_SIX);


            for (int i = 0; i < points.size(); i++) {
                Log.e("points_loop: ", "index " + String.valueOf(i) + " points: " + String.valueOf(points.get(i)));
            }
*/
            return null;
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/

        @Override
        protected void onPostExecute(String file_url) {
            arrStar = new int[]{levelOneStar, levelTwoStar, levelThreeStar, levelFourStar, levelFiveStar, levelSixStar};
            tempArrStar = arrStar;
            //   getStar from ResultActivity  getStar= Level Star
            //  prvStar from ResultActivity prvStar = level previous Star
            if (getStar != -1 && getStar > prvStar) {
                avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars() - getStar,
                        user.getName(), userInfo.avatarDarkColor(user.getAvatar()), TaskPackActivity.this);
                rlAvatar.removeAllViews();
                avatarUpdateView.flyStar(0);
                rlAvatar.addView(avatarUpdateView);


            } else
                avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars(),
                        user.getName(), userInfo.avatarDarkColor(user.getAvatar()), TaskPackActivity.this);


            // Level View Show users levels Status
            // users Avatar show users Star wise
            levelView = new LevelView(activity, activity, arrStar, userInfo.avatarDarkColor(user.getAvatar()), userInfo.avatarLiteColor(user.getAvatar()), userInfo.getAvatarLevel(user.getAvatar(), user.getStars()), formatePoints(points));
            rlLeveView.setBackgroundColor(Color.parseColor(userInfo.avatarDarkColor(user.getAvatar())));
            rlLeveView.addView(levelView);
            //  default Level view show  when user not play yet
            if (getResult == -1) {
                Log.e("getResult: ", String.valueOf(getResult));
                avatarUpdateView.flyStar(0);
                rlAvatar.addView(avatarUpdateView);
                controller();
            }
            // THIS USE only for after Palying
            if (getResultFlag == 1) {
                // rlTransParent.setVisibility(View.VISIBLE);
                //animationResultProgress(getResult);

                /////////*********** again minus level star view set new star values to show star  in correct time*********/////////////////
                levelView = new LevelView(activity, activity, minusPrevLevelStar(tempArrStar), userInfo.avatarDarkColor(user.getAvatar()), userInfo.avatarLiteColor(user.getAvatar()), userInfo.getAvatarLevel(user.getAvatar(), user.getStars()), formatePoints(points));
                rlLeveView.setBackgroundColor(Color.parseColor(userInfo.avatarDarkColor(user.getAvatar())));
                rlLeveView.removeAllViews();
                rlLeveView.addView(levelView);


                musicControl = true;
                centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), userInfo.getAvatarLevel(user.getAvatar(), user.getStars() - getStar)));
                centerImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        centerImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        fixPositionX = centerImage.getX();
                        fixpositionY = centerImage.getY();
                    }
                });
                // touch move need to DraggableResourceID
                draggableResourceId = userInfo.getAvatarDragLevel(user.getAvatar(), user.getStars() - getStar);
                Intent in = new Intent(activity, TransparenActivity_Level.class);
                in.putExtra(StaticAccess.RESULT_STAR, getStar);
                in.putExtra(StaticAccess.RESULT_POINT, getResult);
                startActivityForResult(in, TransparenActivity_Level.LevelTransparentTag);

            } else {
                //Center Image Set
                centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), userInfo.getAvatarLevel(user.getAvatar(), user.getStars())));
                centerImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        centerImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        fixPositionX = centerImage.getX();
                        fixpositionY = centerImage.getY();
                    }
                });
                // touch move need to DraggableResourceID
                draggableResourceId = userInfo.getAvatarDragLevel(user.getAvatar(), user.getStars());
            }
            pDialog.dismiss();
        }
    }

    int restorResetVal; ///restore minus star value

    int[] minusPrevLevelStar(int[] arrayStar) { /// method that minus star from level
        if (cur_level != -1)
            restorResetVal = arrayStar[cur_level];
        arrayStar[cur_level] = prvStar;
        return arrayStar;
    }

    int[] plusPrevLevelStar(int[] arrayStar) { /// method that assign previus star value that minus before
        if (cur_level != -1)
            arrayStar[cur_level] = restorResetVal;
        return arrayStar;
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
                    mpSoundRelease();
                    if (staticInstance.getLevel() != null) {
                        musicControl = true;
//                        overridePendingTransition(R.anim.zoom_exit,R.anim.zoom_enter);
                        Intent intent = new Intent(activity, PlayerActivity.class);
                        //  intent.putExtra("taskPackId", taskPack.getId());
                        intent.putExtra("position", 0);
                        startActivity(intent);
                        finish();
                    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        musicControl = false;

        // TaskPackActivity > PlayerActivity > ResultActivity >TaskPackActivity > Transparent_LevelActivity > Check User Avatar Update
        // if (user avatar Update GiftBox show > AvatarUpdateActivity) oR >TaskPackActivity


        if (resultCode == TransparenActivity_Level.LevelTransparentTag) {
            //  getting giftBox and Star Dialog

            //avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()));
            //rlAvatar.removeAllViews();
            //avatarUpdateView.flyStar(0);
            //rlAvatar.addView(avatarUpdateView);
            //this is called once the transparent levelactivity is finished
            //if needed to refresh view or something else dont use it
            //when the damn stars changed and it came from result

            /////////*********** again  plus level star view set new star values to show star  in correct time*********/////////////////
            levelView = new LevelView(activity, activity, plusPrevLevelStar(arrStar), userInfo.avatarDarkColor(user.getAvatar()), userInfo.avatarLiteColor(user.getAvatar()), userInfo.getAvatarLevel(user.getAvatar(), user.getStars()), formatePoints(points));
            rlLeveView.setBackgroundColor(Color.parseColor(userInfo.avatarDarkColor(user.getAvatar())));
            rlLeveView.removeAllViews();
            rlLeveView.addView(levelView);


            int prvStar = user.getStars() - getStar;
            int prvLevel = userInfo.getLevel(prvStar);
            int currentLevel = userInfo.getLevel(user.getStars());
            if (currentLevel > prvLevel) {
                centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), userInfo.getAvatarLevel(user.getAvatar(), user.getStars())));
                centerImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        centerImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        fixPositionX = centerImage.getX();
                        fixpositionY = centerImage.getY();
                    }
                });
                // touch move need to DraggableResourceID
                draggableResourceId = userInfo.getAvatarDragLevel(user.getAvatar(), user.getStars());
            } else {
                centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), userInfo.getAvatarLevel(user.getAvatar(), user.getStars() - getStar)));
                centerImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        centerImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        fixPositionX = centerImage.getX();
                        fixpositionY = centerImage.getY();
                    }
                });
                // touch move need to DraggableResourceID
                draggableResourceId = userInfo.getAvatarDragLevel(user.getAvatar(), user.getStars() - getStar);
            }

            whereItCameFrom = 2;
            controller();
        } else if (resultCode == StaticAccess.TAG_AVATAR_UPDATE) {

            avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()));
            rlAvatar.removeAllViews();
            avatarUpdateView.flyStar(0);
            rlAvatar.addView(avatarUpdateView);
            whereItCameFrom = 3;


        }
    }

    //will be initialized from oncreate
    float fixPositionX = 0, fixpositionY = 0, dX = 0, dY = 0;
    int lastAction;

    //// for draggable center view if you use it for anything else better modify it TOUCH *******************************
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (turnDragOn) {/// checking weather flushing animation finish or not  if true then enable dragging {sumon}
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (turnDragOn) {/// checking weather flushing animation finish or not  if true then enable dragging {sumon}
                    view.setY(event.getRawY() + dY);
                    view.setX(event.getRawX() + dX);
                    lastAction = MotionEvent.ACTION_MOVE;
                    centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), draggableResourceId));
                }
                break;

            case MotionEvent.ACTION_UP:
                // if (lastAction == MotionEvent.ACTION_DOWN)
                // Toast.makeText(TaskPackActivity.this, "Clicked!" + String.valueOf(fixPositionX), Toast.LENGTH_SHORT).show();

                centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), userInfo.getAvatarLevel(user.getAvatar(), user.getStars())));
                int width = view.getWidth();
                int height = view.getHeight();
                float x = view.getX();
                float y = view.getY();
                //call the levelviews function to check intersection
                boolean isTrue = false;
                if (turnDragOn)/// checking weather flushing animation finish or not  if true then enable dragging {sumon}
                    isTrue = levelView.checkOverLaping(x, y, width, height);

//                if (isTrue)
////                    Toast.makeText(activity, "is true called after overlapping", Toast.LENGTH_SHORT).show();
                if (isTrue) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setX(fixPositionX);
                    view.setY(fixpositionY);
                }
                break;

            default:
                return false;
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(StaticAccess.TAG_BROADCAST_RECEIVER_FOR_STAR));
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
        if (lockerHandler != null) {
            mpSoundRelease();
            lockerHandler.removeCallbacks(lockerRunable);
        }

    }

    public int lavelID = -1;

    @Override
    public void onResume() {
        super.onResume();
        if (user.getMusic() > 0) {
            BackgroundMusicService.startMusic(activity);
        }
        // avatarUpdateView = user pic and user total star show
        avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars(),
                user.getName(), userInfo.avatarDarkColor(user.getAvatar()), TaskPackActivity.this);
        rlAvatar.removeAllViews();
        avatarUpdateView.flyStar(0);
        rlAvatar.addView(avatarUpdateView);

        if (lavelID != -1 && staticInstance.getLevel() != null) {
            TaskPack level = taskPacks.get(lavelID);
            staticInstance.clearLevel();
            staticInstance.setLevel(level);
            playSoundAsset(userInfo.getAvatarWiseSound(user.getAvatar()));
        }

    }

    // ******************************** Communication between all the the activity***********************************************************////////////
    //when avatar view animation is done CALLED FROM AVATAR ********************************///////

    // this function only gets called when the called sets the listener . here we used both
    @Override
    public void avatarAnimationFininished() {
        Log.e("AnimationFininished", "called here");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //lockerHandler.postDelayed(lockerRunable, 1500);

            }
        });

    }

    Runnable lockerRunable = new Runnable() {
        @Override
        public void run() {
            if (levelView != null) {
                //call levelview to invalidate
                Log.e("lockerRunable", "called: ");
                /*levelView.enabledCircleClick(true);
                if (checkSoundPlayingAbility())
                    playUnlockSound();*/

                Log.e("lockerRunable 2nd", "called: ");
            }
        }
    };
    private LockerCustomView lockerCustomView;

    // level point percent show in center level wise
    int[] formatePoints(ArrayList<Integer> points) {
        int[] point = {0, 0, 0, 0, 0, 0};
        if (points != null) {
            for (int i = 0; i < points.size(); i++) {
                point[i] = points.get(i);
            }
        }
        return point;
    }

    /// ***************************** OVER RIDE FUNCTIONS FROM LEVEL VIEW ***********************

    // level event and set level in Instance
    @Override
    public void viewPosition(int lavelID) {
        this.lavelID = lavelID;
        if (taskPacks != null) {
            try {
//                if(taskPacks.size()>labelID-1){
                TaskPack level = taskPacks.get(lavelID);
                staticInstance.clearLevel();
                staticInstance.setLevel(level);
                playSoundAsset(userInfo.getAvatarWiseSound(user.getAvatar()));
//                }

            } catch (IndexOutOfBoundsException e) {
                CustomToast.t(activity, getResources().getString(R.string.levelMissing));
            }

        }
      /*  if (taskPacks != null) {
            TaskPack level = taskPacks.get(labelID);
            staticInstance.clearLevel();
            staticInstance.setLevel(level);
            playSoundAsset(userInfo.getAvatarWiseSound(user.getAvatar()));
        }*/
    }

    //lockerRunable calls this shit
    @Override
    public void getUnlockCircleInfo(float cx, float cy, int radius) {
        Log.e("getUnlockCircleInfo: ", "called");
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        /*  rlProgressIDStar.clearAnimation();
        rlProgressIDStar.removeAllViews();

        int deviation = (radius * 80) / 100;
        int absoulateRadius = (radius * 2) + deviation;
        lockerCustomView = new LockerCustomView(this, R.drawable.ic_sun, 0, 0, absoulateRadius);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(absoulateRadius, absoulateRadius);
        rlProgressIDStar.setLayoutParams(params);
        rlProgressIDStar.addView(lockerCustomView);
        rlProgressIDStar.setX(cx - (absoulateRadius / 2));
        rlProgressIDStar.setY(cy - (absoulateRadius / 2));
        Animanation.blinkAnim(rlProgressIDStar);
      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Animanation.lockerRotateAnimation(lockerCustomView);
            }
        }, 500);*/
    }


    // From 100% dialog the Broadcast receinver and its works ***********************************
// getting star show star dialog and execute FlyStar local broadCastReceiver
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(StaticAccess.TAG_BROADCAST_RECEIVER_KEY);
            Log.d("receiver", "Got message: " + message);
//            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            starFlyAfterPlay();

        }
    };

    // this method show Flying Star
    public void starFlyAfterPlay() {
        if (getStar > prvStar) {
//            draggableResourceId = userInfo.getAvatarDragLevel(user.getAvatar(), user.getStars());
//            centerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), userInfo.getAvatarLevel(user.getAvatar(), user.getStars())));
            if (getStar != -1)
                avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars() - getStar,
                        user.getName(), userInfo.avatarDarkColor(user.getAvatar()), TaskPackActivity.this);
            else
                avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars(),
                        user.getName(), userInfo.avatarDarkColor(user.getAvatar()), TaskPackActivity.this);
            rlAvatar.removeAllViews();
            avatarUpdateView.flyStar(getStar);
            rlAvatar.addView(avatarUpdateView);
        } else {
            //this one shows the avatar if no star change happens
//            draggableResourceId = userInfo.getAvatarDragLevel(user.getAvatar(), user.getStars());
            avatarUpdateView = new AvatarUpdateView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()), TaskPackActivity.this);
            rlAvatar.removeAllViews();
            avatarUpdateView.flyStar(0);
            rlAvatar.addView(avatarUpdateView);
        }
    }

    ////////// ******************************  SOUND SHITS  **************************************************/////////////
    String unlockSong = "unlock_sound.mp3";
    String flushingSound = "flashing.mp3";

    // Sound Play for unlock (only call it for playing unlock sound)
    private void playUnlockSound() {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(unlockSong);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mpSoundRelease();
                    mp = new MediaPlayer();
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    AssetFileDescriptor afd;
                    try {
                        mp.reset();
                        afd = getAssets().openFd(flushingSound);
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //after the greeting sound do the damn animation
                    unlockTheVieewWithSound();
                    levelView.makeUnlockDisAppear(true);
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

    // level wise sound check
    private boolean checkSoundPlayingAbility() {
        boolean hasAbility = false;
        if (points != null) {
            int[] formatPoint = formatePoints(points);
            Log.e("SoundPlayingAbility", "points: " + String.valueOf(formatPoint.length));
            for (int i = 0; i < formatPoint.length; i++) {
                if (formatPoint[i] == 0) {
                    hasAbility = true;
                }
            }
        }
        return hasAbility;
    }


    //////////////////// Control Mechanism *************************************************/////
    // where the intent came from 0=firstlayer,1=result, 2= transparent level dialog, 3=avatar update,4=from close app
    int whereItCameFrom = 0;

    // calls -- AysncTask()
    public void controller() {
        switch (whereItCameFrom) {
            case 0:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playFirstUnlockSound();
                    }
                }, 1000);
                break;

            case 1:
                //when previous Star >getStar and came from ResultActivity
                if (getResult == -1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playFirstUnlockSound();
                        }
                    }, 1000);
                }
                break;
            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playFirstUnlockSound();
                    }
                }, 1000);

                break;
            case 3:
                break;
            case 4:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playFirstUnlockSound();
                    }
                }, 1000);
                break;

        }
    }


    //the values will be updated from getUnlockCircleInfo
    float cx = 0, cy = 0;
    int radius = 0;

    public void playFirstUnlockSound() {
        levelView.enabledCircleClick(true);
        if (checkSoundPlayingAbility()) {
            // the call to unlockTheVieewWithSound is in oncomple of this sound
            Log.e("isAppClosePressed: ", String.valueOf(isAppClosePressed));
            if (isAppClosePressed) {
                UnlockViewOnAppClose();
            } else {
                playUnlockSound();
            }

        } else {
            turnDragOn = true; // if all level are played by default turn drag on {sumon}
        }

    }

    /// used for handle app close button
    private void UnlockViewOnAppClose() {
        lockerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlockTheVieewWithSound();
                levelView.makeUnlockDisAppear(true);
            }
        }, 1500);
    }

    boolean turnDragOn = false;/// for checking is flushing is on or not {sumoon}

    //for the Firstlayer transition
    public void unlockTheVieewWithSound() {
        turnDragOn = true;
        rlProgressIDStar.clearAnimation();
        rlProgressIDStar.removeAllViews();

        int deviation = (radius * 80) / 100;
        int absoulateRadius = (radius * 2) + deviation;
        lockerCustomView = new LockerCustomView(this, R.drawable.ic_sun, 0, 0, absoulateRadius);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(absoulateRadius, absoulateRadius);
        rlProgressIDStar.setLayoutParams(params);
        rlProgressIDStar.addView(lockerCustomView);
        rlProgressIDStar.setX(cx - (absoulateRadius / 2));
        rlProgressIDStar.setY(cy - (absoulateRadius / 2));

        //Animanation.blinkAnim(rlProgressIDStar);
    }


}
