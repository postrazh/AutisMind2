package com.autismindd.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.autismindd.activities.TransparentToastActivity;

/**
 * Created by ibrar on 8/11/2016.
 */
public class CustomToast {

    public static void m(String message) {
        Log.d("md", "" + message);
    }

    public static void M(String str, String message) {
        Log.e(str, message);
    }

    public static void t(Context ctx, String message) {

        Intent intent = new Intent(ctx, TransparentToastActivity.class);
        intent.putExtra("message", message);
        ctx.startActivity(intent);

    }

}
