package com.autismindd.Dialog;

import com.autismindd.utilities.CustomToast;
import com.autismindd.R;
import com.autismindd.activities.TaskPackActivity;
import com.autismindd.dao.TaskPack;
import com.autismindd.manager.DatabaseManager;
import com.autismindd.manager.IDatabaseManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class TaskPackDialog extends Dialog {

    Context context;
    TaskPackActivity taskPackActivity;
    EditText edtTaskPack;
    Button btnDone;
    public TaskPack pack;
    private IDatabaseManager databaseManager;
    TaskPackDialog dialog;


    public TaskPackDialog(Context context, TaskPackActivity taskPackActivity) {
        super(context, R.style.CustomAlertDialog);
        this.context = context;
        this.taskPackActivity = taskPackActivity;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pack_dialog);

        pack = new TaskPack();
        databaseManager = new DatabaseManager(context);

        edtTaskPack = (EditText) findViewById(R.id.edtTaskPack);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTaskPack.getText().toString().length() == 0) {
                    CustomToast.t(taskPackActivity, context.getResources().getString(R.string.addText));
                } else {
                    pack.setName(edtTaskPack.getText().toString());
                    pack.setCreatedAt(new Date());
                    databaseManager.insertTaskPack(pack);
                    edtTaskPack.setText("");
//                    taskPackActivity.fillGridview();
                    dialog.dismiss();
                }


            }
        });


    }
}
