package com.example.crcle;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Loading_alert {

    Activity activity;
    AlertDialog dialog;

    public Loading_alert(AlertDialog myActivity){
        dialog=myActivity;
    }
    void startAlertdialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_layout,null));
        builder.setCancelable(true);

        dialog=builder.create();
        dialog.show();
    }

    void closeAlertdialog(){
        dialog.dismiss();
    }
}
