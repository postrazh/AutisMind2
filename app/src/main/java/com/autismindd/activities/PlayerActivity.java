package com.autismindd.activities;

// This is the player fragment. All kind of task can be played through this activity.

//1. Items of a specific task generate as "Limbikaview" from Database
//2.

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autismindd.customui.CustomCircleStrokeAnimation;
import com.autismindd.customui.CustomVariousColorCircleAnimationView;
import com.autismindd.customui.CustomVariousColorCircleStrokeAnimationView;
import com.autismindd.customui.OnTouchCustomView;
import com.autismindd.customui.ProgressBarSurfaceView;
import com.autismindd.dao.Task;
import com.autismindd.dao.User;
import com.autismindd.listener.ErrorInterface;
import com.autismindd.manager.DatabaseManager;

import com.autismindd.player.AssistiveMode;
import com.autismindd.player.DragAndDropMode;
import com.autismindd.player.GeneralMode;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.TypeFace_MY;
import com.autismindd.utilities.UserInfo;
import com.dmk.limbikasdk.pojo.LimbikaViewItemValue;
import com.dmk.limbikasdk.views.LimbikaView;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.customui.PanelMapping;
import com.autismindd.dao.Item;
import com.autismindd.dao.TaskPack;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.utilities.ApplicationMode;
import com.autismindd.utilities.Limbika_mesurement;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.TaskType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PlayerActivity extends BaseActivity implements View.OnClickListener, PanelMapping.GETClickValue, ErrorInterface.ErrorListner {

    /*
     * 1. RT = Response Time
     * 2. DT= Duration Time
     */
    private IDatabaseManager databaseManager;
    //this is the upper layer which is only used in assistive touch
    public RelativeLayout assistiveRelLayout;
    FrameLayout flPlay;
    LimbikaView limbikaView;
    ImageButton ibtnBackPlay;
    PlayerActivity activity;


    public LinkedHashMap<Long, LimbikaView> dropTargetsMap = new LinkedHashMap<>();
    public LinkedHashMap<Long, LimbikaView> dropItemsMap = new LinkedHashMap<>();

    LinkedHashMap<Long, LimbikaView> NoItemsMap = new LinkedHashMap<>();

    //both are used in assistive and drag & drop
    HashMap<Long, ArrayList<LimbikaView>> targets_with_droapItems_map = new HashMap<>();
    public ArrayList<Long> alreadyCorrectDragandDrop = new ArrayList<>();

    //all the dropable Items are here
    LinkedHashMap<Long, Item> itemLinkedHashMap = new LinkedHashMap<>();
    // Holds view all items
    LinkedHashMap<Long, LimbikaView> itemViewHolderMap = new LinkedHashMap<>();

    //in assistive and drag and drop only the DROP ITEMS are in this list
    public ArrayList<Long> positiveResult = new ArrayList<>();
    ArrayList<Long> negativeResult = new ArrayList<>();


    //    ArrayList<TimeResponseModel> rtMelliSecArray = new ArrayList<>();
    HashMap<Integer, Long> rtMelliSecMap = new HashMap<>();
    ArrayList<Task> tasks = new ArrayList<>();
    //this one holds all the the  limbika views of a specific task whick is used to clear animation and gets reset
    //in every task
    HashMap<Long, LimbikaView> allTheViewsToClearAnimation = new HashMap<Long, LimbikaView>();
    ImageProcessing imageProcessing;
    long taskPackId;
    LinkedHashMap<Long, Item> showedByList = new LinkedHashMap<>();

    public MediaPlayer mp;

    //this collection contains all the limbika views (using this one to send target list to each limbikaview )
    HashMap<Long, LimbikaView> alltheViews = new HashMap<>();
    //this collection contains only  drop targets in Drag and drop
    ArrayList<LimbikaView> allthetargets = new ArrayList<>();
    PanelMapping mapping;
    public static int screenheight, screenwidth;
    private double density;
    public int currentTaskIndex;
    Task task;
    public LinkedHashMap<Long, Item> items;
    public boolean isBlockUser = false;
    boolean isInnerBlock = false;
    int soundTypeCount = 0;
    ErrorInterface errorInterface;
    public ErrorInterface.ErrorListner errorListner;


    public boolean error_mode_on_dragDrop = false;
    public long assistiveTargetKey = -1;
    TaskPack level;
    int dragDropcopunt = 0;
    int totalTaskSize = 0;
    public StaticInstance staticInstance;
    TaskPack taskPack;
    public User user;
    public ProgressBarSurfaceView progressOne;
    public int madatoryError = 0;
    private float cx, cy;
    private RelativeLayout rlPlayer, rlTopLayer;
    UserInfo userInfo;
    long loadTaskDTMilliSec;
    long loadTaskRTMilliSec;
    boolean isTap = false;
    long startRT;
    long endRT;
    public boolean getUserHelper = false, getUserProgressPlay = false, getUserSound = false, getUserMusic = false;
    int numberOftasks = 0;
    DragAndDropMode dragAndDropMode;
    AssistiveMode assistiveMode;

    //saves the wrong tasks so that we can move there when eevrything done
    public ArrayList<Task> erroorTasksList = new ArrayList<>();
    boolean errorTasks_MapNotEmpty = false;

    public boolean musicControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
        calculateDisplaySize();
        setContentView(R.layout.activity_player);

        rlPlayer = (RelativeLayout) findViewById(R.id.rlPlayer);
        rlTopLayer = (RelativeLayout) findViewById(R.id.rlTopLayer);

        assistiveRelLayout = (RelativeLayout) findViewById(R.id.parentRelLayout);
        flPlay = (FrameLayout) findViewById(R.id.flPlay);
        //oncreates UI related works
        mapping = new PanelMapping(PlayerActivity.this, screenheight, screenwidth, PlayerActivity.this);
        assistiveRelLayout.addView(mapping);

        staticInstance = StaticInstance.getInstance();
        userInfo = new UserInfo();
        level = staticInstance.getLevel();
        new FirstTime_Asnc().execute();
        user = staticInstance.getUser();

        getUserHelper = isNumToBol(user.getHelper());
        getUserProgressPlay = isNumToBol(user.getTaskPlayProgress());
        getUserSound = isNumToBol(user.getSoundEffect());
        getUserMusic = isNumToBol(user.getMusic());
        progressOne = (ProgressBarSurfaceView) findViewById(R.id.progressOne);


        if (user.getTaskPlayProgress() == StaticAccess.PLAYER_PROGRESS_ON) {
            taskCompletionProgress();
        } else if (user.getTaskPlayProgress() == StaticAccess.PLAYER_PROGRESS_OFF) {
            progressOne.setVisibility(View.INVISIBLE);
        }
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
    }

    public class FirstTime_Asnc extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ibtnBackPlay = (ImageButton) findViewById(R.id.ibtnBackPlay);
            ibtnBackPlay.setOnClickListener(PlayerActivity.this);
            activity = PlayerActivity.this;

            if (!ApplicationMode.devMode) {
                ibtnBackPlay.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            databaseManager = new DatabaseManager(activity);
            imageProcessing = new ImageProcessing(activity);
            errorInterface = new ErrorInterface(PlayerActivity.this);
            errorListner = errorInterface.getListener();
            if (getIntent().getExtras() != null) {
                currentTaskIndex = getIntent().getIntExtra("position", 0);
                taskPack = databaseManager.getTaskPackById(level.getId());
                tasks = (ArrayList<Task>) databaseManager.listTasksByTAskPackId(level.getId());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberOftasks = calculateNumberOfTastksInTotal(tasks);
                        totalTaskSize = numberOftasks;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadTask(currentTaskIndex);
                            }
                        },100);

                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadTaskDTMilliSec = System.currentTimeMillis();
        }
    }

    public int calculateNumberOfTastksInTotal(ArrayList<Task> list) {
        int res = 0;
        if (list != null) {
            for (Task t : list) {
                if (t.getTutorial() < 1) {
                    res++;
                }
            }
        }
        return res;
    }

    int transionTime = 0;
    boolean taskIsTutorial = false;

    Handler transitionHandler = new Handler();
    Runnable transitionRunnable = new Runnable() {
        @Override
        public void run() {
            gotoNextTask(currentTaskIndex);
        }
    };

    public void startTransitionTime(int timeSec) {
        transitionHandler.postDelayed(transitionRunnable, timeSec * 1000);
    }

    public void finishtheActivity() {
        if (transitionHandler != null) {
            transitionHandler.removeCallbacks(transitionRunnable);
        }
        musicControl = true;
        finish();
    }


    public void initializeVariableForEachTask() {

        isTap = true;
        //mir // FIXME: 31/08/2016 clearing all the hasmaps with each task
        assistiveRelLayout.removeAllViews();
        if (mapping != null)
            assistiveRelLayout.addView(mapping);
        dropTargetsMap.clear();
        dropItemsMap.clear();
        itemLinkedHashMap.clear();
        NoItemsMap.clear();
        itemViewHolderMap.clear();
        negativeResult.clear();
        error_mode_on_dragDrop = false;
        error_mode_on_General = false;
        assistiveTargetKey = -1;
        alltheViews.clear();

        targets_with_droapItems_map.clear();
        alreadyCorrectDragandDrop.clear();//to calculate already correct dropitems

        //clearing assistive touch collection
        measurement_list.clear();
        limbikaview_list.clear();
        items = new LinkedHashMap<>();

        //clear scale animations
        positiveObjectanimators.clear();
    }

    //Load Items according to task
    public void loadTask(final int currentTaskIndex) {
        //Global object thats why need to clear item task

        Animanation.zoomInTransition(flPlay);
        if (errorTasks_MapNotEmpty) {
            //if there where error start playing the errors now Suckers
            this.currentTaskIndex = 0;
            errorTasks_MapNotEmpty = false;
            tasks = (ArrayList<Task>) erroorTasksList.clone();
            erroorTasksList.clear();
            task = tasks.get(this.currentTaskIndex);
        } else {
            this.currentTaskIndex = currentTaskIndex;
            task = tasks.get(currentTaskIndex);
        }
        initializeVariableForEachTask();

        madatoryError = task.getErrorMandatoryScreen();
        //handling tutorial and transition
        if (transitionHandler != null) {
            transitionHandler.removeCallbacks(transitionRunnable);
        }

        if (task.getTutorial() > 0) {
            //check if it has a transition value
            if (task.getTransition() > 0) {
                transionTime = task.getTransition();
                startTransitionTime(transionTime);
            } else {
                transionTime = 0;
            }
            taskIsTutorial = true;
        } else {
            taskIsTutorial = false;
            transionTime = 0;
            startRT = System.currentTimeMillis();
        }
        if (task != null) {
            flPlay.setBackgroundColor(task.getBackgroundColor());
            items = databaseManager.loadTaskWiseItem(task);

            if (items != null) {

                for (final Map.Entry<Long, Item> itemValue : items.entrySet()) {
                    final Item item = itemValue.getValue();
                    limbikaView = new LimbikaView(this, item.getKey(), true);
                    alltheViews.put(item.getKey(), limbikaView);

                    //if helper no animation pls
                    if (item.getHelper() == 0)
                        limbikaView.startOnTapAnimation(taskPack.getItemOfAnimation());

                    LimbikaViewItemValue limbikaViewItemValue = new LimbikaViewItemValue();
                    limbikaViewItemValue.setX(item.getX());
                    limbikaViewItemValue.setY(item.getY());
                    limbikaViewItemValue.setRotation(item.getRotation());
                    limbikaViewItemValue.setKey(item.getKey());
                    limbikaViewItemValue.setIsCircleView(item.getIsCircleView());
                    limbikaViewItemValue.setCircleColor(item.getCircleColor());
                    limbikaViewItemValue.setUserText(item.getUserText());
                    limbikaViewItemValue.setTextColor(item.getTextColor());
                    limbikaViewItemValue.setTextSize(item.getTextSize());
                    limbikaViewItemValue.setBorderColor(item.getBorderColor());
                    limbikaViewItemValue.setBackgroundColor(item.getBackgroundColor());
                    limbikaViewItemValue.setDrawable(item.getDrawable());
                    limbikaViewItemValue.setWidth(item.getWidth());
                    limbikaViewItemValue.setHeight(item.getHeight());
                    limbikaViewItemValue.setLeft(item.getLeft());
                    limbikaViewItemValue.setRight(item.getRight());
                    limbikaViewItemValue.setTop(item.getTop());
                    limbikaViewItemValue.setBottom(item.getBottom());
                    limbikaViewItemValue.setImagePath(item.getImagePath());
                    limbikaViewItemValue.setCornerRound(item.getCornerRound());
                    limbikaViewItemValue.setFontTypeFace(item.getFontTypeFace());
                    limbikaViewItemValue.setFontAlign(item.getFontAlign());
                    limbikaViewItemValue.setBorderPixel(item.getBorderPixel());

                    //this part is where u decide to show the upper layer or not
                    if (task.getType().equals(TaskType.Assistive)) {
                        assistiveRelLayout.setVisibility(View.VISIBLE);
                    } else {
                        assistiveRelLayout.setVisibility(View.GONE);
                    }
                    if (taskIsTutorial && item.getTutorialAnimation() > -1) {
                        int animation = item.getTutorialAnimation();
                        if (animation == 1) {
                            //without reverse
                            int valueX = item.getTutorialX();
                            int valueY = item.getTutorialY();
                            Animanation.tutorialWithOutReverse(limbikaView, valueX, valueY);
                        } else if (animation == 2) {
                            //with reverse
                            int valueX = item.getTutorialX();
                            int valueY = item.getTutorialY();
                            Animanation.tutorialWithReverse(limbikaView, valueX, valueY);
                        }
                    }

/////////////////////////////***************** GENERAL MODE****************************//////////////////////
                    // Checking Result for general types of tasks that accept only True/False
                    if (task.getType().equals(TaskType.Normal)) {
                        limbikaView.setEnabled(false);
                        if (item.getResult().equals(TaskType.NORMAL_TRUE))
                            positiveResult.add(item.getKey());
                        else if (item.getResult().equals(TaskType.NORMAL_FALSE))
                            negativeResult.add(item.getKey());

                        limbikaView.setSingleTapListener(new LimbikaView.SingleTapListener() {
                            @Override
                            public void onSingleTap(View v, LimbikaViewItemValue limbikaViewItemValue) {

                                //when u come back from error screen this will remove continuous animation
                                if (v != null)
                                    v.clearAnimation();
                                Item item1 = items.get(limbikaViewItemValue.getKey());
                                if (item1 != null) {
                                    GeneralMode generalMode = new GeneralMode(activity, task, item1);
                                    generalMode.generalMode(v, limbikaViewItemValue);
                                }
                            }
                        });

                        //////////***********************END OF GENERAL MODE**********************/////////////
                    }

                    //////////////////************************  DRAG & DROP Task**************************//
                    else if (task.getType().equals(TaskType.DragDrop)) {
                        limbikaView.hideBalls();

                        dragAndDropMode = new DragAndDropMode(activity, task);
                        limbikaView.setSingleTapListener(new LimbikaView.SingleTapListener() {
                            @Override
                            public void onSingleTap(View v, LimbikaViewItemValue limbikaViewItemValue) {

                                Item item1 = items.get(limbikaViewItemValue.getKey());
                                dragAndDropMode.singleTap(item1);
                            }
                        });

                        //Drop Target
                        if (item.getAllowDragDrop() == 1) {
                            limbikaView.setDraggable(false);
                            //gets the targets
                            dropTargetsMap.put(item.getKey(), limbikaView);
                        } //no need to set all the listeners here we will find it from collection and call methods
                        else if (item.getDragDropTarget() == 0 && item.getAllowDragDrop() == 0) {
                            limbikaView.setDraggable(false);
                            NoItemsMap.put(item.getKey(), limbikaView);
                        }

                        //Drop Items so it will move
                        else {
                            limbikaView.setDraggable(true);
                            limbikaView.setChildView(true);
                            dropItemsMap.put(item.getKey(), limbikaView);
                            itemLinkedHashMap.put(item.getKey(), item);
                            if (item.getDragDropTarget() != 0) {
                                positiveResult.add(item.getKey());
                            }
                            limbikaView.setDropTargetListener(new LimbikaView.DropTargetListener() {
                                @Override
                                public void onViewDropped(LimbikaView view, Long dropTargetKey) {
                                    dragAndDropMode.onDropTarget(view, dropTargetKey);
                                }

                                @Override
                                public void onViewDroppedonWrongTarge(LimbikaView view, Long dropTargetKey) {
                                    dragAndDropMode.onDroppedonWrongTarge(view, dropTargetKey);
                                }
                            });
                        }

                        /////////////////////////DRAG &DROP LISTENER DONE *************/////////////////////
                        //end of DRAG & DROP view
                    }

                    //***************** Checking ASSISTIVE types of Task*********************//////////////
                    else if (task.getType().equals(TaskType.Assistive)) {
                        limbikaView.hideBalls();
                        assistiveMode = new AssistiveMode(activity, task);
                        //Drop Target
                        if (item.getAllowDragDrop() == 1) {
                            limbikaView.setDraggable(false);
                            //gets the targets
                            dropTargetsMap.put(item.getKey(), limbikaView);
                        }
                        //no need to set all the listeners here we will find it from collection and call methods
                        else if (item.getDragDropTarget() == 0 && item.getAllowDragDrop() == 0) {
                            limbikaView.setDraggable(false);
                            NoItemsMap.put(item.getKey(), limbikaView);
                        }

                        //Drop Items so it will move
                        else if (item.getAllowDragDrop() == 0) {
                            limbikaView.setDraggable(true);
                            limbikaView.setChildView(true);
                            dropItemsMap.put(item.getKey(), limbikaView);
                            itemLinkedHashMap.put(item.getKey(), item);
                            if (item.getDragDropTarget() != 0) {
                                positiveResult.add(item.getKey());
                            }
                            limbikaView.setDropTargetListener(new LimbikaView.DropTargetListener() {
                                @Override
                                public void onViewDropped(LimbikaView view, Long dropTargetKey) {

                                }
                                @Override
                                public void onViewDroppedonWrongTarge(LimbikaView view, Long dropTargetKey) {

                                }
                            });

                        }
                        /////////////////////////Assistive LISTENER DONE *************/////////////////////
                        // Assistive mode unnecessary  commented by reaz

                    }

                    limbikaView.onResume(limbikaViewItemValue);

                    //setting measurement to calculate from upperlayer touch
                    if (task.getType().equals(TaskType.Assistive))
                        if (limbikaViewItemValue != null) {
                            Limbika_mesurement measurement = new Limbika_mesurement(limbikaView.getX()
                                    , limbikaView.getY(), limbikaView.canvasWidth,
                                    limbikaView.canvasHeight, limbikaView.getKey());
                            measurement_list.add(measurement);
                            limbikaview_list.put(limbikaViewItemValue.getKey(), limbikaView);
                        }


                    limbikaView.setLayoutParams(new FrameLayout.LayoutParams(500, 500));
                    flPlay.addView(limbikaView);

                    itemViewHolderMap.put(item.getKey(), limbikaView);
                    allTheViewsToClearAnimation.put(limbikaViewItemValue.getKey(), limbikaView);


                    setDropTarget();
                    // manageAlignment();
                    if (limbikaViewItemValue.getUserText().length() > 0) {
                        setFontType(limbikaViewItemValue.getFontTypeFace(), limbikaView);
                    }
                    // if sound delay and auto play  exists that time it only execute sound delay
                    if (item.getAutoPlay() > 0 && item.getItemSound().length() > 0) {
                        // if sound play after  few sec later check here
                        if (item.getSoundDelay() > 0 && item.getItemSound().length() > 0) {
                            if (activity.getUserSound) {
                                //check if helper is on checking needed otherwise move on
                                if (item.getHelper() > 0) {
                                    if (activity.getUserHelper)
                                        delaySoundPlay(item.getSoundDelay(), item.getItemSound());

                                } else {
                                    delaySoundPlay(item.getSoundDelay(), item.getItemSound());
                                }
                            }


                        } else {
                            if (activity.getUserSound) {
                                //check if helper is on checking needed otherwise move on
                                if (item.getHelper() > 0) {
                                    if (activity.getUserHelper)
                                        playSound(item.getItemSound());

                                } else {
                                    playSound(item.getItemSound());
                                }
                            }


                        }

                    }
                    if (item.getShowedBy() > 0) {
                        limbikaView.setVisibility(View.INVISIBLE);
                    }

                }
                //end of loop
                //this sets all the targets to each limbika view
                setDropTargetMap();
                //hideItems();
            }
        }

    }




    public void playSoundDragAndAssistive(final Task task, String sound) {
//  isBlockUser is true because single tap need to block  and after complete the sound isBlockUser is false
        if (!sound.equals("")) {
            try {
                isBlockUser = true;
                mpSoundRelease();
                mp = new MediaPlayer();
                mp.setLooping(false);
                mp.setDataSource(Environment.getExternalStorageDirectory() +
                        "/Android/Data/" + activity.getPackageName() + "/Sound/" + sound);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        isBlockUser = false;
                        if (positiveResult.size() == 0) {
                            if (task.getFeedbackImage().length() > 0) {
                                delayfeedBack(task.getFeedbackImage(), task.getFeedbackAnimation(), currentTaskIndex, task.getFeedbackSound(), task.getFeedbackType());
                            } else {
                                delayGotoNextTask(currentTaskIndex);
                            }
                        }

                    }
                });
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            isBlockUser = false;
            if (positiveResult.size() == 0) {
                if (task.getFeedbackImage().length() > 0) {
                    delayfeedBack(task.getFeedbackImage(), task.getFeedbackAnimation(), currentTaskIndex, task.getFeedbackSound(), task.getFeedbackType());
                } else {
                    delayGotoNextTask(currentTaskIndex);
                }
            }
        }

    }

    //Setting Drop Target
    private void setDropTarget() {
        // Setting Targets to the Items
        for (Map.Entry<Long, LimbikaView> itemValue : dropItemsMap.entrySet()) {
            Item item = itemLinkedHashMap.get(itemValue.getKey());
            LimbikaView dropItem = itemValue.getValue();
            if (item != null)
                if (dropTargetsMap.containsKey(item.getDragDropTarget())) {
                    LimbikaView targetView = dropTargetsMap.get(item.getDragDropTarget());
                    itemValue.getValue().setDropTarget(targetView);
                    itemValue.getValue().setTargetKey(targetView.getLimbikaViewItemValue().getKey());

                    //each drop target now has its assosiative child views in targets_with_droapItems_map
                    ArrayList<LimbikaView> savedViews = null;
                    if (targets_with_droapItems_map.get(item.getDragDropTarget()) != null)
                        savedViews = (ArrayList<LimbikaView>)
                                targets_with_droapItems_map.get(item.getDragDropTarget()).clone();
                    if (savedViews == null) {
                        savedViews = new ArrayList<>();
                        savedViews.add(dropItem);
                    } else {
                        savedViews.add(dropItem);
                    }
                    targets_with_droapItems_map.put(item.getDragDropTarget(), savedViews);
                }
        }
    }

    //set Drop target map this saves all the drop targets to each limbika view
    private void setDropTargetMap() {
        // Setting Targets to the Items
        for (Map.Entry<Long, LimbikaView> itemValue : dropItemsMap.entrySet()) {
            itemValue.getValue().setDropTargetsMap(dropTargetsMap);
        }
    }


    //this will take you to result activity
    private void taskfinishedNowGotoNextActivity() {
        long totalResponseTime = 0;
        for (Map.Entry<Integer, Long> entry : rtMelliSecMap.entrySet()) {
            int index = entry.getKey();
            totalResponseTime = totalResponseTime + rtMelliSecMap.get(index);
        }
        loadTaskRTMilliSec = (totalResponseTime / totalTaskSize); // replace tasks.size
        musicControl = true;
        Intent intent = new Intent(PlayerActivity.this, ResultActivity.class);
        intent.putExtra(StaticAccess.TAG_FINAL_TASK_ID, totalTaskSize);// replace tasks.size
        intent.putExtra(StaticAccess.TAG_DT_MILL_SEC, loadTaskDTMilliSec);
        intent.putExtra(StaticAccess.TAG_RT_MILL_SEC, loadTaskRTMilliSec);
        startActivity(intent);
        finishtheActivity();
    }

    // Move to next Task when current one is completed
    private void gotoNextTask(int currentTaskIndex) {
        //this currenttaskindex is local not global
        currentTaskIndex++;
        mpSoundRelease();
        if (currentTaskIndex == tasks.size()) {

            if (erroorTasksList.size() > 0) {
                errorTasks_MapNotEmpty = true;
            }

            //when there is no wrong tasks finsih it
            if (!errorTasks_MapNotEmpty) {
                if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                    //didnt handle mode by mode rather checked both
                    if (!error_mode_on_General && !error_mode_on_dragDrop)
                        progressOne.setProgressInPercent(progressReport(true)); /// change by sumon
                }
                taskfinishedNowGotoNextActivity();
            } else {
                //this block will be executed when there is wrong tasks
                allTheViewsToClearAnimation.clear();
                flPlay.removeAllViews();
                if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                    //didnt handle mode by mode rather checked both
                    if (!error_mode_on_General && !error_mode_on_dragDrop)
                        progressOne.setProgressInPercent(progressReport(true)); /// change by sumon
                }
                loadTask(currentTaskIndex);
            }

        } else {
            //if animation is there its time to clear it
            for (Map.Entry<Long, LimbikaView> entry : allTheViewsToClearAnimation.entrySet()) {
                long key = entry.getKey();
                LimbikaView v = entry.getValue();
                if (v != null)
                    v.clearAnimation();
            }


            allTheViewsToClearAnimation.clear();
            flPlay.removeAllViews();
            if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                if (!error_mode_on_General && !error_mode_on_dragDrop)
                    progressOne.setProgressInPercent(progressReport(true)); /// change by sumon
            }
            loadTask(currentTaskIndex);
        }

    }

    public void clearErrorPositiveAnimation() {
        //if animation is there its time to clear it
        for(ObjectAnimator scale: positiveObjectanimators){
            if(scale!=null)
                scale.cancel();
        }
    }

    // Move to specific task
    public void gotoSpecificTask(long unidId) {
        mpSoundRelease();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getUniqId() == unidId) {
                flPlay.removeAllViews();
                loadTask(i);
            }
        }
    }


    // Displaying Feedback image in dialog
    private void displayRectangularFeedback(String imagePath, int animation, final int currentTaskIndex, String soundClip) {
        musicControl = true;
        Intent dialogIntent = new Intent(this, TransparentFeedbackActivity.class);
        dialogIntent.putExtra(TransparentFeedbackActivity.IMAGE_PATH, imagePath);
        dialogIntent.putExtra(TransparentFeedbackActivity.SOUND_PATH, soundClip);
        dialogIntent.putExtra(TransparentFeedbackActivity.ANIMATION, animation);
        startActivityForResult(dialogIntent, StaticAccess.TAG_DIALOG);
    }


    //Opening an external application
    public void openApp(String packageName) {
        musicControl = true;
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (LaunchIntent != null)
            startActivity(LaunchIntent);
    }

    //Show Animation
    public void showAnimation(View view, int type) {
        switch (type) {
            case Animanation.shake1:
                Animanation.shakeAnimation(view);
                break;
            case Animanation.shake2:
                Animanation.shakeAnimation2(view);
                break;
            case Animanation.blink1:
                Animanation.blink(view);
                break;
            case Animanation.slideTopToBottom:
                Animanation.topToBottom(view);
                break;
            case Animanation.slideBottomToTop:
                Animanation.bottomToTop(view);
                break;
            case Animanation.slideLeftToRight:
                Animanation.slideLeftToRight(view);
                break;
            case Animanation.slideRightToLeft:
                Animanation.slideRightToLeft(view);
                break;
            case Animanation.wiggleAnimation:
                Animanation.wiggleAnimation(view);
                break;


        }
    }

    //created by Reaz
    public void openWebLink(String Url) {

        //limbikaView need to Check This null
        if (Url != null || Url.length() != 0) {
            musicControl = true;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
            startActivity(browserIntent);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnBackPlay:
                Animanation.zoomOut(ibtnBackPlay);
                dialogBackPermission();
                break;
        }
    }

    // dialog for BackPermission created by Rokan
    public void dialogBackPermission() {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_back_permission);
        dialog.setCancelable(false);

        final TextView tvBackPermission = (TextView) dialog.findViewById(R.id.tvBackPermission);
        tvBackPermission.setText(getResources().getText(R.string.BackPermission));

        Button btnCancelPermission = (Button) dialog.findViewById(R.id.btnCancelPermission);
        Button btnOkPermission = (Button) dialog.findViewById(R.id.btnOkPermission);

        btnCancelPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                musicControl = true;
                Intent intent = new Intent(activity, TaskPackActivity.class);
                startActivity(intent);
                finishtheActivity();

                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(this, dialog);


    }

    // set Font Type add by reaz
    public void setFontType(int fontType, LimbikaView limbikaView) {

        switch (fontType) {
            case StaticAccess.TAG_TYPE_NORMAL:
                limbikaView.setCustomTypeFace(TypeFace_MY.getRoboto(activity));
                break;

            case StaticAccess.TAG_TYPE_FACE_DANCING:
                limbikaView.setCustomTypeFace(TypeFace_MY.getDancing(activity));

                break;
            case StaticAccess.TAG_TYPE_FACE_ROBOTO_THIN:

                limbikaView.setCustomTypeFace(TypeFace_MY.getRobotoThin(activity));
                break;
            case StaticAccess.TAG_TYPE_FACE_ROAD_BRUSH:

                limbikaView.setCustomTypeFace(TypeFace_MY.getRoadBrush(activity));
                break;
            case StaticAccess.TAG_TYPE_FACE_ROBOTO_CONDENSED:

                limbikaView.setCustomTypeFace(TypeFace_MY.getRoboto_condensed(activity));
                break;

            case StaticAccess.TAG_TYPE_FACE_RANCHO:

                limbikaView.setCustomTypeFace(TypeFace_MY.getRancho(activity));
                break;
        }

        limbikaView.setLimbikaView(limbikaView);
        limbikaView.saveViewState();
    }

    // for delay sound in FeedBack Dialog by reaz
    public void delayfeedBack(final String imagePath, final int animation, final int currentTaskIndex, final String soundClip, final int feedBackType) {
        int pos = 0;
        if (mp != null) {
            pos = mp.getDuration() - mp.getCurrentPosition();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                mpSoundRelease();
                if (feedBackType == StaticAccess.TAG_TYPE_CIRCULAR) {
                    displayCircularFeedback(imagePath, animation, currentTaskIndex, soundClip);
                } else {
                    displayRectangularFeedback(imagePath, animation, currentTaskIndex, soundClip);
                }

            }
        }, pos);
    }

    private void displayCircularFeedback(String imagePath, int animation, final int currentTaskIndex, String soundClip) {
        musicControl = true;
        Intent dialogIntent = new Intent(this, TransparentFeedbackActivity.class);
        dialogIntent.putExtra(TransparentFeedbackActivity.IMAGE_PATH, imagePath);
        dialogIntent.putExtra(TransparentFeedbackActivity.SOUND_PATH, soundClip);
        dialogIntent.putExtra(TransparentFeedbackActivity.ANIMATION, animation);
        startActivityForResult(dialogIntent, StaticAccess.TAG_DIALOG);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpSoundRelease();

    }

    //calculating dispaly
    public void calculateDisplaySize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;
        density = getDensity(metrics.density);
        double x = Math.pow(metrics.widthPixels / density, 2);
        double y = Math.pow(metrics.heightPixels / density, 2);
        double screenInches = Math.sqrt(x + y);
    }

    // calculating density
    public double getDensity(double density) {

        double densityy = density * 160;

        return densityy;

    }



    public boolean isInNoItems(LimbikaView v) {
        boolean answer = false;
        for (Map.Entry<Long, LimbikaView> itemValue : NoItemsMap.entrySet()) {
            if (itemValue.getValue().getLimbikaViewItemValue().getKey() == v.getLimbikaViewItemValue().getKey())
                answer = true;
        }
        return answer;
    }

    public boolean isInDropItems(LimbikaView v) {
        boolean answer = false;
        for (Map.Entry<Long, LimbikaView> itemValue : dropItemsMap.entrySet()) {
            if (itemValue.getValue().getLimbikaViewItemValue().getKey() == v.getLimbikaViewItemValue().getKey())
                answer = true;
        }
        return answer;
    }

    ///////////////////***************** ASSISTIVE TOUCH starts here **********////////////////////////////////
    //collection needed for measurement
    public ArrayList<Limbika_mesurement> measurement_list = new ArrayList<Limbika_mesurement>();
    public HashMap<Long, LimbikaView> limbikaview_list = new HashMap<Long, LimbikaView>();
    //overrride method from GETClickValue
    public LimbikaView startView = null;

    @Override
    public void startXYPanelPoint(float x, float y) {
        if(assistiveMode!=null)
            assistiveMode.startXYPanelPoint(x,y);
    }

    @Override
    public void endXYPanelPoint(float x, float y) {
        if(assistiveMode!=null)
            assistiveMode.endXYPanelPoint(x,y);
    }

    @Override
    public void singleTap(float x, float y) {
        if(assistiveMode!=null)
            assistiveMode.singleTap(x,y);
    }

    @Override
    public void doubleTap(float x, float y) {

    }

    ///////////////////***************** ASSISTIVE TOUCH ends here **********////////////////////////////////

    public void delaySoundPlay(int delay, final String play) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playSound(play);
            }
        }, delay * 1000);
    }

    // sound release created by reaz
    public void mpSoundRelease() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    // after 1 sec task will going to be next  Task created by reaz
    public void delayGotoNextTask(final int currentTaskIndex) {
        int pos = 0;
        if (mp != null) {
            pos = mp.getDuration() - mp.getCurrentPosition();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                gotoNextTask(currentTaskIndex);

            }
        }, pos);

    }


    // for Sound play method to method call Created by reaz
    private void playSound(String clipName, final Item item, final boolean noItem) {

        if (clipName == null) {
            soundTypeCount++;
            SingleTapTask(item, soundTypeCount, true, noItem);
        } else if (clipName.length() > 0) {
            mpSoundRelease();
            mp = new MediaPlayer();
            try {
                mp.setDataSource(Environment.getExternalStorageDirectory() + StaticAccess.ANDROID_DATA + activity.getPackageName() + StaticAccess.ANDROID_DATA_PACKAGE_SOUND + clipName);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundTypeCount++;
                        SingleTapTask(item, soundTypeCount, true, noItem);

                    }

                });
//                }

                mp.prepare();
                mp.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Playing sound
    public void playSound(String clipName) {
        if (clipName.length() > 0) {
            mpSoundRelease();
            mp = new MediaPlayer();
            try {
                mp.setDataSource(Environment.getExternalStorageDirectory() + "/Android/Data/" + activity.getPackageName() + "/Sound/" + clipName);
                mp.prepare();
                mp.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    boolean error_mode_on_General = false;

    @Override
    public void onErrorListner(Task task, int taskMode) {
//  go to error Screen Created By reaz
        musicControl = true;
        error_mode_on_General = true;
        Intent intSuperError = new Intent(activity, SuperErrorActivity.class);
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_COLOR, task.getErrorBgColor());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_TEXT, task.getErrortext());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_IMAGE, task.getErrorImage());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_SOUND, task.getNegativeSound());
        intSuperError.putExtra(StaticAccess.TAG_TASK_MODE_KEY, task.getTutorial());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_TASK_ID, currentTaskIndex);
        if (taskMode == StaticAccess.TAG_TASK_GENERAL_MODE) {
            intSuperError.putExtra(StaticAccess.TAG_TASK_MODE_KEY, StaticAccess.TAG_TASK_GENERAL_MODE);
            startActivityForResult(intSuperError, StaticAccess.TAG_TASK_GENERAL_MODE);

            erroorTasksList.add(task);
        }

    }

    @Override
    public void onErrorListner(Task task, int taskMode, long targetKey) {
        //  go to error Screen Created By reaz
        musicControl = true;
        Intent intSuperError = new Intent(activity, SuperErrorActivity.class);
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_COLOR, task.getErrorBgColor());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_TEXT, task.getErrortext());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_IMAGE, task.getErrorImage());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_SOUND, task.getNegativeSound());
        intSuperError.putExtra(StaticAccess.TAG_TASK_MODE_KEY, task.getTutorial());
        intSuperError.putExtra(StaticAccess.TAG_PLAY_ERROR_TASK_ID, currentTaskIndex);

        error_mode_on_dragDrop = true;

        if (taskMode == StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE) {
            intSuperError.putExtra(StaticAccess.TAG_TASK_MODE_KEY, StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE);
            intSuperError.putExtra(StaticAccess.TARGET_KEY, targetKey);
            startActivityForResult(intSuperError, StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE);

            erroorTasksList.add(task);

        } else if (taskMode == StaticAccess.TAG_TASK_ASSISTIVE_MODE) {
            intSuperError.putExtra(StaticAccess.TAG_TASK_MODE_KEY, StaticAccess.TAG_TASK_ASSISTIVE_MODE);
            intSuperError.putExtra(StaticAccess.TARGET_KEY, targetKey);
            startActivityForResult(intSuperError, StaticAccess.TAG_TASK_ASSISTIVE_MODE);

            erroorTasksList.add(task);
        }

    }

    // block only wrong View  created Mir
    void blockAllWrongViews() {
        for (long key : negativeResult) {
            LimbikaView v = allTheViewsToClearAnimation.get(key);
            if (v != null)
                v.setSingleTapListener(null);
        }
    }

    ArrayList<ObjectAnimator> positiveObjectanimators=new ArrayList<>();
    // block only wrong View  created Reaz
    public void allPositiveAnimationViews() {
        for (long key : positiveResult) {
            LimbikaView v = allTheViewsToClearAnimation.get(key);
            if (v != null) {
               // Animanation.blinkAnim(v);
                positiveObjectanimators.add(Animanation.zoomInZoomOutPositive(v)); //blinkAnim
               //positiveObjectanimators.add(Animanation.blinkAnim(v));

               // positiveObjectanimators.add(Animanation.blinkAnim(v));
            }
        }
    }
    //for drag and drop
    public void allPositiveAnimationViews(ArrayList<LimbikaView> list) {
        for (LimbikaView v : list) {

            if (v != null) {
               positiveObjectanimators.add(Animanation.zoomInZoomOutPositive(v));
               // Animanation.blinkAnim(v);
                if (!ifViewAlreadyCorrect(v.getKey())) {
                    v.setDraggable(true);
                }
            }
        }
    }

    //checking if the drop item is already played and it is correct and on target
    boolean ifViewAlreadyCorrect(long key) {
        boolean res = false;
        for (long keys : alreadyCorrectDragandDrop) {
            if (keys == key)
                res = true;
        }
        return res;
    }



    // set everyview to dragable false
    void makeAllViewsNotDragabble() {

        for (Map.Entry<Long, LimbikaView> itemValue : dropItemsMap.entrySet()) {
            LimbikaView v = itemValue.getValue();
            v.setDraggable(false);
        }
    }

    // making all views enable except the already right(which means already played) ones
    //this basically end of error mode for both assistive and drag & drop
    public void makeAllViewsDragable() {
        //unblockViews
        //this variable is only used only in asssistive mode as no
        assistiveTargetKey = -1;
        error_mode_on_dragDrop = false;

        for (Map.Entry<Long, LimbikaView> itemValue : dropItemsMap.entrySet()) {
            LimbikaView v = itemValue.getValue();
            if (!ifViewAlreadyCorrect(v.getKey()))
                v.setDraggable(true);
        }

        //if animation is there its time to clear it
        for (Map.Entry<Long, LimbikaView> entry : allTheViewsToClearAnimation.entrySet()) {
            long key = entry.getKey();
            LimbikaView v = entry.getValue();
            if (v != null)
                v.clearAnimation();
        }
        clearErrorPositiveAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this is needed otherwise we will make it true and it wont work
        musicControl=false;
        if (requestCode == StaticAccess.TAG_TASK_GENERAL_MODE) {
            if (null != data) {
                // fetch the message String
                String message = data.getStringExtra(StaticAccess.TAG_PLAY_ERROR_RESPONSE);

                // Set the message string in textView
                if (message.equals(StaticAccess.TAG_PLAY_ERROR_RESPONSE)) {
                    blockAllWrongViews();
                    allPositiveAnimationViews();
                    if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                        progressOne.setProgressInPercent(progressReport(false)); /// change by sumon
                    }

                } else {

                }
            }
        } else if (requestCode == StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE) {
            if (null != data) {
                // fetch the message String
                String message = data.getStringExtra(StaticAccess.TAG_PLAY_ERROR_RESPONSE);
                long target = data.getLongExtra(StaticAccess.TARGET_KEY, -1);

                if (target != -1) {
                    if (!ifTargetTotallyPlayed(target))
                        makeAllViewsNotDragabble();

                    allPositiveAnimationViews(targets_with_droapItems_map.get(target));
                    if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {

                        progressOne.setProgressInPercent(progressReport(false)); /// change by sumon
                    }
                }

            }
        } else if (requestCode == StaticAccess.TAG_TASK_ASSISTIVE_MODE) {
            if (null != data) {
                // fetch the message String
                String message = data.getStringExtra(StaticAccess.TAG_PLAY_ERROR_RESPONSE);
                long target = data.getLongExtra(StaticAccess.TARGET_KEY, -1);

                if (target != -1) {
                    if (!ifTargetTotallyPlayed(target))
                        makeAllViewsNotDragabble();
                    //refrshing the key (no need)
                    assistiveTargetKey = target;
                    LimbikaView v= dropTargetsMap.get(assistiveTargetKey);
                    if(v!=null)
                        positiveObjectanimators.add(Animanation.zoomInZoomOutPositive(v));
                        //Animanation.blinkAnim(v);

                    allPositiveAnimationViews(targets_with_droapItems_map.get(target));
                    if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {

                        progressOne.setProgressInPercent(progressReport(false)); /// change by sumon
                    }
                }

            }
        }
        //this part is for dialog
        else if (requestCode == StaticAccess.TAG_DIALOG) {
            mpSoundRelease();
            delayGotoNextTask(currentTaskIndex);
        }
    }

    // should we unblock all the views after blocking in error (it basically checks this target is totally played or not)
    public boolean ifTargetTotallyPlayed(long target) {
        boolean res = true;

        ArrayList<LimbikaView> list = targets_with_droapItems_map.get(target);

        if (list != null)
            for (LimbikaView v : list) {
                if (!ifViewAlreadyCorrect(v.getKey()))
                    res = false;
                break;
            }


        return res;
    }

    //will be called from the listeners of drag item
    public void showHideDragandDrop(Item item1) {
        //get showed by list and show them
        if (item1.getShowedByTarget() != null && !item1.getShowedByTarget().equals("")) {
            String str = item1.getShowedByTarget();
            ArrayList<Long> list = formatList(str);
            showItemsFromShowedby(list);
        }
        //get hidden by list and show them
        if (item1.getHiddenByTarget() != null && !item1.getHiddenByTarget().equals("")) {
            String str = item1.getHiddenByTarget();
            ArrayList<Long> list = formatList(str);
            hideItemsFromShowedby(list);
        }
    }

    public void showItemsFromShowedby(ArrayList<Long> list) {
        for (long key : list) {
            LimbikaView v = alltheViews.get(key);
            if (v != null)
                v.setVisibility(View.VISIBLE);
        }
    }

    public void hideItemsFromShowedby(ArrayList<Long> list) {
        for (long key : list) {
            LimbikaView v = alltheViews.get(key);
            if (v != null) {
                v.setVisibility(View.INVISIBLE);
                v.clearAnimation();
            }
        }
    }

    ///convert a string to Array list of long for showed and hidden targets
    public ArrayList<Long> formatList(String commaSeparatedString) {
        ArrayList<Long> list = new ArrayList<>();
        String[] ar = commaSeparatedString.split(",");
        List<String> listStr = Arrays.asList(ar);
        for (String str : listStr) {
            long k = Long.parseLong(str);
            list.add(k);
        }
        return list;
    }

    // close app,Sound, navigate to, open App, open url all implement single tap  created by reaz
    //this function gives handle to a sound function which calls this function again for smooth transition
    // the process is to play the sound and after it finishes do the other tasks
    public void SingleTapTask(Item item, int soundTypeCount, boolean blockuser, boolean noItem) {
        if (blockuser) {
            this.soundTypeCount = soundTypeCount;
            //dont play any sound when sound is off


            switch (soundTypeCount) {
                case StaticAccess.TAG_ITEM_SOUND_PLAY:
                    if (item.getItemSound().length() > 0 && item.getItemSound() != null) {
                        //check sound effect
                        if (getUserSound) {
                            //check if helper is on checking needed otherwise move on
                            if (item.getHelper() > 0) {
                                //if helper check if helper is on otherwise dont play
                                if (getUserHelper)
                                    playSound(item.getItemSound(), item, noItem);
                                else
                                    playSound(null, item, noItem);
                            } else {
                                //if no helper play normally
                                playSound(item.getItemSound(), item, noItem);
                            }
                        } else {
                            //if sound effect is off dont play any sound
                            playSound(null, item, noItem);
                        }
                    } else {
                        playSound(null, item, noItem);
                    }
                    break;

                case StaticAccess.TAG_END_SINGLE_TAP_TASK:

                    if (item.getCloseApp() == 1) {
                        musicControl = true;
                        Intent intent = new Intent(activity, TaskPackActivity.class);
                        staticInstance.clearLevel();
                        staticInstance.clearErrorArray();
                        intent.putExtra(StaticAccess.TAG_DETECT_APP_CLOSED_BUTTON_PRESSED, true);
                        startActivity(intent);
                        mpSoundRelease();
                        finishtheActivity();
                    }
                    if (item.getOpenUrl().length() > 0 && item.getOpenApp() != null) {

                        openWebLink(item.getOpenUrl());
                    }
                    // double tap open 3rd party app added by reaz
                    if (item.getOpenApp().length() > 0 && item.getOpenApp() != null) {
                        mpSoundRelease();
                        openApp(item.getOpenApp());
                    }
                    if (item.getNavigateTo() > 0) {

                        positiveResult.clear();
                        gotoSpecificTask(item.getNavigateTo());
                    }

                    //showed by and hidden by will work here only if its no item otherwise showHideDragandDrop() will handle
                    if (noItem) {
                        //get showed by list and show them
                        if (item.getShowedByTarget() != null && !item.getShowedByTarget().equals("")) {
                            String str = item.getShowedByTarget();
                            ArrayList<Long> list = formatList(str);
                            showItemsFromShowedby(list);
                        }
                        //get hidden by list and show them
                        if (item.getHiddenByTarget() != null && !item.getHiddenByTarget().equals("")) {
                            String str = item.getHiddenByTarget();
                            ArrayList<Long> list = formatList(str);
                            hideItemsFromShowedby(list);
                        }
                    }
                    isBlockUser = false;

                    soundTypeCount = 0;
                    break;
            }
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        cx = ev.getX();
        cy = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cx = ev.getX();
                cy = ev.getY();
                // set Response Time
                if (isTap) {

                    endRT = System.currentTimeMillis();
                    if (!taskIsTutorial)
                        rtMelliSecMap.put(currentTaskIndex, endRT - startRT);
                    isTap = false;
                }
                setAnimationTouchFeedBack(taskPack.getTouchAnimation(), cx, cy, activity);

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setAnimationTouchFeedBack(int type, float cx, float cy, Context context) {

        switch (type) {
            case StaticAccess.ANIM_TOUCH_NONE:
                break;
            case StaticAccess.ANIM_TOUCH_COLOR_CIRCLE:
                CustomVariousColorCircleAnimationView colorCircle = new CustomVariousColorCircleAnimationView(context, cx, cy);
                rlPlayer.addView(colorCircle);
                break;
            case StaticAccess.ANIM_TOUCH_COLOR_CIRCLE_STROKE:
                CustomVariousColorCircleStrokeAnimationView colorCircleStock = new CustomVariousColorCircleStrokeAnimationView(context, cx, cy);
                rlPlayer.addView(colorCircleStock);
                break;
            case StaticAccess.ANIM_TOUCH_CIRCLE_STROKE:
                CustomCircleStrokeAnimation circleStroke = new CustomCircleStrokeAnimation(context, cx, cy);
                rlPlayer.addView(circleStroke);
                break;
            case StaticAccess.ANIM_TOUCH_EVEN:
                OnTouchCustomView touchCustomView = new OnTouchCustomView(context, cx, cy);
                rlPlayer.addView(touchCustomView);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        musicControl = true;
        Intent intent = new Intent(activity, TaskPackActivity.class);
        startActivity(intent);
        finishtheActivity();
    }

    public void taskCompletionProgress() {
        progressOne = (ProgressBarSurfaceView) findViewById(R.id.progressOne);

        String testColor = userInfo.avatarDarkColor(user.getAvatar());
        String subColor = testColor.substring(1);
        String tranfarentPart = "#66";

        progressOne.setProgress(0, userInfo.getAvatarLevelProgress(user.getAvatar(), user.getStars()), tranfarentPart + subColor, userInfo.avatarDarkColor(user.getAvatar()));
    }

    int progressCounter = 1;
    // to check some one is pressing on the same limbika view for several times
    int indexCounter = -1;

    //the progress bar will not move when you do any wrong. the bar moves with first time right answer so
    // if there is 20 task it will move 20 times
    public float progressReport(boolean increamentCounter) {
//        Task t = tasks.get(currentTaskIndex);
        Task t;
        if (currentTaskIndex == 0) {
            t = tasks.get(currentTaskIndex);
        } else {
            t = tasks.get(currentTaskIndex - 1);
        }

        if (t != null) {
            //not all of the tasks shall be counted as tasks because tutorial resies there too
            if (t.getTutorial() < 1) {
                if (increamentCounter)
                    progressCounter = progressCounter + 1;
            }

        }
        //int totalTaskSize = tasks.size();

        float progressReport = (progressCounter * 100) / numberOftasks;
        return progressReport;
    }

// get boolean value Return user wise
    public boolean isNumToBol(int value) {
        boolean helper = false;
        if (value == 1) {
            helper = true;
        } else {
            helper = false;
        }
        return helper;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(progressOne!=null && tasks!=null)
        if(tasks.size()>0)
        progressOne.setProgressInPercent(progressReport(false));
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
