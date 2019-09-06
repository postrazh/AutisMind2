package com.autismindd.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by RAFI on 10/31/2016.
 */
//isLamp = true lamp off and sound play
//islamp= false lamp on and sound play
//starArray this collection used only check one user till has any point
//errorUserLogsArray this collection has layer wise error log
//starArrayLayer this collection used layer wise Star
//TTD = total time duration
// RT = Response time Duration
public class StatisticsActivity extends BaseActivity {
    BarDataSet BDSLevel1, BDSLevel2, BDSLevel3, BDSLevel4, BDSLevel5, BDSLevel6;
    ArrayList<IBarDataSet> dataSets;
    CustomAvatarView customAvatarView;
    StaticInstance staticInstance;
    User user;
    StatisticsActivity activity;
    private IDatabaseManager databaseManager;
    ArrayList<ErrorUserLog> errorUserLogsArray;
    ArrayList<Star> starArray;
    ArrayList<String> fLayerLabels = null;
    ArrayList<Integer> fLayerArray;
    BarData data;
    BarChart chart;
    RelativeLayout rlUserInfo, rlNoData, rlStatisticsLamp;
    TextView tvNoData;
    UserInfo userInfo;
    boolean isLamp = false;
    ImageProcessing imageProcessing;
    ImageButton ibtnBackStatistics, ibtnPdfStatisticsLevel;
    int layer;
    String lampSound = "sound_click_lamp.mp3";
    MediaPlayer mp;
    boolean musicControl = false;
    /////*********** all binding ids varibles here *******************//////
    private TextView tvTitleLevelOne, tvTimeResponseLevelOne, tvTimeDurationLevelOne, tvTitleLevelTwo,
            tvTimeResponseLevelTwo, tvTimeDurationLevelTwo, tvTitleLevelThree, tvTimeResponseLevelThree, tvTimeDurationLevelThree, tvTitleLevelFour, tvTimeResponseLevelFour, tvTimeDurationLevelFour,
            tvTitleLevelFive, tvTimeResponseLevelFive, tvTimeDurationLevelFive, tvTitleLevelSix, tvTimeResponseLevelSix, tvTimeDurationLevelSix;

    private ImageView ivStarOneLevelOne, ivStarTwoLevelOne, ivStarThreeLevelOne, ivStarOneLevelTwo,
            ivStarTwoLevelTwo, ivStarThreeLevelTwo, ivStarOneLevelThree, ivStarTwoLevelThree, ivStarThreeLevelThree, ivStarOneLevelFour,
            ivStarTwoLevelFour, ivStarThreeLevelFour, ivStarOneLevelFive, ivStarTwoLevelFive, ivStarThreeLevelFive, ivStarOneLevelSix, ivStarTwoLevelSix, ivStarThreeLevelSix;

    ImageButton ibtnLampStatistics;
    String pdfImageName, pdfLayerTitle;/// for saving image and layer name in pdf {sumon}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        activity = this;
        layer = getIntent().getExtras().getInt(StaticAccess.TAG_STATISTICS_VALUE_INDEX);
        //// pdf data from statistic layer activity {sumon}
        pdfImageName = getIntent().getExtras().getString(StaticAccess.PDF_TEMP_IMAGE_NAME);
        pdfLayerTitle = getIntent().getExtras().getString(StaticAccess.PDF_LAYER_NAME);
        ///// end here
        rlUserInfo = (RelativeLayout) findViewById(R.id.rlUserInfo);
        ibtnLampStatistics = (ImageButton) findViewById(R.id.ibtnLampStatistics);
        rlStatisticsLamp = (RelativeLayout) findViewById(R.id.rlStatisticsLamp);
        isLamp = SharedPreferenceValue.getLampFlag(activity);
        screenMode(isLamp);
        rlNoData = (RelativeLayout) findViewById(R.id.rlNoData);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        staticInstance = StaticInstance.getInstance();
        user = staticInstance.getUser();
        ibtnBackStatistics = (ImageButton) findViewById(R.id.ibtnBackStatistics);
        ibtnPdfStatisticsLevel = (ImageButton) findViewById(R.id.ibtnPdfStatisticsLevel);
        databaseManager = new DatabaseManager(this);
        errorUserLogsArray = new ArrayList<>();
        starArray = new ArrayList<>();

        imageProcessing = new ImageProcessing(activity);
        userInfo = new UserInfo();
        // here is show user profile pic and star
        customAvatarView = new CustomAvatarView(activity, imageProcessing.getImage(user.getPic()), user.getStars(), user.getName(), userInfo.avatarDarkColor(user.getAvatar()));
        rlUserInfo.addView(customAvatarView);
        UserStatic(user);
        chart = (BarChart) findViewById(R.id.chart);

        ibtnLampStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenMode(isLamp);
                playSound(lampSound);
            }
        });

        ibtnBackStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicControl = true;
                Intent intent = new Intent(StatisticsActivity.this, StatisticsLayer.class);
                startActivity(intent);
                Animanation.zoomOut(v);
                finish();
            }
        });
        ibtnPdfStatisticsLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePdf(); /// generating pdf {sumon}
            }
        });

        bindingIds();
        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
        barChar();
    }

    /// method that used to generate pdf {sumon}
    private void generatePdf() {
        ibtnBackStatistics.setVisibility(View.GONE);
        ibtnPdfStatisticsLevel.setVisibility(View.GONE);

        final PDFHelper helper = new PDFHelper(StatisticsActivity.this);

        boolean isGenerated = helper.generatePdf(user.getName(), pdfImageName, pdfLayerTitle);/// generating pdf {sumon}
        if (isGenerated) {
            CustomToast.t(activity, getResources().getString(R.string.pdfGenerateSuccess));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.viewPDF();

                }
            }, 1000);
        } else {
            CustomToast.t(activity, getResources().getString(R.string.storageCheckPdf));
        }
        ibtnBackStatistics.setVisibility(View.VISIBLE);
        ibtnPdfStatisticsLevel.setVisibility(View.VISIBLE);
    }

    private void bindingIds() {

        //// all level title textviews ids
        tvTitleLevelOne = (TextView) findViewById(R.id.tvTitleLevelOne);
        tvTitleLevelTwo = (TextView) findViewById(R.id.tvTitleLevelTwo);
        tvTitleLevelThree = (TextView) findViewById(R.id.tvTitleLevelThree);
        tvTitleLevelFour = (TextView) findViewById(R.id.tvTitleLevelFour);
        tvTitleLevelFive = (TextView) findViewById(R.id.tvTitleLevelFive);
        tvTitleLevelSix = (TextView) findViewById(R.id.tvTitleLevelSix);


        ////all level timer response textviews ids
        tvTimeResponseLevelOne = (TextView) findViewById(R.id.tvTimeResponseLevelOne);

        tvTimeResponseLevelTwo = (TextView) findViewById(R.id.tvTimeResponseLevelTwo);

        tvTimeResponseLevelThree = (TextView) findViewById(R.id.tvTimeResponseLevelThree);

        tvTimeResponseLevelFour = (TextView) findViewById(R.id.tvTimeResponseLevelFour);

        tvTimeResponseLevelFive = (TextView) findViewById(R.id.tvTimeResponseLevelFive);

        tvTimeResponseLevelSix = (TextView) findViewById(R.id.tvTimeResponseLevelSix);

        //// level timer duration ids
        tvTimeDurationLevelOne = (TextView) findViewById(R.id.tvTimeDurationLevelOne);

        tvTimeDurationLevelTwo = (TextView) findViewById(R.id.tvTimeDurationLevelTwo);

        tvTimeDurationLevelThree = (TextView) findViewById(R.id.tvTimeDurationLevelThree);

        tvTimeDurationLevelFour = (TextView) findViewById(R.id.tvTimeDurationLevelFour);

        tvTimeDurationLevelFive = (TextView) findViewById(R.id.tvTimeDurationLevelFive);

        tvTimeDurationLevelSix = (TextView) findViewById(R.id.tvTimeDurationLevelSix);

        /// level one star imageviews ids

        ivStarOneLevelOne = (ImageView) findViewById(R.id.ivStarOneLevelOne);
        ivStarTwoLevelOne = (ImageView) findViewById(R.id.ivStarTwoLevelOne);
        ivStarThreeLevelOne = (ImageView) findViewById(R.id.ivStarThreeLevelOne);

        /// level two star imageviews ids
        ivStarOneLevelTwo = (ImageView) findViewById(R.id.ivStarOneLevelTwo);
        ivStarTwoLevelTwo = (ImageView) findViewById(R.id.ivStarTwoLevelTwo);
        ivStarThreeLevelTwo = (ImageView) findViewById(R.id.ivStarThreeLevelTwo);

        //// level three star imageviews ids
        ivStarOneLevelThree = (ImageView) findViewById(R.id.ivStarOneLevelThree);
        ivStarTwoLevelThree = (ImageView) findViewById(R.id.ivStarTwoLevelThree);
        ivStarThreeLevelThree = (ImageView) findViewById(R.id.ivStarThreeLevelThree);

        //// level four star imageviews ids
        ivStarOneLevelFour = (ImageView) findViewById(R.id.ivStarOneLevelFour);
        ivStarTwoLevelFour = (ImageView) findViewById(R.id.ivStarTwoLevelFour);
        ivStarThreeLevelFour = (ImageView) findViewById(R.id.ivStarThreeLevelFour);

        //// level five star imageviews ids
        ivStarOneLevelFive = (ImageView) findViewById(R.id.ivStarOneLevelFive);
        ivStarTwoLevelFive = (ImageView) findViewById(R.id.ivStarTwoLevelFive);
        ivStarThreeLevelFive = (ImageView) findViewById(R.id.ivStarThreeLevelFive);

        //// level five star imageviews ids
        ivStarOneLevelSix = (ImageView) findViewById(R.id.ivStarOneLevelSix);
        ivStarTwoLevelSix = (ImageView) findViewById(R.id.ivStarTwoLevelSix);
        ivStarThreeLevelSix = (ImageView) findViewById(R.id.ivStarThreeLevelSix);

        tvTitleLevelOne.setText(getResources().getString(R.string.LevelOne));
        tvTitleLevelTwo.setText(getResources().getString(R.string.LevelTwo));
        tvTitleLevelThree.setText(getResources().getString(R.string.LevelThree));
        tvTitleLevelFour.setText(getResources().getString(R.string.LevelFour));
        tvTitleLevelFive.setText(getResources().getString(R.string.LevelFive));
        tvTitleLevelSix.setText(getResources().getString(R.string.LevelSix));

    }

    void UserStatic(User user) {
        errorUserLogsArray = databaseManager.errorLogUserWiseList(user.getKey());
        starArray = databaseManager.getStarListUserKey(user.getKey());
//        databaseManager.getStarListFlayerWise(user.getKey(),user.getFirstLayerTaskID());
    }

    // barchar all level wise array and dataset initialize
    private void barChar() {
        ArrayList<BarEntry> levelArray1 = null;
        ArrayList<BarEntry> levelArray2 = null;
        ArrayList<BarEntry> levelArray3 = null;
        ArrayList<BarEntry> levelArray4 = null;
        ArrayList<BarEntry> levelArray5 = null;
        ArrayList<BarEntry> levelArray6 = null;

        levelArray1 = new ArrayList<>();
        levelArray2 = new ArrayList<>();
        levelArray3 = new ArrayList<>();
        levelArray4 = new ArrayList<>();
        levelArray5 = new ArrayList<>();
        levelArray6 = new ArrayList<>();

        dataSets = new ArrayList<>();

        fLayerLabels = new ArrayList<String>();
        fLayerArray = new ArrayList<Integer>();
        fLayerLabels.add(String.valueOf("TASK"));


        for (int j = 0; j < starArray.size(); j++) {
            if (starArray.get(j).getFirstLayerTaskId() == layer) {
                if (starArray.get(j).getLevelTaskPackId() == 0) {
                    levelArray1.add(new BarEntry(Float.valueOf(starArray.get(j).getLevelPoint()), 0));
                    getStar(0, starArray.get(j).getLevelStar(), starArray.get(j).getLevelRT(), starArray.get(j).getLevelDT());
                }
                if (starArray.get(j).getLevelTaskPackId() == 1) {
                    levelArray2.add(new BarEntry(Float.valueOf(starArray.get(j).getLevelPoint()), 0));
                    getStar(1, starArray.get(j).getLevelStar(), starArray.get(j).getLevelRT(), starArray.get(j).getLevelDT());
                }
                if (starArray.get(j).getLevelTaskPackId() == 2) {
                    levelArray3.add(new BarEntry(Float.valueOf(starArray.get(j).getLevelPoint()), 0));
                    getStar(2, starArray.get(j).getLevelStar(), starArray.get(j).getLevelRT(), starArray.get(j).getLevelDT());
                }
                if (starArray.get(j).getLevelTaskPackId() == 3) {
                    levelArray4.add(new BarEntry(Float.valueOf(starArray.get(j).getLevelPoint()), 0));
                    getStar(3, starArray.get(j).getLevelStar(), starArray.get(j).getLevelRT(), starArray.get(j).getLevelDT());
                }
                if (starArray.get(j).getLevelTaskPackId() == 4) {
                    levelArray5.add(new BarEntry(Float.valueOf(starArray.get(j).getLevelPoint()), 0));
                    getStar(4, starArray.get(j).getLevelStar(), starArray.get(j).getLevelRT(), starArray.get(j).getLevelDT());
                }
                if (starArray.get(j).getLevelTaskPackId() == 5) {
                    levelArray6.add(new BarEntry(Float.valueOf(starArray.get(j).getLevelPoint()), 0));
                    getStar(5, starArray.get(j).getLevelStar(), starArray.get(j).getLevelRT(), starArray.get(j).getLevelDT());
                }
            }
        }

        // barChart for level one
        BDSLevel1 = new BarDataSet(levelArray1, getResources().getString(R.string.LevelOne));  // creating dataset for evel one
        BDSLevel1.setColors(new int[]{userInfo.getFirstLayerColor(layer)});
        BDSLevel1.setValueFormatter(new MyValueFormatter());
        if (levelArray1.size() > 0) {
            tvTitleLevelOne.setVisibility(View.VISIBLE);
            tvTimeDurationLevelOne.setVisibility(View.VISIBLE);
            tvTimeResponseLevelOne.setVisibility(View.VISIBLE);
            ivStarOneLevelOne.setVisibility(View.VISIBLE);
            ivStarTwoLevelOne.setVisibility(View.VISIBLE);
            ivStarThreeLevelOne.setVisibility(View.VISIBLE);

        } else {
            tvTitleLevelOne.setVisibility(View.INVISIBLE);
            tvTimeDurationLevelOne.setVisibility(View.INVISIBLE);
            tvTimeResponseLevelOne.setVisibility(View.INVISIBLE);
            ivStarOneLevelOne.setVisibility(View.INVISIBLE);
            ivStarTwoLevelOne.setVisibility(View.INVISIBLE);
            ivStarThreeLevelOne.setVisibility(View.INVISIBLE);
        }

        // barChart for level two
        BDSLevel2 = new BarDataSet(levelArray2, getResources().getString(R.string.LevelTwo)); // creating dataset for level two
        BDSLevel2.setColors(new int[]{userInfo.getFirstLayerColor(layer)});
        BDSLevel2.setValueFormatter(new MyValueFormatter());
        if (levelArray2.size() > 0) {
            tvTitleLevelTwo.setVisibility(View.VISIBLE);
            tvTimeDurationLevelTwo.setVisibility(View.VISIBLE);
            tvTimeResponseLevelTwo.setVisibility(View.VISIBLE);
            ivStarOneLevelTwo.setVisibility(View.VISIBLE);
            ivStarTwoLevelTwo.setVisibility(View.VISIBLE);
            ivStarThreeLevelTwo.setVisibility(View.VISIBLE);

        } else {
            tvTitleLevelTwo.setVisibility(View.INVISIBLE);
            tvTimeDurationLevelTwo.setVisibility(View.INVISIBLE);
            tvTimeResponseLevelTwo.setVisibility(View.INVISIBLE);
            ivStarOneLevelTwo.setVisibility(View.INVISIBLE);
            ivStarTwoLevelTwo.setVisibility(View.INVISIBLE);
            ivStarThreeLevelTwo.setVisibility(View.INVISIBLE);
        }

        // barChart for level three
        BDSLevel3 = new BarDataSet(levelArray3, getResources().getString(R.string.LevelThree)); // creating dataset for three
        BDSLevel3.setColors(new int[]{userInfo.getFirstLayerColor(layer)});
        BDSLevel3.setValueFormatter(new MyValueFormatter());
        if (levelArray3.size() > 0) {
            tvTitleLevelThree.setVisibility(View.VISIBLE);
            tvTimeDurationLevelThree.setVisibility(View.VISIBLE);
            tvTimeResponseLevelThree.setVisibility(View.VISIBLE);
            ivStarOneLevelThree.setVisibility(View.VISIBLE);
            ivStarTwoLevelThree.setVisibility(View.VISIBLE);
            ivStarThreeLevelThree.setVisibility(View.VISIBLE);

        } else {
            tvTitleLevelThree.setVisibility(View.INVISIBLE);
            tvTimeDurationLevelThree.setVisibility(View.INVISIBLE);
            tvTimeResponseLevelThree.setVisibility(View.INVISIBLE);
            ivStarOneLevelThree.setVisibility(View.INVISIBLE);
            ivStarTwoLevelThree.setVisibility(View.INVISIBLE);
            ivStarThreeLevelThree.setVisibility(View.INVISIBLE);
        }
        // barChart for level four
        BDSLevel4 = new BarDataSet(levelArray4, getResources().getString(R.string.LevelFour)); // creating dataset for level four
        BDSLevel4.setColors(new int[]{userInfo.getFirstLayerColor(layer)});
        BDSLevel4.setValueFormatter(new MyValueFormatter());
        if (levelArray4.size() > 0) {
            tvTitleLevelFour.setVisibility(View.VISIBLE);
            tvTimeDurationLevelFour.setVisibility(View.VISIBLE);
            tvTimeResponseLevelFour.setVisibility(View.VISIBLE);
            ivStarOneLevelFour.setVisibility(View.VISIBLE);
            ivStarTwoLevelFour.setVisibility(View.VISIBLE);
            ivStarThreeLevelFour.setVisibility(View.VISIBLE);

        } else {
            tvTitleLevelFour.setVisibility(View.INVISIBLE);
            tvTimeDurationLevelFour.setVisibility(View.INVISIBLE);
            tvTimeResponseLevelFour.setVisibility(View.INVISIBLE);
            ivStarOneLevelFour.setVisibility(View.INVISIBLE);
            ivStarTwoLevelFour.setVisibility(View.INVISIBLE);
            ivStarThreeLevelFour.setVisibility(View.INVISIBLE);
        }
        // barChart for level five
        BDSLevel5 = new BarDataSet(levelArray5, getResources().getString(R.string.LevelSix)); // creating dataset for level five
        BDSLevel5.setColors(new int[]{userInfo.getFirstLayerColor(layer)});
        BDSLevel5.setValueFormatter(new MyValueFormatter());
        if (levelArray5.size() > 0) {
            tvTitleLevelFive.setVisibility(View.VISIBLE);
            tvTimeDurationLevelFive.setVisibility(View.VISIBLE);
            tvTimeResponseLevelFive.setVisibility(View.VISIBLE);
            ivStarOneLevelFive.setVisibility(View.VISIBLE);
            ivStarTwoLevelFive.setVisibility(View.VISIBLE);
            ivStarThreeLevelFive.setVisibility(View.VISIBLE);

        } else {
            tvTitleLevelFive.setVisibility(View.INVISIBLE);
            tvTimeDurationLevelFive.setVisibility(View.INVISIBLE);
            tvTimeResponseLevelFive.setVisibility(View.INVISIBLE);
            ivStarOneLevelFive.setVisibility(View.INVISIBLE);
            ivStarTwoLevelFive.setVisibility(View.INVISIBLE);
            ivStarThreeLevelFive.setVisibility(View.INVISIBLE);
        }
        // barChart for level six
        BDSLevel6 = new BarDataSet(levelArray6, getResources().getString(R.string.LevelSix)); // creating dataset for level six
        BDSLevel6.setColors(new int[]{userInfo.getFirstLayerColor(layer)});
        BDSLevel6.setValueFormatter(new MyValueFormatter());
        if (levelArray6.size() > 0) {
            tvTitleLevelSix.setVisibility(View.VISIBLE);
            tvTimeDurationLevelSix.setVisibility(View.VISIBLE);
            tvTimeResponseLevelSix.setVisibility(View.VISIBLE);
            ivStarOneLevelSix.setVisibility(View.VISIBLE);
            ivStarTwoLevelSix.setVisibility(View.VISIBLE);
            ivStarThreeLevelSix.setVisibility(View.VISIBLE);

        } else {
            tvTitleLevelSix.setVisibility(View.INVISIBLE);
            tvTimeDurationLevelSix.setVisibility(View.INVISIBLE);
            tvTimeResponseLevelSix.setVisibility(View.INVISIBLE);
            ivStarOneLevelSix.setVisibility(View.INVISIBLE);
            ivStarTwoLevelSix.setVisibility(View.INVISIBLE);
            ivStarThreeLevelSix.setVisibility(View.INVISIBLE);
        }

        // combined all dataset into an arraylist
        dataSets.add(BDSLevel1);
        dataSets.add(BDSLevel2);
        dataSets.add(BDSLevel3);
        dataSets.add(BDSLevel4);
        dataSets.add(BDSLevel5);
        dataSets.add(BDSLevel6);

// initialize the Bardata with argument labels and dataSet
        data = new BarData(fLayerLabels, dataSets);
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

        yAxis = chart.getAxisRight();
        yAxis.setDrawGridLines(false);

        chart.invalidate();
        if (starArray.size() == 0) {
            rlNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getResources().getText(R.string.tvNoData));
        }
    }

    //  get time  formate as a string
    public static String combinationFormatter(final long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long hours = TimeUnit.MILLISECONDS.toHours(millis);

        StringBuilder b = new StringBuilder();
        if (hours != 0) {
            b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) :
                    String.valueOf(hours));
            b.append("h. ");
            b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :
                    String.valueOf(minutes));
            b.append("m. ");
            b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                    String.valueOf(seconds));
            b.append("s. ");
            return b.toString();
        } else if (minutes != 0) {
            b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :
                    String.valueOf(minutes));
            b.append("m. ");
            b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                    String.valueOf(seconds));
            b.append("s. ");
            return b.toString();
        } else if (minutes != 0) {
            b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                    String.valueOf(seconds));
            b.append("s. ");
            return b.toString();
        } else
            b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                    String.valueOf(seconds));
        b.append("s. ");

        return b.toString();

    }

    // user level wise TTD = total time duration and RT = Response time Duration text view show and star show
    public void getStar(int level, int star, long RT, long DT) {
        switch (level) {
            case 0:
                if (star == 1) {
                    ivStarOneLevelOne.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelOne.setImageResource(R.drawable.ic_star_grey);
                    ivStarThreeLevelOne.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 2) {
                    ivStarOneLevelOne.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelOne.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelOne.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 3) {
                    ivStarOneLevelOne.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelOne.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelOne.setImageResource(R.drawable.ic_star_yellow);
                }
                tvTimeDurationLevelOne.setText("TTD  " + combinationFormatter(DT));
                tvTimeResponseLevelOne.setText("TMR  " + combinationFormatter(RT));
                break;

            case 1:

                if (star == 1) {
                    ivStarOneLevelTwo.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelTwo.setImageResource(R.drawable.ic_star_grey);
                    ivStarThreeLevelTwo.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 2) {
                    ivStarOneLevelTwo.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelTwo.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelTwo.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 3) {
                    ivStarOneLevelTwo.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelTwo.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelTwo.setImageResource(R.drawable.ic_star_yellow);
                }
                tvTimeDurationLevelTwo.setText(("TTD  " + combinationFormatter(DT)));
                tvTimeResponseLevelTwo.setText("TMR  " + combinationFormatter(RT));
                break;

            case 2:

                if (star == 1) {
                    ivStarOneLevelThree.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelThree.setImageResource(R.drawable.ic_star_grey);
                    ivStarThreeLevelThree.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 2) {
                    ivStarOneLevelThree.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelThree.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelThree.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 3) {
                    ivStarOneLevelThree.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelThree.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelThree.setImageResource(R.drawable.ic_star_yellow);
                }

                tvTimeDurationLevelThree.setText(("TTD  " + combinationFormatter(DT)));
                tvTimeResponseLevelThree.setText("TMR  " + combinationFormatter(RT));

                break;

            case 3:

                if (star == 1) {
                    ivStarOneLevelFour.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelFour.setImageResource(R.drawable.ic_star_grey);
                    ivStarThreeLevelFour.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 2) {
                    ivStarOneLevelFour.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelFour.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelFour.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 3) {
                    ivStarOneLevelFour.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelFour.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelFour.setImageResource(R.drawable.ic_star_yellow);
                }
                tvTimeDurationLevelFour.setText(("TTD  " + combinationFormatter(DT)));
                tvTimeResponseLevelFour.setText("TMR  " + combinationFormatter(RT));

                break;
            case 4:

                if (star == 1) {
                    ivStarOneLevelFive.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelFive.setImageResource(R.drawable.ic_star_grey);
                    ivStarThreeLevelFive.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 2) {
                    ivStarOneLevelFive.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelFive.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelFive.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 3) {
                    ivStarOneLevelFive.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelFive.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelFive.setImageResource(R.drawable.ic_star_yellow);
                }
                tvTimeDurationLevelFive.setText(("TTD  " + combinationFormatter(DT)));
                tvTimeResponseLevelFive.setText("TMR  " + combinationFormatter(RT));

                break;
            case 5:

                if (star == 1) {
                    ivStarOneLevelSix.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelSix.setImageResource(R.drawable.ic_star_grey);
                    ivStarThreeLevelSix.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 2) {
                    ivStarOneLevelSix.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelSix.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelSix.setImageResource(R.drawable.ic_star_grey);
                } else if (star == 3) {
                    ivStarOneLevelSix.setImageResource(R.drawable.ic_star_yellow);
                    ivStarTwoLevelSix.setImageResource(R.drawable.ic_star_yellow);
                    ivStarThreeLevelSix.setImageResource(R.drawable.ic_star_yellow);
                }
                tvTimeDurationLevelSix.setText(("TTD  " + combinationFormatter(DT)));
                tvTimeResponseLevelSix.setText("TMR  " + combinationFormatter(RT));

                break;
        }

    }

    // lamp on and off function
    public void screenMode(boolean isLampTem) {
        if (isLampTem) {
            rlStatisticsLamp.setVisibility(View.VISIBLE);
            ibtnLampStatistics.setImageResource(R.drawable.ic_lamp_dream);
            isLamp = false;

        } else {
            rlStatisticsLamp.setVisibility(View.GONE);
            ibtnLampStatistics.setImageResource(R.drawable.ic_lamp);
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
