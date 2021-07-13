package com.malcoo.malcotask1.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    public static final int PERMISSION_ID = 120;

    public void requestPermission(Activity activity) {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(
                activity, permissions
                , PERMISSION_ID
        );
    }

    public boolean permissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void showDialog(Context context,Activity activity){
        AlertDialog dialog=new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Permission needed")
                .setMessage("location permission is needed for completing in app, so please accept permission")
                .setPositiveButton("accept permission", (dialog1, which) -> {
                    requestPermission(activity);

                }).setNegativeButton("exit", (dialog12, which) -> {
                    activity.finish();
                }).create();
        dialog.show();

    }
}
