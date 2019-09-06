package com.autismindd.services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.autismindd.R;
import com.autismindd.activities.WelcomeActivity;
import com.autismindd.share.Share;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.TaskImageDownload;


import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by RAFI on 1/2/2017.
 */

public class DownloadService extends Service {
    Context context;

    //    String url = "http://192.52.243.6/MindApp/ImagesManager/TaskPackZipImages?fileName=packImages.zip";
    String url = StaticAccess.ROOT_URL+"Download/GetFile?file=packImages.zip";
    //    String taskImageDownloadUrl="";
    private static final String TAG = "Service";

    private boolean isRunning = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning)
//            makeRequest(url);
            new JsonReading(url).execute();
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }

  /*  private void makeRequest(String url) {
        TaskImageDownload taskImage = new TaskImageDownload(getApplicationContext(), url);
        taskImage.imgDownload();
        if (taskImage.isDownload) {
            SharedPreferenceValue.setInsert(getApplicationContext(), true);
            SharedPreferenceValue.setDate(getApplicationContext(), System.currentTimeMillis());
            stopSelf();
        }

    }*/

    class JsonReading extends AsyncTask<String, String, String> {
        String url;
        //        Share share;
        TaskImageDownload taskImageDownload;

        public JsonReading(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  share = new Share(context);
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.pleaseWait));
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            taskImageDownload = new TaskImageDownload(getApplicationContext(), url);
            taskImageDownload.imgDownload();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            stopSelf();
        }

    }
}


