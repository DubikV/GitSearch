package com.gmail.vanyadubik.gitsearch.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.gmail.vanyadubik.gitsearch.R;

public class ActivityUtils {

    public ActivityUtils() {
    }

    public int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)(dp * (displayMetrics.densityDpi / 160f));
    }

    public void showMessage(String textMessage, Context mContext) {
        if (textMessage == null || textMessage.isEmpty()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.questions_title_info));
        builder.setMessage(textMessage);

        builder.setNeutralButton(mContext.getString(R.string.questions_answer_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showShortToast(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

}
