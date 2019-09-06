package com.dmk.limbikasdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DENNOH on 8/9/2015.
 */
public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "limbikaView";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table viewState (x int,y int,rotation int,key text,isCircleView int,circleColor int,userText text,textColor int,textSize int,borderColor int,backgroundColor int,drawable int,width int,height int,left int,right int,top int,bottom int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        onCreate(sqLiteDatabase);
    }
}
