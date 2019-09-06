package com.autismindd.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.adapter.UserListAdapter;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.Item;
import com.autismindd.dao.Task;
import com.autismindd.dao.TaskPack;
import com.autismindd.download.SynTaskPack;
import com.autismindd.listener.MultichoiceAdapterInterface;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.R;

import com.autismindd.dao.User;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.share.Share;
import com.autismindd.utilities.ArrowDownloadButton;
import com.autismindd.utilities.ConnectionManagerPromo;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.FileProcessing;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;

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
 * Created by RAFI on 9/27/2016.
 */

public class UserListActivity extends MultichoiceViewActivity {
    private IDatabaseManager databaseManager;
    UserListAdapter userListAdapter;
    public ArrayList<User> userList = new ArrayList<>();
    GridView gvUser;
    long UserId;
    Share share;
    int mode = 0;
    ImageButton ibtnBackTask;
    //public RelativeLayout rlSync;
    //public ArrowDownloadButton adbSync;
    UserListActivity ref;
    StaticInstance staticInstance;
    UserInfo userInfo;
    ActionMode actionMode;
    // this arraylist only for selected user
    ArrayList<User> selectedForDelete = new ArrayList<>();
    ArrayList<String> selections = new ArrayList<>();
    User user;
    MultichoiceAdapterInterface.ControlMethods multiChoice_listener;
    long singelTappedTaskPack = -1;
    String lampSound = "sound_click_lamp.mp3";
    boolean musicControl = false;
//    SynTaskPack synTaskPack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseManager = new DatabaseManager(this);
        share = new Share(activity);
        ref = this;

        staticInstance = StaticInstance.getInstance();
        staticInstance.clearAll();
        userInfo = new UserInfo();
        gvUser = (GridView) findViewById(R.id.gvUserList);
//        synTaskPack = new SynTaskPack(activity);
        //rlSync = (RelativeLayout) findViewById(R.id.rlSync);
        //adbSync = (ArrowDownloadButton) findViewById(R.id.adbSync);

        ibtnBackTask = (ImageButton) findViewById(R.id.ibtnBackTask);
        ibtnBackTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        getUsersFromDatabaseAndInitializeAdapter();
        gvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //communicating with MultiChoiceAdapter
                // (Single click on multichoce enabled mode is mandled in multichoice adapter not here)
                if (multiChoice_listener != null) {
                    if (userList != null) {
                        multiChoice_listener.setSingleModeKey(userList.get(position));
                        user = userList.get(position);
                    }
                }
            }
        });
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        gvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (multiChoice_listener != null) {
                    if (userList != null) {
                        multiChoiceMode = true;
                        //this call will automatically Direct to multiChoiceModeEnter() int his activity
                        multiChoice_listener.clearSingleChoiceMode();
                        multiChoice_listener.addMultichoiseModeKey(userList.get(position));
                        vibe.vibrate(100);
                    }
                }

                return false;
            }
        });


    }


    // adapter implement dor userList for database manager
    private void getUsersFromDatabaseAndInitializeAdapter() {
        userList = (ArrayList<User>) databaseManager.listUsers();
        if (userList != null) {
            userListAdapter = new UserListAdapter(this, userList);
            gvUser.setAdapter(userListAdapter);
        }

    }

    // delete selected users
    public void deleteSelectedUsers(long UserKey) {
        userList_multichoiceTemp.clear();
        if (UserKey != -1) {
            databaseManager.deleteUserByKey(UserKey);
            databaseManager.deleteErrorUserLogByUserKey(UserKey);
            databaseManager.deleteStarUserKey(UserKey);
        }

        getUsersFromDatabaseAndInitializeAdapter();
    }

    /////////******************* CONTROL LOGICS ********************************//
    //called from multichoice Adapter
    @Override
    public void setMultichoiceListener(MultichoiceAdapterInterface.ControlMethods listener) {
        this.multiChoice_listener = listener;
    }

    @Override
    public void singleTapModeOn(long key) {
        singelTappedTaskPack = key;
    }

    @Override
    public void singleTapDone(long key, User user) {
        musicControl = true;
        Intent intent = new Intent(ref, FirstLayerActivity.class);
        staticInstance.setUser(user);
        intent.putExtra("key", key);
        startActivity(intent);
        finish();
    }

    ArrayList<User> userList_multichoiceTemp = new ArrayList<>();
    boolean multiChoiceMode = false;

    @Override
    public void multiChoiceModeEnter(ArrayList<User> userList, boolean mode) {
        //do not call listener from here
        singelTappedTaskPack = -1;
        userList_multichoiceTemp = userList;
        multiChoiceMode = mode;
    }

    @Override
    public void multiChoiceClear() {
        singelTappedTaskPack = -1;
        userList_multichoiceTemp.clear();
        multiChoiceMode = false;
        multiChoice_listener.clearMultichoiceMode();
    }

    @Override
    public void rendomOutSideClicked(View v) {
        singelTappedTaskPack = -1;
        userList_multichoiceTemp.clear();
        multiChoiceMode = false;
        multiChoice_listener.clearMultichoiceMode();
        multiChoice_listener.clearSingleChoiceMode();
    }

    @Override
    public void firstButtonClicked(View v) {
        mode = StaticAccess.USER_CREATE;
        musicControl = true;
        //add button
        Intent intent = new Intent(UserListActivity.this, UserCreateActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
        finish();
    }

    @Override
    public void secondButtonClicked(View v) {
        mode = StaticAccess.USER_EDIT;
        // Edit user info
        if (user != null) {
            musicControl = true;
            Intent intent = new Intent(UserListActivity.this, UserCreateActivity.class);
            intent.putExtra("CurrentUser", user.getId());
            intent.putExtra("mode", mode);
            startActivity(intent);
            finish();
        } else if (userList.size() == 0) {
            CustomToast.t(ref, getString(R.string.userCreate));
        } else {
            CustomToast.t(ref, getString(R.string.userSelect));
        }

    }

    @Override
    public void thirdButtonClicked(View v) {

        //statistics
        if (user != null) {
            staticInstance.setUser(user);
            musicControl = true;
            Intent intent = new Intent(UserListActivity.this, StatisticsLayer.class);
            startActivity(intent);
            finish();
        } else if (userList.size() == 0) {
            CustomToast.t(ref, getString(R.string.userCreate));
        } else {
            CustomToast.t(ref, getString(R.string.userSelect));
        }


    }

    @Override
    public void fourthButtonClicked(View v) {
        //delete button listener
        if (user != null) {
            dialogDeletePermission();
        } else if (userList.size() == 0) {
            CustomToast.t(ref, getString(R.string.userCreate));
        } else {
            CustomToast.t(ref, getString(R.string.userSelect));
        }


    }

    @Override
    public void fifthButtonClicked(View v) {

      /*  if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
            synTaskPack.downloadCount = 0;
            synTaskPack.dialogSyncPermission();
//                    new DeleteAsyncTask().execute();
        } else
            CustomToast.t(activity, getResources().getString(R.string.connectInternet));
*/

    }

    @Override
    public void onBackPressed() {
        musicControl = true;
        Intent intent = new Intent(UserListActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }


    // dialog for Delete Permission created by Rokan
    public void dialogDeletePermission() {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_back_permission);
        dialog.setCancelable(false);

        final TextView tvBackPermission = (TextView) dialog.findViewById(R.id.tvBackPermission);
        tvBackPermission.setText(getResources().getText(R.string.DeletePermission));

        Button btnCancelPermission = (Button) dialog.findViewById(R.id.btnCancelPermission);
        Button btnOkPermission = (Button) dialog.findViewById(R.id.btnOkPermission);


        btnOkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteSelectedUsers(singelTappedTaskPack);
                user = null;
                dialog.dismiss();
            }
        });

        btnCancelPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(this, dialog);


    }

    /*****************************
     * SYNC
     **************************************/
    // for sync
    boolean isDownload = false;
    boolean hasSizeStorage = false;
    boolean isDelete = false;
    ProgressDialog pDialogUnzip;
    ArrayList<TaskPack> selectedForShare;
    ArrayList<FirstLayer> fLayerList;

    /*******************************************************************/

    // dialog for Sync Permission created by Rokan
    public void dialogSyncPermission() {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_back_permission);
        dialog.setCancelable(false);

        final TextView tvBackPermission = (TextView) dialog.findViewById(R.id.tvBackPermission);
        tvBackPermission.setText(R.string.sync_permition);

        Button btnCancelPermission = (Button) dialog.findViewById(R.id.btnCancelPermission);
        Button btnOkPermission = (Button) dialog.findViewById(R.id.btnOkPermission);


        btnOkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rlSync.setVisibility(View.VISIBLE);
                //rlSync.setClickable(true);

                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    downloadCount = 0;
                    new DeleteAsyncTask().execute();
                } else
                    CustomToast.t(activity, getResources().getString(R.string.connectInternet));

                dialog.dismiss();
            }
        });

        btnCancelPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(this, dialog);


    }

    class DeleteAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogUnzip = new ProgressDialog(activity);
            pDialogUnzip.setMessage(getResources().getString(R.string.UnzippinMessage));
            pDialogUnzip.setIndeterminate(false);
            pDialogUnzip.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogUnzip.setCancelable(false);
            pDialogUnzip.show();
            DialogNavBarHide.navBarHide(activity, pDialogUnzip);

        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                taskPackDelete();
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

            fLayerList = new ArrayList<>();
            if (isDelete) {
                fLayerList = databaseManager.getStatusTrueFirstLayerList(false);
                if (fLayerList.size() > 0) {
                    // asyntask
                    pDialogUnzip.dismiss();
                    new DownloadFileFromURL(getUrl(downloadCount), fLayerList.get(downloadCount)).execute();
                }
            } else
                CustomToast.t(activity, getResources().getString(R.string.connectInternet));

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

    @Override
    public void onResume() {
        super.onResume();
        BackgroundMusicService.startMusic(activity);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }

    int downloadCount = 0;

    //for Download
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String download_url;
        FirstLayer firstLayer;

        public DownloadFileFromURL(String download_url, FirstLayer firstLayer) {
            this.download_url = download_url;
            this.firstLayer = firstLayer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //rlSync.setVisibility(View.VISIBLE);
            //rlSync.setClickable(true);
            //adbSync.setVisibility(View.VISIBLE);
            //adbSync.startAnimating();
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
                Log.e("size ", "file " + String.valueOf(lenghtOfFile));
                Log.e("size ", "Device " + String.valueOf(getInternalAvailableSpace()));
                if (lenghtOfFile < getInternalAvailableSpace()) {
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindApp));
                    if (!sdCardDirectory.exists()) {
                        sdCardDirectory.mkdirs();
                    }

                    OutputStream output = new FileOutputStream(getResources().getString(R.string.sdcardLocation) + firstLayer.getFileName());
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
            //adbSync.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
//            dismissDialog(progress_bar_type);
//            rlDownloadBack.setClickable(true);
            //rlSync.setVisibility(View.GONE);
            //adbSync.reset();
            //adbSync.setVisibility(View.GONE);
            if (isDownload) {
                new UnzipFromDownloadedFolder(firstLayer).execute();
                //AllButtonVisibility(true);

            } else if (hasSizeStorage) {
                hasSizeStorage = false;
                CustomToast.t(activity, getResources().getString(R.string.storageCheck));
                //AllButtonVisibility(true);
            } else
                CustomToast.t(activity, getResources().getString(R.string.downloadFailed));
            //AllButtonVisibility(true);
        }
    }


    class UnzipFromDownloadedFolder extends AsyncTask<String, String, String> {
        FirstLayer firstLayer;

        public UnzipFromDownloadedFolder(FirstLayer firstLayer) {
            this.firstLayer = firstLayer;
        }

        String FILE_PATH = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogUnzip = new ProgressDialog(activity);
            pDialogUnzip.setMessage(getResources().getString(R.string.UnzippinMessage));
            pDialogUnzip.setIndeterminate(false);
            pDialogUnzip.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogUnzip.setCancelable(false);
            pDialogUnzip.show();
            DialogNavBarHide.navBarHide(activity, pDialogUnzip);
            FILE_PATH = Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindAppSlash) + firstLayer.getFileName();
        }

        @Override
        protected String doInBackground(String... params) {

            // here confuse
            SharedPreferenceValue.setDownloadFlag(activity, StaticAccess.TAG_SP_DOWNLOAD_FLAG);
            share = new Share(activity);

            try {
                share.unZip(FILE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }

            share.readSharedTaskPackJSONtoDatabase();
            share.deleteReceivedFolder();


            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialogUnzip.dismiss();
//            rlFullPurchase.setClickable(false);
            if (share.getIsInstallStatus() && isDownload) {
                firstLayer.setLocked(false);
                firstLayer.setState(false);
                databaseManager.updateFirstLayer(firstLayer);
                downloadCount++;
                if (downloadCount < fLayerList.size()) {
                    new DownloadFileFromURL(getUrl(downloadCount), firstLayer).execute();
                } else {
                    //when everything done
                }


            } else
                CustomToast.t(activity, getResources().getString(R.string.installFailed));


        }

    }

    // delete taskPack for sync
    public void taskPackDelete() {
        selectedForShare = new ArrayList<>();
        selectedForShare = (ArrayList<TaskPack>) databaseManager.listTaskPacks();
        if (selectedForShare != null) {
            for (TaskPack taskPack : selectedForShare) {
                ImageProcessing imageProcessing = new ImageProcessing(activity);
                FileProcessing fileProcessing = new FileProcessing(activity);
                ArrayList<Task> tasks = (ArrayList<Task>) databaseManager.listTasksByTAskPackId(taskPack.getId());
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
                    }
                }
                databaseManager.deleteTaskPackById(taskPack.getId());
            }
        }
        if (selectedForShare != null && selectedForShare.size() > 0) {
            selectedForShare.clear();
        }
        //fabMenu.close(true);

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


    /*****************************  end Sync **************************************/
}
