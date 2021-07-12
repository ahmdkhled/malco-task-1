package com.malcoo.malcotask1.Utils;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    public static final int PERMISSION_ID = 120;

    public void requestPermission(Activity activity){
        String[] permissions={
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(
                activity,permissions
                , PERMISSION_ID
        );
    }
}
