package com.malcoo.malcotask1.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    public static final int PERMISSION_ID = 120;
    public static final int Camera_PERMISSION_ID = 122;

    // request location permission fro getting user current location
    public void requestPermission(Activity activity) {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
        ActivityCompat.requestPermissions(
                activity, permissions
                , PERMISSION_ID
        );
    }


    // request camera permission for barcode reader
    public void requestCameraPermission(Activity activity){
        String[] permissions = {
                Manifest.permission.CAMERA,
        };
        ActivityCompat.requestPermissions(
                activity, permissions
                , Camera_PERMISSION_ID
        );
    }

    /* dialog to show to user when he deny to accept permissions
    * if he accept permissions dialog appear again
    * if insist on denying app will close
    * */
    public void showDialog(Context context,Activity activity){
        AlertDialog dialog=new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Permission needed")
                .setMessage("this permission is needed for completing in app, so please accept permission")
                .setPositiveButton("accept permission", (dialog1, which) -> {
                    requestPermission(activity);

                }).setNegativeButton("exit", (dialog12, which) -> {
                    activity.finish();
                }).create();
        dialog.show();

    }
}
