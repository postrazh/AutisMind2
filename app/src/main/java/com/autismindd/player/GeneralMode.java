package com.autismindd.player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.View;

import com.autismindd.activities.PlayerActivity;
import com.autismindd.activities.TaskPackActivity;
import com.autismindd.dao.Item;
import com.autismindd.dao.Task;
import com.autismindd.dao.TaskPack;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.TaskType;
import com.dmk.limbikasdk.pojo.LimbikaViewItemValue;
import com.dmk.limbikasdk.views.LimbikaView;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RAFI on 11/10/2016.
 */

public class GeneralMode {
    Context context;
    Task task;
    Item item;
    PlayerActivity activity;


    public GeneralMode(Context context, Task task, Item item) {
        this.context = context;
        this.task = task;
        this.item = item;
        activity = (PlayerActivity) context;

    }


    public void generalMode(View v, LimbikaViewItemValue limbikaViewItemValue) {
        LimbikaView limbikaView = (LimbikaView) v;

        boolean handleShowHideIfNotCorrect=false;

        if(item!=null)
        if (item.getResult() != null)
            if (item.getResult().length() > 0 && activity.positiveResult.size()>0) {

                // the second condition(&&) diminishs the posibity of playing a positive view twice
                if (item.getResult().equals(StaticAccess.NORMAL_TRUE) &&
                        ifInthePositiveListGenearal(item.getKey())) {
                    //task.getNegative sound no need this  playSoundOnlyGeneral Methods that's why we send Blank String
                    //all the sound and feed back related works are done here so that everything happens simultaneously

//                LimbikaView limbikaView = (LimbikaView) v;
                    //when u come back from error screen this will remove continuous animation

                    activity.clearErrorPositiveAnimation();
                    limbikaView.showPositiveTag();
                    activity.positiveResult.remove(limbikaViewItemValue.getKey());

                    if (task.getPositiveAnimation() > 0) {
                        activity.showAnimation(limbikaView, task.getPositiveAnimation());
                    }

                    if (activity.getUserSound) {
                        //check if helper is on checking needed otherwise move on
                        if (item.getHelper() > 0) {
                            if (activity.getUserHelper)
                                playSoundOnlyGeneral(item.getItemSound(), task.getPositiveSound(), "", task, item);
                            else
                                playSoundOnlyGeneral("", "", "", task, item);
                        } else {
                            playSoundOnlyGeneral(item.getItemSound(), task.getPositiveSound(), "", task, item);
                        }
                    } else {
                        playSoundOnlyGeneral("", "", "", task, item);
                    }

                    //get showed by list and show them
                    if (item.getShowedByTarget() != null && !item.getShowedByTarget().equals("")) {
                        String str = item.getShowedByTarget();
                        ArrayList<Long> list = activity.formatList(str);
                        activity.showItemsFromShowedby(list);
                    }
                    //get hidden by list and show them
                    if (item.getHiddenByTarget() != null && !item.getHiddenByTarget().equals("")) {
                        String str = item.getHiddenByTarget();
                        ArrayList<Long> list = activity.formatList(str);
                        activity.hideItemsFromShowedby(list);
                    }
                    handleShowHideIfNotCorrect=true;
                    //positive end////

                } else if (item.getResult().equals(StaticAccess.NORMAL_FALSE)) {
                    //if another sound is playing or not
                    if (activity.mp != null) {
                        if (!activity.mp.isPlaying()) {
                            if (task.getNegativeAnimation() > 0) {
                                activity.showAnimation(limbikaView, task.getNegativeAnimation());

                            }

                            if (activity.getUserSound) {
                                //check if helper is on checking needed otherwise move on
                                if (item.getHelper() > 0) {
                                    if (activity.getUserHelper)
                                        playSoundOnlyGeneral(item.getItemSound(), "", task.getNegativeSound(), task, item);
                                    else
                                        playSoundOnlyGeneral("", "", "", task, item);
                                } else {
                                    playSoundOnlyGeneral(item.getItemSound(), "", task.getNegativeSound(), task, item);
                                }
                            } else {
                                playSoundOnlyGeneral("", "", "", task, item);
                            }

                        }
                    } else {
                        //Dont understand the use of this block


                        if (task.getNegativeAnimation() > 0) {
                            activity.showAnimation(limbikaView, task.getNegativeAnimation());

                        }

                        if (activity.getUserSound) {
                            //check if helper is on checking needed otherwise move on
                            if (item.getHelper() > 0) {
                                if (activity.getUserHelper)
                                    playSoundOnlyGeneral(item.getItemSound(), "", task.getNegativeSound(), task, item);
                                else
                                    playSoundOnlyGeneral("", "", "", task, item);
                            } else {
                                playSoundOnlyGeneral(item.getItemSound(), "", task.getNegativeSound(), task, item);
                            }
                        } else {
                            playSoundOnlyGeneral("", "", "", task, item);
                        }

                    }
                    //playNegSoundGeneral(task, task.getNegativeSound());

                    //LimbikaView limbikaView = (LimbikaView) v;
                    //limbikaView.showNegativeTag();

                } else {
                    //task.getNegative sound no need this  playSoundOnlyGeneral Methods that's why we send Blank String
                    //all the sound and feed back related works are done here so that everything happens simultaneously

                    if (activity.getUserSound) {
                        //check if helper is on checking needed otherwise move on
                        if (item.getHelper() > 0) {
                            if (activity.getUserHelper)
                                playSoundOnlyGeneral(item.getItemSound(), "", "", task, item);
                            else
                                playSoundOnlyGeneral("", "", "", task, item);
                        } else {
                            playSoundOnlyGeneral(item.getItemSound(), "", "", task, item);
                        }
                    } else {
                        playSoundOnlyGeneral("", "", "", task, item);
                    }

                }

            } else {
                if(activity.positiveResult.size()>0)
                if (activity.getUserSound) {
                    //check if helper is on checking needed otherwise move on
                    if (item.getHelper() > 0) {
                        if (activity.getUserHelper)
                        {
                            // if positive or negative sound is playing that time item sound not play
                           if(activity.mp!=null){
                               if(activity.mp.isPlaying()){

                               }else {
                                   playSoundOnlyGeneral(item.getItemSound(), "", "", task, item);
                               }
                           }else {
                               playSoundOnlyGeneral(item.getItemSound(), "", "", task, item);
                           }

                        }
                        else
                            playSoundOnlyGeneral("", "", "", task, item);
                    } else {
                        playSoundOnlyGeneral(item.getItemSound(), "", "", task, item);
                    }
                } else {
                    playSoundOnlyGeneral("", "", "", task, item);
                }
            }


        //DONOT LET THE USERS USE POSITIVE NEGATIVE AND THE BELOW FUNCTIONS TOGETHER THIS WILL CREATE MANICE

        //get showed by list and show them
        if(!handleShowHideIfNotCorrect) {
            if (item.getShowedByTarget() != null && !item.getShowedByTarget().equals("")) {
                String str = item.getShowedByTarget();
                ArrayList<Long> list = activity.formatList(str);
                activity.showItemsFromShowedby(list);
            }
            //get hidden by list and show them
            if (item.getHiddenByTarget() != null && !item.getHiddenByTarget().equals("")) {
                String str = item.getHiddenByTarget();
                ArrayList<Long> list = activity.formatList(str);
                activity.hideItemsFromShowedby(list);
            }
        }

        if (item.getCloseApp() == 1) {
            activity.musicControl = true;
            Intent intent = new Intent(activity, TaskPackActivity.class);
            activity.staticInstance.clearLevel();
            activity.staticInstance.clearErrorArray();
            intent.putExtra(StaticAccess.TAG_DETECT_APP_CLOSED_BUTTON_PRESSED, true);
            activity.startActivity(intent);
            activity.mpSoundRelease();
            activity.finish();
        }
        if (item.getOpenUrl().length() > 0 && item.getOpenApp() != null) {
            limbikaView.setDraggable(false);
            activity.openWebLink(item.getOpenUrl());
        }
        // single tap open 3rd party app added by reaz
        if (item.getOpenApp().length() > 0 && item.getOpenApp() != null) {
            activity.mpSoundRelease();
            activity.openApp(item.getOpenApp());
        }
        if (item.getNavigateTo() > 0) {
            limbikaView.setDraggable(false);
            activity.positiveResult.clear();
            activity.gotoSpecificTask(item.getNavigateTo());
        }
        activity.isBlockUser = false;

    }

    //general mode work:: checking the view is in positiveAnswer list
    boolean ifInthePositiveListGenearal(long key) {
        boolean res = false;
        for (long k : activity.positiveResult) {
            if (k == key)
                res = true;
        }
        return res;
    }


    //mir the kings code ///////////////////////////////////////
    // we handled HELPER from out, but DO NOT MAKE HELPER AND POSITIVE THE SAME DAMN VIEW
    void playSoundOnlyGeneral(final String itemsound, final String posSound, final String negSound, final Task task, final Item item) {
        //when item sound is available
        if (itemsound.length() > 0 && itemsound != null) {
            activity.mpSoundRelease();
            activity.mp = new MediaPlayer();
            try {
                activity.mp.setDataSource(Environment.getExternalStorageDirectory() + StaticAccess.ANDROID_DATA + activity.getPackageName() + StaticAccess.ANDROID_DATA_PACKAGE_SOUND + itemsound);

                activity.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        activity.mpSoundRelease();
                        activity.mp = new MediaPlayer();

                        String playString = "";

                        if (posSound != null && !posSound.equals(""))
                            playString = posSound;
                        else
                            playString = negSound;
                        //if itemsound is only available no pos or neg sound
                        if (playString.equals("") || playString == null) {

                            if (activity.positiveResult.size() == 0) {
                             /*   if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                                    activity.progressOne.setProgressInPercent(activity.progressReport(activity.currentTaskIndex));/// change by sumon
                                }*/
                                if (task.getFeedbackImage().length() > 0) {
                                    activity.delayfeedBack(task.getFeedbackImage(), task.getFeedbackAnimation(), activity.currentTaskIndex, task.getFeedbackSound(), task.getFeedbackType());
                                } else {
                                    if (item.getResult().equals(TaskType.NORMAL_TRUE))
                                        activity.delayGotoNextTask(activity.currentTaskIndex);
                                }
                            }
                            //handle the error screen if item sound is there but no pos neg sound but its error
                            if (item.getResult().equals(TaskType.NORMAL_FALSE)) {
                                if (activity.madatoryError == 1 && negSound.equals(""))
                                    activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_GENERAL_MODE);
                            }

                        } else
                            try {
                                //Positive or negative sound available after item sound
                                activity.mp.setDataSource(Environment.getExternalStorageDirectory() +
                                        StaticAccess.ANDROID_DATA + activity.getPackageName() + StaticAccess.ANDROID_DATA_PACKAGE_SOUND + playString);
                                activity.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        if (!posSound.equals("") || posSound != null) {
                                            if (activity.positiveResult.size() == 0) {
                                              /*  if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                                                    activity.progressOne.setProgressInPercent(activity.progressReport(activity.currentTaskIndex)); // change by sumon
                                                }*/
                                                if (task.getFeedbackImage().length() > 0) {
                                                    activity.delayfeedBack(task.getFeedbackImage(), task.getFeedbackAnimation(), activity.currentTaskIndex, task.getFeedbackSound(), task.getFeedbackType());
                                                } else {
                                                    if (item.getResult().equals(TaskType.NORMAL_TRUE))
                                                        activity.delayGotoNextTask(activity.currentTaskIndex);
                                                }
                                            }
                                        } else {
                                            //for negative
                                            //if mandatory is true then send it to uper error else do nothing
                                            if (activity.madatoryError == 1)
                                                activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_GENERAL_MODE);
                                        }
                                    }
                                });
                                activity.mp.prepare();

                             /*do not play negative sound if mandatory supererror is on.
                                 then the sound will be played on Super activity */
                                if (activity.madatoryError == 1 && negSound != null && !negSound.equals("")) {
                                    activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_GENERAL_MODE);
                                } else
                                    activity.mp.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                });

                activity.mp.prepare();
                activity.mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //no item sound and no Positive or negetive sound
        else if (itemsound.equals("") && posSound.equals("") && negSound.equals("")) {
            if (activity.positiveResult.size() == 0) {
             /*   if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                    activity.progressOne.setProgressInPercent(activity.progressReport(activity.currentTaskIndex)); /// change by sumon
                }*/
                if (task.getFeedbackImage().length() > 0) {
                    activity.delayfeedBack(task.getFeedbackImage(), task.getFeedbackAnimation(), activity.currentTaskIndex, task.getFeedbackSound(), task.getFeedbackType());
                } else {
                    if (item.getResult().equals(TaskType.NORMAL_TRUE))
                        activity.delayGotoNextTask(activity.currentTaskIndex);
                }
            }

            //if no sound anywhere but its a negative view
            if (item.getResult().equals(TaskType.NORMAL_FALSE)) {
                if (activity.madatoryError == 1)
                    activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_GENERAL_MODE);
            }
        }
        //no item sound but positive or negative sound is available
        else {
            activity.mpSoundRelease();
            activity.mp = new MediaPlayer();

            String playString = "";

            if (posSound != null && !posSound.equals(""))
                playString = posSound;
            else
                playString = negSound;

            try {

                activity.mp.setDataSource(Environment.getExternalStorageDirectory() +
                        StaticAccess.ANDROID_DATA + activity.getPackageName() + StaticAccess.ANDROID_DATA_PACKAGE_SOUND + playString);
                activity.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (!posSound.equals("") || posSound != null) {
                            if (activity.positiveResult.size() == 0) {
                              /*  if (StaticAccess.PLAYER_PROGRESS_ON == activity.user.getTaskPlayProgress()) {
                                    activity.progressOne.setProgressInPercent(activity.progressReport(activity.currentTaskIndex));// change by suomon
                                }*/
                                if (task.getFeedbackImage().length() > 0) {
                                    activity.delayfeedBack(task.getFeedbackImage(), task.getFeedbackAnimation(), activity.currentTaskIndex, task.getFeedbackSound(), task.getFeedbackType());
                                } else {
                                    if (item.getResult().equals(TaskType.NORMAL_TRUE))
                                        activity.delayGotoNextTask(activity.currentTaskIndex);
                                }
                            }
                        } else {
                            //for negative
                            //if mandatory is true then send it to uper error else do nothing
                            /*if (madatoryError == 1)
                                errorListner.onErrorListner(task, StaticAccess.TAG_TASK_GENERAL_MODE);*/
                        }
                    }
                });
                activity.mp.prepare();
                /*do not play negative sound if mandatory supererror is on
                                 then the sound will be played on activity */
                if (activity.madatoryError == 1 && negSound != null && !negSound.equals("")) {
                    activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_GENERAL_MODE);
                } else
                    activity.mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}

