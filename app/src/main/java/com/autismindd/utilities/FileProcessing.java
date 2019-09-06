package com.autismindd.utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.autismindd.manager.DatabaseManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by RAFI on 8/2/2016.
 */
public class FileProcessing {
    /**
     * Class tag. Used for debug.
     */
    String TAG = DatabaseManager.class.getCanonicalName();
    Context ctx;
    private String appOutputSoundPath = null;
    private String appOutputSoundFullPath = null;
    private String appSoundPath = null;

    public FileProcessing(Context ctx) {
        this.ctx = ctx;
        appOutputSoundPath = "/Android/Data/" + ctx.getPackageName() + "/Sound/";
    }

    //return file size created by reaz
    public int fileSize(String path) {
        File file = new File(path);
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        return file_size;
    }

    //create sound folder and copy file by reaz
    public String createSoundFile(String inputPath) {
        InputStream in = null;
        OutputStream out = null;


        try {
            File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + appOutputSoundPath);
            if (!sdCardDirectory.exists()) {
                sdCardDirectory.mkdirs();
            }
            appSoundPath = "md_" + System.currentTimeMillis() + ".3gpp";
            File soundFile = new File(sdCardDirectory, appSoundPath);
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(soundFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e(TAG, fnfe1.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appSoundPath;
    }

    //remove sound file by reaz
    public boolean deleteSound(String soundName) {
        boolean deleted = false;
        File sound = new File(getAbsolutepath_Of_Sound(soundName));
        if (sound.exists()) {
            deleted = sound.delete();
        }
        return deleted;
    }

    public String getAbsolutepath_Of_Sound(String soundName) {
        return Environment.getExternalStorageDirectory() + appOutputSoundPath + soundName;
    }

}
