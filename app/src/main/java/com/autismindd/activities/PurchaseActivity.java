package com.autismindd.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.adapter.MultiChoiceViewActivityFirstLayer;
import com.autismindd.adapter.PurchaseAdapter;
import com.autismindd.adapter.TutorialAdapter;
import com.autismindd.customui.BookCaseView;
import com.autismindd.dao.FirstLayer;
import com.autismindd.dao.FirstLayerTaskImage;
import com.autismindd.download.PurchaseSyncTask;
import com.autismindd.inapputil.IabBroadcastReceiver;
import com.autismindd.inapputil.IabHelper;
import com.autismindd.inapputil.IabResult;
import com.autismindd.inapputil.Inventory;
import com.autismindd.inapputil.Purchase;
import com.autismindd.listener.MultiChoiceAdapterFirstLayerInterface;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.parser.DailyFirstLayerParser;
import com.autismindd.pojo.FLayer;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.share.Share;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.AppController;
import com.autismindd.utilities.ApplicationMode;
import com.autismindd.utilities.ArrowDownloadButton;
import com.autismindd.utilities.ConnectionManagerPromo;
import com.autismindd.utilities.CustomToast;
import com.autismindd.utilities.ImageProcessing;
import com.autismindd.utilities.SharedPreferenceValue;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;
import com.autismindd.utilities.UserInfo;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by RAFI on 10/23/2016.
 */
//implements BillingProcessor.IBillingHandler
public class PurchaseActivity extends MultiChoiceViewActivityFirstLayer implements IabBroadcastReceiver.IabBroadcastListener {
    String lampSound = "sound_click_lamp.mp3";
    MediaPlayer mp;
    PurchaseActivity activity;
    public IDatabaseManager databaseManager;
    public ArrayList<FirstLayer> firstLayerList = new ArrayList<>();
    BookCaseView gvFirstLayerPurchase;
    PurchaseAdapter purchaseAdapter;
    PagerAdapter adapter;
    MultiChoiceAdapterFirstLayerInterface.ControlMethods multiChoice_listener;
    StaticInstance staticInstance;
    ImageView ivProPicFlayerPurchase, ivPurchaseLamp;
    String[] tutorialText;
    ArrayList<FirstLayerTaskImage> firstTaskImages;
    ViewPager vpTaskImage;
    ImageButton ibtnPrev, ibtnNext, ibtnBackPurchase;
    LinearLayout llBookCase;
    int counter = 0, lastPage, swipeFlag;
    FirstLayer firstLayer;
    TextView tvFlayerNamePurchase, tvPurchase, tvTask;
    LinearLayout lnCellBgFlayerPurchase;
    public ProgressDialog pDialogUnzip;

    ArrowDownloadButton downloadBtnPurchace;
    private String download_url;
    Share share;
    String FILE_PATH;
    boolean isDownload = false;
    public boolean readyToPurchase = false;
    public boolean isDailyDownload = false;
    RelativeLayout rlPurchaseLamp_off;
    public RelativeLayout rlDownloadBack, rlFullPurchase;
    boolean isPurchase = false;
    IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;
    static final String TAG = "Austismind";
    static final int RC_REQUEST = 10001;
    boolean musicControl = false;
    boolean hasSizeStorage = false;
    ImageProcessing imageProcessing;
    public ArrayList<FLayer> arrDailyFlayer;
    public ArrayList<FirstLayer> arrFirstUpdateList;

    public static String json_url = StaticAccess.ROOT_URL + "ImagesManager/GetAllTask/?authenticationUserName=AutisMind&password=dniMsituA";
    private static final char[] symbols = new char[36];

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.activity_purches);
        databaseManager = new DatabaseManager(this);
        imageProcessing = new ImageProcessing(activity);

        staticInstance = StaticInstance.getInstance();
        user = staticInstance.getUser();

        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
        }
        ibtnBackPurchase = (ImageButton) findViewById(R.id.ibtnBackPurchase);
        llBookCase = (LinearLayout) findViewById(R.id.llBookCase);
        firstLayer = staticInstance.getFirstLayer();

        gvFirstLayerPurchase = (BookCaseView) findViewById(R.id.gvFirstLayerPurchase);
        ivProPicFlayerPurchase = (ImageView) findViewById(R.id.ivProPicFlayerPurchase);
        lnCellBgFlayerPurchase = (LinearLayout) findViewById(R.id.lnCellBgFlayerPurchase);
        tvFlayerNamePurchase = (TextView) findViewById(R.id.tvFlayerNamePurchase);
        tvPurchase = (TextView) findViewById(R.id.tvPurchase);
        vpTaskImage = (ViewPager) findViewById(R.id.vpTaskImage);
        ibtnPrev = (ImageButton) findViewById(R.id.ibtnPrev);
        ibtnNext = (ImageButton) findViewById(R.id.ibtnNext);
        tvTask = (TextView) findViewById(R.id.tvTask);
        downloadBtnPurchace = (ArrowDownloadButton) findViewById(R.id.downloadBtnPurchace);
        rlPurchaseLamp_off = (RelativeLayout) findViewById(R.id.rlPurchaseLamp_off);
        rlDownloadBack = (RelativeLayout) findViewById(R.id.rlDownloadBack);
        rlFullPurchase = (RelativeLayout) findViewById(R.id.rlFullPurchase);
        ivPurchaseLamp = (ImageView) findViewById(R.id.ivPurchaseLamp);
        rlDownloadBack.setVisibility(View.GONE);
        ibtnPrev.setVisibility(View.INVISIBLE);
        

        ibtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                swipeFlag = 1;
                previous(0);
            }
        });


        ibtnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                swipeFlag = 1;
                next(0);
            }
        });


        isLamp = SharedPreferenceValue.getLampFlag(activity);
        // lamp on or off
        screenModePurchase(isLamp);
        ivPurchaseLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenModePurchase(isLamp);
                playSound(lampSound);
            }
        });


        if (firstLayer != null) {
            setFirstLayerImage(firstLayer);
        } else {

        }
        if (!ApplicationMode.devMode) {
        }
        gvLoad();
        if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
            showProgressDialog();
            requestForFirstLayer(json_url);
        }

        gvFirstLayerPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staticInstance.clearFirstLayer();
                firstLayer = firstLayerList.get(position);
                setFirstLayerImage(firstLayer);
                multiChoice_listener.setSingleModeId(firstLayerList.get(position));


            }
        });


        ibtnBackPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animanation.zoomOut(v);
                musicControl = true;
                Intent intent = new Intent(PurchaseActivity.this, FirstLayerActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // lamp light
    private void screenModePurchase(boolean isLampTem) {
        if (isLampTem) {
            rlPurchaseLamp_off.setVisibility(View.VISIBLE);
            ivPurchaseLamp.setImageResource(R.drawable.ic_lamp_dream);
            isLamp = false;

        } else {
            rlPurchaseLamp_off.setVisibility(View.GONE);
            ivPurchaseLamp.setImageResource(R.drawable.ic_lamp);
            isLamp = true;
        }
        SharedPreferenceValue.setLampFlag(activity, isLampTem);
    }


    // previous slide
    public void previous(int prev_page) {
        if (firstTaskImages.size() > 0)
            if (counter > 0) {
                counter--;
                vpTaskImage.setCurrentItem(counter);

            } else {
                counter = firstTaskImages.size();
                vpTaskImage.setCurrentItem(counter);
            }
    }

    // next slide
    public void next(int next_page) {

        if (firstTaskImages.size() > 0)
            if (counter < firstTaskImages.size()) {
                counter++;
                vpTaskImage.setCurrentItem(counter);

            } else {
                counter = firstTaskImages.size();
                vpTaskImage.setCurrentItem(counter);
            }
    }

    @Override
    public void setMultiChoiceListener(MultiChoiceAdapterFirstLayerInterface.ControlMethods listener) {
        multiChoice_listener = listener;
    }

    @Override
    public void singleTapDone(FirstLayer firstLayer) {


    }

    @Override
    public void multiChoiceClear() {

    }

    @Override
    public void multiChoiceModeEnter(ArrayList<FirstLayer> FirstLayerList, boolean mode) {

    }

    @Override
    public void firstButtonClicked(View v) {

    }

    @Override
    public void secondButtonClicked(View v) {

    }

    @Override
    public void thirdButtonClicked(View v) {

    }

    @Override
    public void fourthButtonClicked(View v) {

    }

    @Override
    public void rendomOutSideClicked(View v) {

    }


    //First first layer Id wise
    void setFirstLayerImage(final FirstLayer firstLayer) {

        this.firstLayer = firstLayer;
        if (firstLayer != null) {
            tvFlayerNamePurchase.setText(firstLayer.getName());
           /* Picasso.with(activity)
                    .load(firstLayer.getImgUrl())
                    .resize(128,128)
                    .into(ivProPicFlayerPurchase);*/
            imageProcessing.setImageWith_loader(ivProPicFlayerPurchase, firstLayer.getImgUrl());
//            xcsdfd

            firstLayerBGShape(firstLayer.getFirstLayerTaskID());
            tvTask.setText(getStringArrayTvTask(firstLayer.getFirstLayerTaskID()));

            // URL
            FILE_PATH = Environment.getExternalStorageDirectory() + getResources().getString(R.string.MindAppSlash) + firstLayer.getFileName();
            //Id =FirstLayerID
            download_url = StaticAccess.ROOT_URL + "ImagesManager/GetPackByID/?id=" + firstLayer.getFirstLayerTaskID() + "&authenticationUserName=AutisMind&password=dniMsituA";

            viewPagerLoad(firstLayer.getFirstLayerTaskID());
//            appBilling(firstLayer);
            //purchase Button
            isPurchase = false;
            mHelper = new IabHelper(this, StaticAccess.LICENSE_KEY);
            mHelper.enableDebugLogging(true);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        CustomToast.t(activity, getString(R.string.problemSettingUpBilling) + result);
                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;


                    mBroadcastReceiver = new IabBroadcastReceiver(activity);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);

                    // IAB is fully set up. Now, let's get an inventory of stuff we own.
                    Log.d(TAG, "Setup successful. Querying inventory.");
                    try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        CustomToast.t(activity, getString(R.string.Error_querying_inventory));
                    }
                }
            });


            tvPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animanation.zoomOut(tvPurchase);
                    if (isPurchase) {

                        if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                            new DownloadFileFromURL(download_url, firstLayer).execute();
                        } else
                            CustomToast.t(activity, getResources().getString(R.string.connectInternet));

                        isPurchase = false;
                    } else {
                        onBuyTaskPackButtonClicked(UserInfo.getProductID(firstLayer.getFirstLayerTaskID()));
                    }

                }
            });


        }

    }

    void firstLayerBGShape(int color) {
        //Image layout single cell
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(10);
        shape.setColor(Color.parseColor("#ffffff"));
        lnCellBgFlayerPurchase.setBackground(shape);

        //Text layout cell
        GradientDrawable shapeText = new GradientDrawable();
        shapeText.setCornerRadii(new float[]{0f, 0f, 0f, 0f, 10f, 10f, 10f, 10f});
        shapeText.setColor(UserInfo.rendomColorGeneretor(color));
        tvFlayerNamePurchase.setBackground(shapeText);

        //purchase textView Design
        GradientDrawable shapePurchase = new GradientDrawable();
        shapePurchase.setCornerRadii(new float[]{10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f});
        shapePurchase.setColor(UserInfo.rendomColorGeneretor(color));
        tvPurchase.setBackground(shapePurchase);
    }

    public void onBuyTaskPackButtonClicked(String productID) {
        Log.d(TAG, "Launching purchase flow for TaskPack.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        RandomString randomString = new RandomString(36);
        String payload = randomString.nextString();

        try {
            mHelper.launchPurchaseFlow(this, productID, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.d(TAG, "Error launching purchase flow. Another async operation in progress.");
        }
    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                CustomToast.t(activity, getString(R.string.Error_purchasing) + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                CustomToast.t(activity, getString(R.string.Error_purchasing_authincity));
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(UserInfo.getProductID(firstLayer.getFirstLayerTaskID()))) {
                // bought 1/4 task of gas. So consume it.

                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    new DownloadFileFromURL(download_url, firstLayer).execute();
                } else
                    CustomToast.t(activity, getResources().getString(R.string.connectInternet));

                Log.d(TAG, "Purchase is task pack. Starting gas consumption.");

            }


        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);


            if (mHelper == null) return;

            if (result.isSuccess()) {

                Log.d(TAG, "Consumption successful. Provisioning.");
                if (ConnectionManagerPromo.getConnectivityStatus(activity) != StaticAccess.TYPE_NOT_CONNECTED) {
                    new DownloadFileFromURL(download_url, firstLayer).execute();
                } else
                    CustomToast.t(activity, getResources().getString(R.string.connectInternet));

            } else {
                CustomToast.t(activity, getString(R.string.Error_consuming) + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                CustomToast.t(activity, getString(R.string.Failed_query_inventory) + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");


            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(UserInfo.getProductID(firstLayer.getFirstLayerTaskID()));

            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have task pack. Consuming it.");
                if (UserInfo.getProductID(firstLayer.getFirstLayerTaskID()).equals(gasPurchase.getSku())) {
                    isPurchase = true;
                } else {
                    isPurchase = false;
                }

                return;
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            CustomToast.t(activity, getString(R.string.Error_inventory_another));
        }
    }

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
            rlDownloadBack.setVisibility(View.VISIBLE);
            rlDownloadBack.setClickable(true);
            downloadBtnPurchace.setVisibility(View.VISIBLE);
            downloadBtnPurchace.startAnimating();
            // AllButtonVisibility(false);
            rlFullPurchase.setClickable(true);

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
            downloadBtnPurchace.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
//            dismissDialog(progress_bar_type);
//            rlDownloadBack.setClickable(true);
            rlDownloadBack.setVisibility(View.GONE);
            downloadBtnPurchace.reset();
            downloadBtnPurchace.setVisibility(View.GONE);
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


            SharedPreferenceValue.setDownloadFlag(activity, StaticAccess.TAG_SP_DOWNLOAD_FLAG);
            share = new Share(activity);

            try {
                share.unZip(FILE_PATH);
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
            rlFullPurchase.setClickable(false);
            if (share.getIsInstallStatus() && isDownload) {

                firstLayer.setLocked(false);
                firstLayer.setState(false);
                databaseManager.updateFirstLayer(firstLayer);
                if (firstLayerList.size() > 0) {
                    gvLoad();
                    setFirstLayerImage(firstLayerList.get(0));
                } else {
                    musicControl = true;
                    Intent intentD = new Intent(activity, FirstLayerActivity.class);
                    intentD.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentD);
                    finish();

                }
            } else
                CustomToast.t(activity, getResources().getString(R.string.installFailed));


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        musicControl = false;
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    // gridView Reload
    public void gvLoad() {
        firstLayerList = databaseManager.getStatusTrueFirstLayerList(true);
        if (firstLayerList != null)
            purchaseAdapter = new PurchaseAdapter(activity, firstLayerList);
        gvFirstLayerPurchase.setAdapter(purchaseAdapter);
    }

    // viewPage task wise Image Load
    void viewPagerLoad(int fLayerID) {
        firstTaskImages = new ArrayList<>();
        firstTaskImages = databaseManager.getFirstLayerImageList(fLayerID);
        adapter = new TutorialAdapter(activity, firstTaskImages);
        vpTaskImage.setCurrentItem(1);
        vpTaskImage.setAdapter(adapter);

        vpTaskImage.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (lastPage > position) {
                    if (swipeFlag != 1) {
                        previous(0);
                    }
                } else {
                    if (swipeFlag != 1) {
                        next(0);
                    }
                }

                if (position == 0) {
                    ibtnPrev.setVisibility(View.INVISIBLE);
                } else {
                    ibtnPrev.setVisibility(View.VISIBLE);
                }
                if (position == firstTaskImages.size() - 1) {
                    ibtnNext.setVisibility(View.INVISIBLE);
                } else {
                    ibtnNext.setVisibility(View.VISIBLE);
                }
                lastPage = position;
                swipeFlag = 0;
            }
        });
    }

    //get for task string from resource
    public String getStringArrayTvTask(int i) {
        String taskText[] = getResources().getStringArray(R.array.FirstLayerTaskString);
        String taskName = "";
        switch (i) {
            case 0:
                taskName = taskText[0];
                break;
            case 1:
                taskName = taskText[1];
                break;
            case 2:
                taskName = taskText[2];
                break;
            case 3:
                taskName = taskText[3];
                break;
            case 4:
                taskName = taskText[4];
                break;
            case 5:
                taskName = taskText[5];
                break;
            case 6:
                taskName = taskText[6];
                break;
            case 7:
                taskName = taskText[7];
                break;
            case 8:
                taskName = taskText[8];
                break;
            case 9:
                taskName = taskText[9];
                break;

        }
        return taskName;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    // Dialog for Purchase by Rokan
    private void dialogPurchase() {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_purchase);
        final TextView tvPurchase = (TextView) dialog.findViewById(R.id.tvPurchase);

        Button btnCancelPurchase = (Button) dialog.findViewById(R.id.btnCancelPurchase);
        Button btnOkPurchase = (Button) dialog.findViewById(R.id.btnOkPurchase);

        btnCancelPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        btnOkPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(this, dialog);

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

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    public class RandomString {

        /*
         * static { for (int idx = 0; idx < 10; ++idx) symbols[idx] = (char)
         * ('0' + idx); for (int idx = 10; idx < 36; ++idx) symbols[idx] =
         * (char) ('a' + idx - 10); }
         */


        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }

    }

    public final class SessionIdentifierGenerator {

        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }

    }


  /*  public void AllButtonVisibility(boolean control) {

        ibtnBackPurchase.setClickable(control);
        ibtnNext.setClickable(control);
        ibtnPrev.setClickable(control);
        llBookCase.setClickable(control);
        vpTaskImage.setHorizontalScrollBarEnabled(control);
        gvFirstLayerPurchase.setVerticalScrollBarEnabled(control);
        tvPurchase.setClickable(control);
    }*/

    public void requestForFirstLayer(String Url) {

        JsonArrayRequest jreq = new JsonArrayRequest(Url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        parserJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialogUnzip.dismiss();
            }
        });

        AppController.getInstance().addToRequestQueue(jreq, "jreq");


    }


    private void parserJson(JSONArray response) {
        DailyFirstLayerParser dailyParser = new DailyFirstLayerParser(activity, response);
        arrDailyFlayer = new ArrayList<>();
        arrDailyFlayer = dailyParser.getParserArray();
        isDailyDownload = dailyParser.isDownload;
        if (isDailyDownload) {
            PurchaseSyncTask purchaseSyncTask=new PurchaseSyncTask(activity);
            purchaseSyncTask.getCheckFileNameAsync();
        }
    }


    // Json Downloading Progress
    void showProgressDialog() {
        pDialogUnzip = new ProgressDialog(activity);
        pDialogUnzip.setMessage(getResources().getString(R.string.synMessage));
        pDialogUnzip.setIndeterminate(false);
        pDialogUnzip.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialogUnzip.setCancelable(false);
        pDialogUnzip.show();
    }
}
