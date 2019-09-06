package com.autismindd.player;

import android.content.Context;

import com.autismindd.activities.PlayerActivity;
import com.autismindd.dao.Item;
import com.autismindd.dao.Task;
import com.autismindd.utilities.StaticAccess;
import com.dmk.limbikasdk.views.LimbikaView;


/**
 * Created by RAFI on 11/13/2016.
 */

public class DragAndDropMode {
    Context context;
    Task task;
    Item item;
    PlayerActivity activity;

    public DragAndDropMode(Context context, Task task) {
        this.context = context;
        this.task = task;
        activity=(PlayerActivity)context;

    }
// drag and drop single Tap handle
    public void singleTap(Item item){
        if (!activity.isBlockUser) {
            activity.isBlockUser = true;

            //now no Items will handle showed and hidden too
            if (item.getDragDropTarget() == 0 && item.getAllowDragDrop() == 0)
                activity.SingleTapTask(item, 0, true, true);
            else
                activity.SingleTapTask(item, 0, true, false);
        }
    }
    // drag and drop drag target handle
    public void onDropTarget(LimbikaView view, Long dropTargetKey ){
        Item selectedItem = activity.items.get(view.getLimbikaViewItemValue().getKey());
        Item selectedItemTarget = activity.items.get(dropTargetKey);
        // response Time Calculate

        // For positive Tag
        if (selectedItem.getDragDropTarget() == dropTargetKey) {
            //when u come back from error screen this will remove continuous animation
            if(view!=null)
                view.clearAnimation();

            LimbikaView limbikaView = activity.dropItemsMap.get(selectedItem.getKey());
            limbikaView.showPositiveTag();
            LimbikaView limbikaViewTarget = activity.dropTargetsMap.get(dropTargetKey);


//                                        playSound(task.getPositiveSound());
            activity.positiveResult.remove(selectedItem.getKey());
            activity.alreadyCorrectDragandDrop.add(selectedItem.getKey());

            //check if all the dropItems of the target is already droped on it or anything left
            //if nothing is left unvlock the other views
            if (activity.error_mode_on_dragDrop)
                if (activity.ifTargetTotallyPlayed(dropTargetKey)) {
                    activity.makeAllViewsDragable();
                }


            if (task.getPositiveAnimation() > 0) {
                activity.showAnimation(limbikaViewTarget, task.getPositiveAnimation());
            }
            if (activity.getUserSound) {
                //check if helper is on checking needed otherwise move on
                if(selectedItem.getHelper()>0) {
                    if(activity.getUserHelper)
                        activity.playSoundDragAndAssistive(task, task.getPositiveSound());
                    else
                        activity.playSoundDragAndAssistive(task, "");
                }else{
                    activity.playSoundDragAndAssistive(task, task.getPositiveSound());
                }
            }else {
                activity.playSoundDragAndAssistive(task, "");
            }
            //when the item is dropped on target then show the hide/show item
            if (!selectedItem.getShowedByTarget().equals(""))
                activity.showHideDragandDrop(selectedItem);
            if (!selectedItemTarget.getShowedByTarget().equals(""))
                activity.showHideDragandDrop(selectedItemTarget);

            //Hidden
            //when the item is dropped on target then show the hide/show item
            if (!selectedItem.getHiddenByTarget().equals(""))
                activity.showHideDragandDrop(selectedItem);

            //if target has showedby/hiden by
            if (!selectedItemTarget.getHiddenByTarget().equals(""))
                activity.showHideDragandDrop(selectedItemTarget);
        }

    }
    // drag and drop drag target wrong handle
    public  void onDroppedonWrongTarge(LimbikaView view, Long dropTargetKey){
        if (dropTargetKey != -1) {
            Item selectedItem = activity.items.get(view.getLimbikaViewItemValue().getKey());
            LimbikaView limbikaView = activity.dropItemsMap.get(selectedItem.getKey());
            limbikaView.showNegativeTag();
            LimbikaView limbikaViewTarget = activity.dropTargetsMap.get(dropTargetKey);
            //we must draw negative animation on target
            if (activity.madatoryError == 1)
                activity.errorListner.onErrorListner(task, StaticAccess.TAG_TASK_DRAG_AND_DROP_MODE,
                        dropTargetKey);
            else {
                if (activity.getUserSound) {
                    //check if helper is on checking needed otherwise move on
                    if(selectedItem.getHelper()>0) {
                        if(activity.getUserHelper)
                            activity.playSoundDragAndAssistive(task, task.getNegativeSound());
                        else
                            activity.playSoundDragAndAssistive(task, "");
                    }else{
                        activity.playSoundDragAndAssistive(task, task.getNegativeSound());
                    }
                }else {
                    activity.playSoundDragAndAssistive(task, "");
                }
            }
//                                        playSound(task.getNegativeSound());
            if (task.getNegativeAnimation() > 0) {
                //mir change
                activity.showAnimation(limbikaViewTarget, task.getNegativeAnimation());
            }
        }

    }
}
