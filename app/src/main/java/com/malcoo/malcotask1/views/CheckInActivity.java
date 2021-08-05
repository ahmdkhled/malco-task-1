package com.malcoo.malcotask1.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.FragmentUtils;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.databinding.ActivityCheckInBinding;


public class CheckInActivity extends AppCompatActivity {

    ActivityCheckInBinding binding;
    PermissionUtil permissionUtil;
    public LatLng currentLocation;
    private static final String TAG = "MainActivityyy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_check_in);
        permissionUtil=new PermissionUtil();

        permissionUtil.requestCameraPermission(this);

        currentLocation=getIntent().getParcelableExtra(MapsActivity.LOCATION_KEY);



    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.Camera_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FragmentUtils.addFrag(this,new CheckInFrag(currentLocation,CheckInFrag.CHECK_IN));
                Log.d(TAG, "onRequestPermissionsResult: ");
            }else{
                permissionUtil.showDialog(this,this);
            }
        }
    }

}