package com.autismindd.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.activities.PurchaseActivity;
import com.autismindd.activities.WelcomeActivity;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.pojo.FLayer;
import com.autismindd.share.Share;
import com.autismindd.utilities.FileProcessing;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.StaticAccess;

import java.util.ArrayList;

/**
 * Created by RAFI on 3/9/2017.
 */

public class PurchaseSyncTask {

    Context context;
    PurchaseActivity activity;



    public PurchaseSyncTask(Context context) {
        this.context = context;
        activity = (PurchaseActivity) context;


    }

    public void getCheckFileNameAsync() {
        new CheckFileNameAsync().execute();
    }

    //from server data file name check
    class CheckFileNameAsync extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                activity.arrFirstUpdateList = new ArrayList<>();
                Log.d("download", "downloading  local List size: " + String.valueOf(activity.firstLayerList.size()));
                Log.d("download", "downloading  dailyList size: " + String.valueOf(activity.arrDailyFlayer.size()));

                if (activity.firstLayerList != null && activity.firstLayerList.size() > 0)
                    for (int i = 0; i < activity.firstLayerList.size(); i++) {

                        for (int j = 0; j < activity.arrDailyFlayer.size(); j++) {

                            if (activity.arrDailyFlayer.get(j).getFirstLayerTaskID()==(activity.firstLayerList.get(i).getFirstLayerTaskID()))
                            if (!activity.arrDailyFlayer.get(j).getFileName().equals(activity.firstLayerList.get(i).getFileName())) {
                                activity.arrFirstUpdateList.add(activity.firstLayerList.get(i));
                            }
                        }
                    }
                Log.d("download", "downloading  file size: " + String.valueOf(activity.arrFirstUpdateList.size()));
                if (activity.arrFirstUpdateList != null && activity.arrFirstUpdateList.size() > 0)
                    getDownloadedPacksIds(activity.arrFirstUpdateList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            activity.pDialogUnzip.dismiss();
            activity.gvLoad();
        }

    }

    // get FirstLayerTask Ids when doesn't match .map file name
    private void getDownloadedPacksIds(ArrayList<FirstLayer> arrFirstUpdateList) {
        ArrayList<Integer> updateTaskPacksIDS = new ArrayList<>();
        for (int i = 0; i < arrFirstUpdateList.size(); i++) {
            if (arrFirstUpdateList.get(i).getLocked()) {
                Log.d("download", "downloading firstLayer Locked Packed File Name: " + arrFirstUpdateList.get(i).getFileName() + " to the schema.");
                //locked file name
                updateTaskPacksIDS.add(arrFirstUpdateList.get(i).getFirstLayerTaskID());
            }
        }
        Log.d("download", "downloading  file size: " + String.valueOf(updateTaskPacksIDS.size()));
        if (updateTaskPacksIDS.size() > 0) {
            for (int i = 0; i < updateTaskPacksIDS.size(); i++) {
//                arrFirstLayerID.get(i).
                if (activity.arrDailyFlayer != null && activity.arrDailyFlayer.size() > 0)
                    for (int j = 0; j < activity.arrDailyFlayer.size(); j++) {
                        if (activity.arrDailyFlayer.get(j).getFirstLayerTaskID() == updateTaskPacksIDS.get(i)) {
                            updateFLayer(activity.arrDailyFlayer.get(j));
                        }
                    }
            }
        }
    }

    // Local database FirsLayer and FirstLayerImages table update
    public void updateFLayer(FLayer fLayer) {

        if (activity.firstLayerList != null && activity.firstLayerList.size() > 0) {
            for (int i = 0; i < activity.firstLayerList.size(); i++) {
                if (fLayer.getFirstLayerTaskID() == activity.firstLayerList.get(i).getFirstLayerTaskID()) {
                    FirstLayer dbFirstLayer = new FirstLayer();
                    dbFirstLayer.setId(activity.firstLayerList.get(i).getId());
                    dbFirstLayer.setName(fLayer.getName());
                    dbFirstLayer.setFileName(fLayer.getFileName());
                    dbFirstLayer.setFirstLayerTaskID(fLayer.getFirstLayerTaskID());
                    dbFirstLayer.setImgUrl(fLayer.getImgUrl());
                    dbFirstLayer.setLocked(activity.firstLayerList.get(i).getLocked());
                    dbFirstLayer.setState(activity.firstLayerList.get(i).getState());

                    activity.databaseManager.updateFirstLayer(dbFirstLayer);

                    boolean isDeleteImages = activity.databaseManager.deleteImageList(fLayer.getFirstLayerTaskID());
                    if (isDeleteImages) {
                        for (int j = 0; j < fLayer.getfLayerImagesList().size(); j++) {
                            FirstLayerTaskImage firstLayerTaskImage = new FirstLayerTaskImage();
                            firstLayerTaskImage.setFirstLayerTaskDes(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskDes());
                            firstLayerTaskImage.setFirstLayerTaskId(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskId());
                            firstLayerTaskImage.setFirstLayerTaskImages(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskImages());
                            activity.databaseManager.insertFLayerTaskImage(firstLayerTaskImage);
                        }
                    }
                }
            }
        }
    }
}
