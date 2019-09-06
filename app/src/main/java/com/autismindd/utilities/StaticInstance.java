package com.autismindd.utilities;

import com.autismindd.dao.ErrorUserLog;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.TaskPack;
import com.autismindd.pojo.ErrorLogModel;
import com.autismindd.dao.User;

import java.util.ArrayList;

/**
 * Created by RAFI on 10/3/2016.
 */

public class StaticInstance {
    private static StaticInstance instance = null;

    // Getter-Setters
    public static StaticInstance getInstance() {
        if (instance == null) {
            instance = new StaticInstance();
            return instance;
        } else
            return instance;
    }

    public static void setInstance(StaticInstance instance) {
        StaticInstance.instance = instance;
    }

    private User user;
    private FirstLayer firstLayer;
    private TaskPack level;
    private ErrorUserLog errorUserLog;

    public boolean isLevelUp() {
        return isLevelUp;
    }

    public void setLevelUp(boolean levelUp) {
        isLevelUp = levelUp;
    }

    private  boolean isLevelUp=false;


    private int pvUserStar;
    // this collection for only task basis and update if getting any error;
    private ArrayList<ErrorLogModel> errorLogModelList = new ArrayList<>();


    private StaticInstance() {
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void clearUser() {
        user = null;
    }


    public FirstLayer getFirstLayer() {
        return firstLayer;
    }

    public void setFirstLayer(FirstLayer firstLayer) {
        this.firstLayer = firstLayer;
    }

    public TaskPack getLevel() {
        return level;
    }

    public void setLevel(TaskPack level) {
        this.level = level;
    }

    public ErrorUserLog getErrorUserLog() {
        return errorUserLog;
    }

    public void setErrorUserLog(ErrorUserLog errorUserLog) {
        this.errorUserLog = errorUserLog;
    }

    public ArrayList<ErrorLogModel> getErrorLogModelsList() {
        return errorLogModelList;
    }

    public void setErrorLogModelList(ErrorLogModel errorLogModelList) {
        this.errorLogModelList.add(errorLogModelList);
    }

    public void clearFirstLayer() {
        firstLayer = null;
    }

    public void clearLevel() {
        level = null;
    }

    public void clearErrorArray() {
        errorLogModelList.clear();
    }

    public void clearAll() {
        user = null;
        firstLayer = null;
        level = null;
        errorUserLog = null;
        errorLogModelList.clear();
        isLevelUp=false;
    }

    public boolean isTaskIdArray(long taskId) {

        boolean has = false;
        if (errorLogModelList != null)
            for (int i = 0; i < errorLogModelList.size(); i++) {
                if (taskId == errorLogModelList.get(i).getTaskId()) {
                    has = true;
                } else {
                    has = false;
                }

            }
        return has;
    }

    public int getPvUserStar() {
        return pvUserStar;
    }

    public void setPvUserStar(int pvUserStar) {
        this.pvUserStar = pvUserStar;
    }

    public void clearPvUserStar() {
        pvUserStar = 0;
    }

}
