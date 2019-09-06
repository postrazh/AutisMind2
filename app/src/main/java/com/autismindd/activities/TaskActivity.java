package com.autismindd.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.autismindd.dao.Task;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;
import com.autismindd.Dialog.DialogNavBarHide;
import com.autismindd.R;
import com.autismindd.adapter.TaskAdapter;
import com.autismindd.share.Share;
import com.autismindd.utilities.Animanation;
import com.autismindd.utilities.ApplicationMode;

import java.util.ArrayList;

public class TaskActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private IDatabaseManager databaseManager;
    TaskAdapter taskAdapter;
    public ArrayList<Task> tasks = new ArrayList<>();
    GridView gvTask;
    long taskPackId;
    Share share;
    ImageButton ibtnBackTask;
    TaskActivity ref;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);
        databaseManager = new DatabaseManager(this);
        share = new Share(this);
        ref = this;

       ibtnBackTask = (ImageButton) findViewById(R.id.ibtnBackTask);


        if(!ApplicationMode.devMode){
//            ibtnBackTask.setVisibility(View.INVISIBLE);
        }

        gvTask = (GridView) findViewById(R.id.gvTask);
        gvTask.setOnItemClickListener(this);
        gvTask.setDrawSelectorOnTop(true);

        if (getIntent().getExtras() != null) {
            taskPackId = getIntent().getLongExtra("taskPackId", -1);
            fillGridview();
        }
          ibtnBackTask.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intentBack = new Intent(TaskActivity.this, FirstLayerActivity.class);
                  startActivity(intentBack);
                  Animanation.zoomOut(v);
                  finish();
              }
          });
    }

    // Gridview on Item Click redirect to Player / Editor
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TaskActivity.this, PlayerActivity.class);
        intent.putExtra("taskPackId", taskPackId);
        intent.putExtra("position", position);
        startActivity(intent);
        finish();
    }

    // Filling gridview from Database
    private void fillGridview() {
        tasks = (ArrayList<Task>) databaseManager.listTasksByTAskPackId(taskPackId);
        if (tasks != null) {
            taskAdapter = new TaskAdapter(this, tasks);
            gvTask.setAdapter(taskAdapter);
        }
    }



    // dialog for BaclPermission created by Rokan
    private void dialogBackPermission() {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_back_permission);
        dialog.setCancelable(false);

        final TextView tvBackPermission = (TextView) dialog.findViewById(R.id.tvBackPermission);
        tvBackPermission.setText(getResources().getText(R.string.BackPermission));
        Button btnCancelPermission = (Button) dialog.findViewById(R.id.btnCancelPermission);
        Button btnOkPermission = (Button) dialog.findViewById(R.id.btnOkPermission);

        btnCancelPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(TaskActivity.this, TaskPackActivity.class);
                startActivity(intentBack);
                finish();
                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(this, dialog);

    }

}
