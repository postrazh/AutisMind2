package com.autismindd.adapter;
//MultiChoiceFirstLayerAdapter
/**
 * Created by RAFI on 10/3/2016.
 */

import android.content.Context;
import android.widget.BaseAdapter;

import com.autismindd.listener.MultiChoiceAdapterFirstLayerInterface;
import com.autismindd.dao.FirstLayer;


import java.util.ArrayList;

/**
 * Created by Macbook on 28/09/2016.
 * PLEASE FOLLOW THE USER MODEL OF THIS PROJECT IF YOU WANT TO RE USE IT IN OTHER WAYS YOU MUST CHANGE IT ON YOU OWN
 * THE SEARCHING AND HANDLING IS DEPENDENT ON A LONG KEY SO YOUR MODEL MUST HAVE A LONG KEY
 */

public abstract class MultiChoiceFirstLayerAdapter extends BaseAdapter implements MultiChoiceAdapterFirstLayerInterface.ControlMethods {

    //this array list contains keys of selected items
    public ArrayList<FirstLayer> multiChoiceTaskListTemp = new ArrayList<>();
    //this variable holds the key of single item selected
    public long SingleModeKey = -1;

    ArrayList<FirstLayer> firstLayerArrayList = new ArrayList<>();
    MultiChoiceViewActivityFirstLayer acticity;
    Context ctx;

    boolean multichoiceMode = false;

    public MultiChoiceFirstLayerAdapter(MultiChoiceViewActivityFirstLayer activity, ArrayList<FirstLayer> firstLayerArrayList) {
        this.firstLayerArrayList = firstLayerArrayList;
        this.acticity = activity;
        ctx = activity;
        activity.setMultiChoiceListener(this);
    }

    //////////////////////////////////////////CONTROL METHODS OF INTERFACE/////////////////
    //Called from the Implemented Activity through MultichoiceInterface
    @Override
    public void setSingleModeId(FirstLayer firstLayer) {
        //this block will be entered when previously u have selected an item
//        Toast.makeText(ctx,"hsghghsgh",Toast.LENGTH_LONG).show();
        long singleModeKey = firstLayer.getFirstLayerTaskID();
        if (!multichoiceMode) {

            if (SingleModeKey != -1) {

                //checking previous view and current view is same or not
                if (singleModeKey == SingleModeKey) {
                    singleTapModeDone(firstLayer);

                } else {

                    changeToSelectedOrDeselect(singleModeKey, SingleModeKey);
                    SingleModeKey = singleModeKey;
                }
            } else {

                changeToSelectedOrDeselect(singleModeKey, SingleModeKey);
                SingleModeKey = singleModeKey;
                singleTapModeOn(SingleModeKey);
            }
        } else {
            //multichoice mode on so add it in list
            SingleModeKey = -1;
            multichoiceMode = true;
            multiChoiceTaskListTemp.add(firstLayer);
            changeToSelectedOrDeselectMultiChoice(firstLayer);
            multiChoiceModeOn((ArrayList<FirstLayer>) multiChoiceTaskListTemp.clone(), multichoiceMode);
        }
    }

   /* @Override
    public void addMultichoiseModeKey(Task task) {
        multichoiceMode = true;
        multiChoiceTaskListTemp.add(task);
        changeToSelectedOrDeselectMultiChoice(task);
        multiChoiceModeOn((ArrayList<Task>) multiChoiceTaskListTemp.clone(), multichoiceMode);
    }*/

    @Override
    public void clearMultichoiceMode() {
        multiChoiceTaskListTemp.clear();
        multichoiceMode = false;
        changeToSelectedOrDeselect(-1, -1);
    }

    @Override
    public void clearSingleChoiceMode() {
        SingleModeKey = -1;
    }
    //////////////////////////////////////////CONTROL METHODS OF INTERFACE FINISH/////////////////

    abstract void singleTapModeOn(long key);

    abstract void singleTapModeDone(FirstLayer frstlayer);

    abstract void multiChoiceModeOn(ArrayList<FirstLayer> firstLayerList, boolean mode);

    abstract void singleTapModeOff();

    abstract void multiChoiceModeOff();

    abstract void notifyDatasetChangeCustom(ArrayList<FirstLayer> taskList);


    /// Class Methods

    //use this method to select or desect keys from both single and multichoice
    //key=-1,prevKey=-1===== deselect/clear all
    //key=value, prevKey=-1 ===== select item
    // key=value, prevKey =value ==== select key and deselt prevkey
    public void changeToSelectedOrDeselect(long key, long prevKey) {
        //1.clearing everything
        ArrayList<FirstLayer> cloneTask = new ArrayList<>();
        if (prevKey == -1 && key == -1) {
            for (FirstLayer firstLayer : firstLayerArrayList) {
                firstLayer.setState(false);
                cloneTask.add(firstLayer);
            }
            notifyDatasetChangeCustom((ArrayList<FirstLayer>) cloneTask.clone());
        }
        //2. Select Item
        if (prevKey == -1 && key > -1) {
            for (FirstLayer firstLayer : firstLayerArrayList) {
                if (firstLayer.getFirstLayerTaskID() == key)
                    firstLayer.setState(true);
                else
                    firstLayer.setState(false);
                cloneTask.add(firstLayer);
            }
            notifyDatasetChangeCustom((ArrayList<FirstLayer>) cloneTask.clone());
        }

        //2. Deselect prev & Select Item
        if (prevKey > -1 && key > -1) {
            for (FirstLayer firstLayer : firstLayerArrayList) {
                if (firstLayer.getFirstLayerTaskID() == prevKey) {
                    firstLayer.setState(false);
                    cloneTask.add(firstLayer);
                } else if (firstLayer.getFirstLayerTaskID() == key) {
                    firstLayer.setState(true);
                    cloneTask.add(firstLayer);
                } else {
                    firstLayer.setState(false);
                    cloneTask.add(firstLayer);
                }

            }
            notifyDatasetChangeCustom((ArrayList<FirstLayer>) cloneTask.clone());
        }

    }

    public void changeToSelectedOrDeselectMultiChoice(FirstLayer t) {
        ArrayList<FirstLayer> cloneTasks = new ArrayList<>();
        for (FirstLayer firstLayer : firstLayerArrayList) {
            if (firstLayer.getFirstLayerTaskID() == firstLayer.getFirstLayerTaskID()) {
                firstLayer.setState(true);
            }
            notifyDatasetChangeCustom((ArrayList<FirstLayer>) firstLayerArrayList.clone());
        }
    }

}
