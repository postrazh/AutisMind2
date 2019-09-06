package com.autismindd.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import com.autismindd.listener.MultichoiceAdapterInterface;
import com.autismindd.activities.MultichoiceViewActivity;
import com.autismindd.dao.User;

import java.util.ArrayList;

/**
 * Created by Macbook on 28/09/2016.
 * PLEASE FOLLOW THE USER MODEL OF THIS PROJECT IF YOU WANT TO RE USE IT IN OTHER WAYS YOU MUST CHANGE IT ON YOU OWN
 * THE SEARCHING AND HANDLING IS DEPENDENT ON A LONG KEY SO YOUR MODEL MUST HAVE A LONG KEY
 */

public abstract class MultiChoiceAdapter extends BaseAdapter implements MultichoiceAdapterInterface.ControlMethods {

    //this array list contains keys of selected items
    public ArrayList<User> multiChoiceUserListTemp=new ArrayList<>();
    //this variable holds the key of single item selected
    public long SingleModeKey=-1;

    ArrayList<User> userArrayList=new ArrayList<>();
    MultichoiceViewActivity acticity;
    Context ctx;

    boolean multichoiceMode=false;

    public MultiChoiceAdapter(MultichoiceViewActivity activity, ArrayList<User> userArrayList){
        this.userArrayList=userArrayList;
        this.acticity=activity;
        ctx=activity;
        activity.setMultichoiceListener(this);
    }

    //////////////////////////////////////////CONTROL METHODS OF INTERFACE/////////////////
    //Called from the Implemented Activity through MultichoiceInterface
    @Override
    public void setSingleModeKey(User user) {
        //this block will be entered when previously u have selected an item
        long singleModeKey = user.getKey();
        if (!multichoiceMode) {
            if (SingleModeKey != -1) {

                //checking previous view and current view is same or not
                if (singleModeKey == SingleModeKey) {
                    singleTapModeDone(SingleModeKey,user);

                } else {

                    changeToSelectedOrDeselect(singleModeKey, SingleModeKey);
                    SingleModeKey = singleModeKey;
                    singleTapModeOn(SingleModeKey);
                }
            } else {

                changeToSelectedOrDeselect(singleModeKey, SingleModeKey);
                SingleModeKey = singleModeKey;
                singleTapModeOn(SingleModeKey);
            }
        } else {
            //multichoice mode on so add it in list
            if(!isInMultichoicealready(user)) {
                SingleModeKey = -1;
                multichoiceMode = true;
                multiChoiceUserListTemp.add(user);
                //notify adapter
                changeToSelectedOrDeselectMultiChoice(user,true);
                //notifying activity
                multiChoiceModeOn((ArrayList<User>) multiChoiceUserListTemp.clone(), multichoiceMode);
            }else{
                removeSelectionInSingleClick(user);
                //notifying adapter
                changeToSelectedOrDeselectMultiChoice(user,false);
            }
        }
    }

    @Override
    public void addMultichoiseModeKey(User u) {

        if(!isInMultichoicealready(u)) {
            multichoiceMode = true;
            multiChoiceUserListTemp.add(u);
            changeToSelectedOrDeselectMultiChoice(u,true);
            multiChoiceModeOn((ArrayList<User>) multiChoiceUserListTemp.clone(), multichoiceMode);
        }else{
            //if already selected deselect it from activity
            removeSelectionInSingleClick(u);
            //notifying adapter
            changeToSelectedOrDeselectMultiChoice(u,false);
        }
      /*
        multichoiceMode=true;
        multiChoiceUserListTemp.add(u);
        changeToSelectedOrDeselectMultiChoice(u);
        multiChoiceModeOn((ArrayList<User>) multiChoiceUserListTemp.clone(), multichoiceMode);*/
    }
    @Override
    public void clearMultichoiceMode(){
        multiChoiceUserListTemp.clear();
        multichoiceMode = false;
        changeToSelectedOrDeselect(-1, -1);
        multiChoiceModeOff();
    }
    @Override
    public void clearSingleChoiceMode(){
        SingleModeKey = -1;
        changeToSelectedOrDeselect(-1,-1);
        singleTapModeOff();
    }

    public boolean isInMultichoicealready(User u){
        long key=u.getKey();
        for(User taskTemp:multiChoiceUserListTemp){
            if(taskTemp.getKey()==key){
                return true;
            }
        }
        return  false;
    }

    public void removeSelectionInSingleClick(User u){
        long key=u.getKey();;
        ArrayList<User> tempList= (ArrayList<User>) multiChoiceUserListTemp.clone();
        for(User taskTemp:tempList){
            if(taskTemp.getKey()==key){
                multiChoiceUserListTemp.remove(u);
            }
        }
        multiChoiceModeOn((ArrayList<User>) multiChoiceUserListTemp.clone(), multichoiceMode);
    }
    //////////////////////////////////////////CONTROL METHODS OF INTERFACE FINISH/////////////////

    abstract void singleTapModeOn(long key);
    abstract void singleTapModeDone(long key,User user);
    abstract void multiChoiceModeOn(ArrayList<User> userList, boolean mode);
    abstract void singleTapModeOff();
    abstract void multiChoiceModeOff();
    abstract void notifyDatasetChangeCustom(ArrayList<User> userList);


    /// Class Methods

    //use this method to select or desect keys from both single and multichoice
    //key=-1,prevKey=-1===== deselect/clear all
    //key=value, prevKey=-1 ===== select item
    // key=value, prevKey =value ==== select key and deselt prevkey
    public void changeToSelectedOrDeselect(long key, long prevKey){
        //1.clearing everything
        ArrayList<User> cloneUsers= new ArrayList<>();
        if(prevKey==-1 && key==-1){
            for(User user: userArrayList ){
                user.setUserState(false);
                cloneUsers.add(user);
            }
            notifyDatasetChangeCustom((ArrayList<User>) cloneUsers.clone());
        }
        //2. Select Item
        if(prevKey==-1 && key>-1){
            for(User user: userArrayList ){
                if(user.getKey()==key) {
                    user.setUserState(true);
                }
                else {
                    user.setUserState(false);
                }
                cloneUsers.add(user);
            }

            notifyDatasetChangeCustom((ArrayList<User>) cloneUsers.clone());
        }

        //2. Deselect prev & Select Item
        if(prevKey>-1 && key>-1){
            for(User user: userArrayList ){
                if(user.getKey()==prevKey){
                    user.setUserState(false);
                    cloneUsers.add(user);
                }
                else if(user.getKey()==key) {
                    user.setUserState(true);
                    cloneUsers.add(user);
                }else{
                    user.setUserState(false);
                    cloneUsers.add(user);
                }

            }
            notifyDatasetChangeCustom((ArrayList<User>) cloneUsers.clone());
        }

    }

    public void changeToSelectedOrDeselectMultiChoice(User u, boolean select) {
        ArrayList<User> cloneUsers= new ArrayList<>();
      /*  for (User user : userArrayList) {
            if (user.getKey() == u.getKey()) {
                user.setUserState(true);
            }
            notifyDatasetChangeCustom((ArrayList<User>) userArrayList.clone());
        }*/

        for (User user : userArrayList) {
            if (user.getKey() == u.getKey()) {
                if(select)
                    user.setUserState(true);
                else user.setUserState(false);

                break;
            }

        }
        notifyDatasetChangeCustom((ArrayList<User>) userArrayList.clone());

    }

}
