package com.autismindd.utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.autismindd.share.Share;

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



public class TaskImageDownload {
    public boolean isDownload=false;
    Context context;
    String taskImgDownloadUrl;

    String downloadPath;
    public TaskImageDownload(Context context,String taskImgDownloadUrl){
        this.context=context;
        this.taskImgDownloadUrl=taskImgDownloadUrl;
        downloadPath=Environment.getExternalStorageDirectory() + "/Android/Data/" + context.getPackageName() + "/Images";

    }
    public void imgDownload() {
        int countImage;

        try {
            /****************************** Image Pack Download *****************************/
            java.net.URL url = new URL(taskImgDownloadUrl);
            Log.d("download", taskImgDownloadUrl);
            URLConnection conection = url.openConnection();
            conection.connect();
            int lenghtOfFile = conection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            File sdCardDirectory = new File(downloadPath);
            if (!sdCardDirectory.exists()) {
                sdCardDirectory.mkdirs();
            }

            OutputStream output = new FileOutputStream(downloadPath + "/packImages.zip");
            byte data[] = new byte[1024];
            long total = 0;
            while ((countImage = input.read(data)) != -1) {
                total += countImage;
//                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, countImage);
            }
            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();
/******************************************************************************************************/
            unZipImage(downloadPath + "/packImages.zip");
            SharedPreferenceValue.setInsert(context, true);
            SharedPreferenceValue.setDate(context, System.currentTimeMillis());
            isDownload=true;
            boolean isDelete=deleteZipFile(downloadPath + "/packImages.zip");
        } catch (IOException e) {
            e.printStackTrace();
            isDownload=false;
        }
    }
    // Unzipping files after receive
    public void unZipImage(String zipFile) throws ZipException, IOException {
        System.out.println(zipFile);
        Log.d("download",zipFile);
        int BUFFER = 2048;
        File file = new File(zipFile);
        ZipFile zip = new ZipFile(file);
        //String newPath = zipFile.substring(0, zipFile.length() - 4);

        //String newPath = Environment.getExternalStorageDirectory() + "/Android/Data/" + context.getPackageName() + "/Recieved";

        new File(downloadPath+"/").mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(downloadPath+"/", currentEntry);
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

            if (currentEntry.endsWith(".zip")) {
                // found a zip file, try to open
                unZipImage(destFile.getAbsolutePath());
            }
        }
    }

    public boolean deleteZipFile(String path){
        boolean deleted=false;
        File file = new File(path);
        deleted = file.delete();
        return deleted;
    }
}
