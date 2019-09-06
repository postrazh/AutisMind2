package com.autismindd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.autismindd.customui.BookCaseView;
import com.autismindd.listener.MultiChoiceAdapterFirstLayerInterface;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.services.BackgroundMusicService;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.CustomToast;
import com.autismindd.R;
import com.autismindd.adapter.FirstLayerMultiAdapter;
import com.autismindd.adapter.MultiChoiceViewActivityFirstLayer;
import com.autismindd.dao.FirstLayer;
import com.autismindd.share.Share;
import com.autismindd.utilities.StaticAccess;
import com.autismindd.utilities.StaticInstance;

import java.util.ArrayList;

/**
 * Created by RAFI on 9/29/2016.
 */

public class FirstLayerActivity extends MultiChoiceViewActivityFirstLayer {
    private IDatabaseManager databaseManager;
    FirstLayerMultiAdapter firstLayerAdapter;
    public ArrayList<FirstLayer> firstLayerList = new ArrayList<>();
    BookCaseView gvFirstLayer;
    long UserId;
    Share share;
    ImageButton ibtnBackFirstLayer;
    FirstLayerActivity activity;
    StaticInstance staticInstance;
    MultiChoiceAdapterFirstLayerInterface.ControlMethods multiChoice_listener;
    boolean musicControl = false;
    boolean isLayerClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        databaseManager = new DatabaseManager(this);

        staticInstance = StaticInstance.getInstance();
        staticInstance.clearFirstLayer();
        staticInstance.clearLevel();
        staticInstance.clearErrorArray();
        firstLayerList = databaseManager.listFirstLayer();

        ibtnBackFirstLayer = (ImageButton) findViewById(R.id.ibtnBackFirstLayer);
        gvFirstLayer = (BookCaseView) findViewById(R.id.gvFirstLayer);

        firstLayerAdapter = new FirstLayerMultiAdapter(activity, firstLayerList);
        gvFirstLayer.setAdapter(firstLayerAdapter);
        gvFirstLayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isLayerClicked) {
                    isLayerClicked = true;
                    if (firstLayerList.get(position).getLocked()) {
                        musicControl = true;
                        staticInstance.setFirstLayer(firstLayerList.get(position));
                        Intent intentFirstLayer = new Intent(activity, PurchaseActivity.class);
                        startActivity(intentFirstLayer);
                        finish();
                    } else if (!firstLayerList.get(position).getLocked()) {
                        musicControl = true;
                        staticInstance.setFirstLayer(firstLayerList.get(position));
                        Intent intentTask = new Intent(activity, TaskPackActivity.class);
                        startActivity(intentTask);
                        finish();

                    } else {
                        multiChoice_listener.setSingleModeId(firstLayerList.get(position));
                    }
                }


            }
        });

        ibtnBackFirstLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animanation.zoomOut(v);
                musicControl = true;
                Intent intent = new Intent(FirstLayerActivity.this, UserListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (user.getMusic() == 0) {
            if (!musicControl)
                BackgroundMusicService.stopMusic(activity);
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
        multiChoice_listener.clearSingleChoiceMode();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        musicControl = true;
        Intent intentBack = new Intent(FirstLayerActivity.this, UserListActivity.class);
        startActivity(intentBack);
        finish();
    }

    //handling avatar view  click
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("add1", "Heeres");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                int x = (int) event.getX();
                int y = (int) event.getY();
                if (customAvatarView.isViewOverlapping(x, y)) {
                    musicControl = true;
                    Intent intent = new Intent(activity, AvatarUpdateActivity.class);
                    intent.putExtra(StaticAccess.TAG_UPDATE_ACTIVITY, StaticAccess.TAG_UPDATE_ACTIVITY_FLAG);
                    startActivity(intent);
                }


                break;

            default:
                return false;
        }
        return super.dispatchTouchEvent(event);
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
