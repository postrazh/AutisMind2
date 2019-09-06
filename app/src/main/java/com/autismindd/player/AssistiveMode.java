package com.autismindd.player;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.autismindd.activities.PlayerActivity;
import com.autismindd.dao.Item;
import com.autismindd.dao.Task;
import com.autismindd.utilities.Limbika_mesurement;
import com.autismindd.utilities.StaticAccess;
import com.dmk.limbikasdk.views.DrawAssistiveLine;
import com.dmk.limbikasdk.views.LimbikaView;

/**
 * Created by RAFI on 12/28/2016.
 */

public class AssistiveMode {
    Context context;
    Task task;
    Item item;
    PlayerActivity activity;

    public AssistiveMode(Context context, Task task) {
        this.context = context;
        this.task = task;
        activity=(PlayerActivity)context;
    }

    public void endXYPanelPoint(float x, float y){

        LimbikaView endView = calculateintersection(x, y);
        if (activity.startView != null && endView != null) {
            //these are targets dont confuse it with start and endview
            LimbikaView startTarget = activity.dropTargetsMap.get(activity.startView.getTargetKey());
            LimbikaView endTarget = activity.dropTargetsMap.get(endView.getTargetKey());
            DrawAssistiveLine drawAssistiveLine = new DrawAssistiveLine(activity);

            //this checks if one of the view is noItem or not (DO NO MOVE THIS BLOCK)
            if (activity.isInNoItems(activity.startView) || activity.isInNoItems(endView)) {
                //(DO NO MOVE THIS BLOCK) this block should be here
            }
            //now check if any drop target is there or not
            else if (startTarget != null || endTarget != null) {
                //checking the startTargetonly the end target is cheked later
                //DO NOT CONFUSE IT WITH START VIEW AND END VIEW THIS IS COMPELTELY DIFFERENT

                if (startTarget != null) {
                    //either targetkey is -1 (which means no error mode) or assistiveTargetKey and targetkey shd be same
                    if (activity.assistiveTargetKey == -1 || (activity.assistiveTargetKey == startTarget.getLimbikaViewItemValue().getKey()))
                        if (startTarget.getLimbikaViewItemValue().getKey() == endView.getLimbikaViewItemValue().getKey()) {
                            //endView is the target for startView so remove startview which is a child

                            activity.positiveResult.remove(activity.startView.getLimbikaViewItemValue().getKey());
                            activity.alreadyCorrectDragandDrop.add(activity.startView.getLimbikaViewItemValue().getKey());

                            if (task.getPositiveAnimation() > 0) {
                                activity.showAnimation(activity.startView, task.getPositiveAnimation());
                                activity.showAnimation(endView, task.getPositiveAnimation());
                            }

                            //check if all the dropItems of the target is already droped on it or anything left
                            //if nothing is left unlock the other views
                            if (activity.error_mode_on_dragDrop)
                                if (activity.ifTargetTotallyPlayed(startTarget.getLimbikaViewItemValue().getKey())) {
                                    activity.makeAllViewsDragable();
                                }

                            drawAssistiveLine.DrawAssistiveLine(activity.startView, endView, true);
                            activity.assistiveRelLayout.addView(drawAssistiveLine);

                            if (activity.getUserSound)
                                activity.playSoundDragAndAssistive(task, task.getPositiveSound());
                            else {
                                activity.playSoundDragAndAssistive(task, "");
                            }

                            if (!activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()).getShowedByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()));
                            //if target has showedby/hiden by
                            if (!activity.items.get(endView.getLimbikaViewItemValue().getKey()).getShowedByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(endView.getLimbikaViewItemValue().getKey()));


                            //Hidden by/////////////////////////////////////////////
                            if (!activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()).getHiddenByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()));
                            //if target has showedby/hiden by
                            if (!activity.items.get(endView.getLimbikaViewItemValue().getKey()).getHiddenByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(endView.getLimbikaViewItemValue().getKey()));


                        } else if (activity.startView == endView) {
                            //do nothing
                        } else if (activity.isInDropItems(activity.startView) && activity.isInDropItems(endView)) {
                            //when both of the views are drop items do nothin
                            //it will always execute this block when both items are child
                            long key=startTarget.getKey();
                            LimbikaView v=activity.dropTargetsMap.get(key);
                            if(v!=null && v.getVisibility()==View.VISIBLE){
                                activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_ASSISTIVE_MODE,
                                        key);
                                activity.assistiveTargetKey = key;
                            }else {
                                // if there is no damn child
                                key=endTarget.getKey();
                                v=activity.dropTargetsMap.get(key);
                                if (v != null && v.getVisibility() == View.VISIBLE) {
                                    activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_ASSISTIVE_MODE,
                                            key);
                                    activity.assistiveTargetKey = key;
                                }
                            }
                        } else {
                            if (task.getNegativeAnimation() > 0) {
                                activity.showAnimation(activity.startView, task.getNegativeAnimation());
                                activity.showAnimation(endView, task.getNegativeAnimation());
                            }
                            // drawAssistiveLine.DrawAssistiveLine(startView, endView, false);
                            //assistiveRelLayout.addView(drawAssistiveLine);
                            //we must select target if target is either of teh view
                            long key=getTheTargetIFAvailable(endView);

                            activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_ASSISTIVE_MODE,
                                    key);
                            activity.assistiveTargetKey = key;

                        }


                }
         /* ****************************** When endview is TARGET ****************************** */
                else if (endTarget != null) {
                    //either targetkey is -1 (which means no error mode) or assistiveTargetKey and targetkey shd be same
                    if (activity.assistiveTargetKey == -1 || (activity.assistiveTargetKey == endTarget.getLimbikaViewItemValue().getKey()))
                        if (endTarget.getLimbikaViewItemValue().getKey() == activity.startView.getLimbikaViewItemValue().getKey()) {

                            activity.positiveResult.remove(endView.getLimbikaViewItemValue().getKey());
                            activity.alreadyCorrectDragandDrop.add(endView.getLimbikaViewItemValue().getKey());

                       /* if (task.getPositiveSound().length() > 0) {
                            playSound(task.getPositiveSound());
                        }*/
                            if (task.getPositiveAnimation() > 0) {
                                activity.showAnimation(activity.startView, task.getPositiveAnimation());
                                activity.showAnimation(endView, task.getPositiveAnimation());
                            }


                            drawAssistiveLine.DrawAssistiveLine(activity.startView, endView, true);
                            activity.assistiveRelLayout.addView(drawAssistiveLine);

                            if (activity.getUserSound)
                                activity.playSoundDragAndAssistive(task, task.getPositiveSound());
                            else {
                                activity.playSoundDragAndAssistive(task, "");
                            }

                            if (!activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()).getShowedByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()));
                            //if target has showedby/hiden by
                            if (!activity.items.get(endView.getLimbikaViewItemValue().getKey()).getShowedByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(endView.getLimbikaViewItemValue().getKey()));


                            //Hidden by/////////////////////////////////////////////
                            if (!activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()).getHiddenByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(activity.startView.getLimbikaViewItemValue().getKey()));
                            //if target has showedby/hiden by
                            if (!activity.items.get(endView.getLimbikaViewItemValue().getKey()).getHiddenByTarget().equals(""))
                                activity.showHideDragandDrop(activity.items.get(endView.getLimbikaViewItemValue().getKey()));


                            //check if all the dropItems of the target is already droped on it or anything left
                            //if nothing is left unlock the other views
                            if (activity.error_mode_on_dragDrop)
                                if (activity.ifTargetTotallyPlayed(endTarget.getLimbikaViewItemValue().getKey())) {
                                    activity.makeAllViewsDragable();
                                }

                            //Toast.makeText(this, "matched", Toast.LENGTH_LONG).show();
                        } else if (activity.startView == endView) {
                            //do nothing
                        } else if (activity.isInDropItems(activity.startView) && activity.isInDropItems(endView)) {
                            //when both of the views are drop items do nothin
                            long key=startTarget.getKey();
                            LimbikaView v=activity.dropTargetsMap.get(key);
                            if(v!=null && v.getVisibility()==View.VISIBLE){
                                activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_ASSISTIVE_MODE,
                                        key);
                                activity.assistiveTargetKey = key;
                            }else {
                                // if there is no damn child
                                key=endTarget.getKey();
                                v=activity.dropTargetsMap.get(key);
                                if (v != null && v.getVisibility() == View.VISIBLE) {
                                    activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_ASSISTIVE_MODE,
                                            key);
                                    activity.assistiveTargetKey = key;
                                }
                            }
                        } else {
                            if (task.getNegativeAnimation() > 0) {
                                activity.showAnimation(activity.startView, task.getNegativeAnimation());
                                activity.showAnimation(endView, task.getNegativeAnimation());
                            }
                            //we must select target if target is either of teh view
                            long key=getTheTargetIFAvailable(endView);
                            activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_ASSISTIVE_MODE,
                                    key);
                            activity.assistiveTargetKey = key;
                            //drawAssistiveLine.DrawAssistiveLine(startView, endView, false);
                            //assistiveRelLayout.addView(drawAssistiveLine);
                        }
                }
            }
        }
        //when finger is lifted the saved view shall be null
        activity.startView = null;
    }
    private long getTheTargetIFAvailable(LimbikaView endView) {
        long key=-1;
        //here we are 100% sure either of the view is a target or we wont be here
        //we are giving start view the priority
        if(activity.dropTargetsMap.get(activity.startView.getKey())!=null){
            key=activity.startView.getKey();
        }else{
            key=endView.getKey();
        }

        return key;
    }

    public void singleTap(float x, float y){
        Item item1 = null;
        LimbikaView v = calculateintersection(x, y);

        if (v != null) {
            item1 = activity.items.get(v.getLimbikaViewItemValue().getKey());
            if (! activity.isBlockUser) {
                activity.isBlockUser = true;
                v.zoomOut(v);
                //now no Items will handle showed and hidden too
                if (item1.getDragDropTarget() == 0 && item1.getAllowDragDrop() == 0)
                    activity.SingleTapTask(item1, 0, true, true);
                else
                    activity.SingleTapTask(item1, 0, true, false);
            }
        }

    }
    public void doubleTap(float x, float y){


    }
    public void startXYPanelPoint(float x, float y){
        activity.startView = calculateintersection(x, y);

    }


    LimbikaView calculateintersection(float x, float y) {
        boolean intersects = false;
        LimbikaView v = null;

        for (Limbika_mesurement measurement : activity.measurement_list) {
            int xx = (int) x;
            int yy = (int) y;
            Rect r = new Rect(xx, yy, xx + 20, yy + 20);

            Log.e("Here: ", String.valueOf(xx) + " " + String.valueOf(yy));

            int xxx = (int) measurement.getX();
            int yyy = (int) measurement.getY();
            int w = (int) measurement.getWidth();
            int h = (int) measurement.getHeight();
            Rect measure = new Rect(xxx, yyy, xxx + w, yyy + h);

            intersects = r.intersect(measure);
            if (intersects) {
                Log.e("Done", "1");
                if(activity.limbikaview_list.get(measurement.getKey()).getVisibility()== View.VISIBLE){
                    Log.e("Done", "2");
                    v= activity.limbikaview_list.get(measurement.getKey());
                }else {
                    Log.e("Done", "3");
                    // v = null;
                }
            }

        }
        return v;
    }
}
