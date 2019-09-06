package com.autismindd.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.activities.UserListActivity;
import com.autismindd.activities.WelcomeActivity;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.share.Share;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.FileProcessing;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.TaskImageDownload;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RAFI on 3/7/2017.
 */

public class DailyUpdateCheck {
    Context context;
    WelcomeActivity activity;
    IDatabaseManager databaseManager;
    ImageProcessing imageProcessing;
    FileProcessing fileProcessing;
    Share share;
    ProgressDialog pDialogUnzip;

    public DailyUpdateCheck(Context context) {
        this.context = context;
        activity = (WelcomeActivity) context;
        databaseManager = new DatabaseManager(activity);
        imageProcessing = new ImageProcessing(activity);
        fileProcessing = new FileProcessing(activity);

    }


    //  execute  asyncTask for downloading Json Data
    public void executeDailyCheck() {
        new JsonReading().execute();
    }

    // asyncTask for downloading Json Data 10 taskPack information for FirstLayer
    public class JsonReading extends AsyncTask<String, String, String> {
        Share share;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            share = new Share(activity);
            activity.pDialog = new ProgressDialog(activity);
            activity.pDialog.setMessage(activity.getString(R.string.pleaseWait));
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

/*
            try {
                requestForFirstLayer(json_url);
                share.unZip(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            share.readSharedTaskPackJSONtoDatabase();
            share.deleteReceivedFolder();

            return null;*/
            try {
                // task pack download Class  and unzip taskpack also delete zip file
                activity.requestForFirstLayer(activity.json_url, StaticAccess.TAG_DAILY_JSON_DATA_FLAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {


        }


    }

    // execute checking file name asyncTaskData
    public void executeCheckFileNameAsync() {

        new CheckFileNameAsync().execute();
    }

    //from server data file name check
    class CheckFileNameAsync extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogUnzip = new ProgressDialog(activity);
            pDialogUnzip.setMessage(context.getString(R.string.synMessage));
            pDialogUnzip.setIndeterminate(false);
            pDialogUnzip.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogUnzip.setCancelable(false);
            pDialogUnzip.show();
            DialogNavBarHide.navBarHide(activity, pDialogUnzip);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                activity.arrLocalFlayerdb = new ArrayList<>();
                activity.arrFirstUpdateList = new ArrayList<>();

                activity.arrLocalFlayerdb = databaseManager.listFirstLayer();
                Log.d("download", "downloading  local db list size: " + String.valueOf(activity.arrLocalFlayerdb.size()));
                Log.d("download", "downloading  server db list size: " + String.valueOf(activity.arrDailyFlayer.size()));

                if (activity.arrLocalFlayerdb != null && activity.arrLocalFlayerdb.size() > 0 &&
                        activity.arrDailyFlayer != null && activity.arrDailyFlayer.size() > 0) {
                    for (int i = 0; i < activity.arrLocalFlayerdb.size(); i++) {
                        //different file name check and added arrFirstUpdateList(firstLayer)
                        for (int j = 0; j < activity.arrDailyFlayer.size(); j++) {
                            if (activity.arrDailyFlayer.get(j).getFirstLayerTaskID() == activity.arrLocalFlayerdb.get(i).getFirstLayerTaskID()) {
                                if (!activity.arrDailyFlayer.get(j).getFileName().equals(activity.arrLocalFlayerdb.get(i).getFileName())) {
                                    activity.arrFirstUpdateList.add(activity.arrLocalFlayerdb.get(i));
                                }
                            }
                        }

                    }
//                    SharedPreferenceValue.setPacksIds(activity, activity.arrFirstUpdateList);

                }
                Log.d("download", "downloading  arrFirstUpdateList  size: " + String.valueOf(activity.arrFirstUpdateList.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialogUnzip.dismiss();
            if (activity.isDailyDownload) {
                //downloading TaskPack asyncTask
                SynTaskPack synTaskPack = new SynTaskPack(activity);
                synTaskPack.downloadStart();
            }

        }

    }
}
