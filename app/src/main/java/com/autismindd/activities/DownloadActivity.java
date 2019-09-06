package com.autismindd.activities;

import android.app.Activity;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.share.Share;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;



/**
 * Created by RAFI on 9/18/2016.
 */

public class DownloadActivity extends BaseActivity {

    private static String download_url = StaticAccess.ROOT_URL+"Download/GetFile?file=TaskPacks.zip";
  int   REQUESTCODE_PICK=1;

    public String currentFileName = "TaskPacks";
    public String linkForDownload;

    GridView gridView;
    DownloadActivity activity;
    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;
    private ProgressDialog pDialogUnzip;
    ImageButton ibtnLimbika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_download);
        activity = this;

        ibtnLimbika = (ImageButton) findViewById(R.id.ibtnLimbika);


        ibtnLimbika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });


    }

    public void setDialog() {
        final Dialog dialog = new Dialog(activity, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.permission_dialog);
        dialog.setCancelable(true);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        tvContent.setText(getResources().getString(R.string.permission) + " " + currentFileName + "?");

        Button btnAccept = (Button) dialog.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFileFromURL().execute();
                setKeepScreenOn();
                dialog.dismiss();

            }
        });

        navBarHide(activity, dialog);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage(getResources().getString(R.string.LoadingMessage));
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
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

                OutputStream output = new FileOutputStream(getResources().getString(R.string.sdcardLocation) + currentFileName + getResources().getString(R.string.zip));
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

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
            new UnzipFromDownloadedFolder().execute();

        }
    }

    class UnzipFromDownloadedFolder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogUnzip = new ProgressDialog(DownloadActivity.this);
            pDialogUnzip.setMessage(getResources().getString(R.string.UnzippinMessage));
            pDialogUnzip.setIndeterminate(false);
            pDialogUnzip.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogUnzip.setCancelable(false);
            pDialogUnzip.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                unZip(Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindAppSlash) + currentFileName + getResources().getString(R.string.zip));
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindAppSlash) + currentFileName + getResources().getString(R.string.zip));
            file.delete();

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

            SharedPreferenceValue.setDownloadFlag(activity, StaticAccess.TAG_SP_DOWNLOAD_FLAG);

          String map="/storage/emulated/0/MindApp/TaskPack_20160915130522.map";
            Share share = new Share(DownloadActivity.this);
            try {
                share.unZip(String.valueOf(map));
            } catch (IOException e) {
                e.printStackTrace();
            }

            share.readSharedTaskPackJSONtoDatabase();
            share.deleteRCVFolder();

            Intent intentD = new Intent(DownloadActivity.this, TaskPackActivity.class);
            intentD.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentD);
            pDialogUnzip.dismiss();
            finish();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_PICK
                && resultCode == Activity.RESULT_OK) {
            Uri map = data.getData();
            Log.d("", "Video URI= " + map);


        }
    }
    public void setKeepScreenOn() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setAttributes(params);
    }

    public void navBarHide(Activity activity, Dialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    // Unzipping files after receive
    public void unZip(String zipFile) throws ZipException, IOException {
        System.out.println(zipFile);
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        //String newPath = zipFile.substring(0, zipFile.length() - 4);

        String newPath = Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindApp);

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

            if (currentEntry.endsWith(getResources().getString(R.string.zip))) {
                // found a zip file, try to open
                unZip(destFile.getAbsolutePath());
            }
        }
    }
}



