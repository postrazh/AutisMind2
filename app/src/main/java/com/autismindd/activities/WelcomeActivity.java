package com.autismindd.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.dao.FirstLayer;
import com.autismindd.download.DailyUpdateCheck;
import com.autismindd.parser.DailyFirstLayerParser;
import com.autismindd.parser.FirstLayerJsonParser;
import com.autismindd.pojo.FLayer;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.services.DownloadService;
import com.autismindd.share.Share;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.AppController;
import com.autismindd.utilities.ApplicationMode;
import com.autismindd.utilities.ArrowDownloadButton;
import com.autismindd.utilities.ConnectionManagerPromo;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.TaskImageDownload;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Macbook on 11/10/2016.
 */

public class WelcomeActivity extends BaseActivity {

    ImageView cloud, info;
    private String spDownloadFlag;
    WelcomeActivity activity;
    boolean isJSONDownload = false;
    public ArrowDownloadButton downloadBtnWelcome;
    ProgressDialog pDialogUnzip;
    Share share;
    boolean musicControl = false;
    String FILE_PATH;
    ImageView ivBrowse;
    boolean isDownload = false;
    public RelativeLayout dwnBack;
    String download_url;
    private static final int MATERIAL_FILE_PICKER = 0x1;
    public ProgressDialog pDialog;
    public static String json_url = StaticAccess.ROOT_URL + "ImagesManager/GetAllTask/?authenticationUserName=AutisMind&password=dniMsituA";
    public String taskImageDownloadUrl = StaticAccess.ROOT_URL + "Download/GetFile?file=packImages.zip";
    //    String taskImageDownloadUrl = "http://192.52.243.6/MindApp/ImagesManager/TaskPackZipImages?fileName=packImages.zip";
    Date lastSynDate;
    Date currentDate;
    //////////////////////////////// Daily Download Json /////////////////////////////
    public ArrayList<FLayer> arrDailyFlayer;
    public ArrayList<FirstLayer> arrLocalFlayerdb;
    public ArrayList<FirstLayer> arrFirstUpdateList;
    public boolean isDailyDownload;
    DailyUpdateCheck dailyUpdateCheck;
    TaskImageDownload taskImageDownload;
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        activity = this;

        cloud = (ImageView) findViewById(R.id.cloudImage);
        info = (ImageView) findViewById(R.id.info_static_button);
        dwnBack = (RelativeLayout) findViewById(R.id.dwnBack);
        ivBrowse = (ImageView) findViewById(R.id.imageView2);
        dwnBack.setVisibility(View.GONE);
        downloadBtnWelcome = (ArrowDownloadButton) findViewById(R.id.downloadBtnWelcome);
        currentDate = new Date(getFormattedDateFromTimestamp(System.currentTimeMillis()));
        lastSynDate = new Date(getFormattedDateFromTimestamp(SharedPreferenceValue.getDate(activity)));
        dailyUpdateCheck = new DailyUpdateCheck(activity);
        // for next day Start service
        if (currentDate.after(lastSynDate)) {
            if (SharedPreferenceValue.getDate(activity) != 0) {
                // NextDate()
//                if (SharedPreferenceValue.getInsert(activity)) {
                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    SharedPreferenceValue.setInsert(activity, false);
                    SharedPreferenceValue.setDate(activity, System.currentTimeMillis());
                    startService(new Intent(activity, DownloadService.class));
                }
            }


        }

        if (ApplicationMode.devMode) {
            ivBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialFilePicker()
                            .withActivity(activity)
                            .withRequestCode(1)
                            .withFilter(Pattern.compile(".*\\.map$")) // Filtering files and directories by file name using regexp
                            .withFilterDirectories(false) // Set directories filterable (false by default)
                            .withHiddenFiles(true) // Show hidden files and folders
                            .start();
                }
            });
        } else {
            download_url = StaticAccess.ROOT_URL + "ImagesManager/GetPackByID/?id=0&authenticationUserName=AutisMind&password=dniMsituA";
            FILE_PATH = Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindAppSlash) + "abc.map";
            spDownloadFlag = SharedPreferenceValue.getDownloadFlag(activity);

            if (spDownloadFlag != null && spDownloadFlag.equalsIgnoreCase(StaticAccess.TAG_SP_DOWNLOAD_FLAG)) {
                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    dailyUpdateCheck.executeDailyCheck();
                }
            } else {
                // first Time install and
                setDialog();

            }
        }

        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handling the shared pref issues
                if (!ApplicationMode.devMode) {
                    spDownloadFlag = SharedPreferenceValue.getDownloadFlag(activity);
                    if (spDownloadFlag != null && spDownloadFlag.equalsIgnoreCase(StaticAccess.TAG_SP_DOWNLOAD_FLAG)) {
                        musicControl = true;
                        Intent mInSeond = new Intent(WelcomeActivity.this, UserListActivity.class);
                        startActivity(mInSeond);
                        finish();
                    } else {
                        setDialog();

                    }
                } else {
                    musicControl = true;
                    Intent mInSeond = new Intent(WelcomeActivity.this, UserListActivity.class);
                    startActivity(mInSeond);
                    finish();
                }

            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicControl = true;
                Intent mInInfo = new Intent(WelcomeActivity.this, InfoActivity.class);
                startActivity(mInInfo);
            }
        });
        Animanation.moveAnimation(cloud);


    }


    @Override
    public void onResume() {
        super.onResume();
        BackgroundMusicService.startMusic(WelcomeActivity.this);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(WelcomeActivity.this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Dialog  for each taskPack map file download
    public void setDialog() {
        final Dialog dialog = new Dialog(activity, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.permission_dialog);
        dialog.setCancelable(false);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        tvContent.setText(getResources().getString(R.string.DowanloadPermission));

        Button btnAccept = (Button) dialog.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    new DownloadFileFromURL(download_url).execute();
                } else {
                    CustomToast.t(activity, getResources().getString(R.string.connectInternet));
                }

                setKeepScreenOn();
                dialog.dismiss();

            }
        });

        navBarHide(activity, dialog);
    }

    public void navBarHide(Activity activity, Dialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public void setKeepScreenOn() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setAttributes(params);
    }

    // Server Request for each taskPack detail
    //Parse Data from server if
    // which ==0 its first time download json Data when install
    // which ==1 download daily and check file name
    public void requestForFirstLayer(String Url, int which) {
        JsonArrayRequest jreq = null;
        if (which == StaticAccess.TAG_INSTALL_JSON_DATA_FLAG) {
            jreq = new JsonArrayRequest(Url,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            parserJson(response, StaticAccess.TAG_INSTALL_JSON_DATA_FLAG);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                }
            });
        } else if (which == StaticAccess.TAG_DAILY_JSON_DATA_FLAG) {
            jreq = new JsonArrayRequest(Url,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            parserJson(response, StaticAccess.TAG_DAILY_JSON_DATA_FLAG);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                }
            });

        }


        AppController.getInstance().addToRequestQueue(jreq, "jreq");


    }


    //Parse Data from server if
    // which ==0 its first time download json Data when install
    // which ==1 download daily and check file name
    private void parserJson(JSONArray response, int which) {
        if (which == StaticAccess.TAG_INSTALL_JSON_DATA_FLAG) {
            FirstLayerJsonParser firstLayerJsonParser = new FirstLayerJsonParser(activity, response);
            firstLayerJsonParser.parser();
            isJSONDownload = firstLayerJsonParser.isJSONException;
        } else if (which == StaticAccess.TAG_DAILY_JSON_DATA_FLAG) {
            DailyFirstLayerParser dailyParser = new DailyFirstLayerParser(activity, response);
            arrDailyFlayer = new ArrayList<>();
            arrDailyFlayer = dailyParser.getParserArray();
            isDailyDownload = dailyParser.isDownload;
            pDialog.dismiss();
            if (isDailyDownload) {
                dailyUpdateCheck.executeCheckFileNameAsync();
            }
        }

    }


    //////////***********************************////////////
    //unzip  taskPack map file download and unzip AsynTask call
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String download_url;

        public DownloadFileFromURL(String download_url) {
            this.download_url = download_url;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dwnBack.setVisibility(View.VISIBLE);
            downloadBtnWelcome.setVisibility(View.VISIBLE);
            downloadBtnWelcome.startAnimating();
            cloud.setClickable(false);

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
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindApp));
                if (!sdCardDirectory.exists()) {
                    sdCardDirectory.mkdirs();
                }

                OutputStream output = new FileOutputStream(getResources().getString(R.string.sdcardLocation) + "abc.map");
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

            } catch (Exception e) {
                //    Log.e("Error: ", e.getMessage());
                isDownload = false;

            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {

            downloadBtnWelcome.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            dwnBack.setVisibility(View.GONE);
            downloadBtnWelcome.reset();
            downloadBtnWelcome.setVisibility(View.GONE);
            if (isDownload) {
                new UnzipFromDownloadedFolder().execute();

            } else
                CustomToast.t(activity, getResources().getString(R.string.downloadFailed));

        }
    }

    //unzip  taskPack map file and install
    class UnzipFromDownloadedFolder extends AsyncTask<String, String, String> {


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
        protected String doInBackground(String... params) {

           /* share = new Share(activity);

            try {
                requestForFirstLayer(json_url);
                share.unZip(FILE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }

            share.readSharedTaskPackJSONtoDatabase();
            share.deleteReceivedFolder();
*/
            share = new Share(activity);

            try {
                // task pack download Class  and unzip taskpack also delete zip file
                //for First time packImage download if(service not working that time)
                if (SharedPreferenceValue.getDate(activity) == 0) {
                    taskImageDownload = new TaskImageDownload(activity, taskImageDownloadUrl);
                    taskImageDownload.imgDownload();
                    if (taskImageDownload.isDownload) {
                        requestForFirstLayer(json_url, StaticAccess.TAG_INSTALL_JSON_DATA_FLAG);
                        share.unZip(FILE_PATH);
                    }
                } else {
                    requestForFirstLayer(json_url, StaticAccess.TAG_INSTALL_JSON_DATA_FLAG);
                    share.unZip(FILE_PATH);
                }

                share.readSharedTaskPackJSONtoDatabase();
                share.deleteRCVFolder();
                File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindApp));
                share.deleteRecursive(sdCardDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialogUnzip.dismiss();
            if (isDownload && isJSONDownload && share.getIsInstallStatus()) {
                SharedPreferenceValue.setDownloadFlag(activity, StaticAccess.TAG_SP_DOWNLOAD_FLAG);
                CustomToast.t(activity, getResources().getString(R.string.installComplete));
                cloud.setClickable(true);
            } else
                CustomToast.t(activity, getResources().getString(R.string.installFailed));


        }

    }

    // for dev mode .map file picker
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


            if (requestCode == MATERIAL_FILE_PICKER) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                if (filePath != null)
                    new JsonReading(filePath).execute();
            }

        }

    }

    //browse taskPack .map file and install
    class JsonReading extends AsyncTask<String, String, String> {
        String filePath;
        Share share;

        public JsonReading(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            share = new Share(activity);
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.pleaseWait));
            pDialog.setCancelable(false);
            pDialog.show();

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
                TaskImageDownload taskImageDownload = new TaskImageDownload(activity, taskImageDownloadUrl);
                taskImageDownload.imgDownload();
                requestForFirstLayer(json_url, StaticAccess.TAG_INSTALL_JSON_DATA_FLAG);
                share.unZip(filePath);
                share.readSharedTaskPackJSONtoDatabase();
                share.deleteRCVFolder();
                File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindApp));
                share.deleteRecursive(sdCardDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }


    }

    // date format mile sec to Date for daily taskpack download
    public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds) {
        Date date = new Date();
        date.setTime(timestampInMilliSeconds);
        String formattedDate = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(date);
        return formattedDate;

    }
}
