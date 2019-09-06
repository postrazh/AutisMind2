package com.autismindd.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.autismindd.R;
import com.autismindd.Dialog.DialogNavBarHide;

public class TransparentToastActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_toast);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String message = getIntent().getExtras().getString("message");

        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_toast);

        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        tvContent.setText(message);

        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DialogNavBarHide.navBarHide(this, dialog);


    }

}
