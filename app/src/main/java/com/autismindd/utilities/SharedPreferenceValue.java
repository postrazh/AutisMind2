package com.autismindd.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.TaskPack;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by RAFI on 9/18/2016.
 */

public class SharedPreferenceValue {

    public static String getDownloadFlag(Context context) {
        SharedPreferences settings = context.getSharedPreferences("TAG_FLAG_RESAT", 0);
        String mFlag = settings.getString("TAG_FLAG", null);
        return mFlag;
    }

    public static void setDownloadFlag(Context context, String flag) {
        SharedPreferences settings = context.getSharedPreferences("TAG_FLAG_RESAT", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("TAG_FLAG", flag);
        editor.commit();
    }

    public static boolean getLampFlag(Context context) {
        SharedPreferences settings = context.getSharedPreferences("TAG_LAMP_FLAG_RESAT", 0);
        boolean mFlag = settings.getBoolean("TAG_LAMP_FLAG", false);
        return mFlag;
    }

    public static void setLampFlag(Context context, boolean flag) {
        SharedPreferences settings = context.getSharedPreferences("TAG_LAMP_FLAG_RESAT", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("TAG_LAMP_FLAG", flag);
        editor.commit();
    }


    public static boolean getInsert(Context context) {
        SharedPreferences settings = context.getSharedPreferences("TASK_IMAGE", 0);
        boolean mFlag = settings.getBoolean("TASK_IMAGE_FLAG", false);
        return mFlag;
    }

    public static void setInsert(Context context, boolean flag) {
        SharedPreferences settings = context.getSharedPreferences("TASK_IMAGE", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("TASK_IMAGE_FLAG", flag);
        editor.commit();
    }

    public static long getDate(Context context) {
        SharedPreferences settings = context.getSharedPreferences("TASK_DOWNLOAD_DATE", 0);
        long mFlag = settings.getLong("TAG_DATE_DOWNLOAD", 0);
        return mFlag;
    }

    public static void setDate(Context context, long date) {
        SharedPreferences settings = context.getSharedPreferences("TASK_DOWNLOAD_DATE", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("TAG_DATE_DOWNLOAD", date);
        editor.commit();
    }


    public static void setFirstLayerID(Context context, String values) {
        SharedPreferences settings = context.getSharedPreferences("TASK_DOWNLOAD_IDS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("TAG_FIRST_LAYER_ID_DOWNLOAD", values);
        editor.commit();
    }

    public static String getFirstLayerID(Context context) {
        SharedPreferences settings = context.getSharedPreferences("TASK_DOWNLOAD_IDS", 0);
        String mFlag = settings.getString("TAG_FIRST_LAYER_ID_DOWNLOAD", null);
        return mFlag;
    }

    private static final String KEY_ID_DEFAULT = "LAYER_ID_";

    public static boolean setPacksIds(Context context, ArrayList<FirstLayer> firstLayers) {
        // save the task list to preference
        SharedPreferences settings = context.getSharedPreferences("TASK_DOWNLOAD_IDS", 0);
        SharedPreferences.Editor editor = settings.edit();
        if (firstLayers.size() > 0) {
            editor.putInt("FIRST_LAYER_SIZE", firstLayers.size());

            for (int i = 0; i < firstLayers.size(); i++) {
                editor.remove(KEY_ID_DEFAULT + i);
                editor.putInt(KEY_ID_DEFAULT + i, firstLayers.get(i).getFirstLayerTaskID());
            }
            return editor.commit();

        } else {
            return false;
        }
    }


    public static ArrayList<Integer> getPacksIds(Context context) {
        ArrayList<Integer> taskPacksIDS = new ArrayList<>();
        SharedPreferences settings = context.getSharedPreferences("TASK_DOWNLOAD_IDS", 0);
        SharedPreferences.Editor editor = settings.edit();
        int size = settings.getInt("FIRST_LAYER_SIZE", 0);
        for (int i = 0; i < size; i++) {
            taskPacksIDS.add(settings.getInt(KEY_ID_DEFAULT + i, 0));
        }
        return taskPacksIDS;
    }
}
