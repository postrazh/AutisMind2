package com.autismindd.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.customui.CustomAvatarView;
import com.autismindd.dao.ErrorUserLog;
import com.autismindd.dao.Star;
import com.autismindd.dao.User;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.MyValueFormatter;
import com.autismindd.utilities.PDFHelper;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RAFI on 11/30/2016.
 */
//isLamp = true lamp off and sound play
//islamp= false lamp on and sound play
//valueArray this collection used user layer wise completed point percent
//starArray this collection used only check one user till has any point
//errorUserLogsArray this collection has layer wise error log
//starArrayLayer this collection used layer wise Star
public class StatisticsLayer extends BaseActivity implements View.OnClickListener {
    BarDataSet BDSLevel1, BDSLevel2, BDSLevel3, BDSLevel4, BDSLevel5, BDSLevel6, BDSLevel7, BDSLevel8, BDSLevel9, BDSLevel10;
    ArrayList<IBarDataSet> dataSets;
    CustomAvatarView customAvatarView;
    StaticInstance staticInstance;
    User user;
    String lampSound = "sound_click_lamp.mp3";
    StatisticsLayer activity;
    private IDatabaseManager databaseManager;
    ArrayList<ErrorUserLog> errorUserLogsArray;
    ArrayList<Star> starArray;
    ArrayList<Star> starArrayLayer;
    ArrayList<String> fLayerLabels = null;
    ArrayList<Integer> fLayerArray;
    BarData data;
    BarChart chart;
    RelativeLayout rlUserInfo, rlNoData, rlStatisticsLayerLamp, rlStatisticsLayerMain;
    TextView tvNoData, tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvTen;
    UserInfo userInfo;
    ImageProcessing imageProcessing;
    ImageButton ibtnBackStatistics, ibtnLampStatisticsLayer;
    ArrayList<Double> valueArray;
    ImageButton ibtnOne, ibtnTwo, ibtnThree, ibtnFour, ibtnFive, ibtnSix, ibtnSeven, ibtnEight, ibtnNine, ibtnTen;
    public boolean isLamp = false;
    MediaPlayer mp;
    boolean musicControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_new);
        activity = this;
        rlStatisticsLayerMain = (RelativeLayout) findViewById(R.id.rlStatisticsLayerMain);
        rlUserInfo = (RelativeLayout) findViewById(R.id.rlUserInfo);
        rlStatisticsLayerLamp = (RelativeLayout) findViewById(R.id.rlStatisticsLayerLamp);
        ibtnLampStatisticsLayer = (ImageButton) findViewById(R.id.ibtnLampStatisticsLayer);
        isLamp = SharedPreferenceValue.getLampFlag(activity);
        screenMode(isLamp);
        ibtnLampStatisticsLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenMode(isLamp);
                playSound(lampSound);

            }
        });
        rlNoData = (RelativeLayout) findViewById(R.id.rlNoData);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        // buttom
        ibtnOne = (ImageButton) findViewById(R.id.ibtnOne);
        ibtnTwo = (ImageButton) findViewById(R.id.ibtnTwo);
        ibtnThree = (ImageButton) findViewById(R.id.ibtnThree);
        ibtnFour = (ImageButton) findViewById(R.id.ibtnFour);
        ibtnFive = (ImageButton) findViewById(R.id.ibtnFive);
        ibtnSix = (ImageButton) findViewById(R.id.ibtnSix);
        ibtnSeven = (ImageButton) findViewById(R.id.ibtnSeven);
        ibtnEight = (ImageButton) findViewById(R.id.ibtnEight);
        ibtnNine = (ImageButton) findViewById(R.id.ibtnNine);
        ibtnTen = (ImageButton) findViewById(R.id.ibtnTen);

        tvOne = (TextView) findViewById(R.id.tvOne);
        tvTwo = (TextView) findViewById(R.id.tvTwo);
        tvThree = (TextView) findViewById(R.id.tvThree);
        tvFour = (TextView) findViewById(R.id.tvFour);
        tvFive = (TextView) findViewById(R.id.tvFive);
        tvSix = (TextView) findViewById(R.id.tvSix);
        tvSeven = (TextView) findViewById(R.id.tvSeven);
        tvEight = (TextView) findViewById(R.id.tvEight);
        tvNine = (TextView) findViewById(R.id.tvNine);
        tvTen = (TextView) findViewById(R.id.tvTen);

        ibtnOne.setOnClickListener(activity);
        ibtnTwo.setOnClickListener(activity);
        ibtnThree.setOnClickListener(activity);
        ibtnFour.setOnClickListener(activity);
        ibtnFive.setOnClickListener(activity);
        ibtnSix.setOnClickListener(activity);
        ibtnSeven.setOnClickListener(activity);
        ibtnEight.setOnClickListener(activity);
        ibtnNine.setOnClickListener(activity);
        ibtnTen.setOnClickListener(activity);

        staticInstance = StaticInstance.getInstance();
        user = staticInstance.getUser();
        ibtnBackStatistics = (ImageButton) findViewById(R.id.ibtnBackStatistics);
        databaseManager = new DatabaseManager(this);
        errorUserLogsArray = new ArrayList<>();
        starArray = new ArrayList<>();
        starArrayLayer = new ArrayList<>();
        imageProcessing = new ImageProcessing(activity);
        userInfo = new UserInfo();
        // check user on or off
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }

        // here is show user profile pic and star
        customAvatarView = new CustomAvatarView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()));
        rlUserInfo.addView(customAvatarView);


        tvOne.setText(getResources().getString(R.string.taskOne));
        tvTwo.setText(getResources().getString(R.string.taskTwo));
        tvThree.setText(getResources().getString(R.string.taskThree));
        tvFour.setText(getResources().getString(R.string.taskFour));
        tvFive.setText(getResources().getString(R.string.taskFive));
        tvSix.setText(getResources().getString(R.string.taskSix));
        tvSeven.setText(getResources().getString(R.string.taskSeven));
        tvEight.setText(getResources().getString(R.string.taskEight));
        tvNine.setText(getResources().getString(R.string.taskNine));
        tvTen.setText(getResources().getString(R.string.taskTen));
        UserStatic(user);
        chart = (BarChart) findViewById(R.id.chart);
        ibtnBackStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staticInstance.clearAll();
                musicControl = true;

                Intent intent = new Intent(activity, UserListActivity.class);
                startActivity(intent);
                Animanation.zoomOut(v);
                finish();

            }
        });
        barChar();

    }

    // user wise show statistics error calculation percent value
    void UserStatic(User user) {
        errorUserLogsArray = databaseManager.errorLogUserWiseList(user.getKey());
        starArray = databaseManager.getStarListUserKey(user.getKey());
//        starArrayLayer = databaseManager.getStarListFlayerWise(user.getKey(), 0);
        valueArray = new ArrayList<>();
        double value = 0;
        for (int i = 0; i < 10; i++) {
            int totalAvgErrorPercent = 0;
            starArrayLayer = databaseManager.getStarListFlayerWise(user.getKey(), i);
            for (int k = 0; k < starArrayLayer.size(); k++) {
                totalAvgErrorPercent += starArrayLayer.get(k).getLevelPoint();
            }
//            value = (totalAvgErrorPercent/StaticAccess.TOTAL_LEVEL) * StaticAccess.PERCENT_HUNDRED ;
//            value = (totalAvgErrorPercent / StaticAccess.TOTAL_LEVEL);
            if (starArrayLayer.size() > 0) {
                value = (totalAvgErrorPercent / starArrayLayer.size());
            }
            valueArray.add(value);
            value = 0;

        }
        Log.d("ERROR LOG", "Layer 1: " + String.valueOf(value));
    }

    // barchar all layer wise array and dataset initialize
    private void barChar() {
        ArrayList<BarEntry> layerArray1 = null;
        ArrayList<BarEntry> layerArray2 = null;
        ArrayList<BarEntry> layerArray3 = null;
        ArrayList<BarEntry> layerArray4 = null;
        ArrayList<BarEntry> layerArray5 = null;
        ArrayList<BarEntry> layerArray6 = null;
        ArrayList<BarEntry> layerArray7 = null;
        ArrayList<BarEntry> layerArray8 = null;
        ArrayList<BarEntry> layerArray9 = null;
        ArrayList<BarEntry> layerArray10 = null;


        layerArray1 = new ArrayList<>();
        layerArray2 = new ArrayList<>();
        layerArray3 = new ArrayList<>();
        layerArray4 = new ArrayList<>();
        layerArray5 = new ArrayList<>();

        layerArray6 = new ArrayList<>();
        layerArray7 = new ArrayList<>();
        layerArray8 = new ArrayList<>();
        layerArray9 = new ArrayList<>();
        layerArray10 = new ArrayList<>();

        dataSets = new ArrayList<>();
        fLayerLabels = new ArrayList<String>();
        fLayerLabels.add(String.valueOf("Layer"));
        fLayerArray = new ArrayList<Integer>();

        // barChart for layer one
        if (valueArray.get(0) != 0) {
            layerArray1.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(0))), 0));
            BDSLevel1 = new BarDataSet(layerArray1, " ");    // creating dataset for layer one
            BDSLevel1.setColors(new int[]{userInfo.getFirstLayerColor(0)});
            BDSLevel1.setValueFormatter(new MyValueFormatter());
            ibtnOne.setVisibility(View.VISIBLE);
            tvOne.setVisibility(View.VISIBLE);
        } else {
            BDSLevel1 = new BarDataSet(layerArray1, " ");     // creating dataset for layer one
            ibtnOne.setVisibility(View.INVISIBLE);
            tvOne.setVisibility(View.INVISIBLE);
        }
        // barChart for layer two
        if (valueArray.get(1) != 0) {
            layerArray2.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(1))), 0));
            BDSLevel2 = new BarDataSet(layerArray2, " "); // creating dataset for layer two
            BDSLevel2.setColors(new int[]{userInfo.getFirstLayerColor(1)});
            BDSLevel2.setValueFormatter(new MyValueFormatter());
            ibtnTwo.setVisibility(View.VISIBLE);
            tvTwo.setVisibility(View.VISIBLE);
        } else {
            BDSLevel2 = new BarDataSet(layerArray2, " ");  // creating dataset for layer two
            ibtnTwo.setVisibility(View.INVISIBLE);
            tvTwo.setVisibility(View.INVISIBLE);
        }
        // barChart for layer three
        if (valueArray.get(2) != 0) {
            layerArray3.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(2))), 0));
            BDSLevel3 = new BarDataSet(layerArray3, " "); // creating dataset for layer three
            BDSLevel3.setColors(new int[]{userInfo.getFirstLayerColor(2)});
            BDSLevel3.setValueFormatter(new MyValueFormatter());
            ibtnThree.setVisibility(View.VISIBLE);
            tvThree.setVisibility(View.VISIBLE);
        } else {
            BDSLevel3 = new BarDataSet(layerArray3, " ");  // creating dataset for layer three
            ibtnThree.setVisibility(View.INVISIBLE);
            tvThree.setVisibility(View.INVISIBLE);
        }
        // barChart for layer Four
        if (valueArray.get(3) != 0) {
            layerArray4.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(3))), 0));
            BDSLevel4 = new BarDataSet(layerArray4, " "); // creating dataset for layer four
            BDSLevel4.setColors(new int[]{userInfo.getFirstLayerColor(3)});
            BDSLevel4.setValueFormatter(new MyValueFormatter());
            ibtnFour.setVisibility(View.VISIBLE);
            tvFour.setVisibility(View.VISIBLE);
        } else {
            BDSLevel4 = new BarDataSet(layerArray4, " ");  // creating dataset for layer four
            ibtnFour.setVisibility(View.INVISIBLE);
            tvFour.setVisibility(View.INVISIBLE);
        }
        // barChart for layer Five
        if (valueArray.get(4) != 0) {
            layerArray5.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(4))), 0));
            BDSLevel5 = new BarDataSet(layerArray5, " "); // creating dataset for layer five
            BDSLevel5.setColors(new int[]{userInfo.getFirstLayerColor(4)});
            BDSLevel5.setValueFormatter(new MyValueFormatter());
            ibtnFive.setVisibility(View.VISIBLE);
            tvFive.setVisibility(View.VISIBLE);
        } else {
            BDSLevel5 = new BarDataSet(layerArray5, " ");  // creating dataset for layer five
            ibtnFive.setVisibility(View.INVISIBLE);
            tvFive.setVisibility(View.INVISIBLE);
        }
        // barChart for layer six
        if (valueArray.get(5) != 0) {
            layerArray6.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(5))), 0));
            BDSLevel6 = new BarDataSet(layerArray6, " ");  // creating dataset for  layer six
            BDSLevel6.setColors(new int[]{userInfo.getFirstLayerColor(5)});
            BDSLevel6.setValueFormatter(new MyValueFormatter());
            ibtnSix.setVisibility(View.VISIBLE);
            tvSix.setVisibility(View.VISIBLE);
        } else {
            BDSLevel6 = new BarDataSet(layerArray6, " ");  // creating dataset for  layer six
            ibtnSix.setVisibility(View.INVISIBLE);
            tvSix.setVisibility(View.INVISIBLE);
        }
        // barChart for layer seven
        if (valueArray.get(6) != 0) {
            layerArray7.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(6))), 0));
            BDSLevel7 = new BarDataSet(layerArray7, " "); // creating dataset for  layer seven
            BDSLevel7.setColors(new int[]{userInfo.getFirstLayerColor(6)});
            BDSLevel7.setValueFormatter(new MyValueFormatter());
            ibtnSeven.setVisibility(View.VISIBLE);
            tvSeven.setVisibility(View.VISIBLE);
        } else {
            BDSLevel7 = new BarDataSet(layerArray7, " ");  // creating dataset for  layer seven
            ibtnSeven.setVisibility(View.INVISIBLE);
            tvSeven.setVisibility(View.INVISIBLE);
        }
        // barChart for layer eight
        if (valueArray.get(7) != 0) {
            layerArray8.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(7))), 0));
            BDSLevel8 = new BarDataSet(layerArray8, " "); // creating dataset for layer eight
            BDSLevel8.setColors(new int[]{userInfo.getFirstLayerColor(7)});
            BDSLevel8.setValueFormatter(new MyValueFormatter());
            ibtnEight.setVisibility(View.VISIBLE);
            tvEight.setVisibility(View.VISIBLE);
        } else {
            BDSLevel8 = new BarDataSet(layerArray8, " ");  // creating dataset for layer eight
            ibtnEight.setVisibility(View.INVISIBLE);
            tvEight.setVisibility(View.INVISIBLE);
        }
        // barChart for layer nine
        if (valueArray.get(8) != 0) {
            layerArray9.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(8))), 0));
            BDSLevel9 = new BarDataSet(layerArray9, " "); // creating dataset layer nine
            BDSLevel9.setColors(new int[]{userInfo.getFirstLayerColor(8)});
            BDSLevel9.setValueFormatter(new MyValueFormatter());
            ibtnNine.setVisibility(View.VISIBLE);
            tvNine.setVisibility(View.VISIBLE);
        } else {
            BDSLevel9 = new BarDataSet(layerArray9, " ");  // creating dataset for layer nine
            ibtnNine.setVisibility(View.INVISIBLE);
            tvNine.setVisibility(View.INVISIBLE);
        }
        // barChart for layer ten
        if (valueArray.get(9) != 0) {
            layerArray10.add(new BarEntry(Float.valueOf(String.valueOf(valueArray.get(9))), 0));
            BDSLevel10 = new BarDataSet(layerArray10, " "); // creating dataset for layer ten
            BDSLevel10.setColors(new int[]{userInfo.getFirstLayerColor(9)});
            BDSLevel10.setValueFormatter(new MyValueFormatter());
            ibtnTen.setVisibility(View.VISIBLE);
            tvTen.setVisibility(View.VISIBLE);
        } else {
            BDSLevel10 = new BarDataSet(layerArray10, " ");  // creating dataset for layer ten
            ibtnTen.setVisibility(View.INVISIBLE);
            tvTen.setVisibility(View.INVISIBLE);
        }

        // combined all dataset into an arraylist
        dataSets.add(BDSLevel1);
        dataSets.add(BDSLevel2);
        dataSets.add(BDSLevel3);
        dataSets.add(BDSLevel4);
        dataSets.add(BDSLevel5);
        dataSets.add(BDSLevel6);
        dataSets.add(BDSLevel7);
        dataSets.add(BDSLevel8);
        dataSets.add(BDSLevel9);
        dataSets.add(BDSLevel10);

        data = new BarData(fLayerLabels, dataSets); // initialize the Bardata with argument labels and dataSet
        chart.setData(data);
        chart.setDescription(getResources().getString(R.string.statisticsDes));
        chart.animateXY(2000, 2000);
        chart.setDrawBorders(false);

        chart.setGridBackgroundColor(128);
        chart.setBorderColor(255);
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(true);//enable for grid line


        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMaxValue(115f);
        yAxis.setStartAtZero(true);

        chart.invalidate();
        if (starArray.size() == 0) {
            rlNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getResources().getText(R.string.tvNoData));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnOne:
                getIntentStatistic(0, valueArray.get(0),getResources().getString(R.string.taskOne));
                break;
            case R.id.ibtnTwo:
                getIntentStatistic(1, valueArray.get(1),getResources().getString(R.string.taskTwo));
                break;
            case R.id.ibtnThree:
                getIntentStatistic(2, valueArray.get(2),getResources().getString(R.string.taskThree));
                break;
            case R.id.ibtnFour:
                getIntentStatistic(3, valueArray.get(3),getResources().getString(R.string.taskFour));
                break;
            case R.id.ibtnFive:
                getIntentStatistic(4, valueArray.get(4),getResources().getString(R.string.taskFive));
                break;
            case R.id.ibtnSix:
                getIntentStatistic(5, valueArray.get(5),getResources().getString(R.string.taskSix));
                break;
            case R.id.ibtnSeven:
                getIntentStatistic(6, valueArray.get(6),getResources().getString(R.string.taskSeven));
                break;
            case R.id.ibtnEight:
                getIntentStatistic(7, valueArray.get(7),getResources().getString(R.string.taskEight));
                break;
            case R.id.ibtnNine:
                getIntentStatistic(7, valueArray.get(7),getResources().getString(R.string.taskNine));
                break;
            case R.id.ibtnTen:
                getIntentStatistic(9, valueArray.get(9),getResources().getString(R.string.taskTen));
                break;

        }
    }

    // layer wise  go to  level statistics activity
    public void getIntentStatistic(int index, double value,String title) {
        if (value != 0) {
            musicControl = true;
            ibtnBackStatistics.setVisibility(View.GONE);
            String imageName = imageProcessing.takeScreenshot(rlStatisticsLayerMain);/// taking screenshot for saving into pdf
            ibtnBackStatistics.setVisibility(View.VISIBLE);
            Intent intentStatisticsValue = new Intent(activity, StatisticsActivity.class);
            intentStatisticsValue.putExtra(StaticAccess.TAG_STATISTICS_VALUE_INDEX, index);
            intentStatisticsValue.putExtra(StaticAccess.PDF_TEMP_IMAGE_NAME, imageName); /// to save image in pdf file {sumon}
            intentStatisticsValue.putExtra(StaticAccess.PDF_LAYER_NAME, title);// to save layer name in pdf {sumon}

            startActivity(intentStatisticsValue);
        } else
            CustomToast.t(activity, "no Value");
    }

    // lamp on and off function
    public void screenMode(boolean isLampTem) {
        if (isLampTem) {
            rlStatisticsLayerLamp.setVisibility(View.VISIBLE);
            ibtnLampStatisticsLayer.setImageResource(R.drawable.ic_lamp_dream);
            isLamp = false;

        } else {
            rlStatisticsLayerLamp.setVisibility(View.GONE);
            ibtnLampStatisticsLayer.setImageResource(R.drawable.ic_lamp);
            isLamp = true;

//
        }
        SharedPreferenceValue.setLampFlag(activity, isLampTem);
    }

    // play Sound for lamp click
    private void playSound(String song) {
        mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(song);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user.getMusic() > 0) {
            BackgroundMusicService.startMusic(activity);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (!musicControl)
            BackgroundMusicService.stopMusic(activity);
    }
}
