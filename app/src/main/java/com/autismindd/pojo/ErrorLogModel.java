package com.autismindd.pojo;

import com.autismindd.dao.User;

/**
 * Created by RAFI on 10/4/2016.
 */

public class ErrorLogModel {
    //    User user;
    int firstLayerId;
    int level;
    long taskId;
    Long userKey;
    public Long getUserKey() {
        return userKey;
    }

    public void setUserKey(Long userKey) {
        this.userKey = userKey;
    }



    public int getFirstLayerId() {
        return firstLayerId;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getLevelID() {
        return level;
    }

    public void setLevelID(int level) {
        this.level = level;
    }

  /*  public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/


    public void setFirstLayerId(int firstLayerId) {
        this.firstLayerId = firstLayerId;
    }
}
