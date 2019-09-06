package com.autismindd.pojo;

import java.util.ArrayList;

/**
 * Created by RAFI on 3/7/2017.
 */

public class FLayer {
    String name;
    String fileName;
    String imgUrl;
    int firstLayerTaskID;
    boolean locked;
    boolean state;
    ArrayList<FLayerImages> fLayerImagesList;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFirstLayerTaskID() {
        return firstLayerTaskID;
    }

    public void setFirstLayerTaskID(int firstLayerTaskID) {
        this.firstLayerTaskID = firstLayerTaskID;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public ArrayList<FLayerImages> getfLayerImagesList() {
        return fLayerImagesList;
    }
    public void setfLayerImagesList(ArrayList<FLayerImages> fLayerImagesList) {
        this.fLayerImagesList = fLayerImagesList;
    }


}
