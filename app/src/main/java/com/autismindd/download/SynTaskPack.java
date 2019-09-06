package com.autismindd.download;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.activities.PlayerActivity;
import com.autismindd.activities.TaskPackActivity;
import com.autismindd.activities.UserListActivity;
import com.autismindd.activities.WelcomeActivity;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.dao.Item;
import com.autismindd.dao.Task;
import com.autismindd.dao.TaskPack;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.pojo.FLayer;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.share.Share;
import com.autismindd.utilities.ConnectionManagerPromo;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.FileProcessing;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.TaskImageDownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by RAFI on 3/5/2017.
 */

public class SynTaskPack {
    Context context;
    WelcomeActivity activity;
    IDatabaseManager databaseManager;
    ImageProcessing imageProcessing;
    FileProcessing fileProcessing;
    Share share;

    public SynTaskPack(Context context) {
        this.context = context;
        activity = (WelcomeActivity) context;
        databaseManager = new DatabaseManager(activity);
        imageProcessing = new ImageProcessing(activity);
        fileProcessing = new FileProcessing(activity);


    }

    /******************************
     * SYNC
     **************************************/
    // for sync
    boolean isDownload = false;
    boolean hasSizeStorage = false;
    boolean isDelete = false;
    ProgressDialog pDialogUnzip;
    ArrayList<TaskPack> selectedForShare;
    ArrayList<FLayer> fLayerList;

    /*******************************************************************/

    // dialog for Sync Permission created by Rokan
    public void dialogSyncPermission() {
        final Dialog dialog = new Dialog(activity, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_back_permission);
        dialog.setCancelable(false);

        final TextView tvBackPermission = (TextView) dialog.findViewById(R.id.tvBackPermission);
        tvBackPermission.setText(R.string.sync_permition);
        tvBackPermission.setGravity(Gravity.LEFT);
        tvBackPermission.setTextSize(18);

        Button btnCancelPermission = (Button) dialog.findViewById(R.id.btnCancelPermission);
        Button btnOkPermission = (Button) dialog.findViewById(R.id.btnOkPermission);
        btnOkPermission.setText(R.string.update);

        btnOkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    downloadCount = 0;
                    new DownloadFileFromURL(getUrl(downloadCount), fLayerList.get(downloadCount)).execute();
                } else
                    CustomToast.t(activity, activity.getResources().getString(R.string.connectInternet));
                dialog.dismiss();
            }
        });

        btnCancelPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(activity, dialog);


    }

    // prev taskPack delete when download complete TaskPack for different file (.map) name
    class DeleteAsyncTask extends AsyncTask<String, String, String> {
        FLayer fLayer;

        public DeleteAsyncTask(FLayer fLayer) {
            this.fLayer = fLayer;
        }

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
        protected String doInBackground(String... f_url) {
            try {
                taskPackDelete(fLayer.getFirstLayerTaskID());
                isDelete = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                isDelete = false;
            }
            return null;
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            pDialogUnzip.dismiss();
            if (isDownload && isDelete) {
                new UnzipFromDownloadedFolder(fLayer).execute();
            }

        }
    }

    // getDounload taskPack URL by downloadCount
    String getUrl(int count) {
        String url = null;
        if (fLayerList != null && fLayerList.size() > 0) {
            url = StaticAccess.ROOT_URL + "ImagesManager/GetPackByID/?id=" + fLayerList.get(count).getFirstLayerTaskID() +
                    "&authenticationUserName=AutisMind&password=dniMsituA";
        }

        return url;
    }

    public int downloadCount = 0;

    //for Download
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String download_url;
        FLayer fLayer;

        public DownloadFileFromURL(String download_url, FLayer fLayer) {
            this.download_url = download_url;
            this.fLayer = fLayer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.dwnBack.setVisibility(View.VISIBLE);
            activity.dwnBack.setClickable(true);
            activity.downloadBtnWelcome.setVisibility(View.VISIBLE);
            activity.downloadBtnWelcome.startAnimating();
            // AllButtonVisibility(false);


        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                java.net.URL url = new URL(download_url);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
//                long lenghtOfFile = 3595898880L;
                Log.e("size ", "file size " + String.valueOf(lenghtOfFile));
                Log.e("size ", "Device size " + String.valueOf(getInternalAvailableSpace()));
                if (lenghtOfFile < getInternalAvailableSpace()) {
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + activity.getResources().getString(R.string.MindApp));
                    if (!sdCardDirectory.exists()) {
                        sdCardDirectory.mkdirs();
                    }

                    OutputStream output = new FileOutputStream(activity.getResources().getString(R.string.sdcardLocation) + fLayer.getFileName());
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }
                    // flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();
                    isDownload = true;
                } else {
                    hasSizeStorage = true;
                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());

                isDownload = false;

            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {

//            pDialog.setProgress(Integer.parseInt(progress[0]));
            activity.downloadBtnWelcome.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
//            dismissDialog(progress_bar_type);
//            rlDownloadBack.setClickable(true);
            activity.dwnBack.setVisibility(View.GONE);
            activity.downloadBtnWelcome.reset();
            activity.downloadBtnWelcome.setVisibility(View.GONE);
            if (isDownload) {
                Log.d("download", "downloading firstLayer : " + fLayer.getFileName());
                new DeleteAsyncTask(fLayer).execute();
                //AllButtonVisibility(true);
            } else if (hasSizeStorage) {
                hasSizeStorage = false;
                CustomToast.t(activity, activity.getResources().getString(R.string.storageCheck));
                //AllButtonVisibility(true);
            } else
                CustomToast.t(activity, activity.getResources().getString(R.string.downloadFailed));
            //AllButtonVisibility(true);
        }
    }

    //Unzip TaskPack and Install
    class UnzipFromDownloadedFolder extends AsyncTask<String, String, String> {
        FLayer fLayer;

        public UnzipFromDownloadedFolder(FLayer fLayer) {
            this.fLayer = fLayer;
        }

        String FILE_PATH = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogUnzip = new ProgressDialog(activity);
            pDialogUnzip.setMessage(activity.getResources().getString(R.string.UnzippinMessage));
            pDialogUnzip.setIndeterminate(false);
            pDialogUnzip.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogUnzip.setCancelable(false);
            pDialogUnzip.show();
            DialogNavBarHide.navBarHide(activity, pDialogUnzip);
            FILE_PATH = Environment.getExternalStorageDirectory() + activity.getResources().getString(R.string.MindAppSlash) + fLayer.getFileName();
        }

        @Override
        protected String doInBackground(String... params) {

            // here confuse

            share = new Share(activity);

            try {
                //for First time packImage download
                if (SharedPreferenceValue.getDate(activity) == 0) {
                    TaskImageDownload taskImageDownload = new TaskImageDownload(activity, activity.taskImageDownloadUrl);
                    taskImageDownload.imgDownload();
                }

                share.unZip(FILE_PATH);
                share.readSharedTaskPackJSONtoDatabase();
                share.deleteRCVFolder();

                File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + activity.getResources().getString(R.string.MindApp));
                share.deleteRecursive(sdCardDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialogUnzip.dismiss();
//            rlFullPurchase.setClickable(false);
            if (share.getIsInstallStatus() && isDownload) {
                isDelete = false;
                SharedPreferenceValue.setDownloadFlag(activity, StaticAccess.TAG_SP_DOWNLOAD_FLAG);
                if (fLayer != null)
                    updateFLayer(fLayer, StaticAccess.TAG_UNLOCKED_FLAG);
                /*fLayer.setLocked(false);
                fLayer.setState(false);
                databaseManager.updateFirstLayer(fLayer);*/
            } else
                CustomToast.t(activity, activity.getResources().getString(R.string.installFailed));


        }

    }

    // delete taskPack for sync
    public void taskPackDelete(int firstLayerID) {
        selectedForShare = new ArrayList<>();

        selectedForShare = (ArrayList<TaskPack>) databaseManager.listTaskPacksFirstLayerID(firstLayerID);
        if (selectedForShare != null) {
            for (TaskPack taskPack : selectedForShare) {
              /*  ImageProcessing imageProcessing = new ImageProcessing(activity);
                FileProcessing fileProcessing = new FileProcessing(activity);*/
                ArrayList<Task> tasks = (ArrayList<Task>) databaseManager.listTasksByTAskPackId(taskPack.getId());
                Log.d("delete", ">>>delete" + String.valueOf(taskPack.getId()));
                if (tasks != null) {
                    for (Task task : tasks) {
                        LinkedHashMap<Long, Item> items = databaseManager.loadTaskWiseItem(task);
                        if (items != null) {
                            for (Map.Entry<Long, Item> itemValue : items.entrySet()) {
                                Item item = itemValue.getValue();
                                imageProcessing.deleteImage(item.getImagePath());
                                fileProcessing.deleteSound(item.getItemSound());
                                databaseManager.deleteItemById(item.getId());
                            }
                        }
                        imageProcessing.deleteImage(task.getTaskImage());
                        imageProcessing.deleteImage(task.getFeedbackImage());
                        imageProcessing.deleteImage(task.getErrorImage());
                        fileProcessing.deleteSound(task.getFeedbackSound());
                        fileProcessing.deleteSound(task.getPositiveSound());
                        fileProcessing.deleteSound(task.getNegativeSound());
                        databaseManager.deleteTaskById(task.getId());
                        Log.d("delete", ">>>Task ID delete" + String.valueOf(task.getId()));
                    }
                }
                databaseManager.deleteTaskPackById(taskPack.getId());
                Log.d("delete", ">>>Task Pack ID delete" + String.valueOf(taskPack.getId()));
            }
        }
        if (selectedForShare != null && selectedForShare.size() > 0) {
            selectedForShare.clear();
        }
        //fabMenu.close(true);

    }

    //Download Start
    public void downloadStart() {
        fLayerList = new ArrayList<>();
        ArrayList<Integer> arrFirstLayerID = new ArrayList<>();
        if (activity.arrFirstUpdateList != null && activity.arrFirstUpdateList.size() > 0)
            arrFirstLayerID = getDownloadedPacksIds(activity.arrFirstUpdateList);
        for (int i = 0; i < arrFirstLayerID.size(); i++) {
//                arrFirstLayerID.get(i).
            if (activity.arrDailyFlayer != null && activity.arrDailyFlayer.size() > 0)
                for (int j = 0; j < activity.arrDailyFlayer.size(); j++) {
                    if (activity.arrDailyFlayer.get(j).getFirstLayerTaskID() == arrFirstLayerID.get(i)) {
                        fLayerList.add(activity.arrDailyFlayer.get(j));
                    }
                }
        }

        Log.d("download", "expected download arraySize : " + String.valueOf(fLayerList.size()));
        if (fLayerList.size() > 0) {
            // dialog for download
            dialogSyncPermission();
        }

    }

    // get FirstLayerTask Ids when doesn't match .map file name
    private ArrayList<Integer> getDownloadedPacksIds(ArrayList<FirstLayer> arrFirstUpdateList) {
        ArrayList<Integer> downloadTaskPacksIDS = new ArrayList<>();
        ArrayList<Integer> updateTaskPacksIDS = new ArrayList<>();
        for (int i = 0; i < arrFirstUpdateList.size(); i++) {
            if (!arrFirstUpdateList.get(i).getLocked()) {
                Log.d("download", "downloading firstLayer : " + arrFirstUpdateList.get(i).getFileName() + " to the schema.");
                downloadTaskPacksIDS.add(arrFirstUpdateList.get(i).getFirstLayerTaskID());
            } else {
                //locked file name
                updateTaskPacksIDS.add(arrFirstUpdateList.get(i).getFirstLayerTaskID());
            }

        }
        if (updateTaskPacksIDS.size() > 0) {
            for (int i = 0; i < updateTaskPacksIDS.size(); i++) {
//                arrFirstLayerID.get(i).
                if (activity.arrDailyFlayer != null && activity.arrDailyFlayer.size() > 0)
                    for (int j = 0; j < activity.arrDailyFlayer.size(); j++) {
                        if (activity.arrDailyFlayer.get(j).getFirstLayerTaskID() == updateTaskPacksIDS.get(i)) {
                            updateFLayer(activity.arrDailyFlayer.get(j), StaticAccess.TAG_LOCKED_FLAG);
                        }
                    }
            }
        }
        return downloadTaskPacksIDS;
    }

    //check internal Storage
    public long getInternalAvailableSpace() {
        long availableSpace = -1L;
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory()
                    .getPath());
            stat.restat(Environment.getDataDirectory().getPath());
            availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableSpace;
    }

    // Local database FirsLayer and FirstLayerImages table update
    public void updateFLayer(FLayer fLayer, int which) {

        if (activity.arrLocalFlayerdb != null && activity.arrLocalFlayerdb.size() > 0) {
            for (int i = 0; i < activity.arrLocalFlayerdb.size(); i++) {
                if (fLayer.getFirstLayerTaskID() == activity.arrLocalFlayerdb.get(i).getFirstLayerTaskID()) {
                    FirstLayer dbFirstLayer = new FirstLayer();
                    dbFirstLayer.setId(activity.arrLocalFlayerdb.get(i).getId());
                    dbFirstLayer.setName(fLayer.getName());
                    dbFirstLayer.setFileName(fLayer.getFileName());
                    dbFirstLayer.setFirstLayerTaskID(fLayer.getFirstLayerTaskID());
                    dbFirstLayer.setImgUrl(fLayer.getImgUrl());
                    dbFirstLayer.setLocked(activity.arrLocalFlayerdb.get(i).getLocked());
                    dbFirstLayer.setState(activity.arrLocalFlayerdb.get(i).getState());

                    databaseManager.updateFirstLayer(dbFirstLayer);

                    boolean isDeleteImages = databaseManager.deleteImageList(fLayer.getFirstLayerTaskID());
                    if (isDeleteImages) {
                        for (int j = 0; j < fLayer.getfLayerImagesList().size(); j++) {
                            FirstLayerTaskImage firstLayerTaskImage = new FirstLayerTaskImage();
                            firstLayerTaskImage.setFirstLayerTaskDes(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskDes());
                            firstLayerTaskImage.setFirstLayerTaskId(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskId());
                            firstLayerTaskImage.setFirstLayerTaskImages(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskImages());
                            databaseManager.insertFLayerTaskImage(firstLayerTaskImage);
                        }
                    }
                }
            }
        }
        if (which == StaticAccess.TAG_UNLOCKED_FLAG) {
            downloadCount++;
            if (downloadCount < fLayerList.size()) {
                new DownloadFileFromURL(getUrl(downloadCount), fLayerList.get(downloadCount)).execute();
            } else {
                //when everything done
            }
        }

    }

    //update locked TaskPack information
    public void updateLockedFLayer(FLayer fLayer) {

        if (activity.arrLocalFlayerdb != null && activity.arrLocalFlayerdb.size() > 0) {
            for (int i = 0; i < activity.arrLocalFlayerdb.size(); i++) {
                if (fLayer.getFirstLayerTaskID() == activity.arrLocalFlayerdb.get(i).getFirstLayerTaskID()) {
                    FirstLayer dbFirstLayer = new FirstLayer();
                    dbFirstLayer.setId(activity.arrLocalFlayerdb.get(i).getId());
                    dbFirstLayer.setName(fLayer.getName());
                    dbFirstLayer.setFileName(fLayer.getFileName());
                    dbFirstLayer.setFirstLayerTaskID(fLayer.getFirstLayerTaskID());
                    dbFirstLayer.setImgUrl(fLayer.getImgUrl());
                    dbFirstLayer.setLocked(activity.arrLocalFlayerdb.get(i).getLocked());
                    dbFirstLayer.setState(activity.arrLocalFlayerdb.get(i).getState());

                    databaseManager.updateFirstLayer(dbFirstLayer);

                    boolean isDeleteImages = databaseManager.deleteImageList(fLayer.getFirstLayerTaskID());
                    if (isDeleteImages) {
                        for (int j = 0; j > fLayer.getfLayerImagesList().size(); j++) {
                            FirstLayerTaskImage firstLayerTaskImage = new FirstLayerTaskImage();
                            firstLayerTaskImage.setFirstLayerTaskDes(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskDes());
                            firstLayerTaskImage.setFirstLayerTaskId(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskId());
                            firstLayerTaskImage.setFirstLayerTaskImages(fLayer.getfLayerImagesList().get(j).getFirstLayerTaskImages());
                            databaseManager.insertFLayerTaskImage(firstLayerTaskImage);
                        }
                    }
                }
            }
        }

      /*  downloadCount++;
        if (downloadCount < fLayerList.size()) {
            new DownloadFileFromURL(getUrl(downloadCount), fLayerList.get(downloadCount)).execute();
        } else {
            //when everything done
        }*/
    }
}
