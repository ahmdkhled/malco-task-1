package com.malcoo.malcotask1.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Repo.LocationRepo;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.FragmentUtils;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.databinding.ActivityCheckInBinding;

import static com.malcoo.malcotask1.views.MapsActivity.CHECKIN_STATUS_KEY;


public class CheckInActivity extends AppCompatActivity {

    ActivityCheckInBinding binding;
    PermissionUtil permissionUtil;
    public LatLng currentLocation;
    private int status;
    private static final String TAG = "MainActivityyy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_check_in);
        permissionUtil=new PermissionUtil();

        permissionUtil.requestCameraPermission(this);

        Intent intent=getIntent();
        status=intent.getIntExtra(CHECKIN_STATUS_KEY,2);
        Log.d("TAGGG", " check in frag status: "+status);



    }


    private void getCurrentLocation(){
        LocationRepo.getInstance(this)
                .trackLocation()
                .observe(this, locationResult -> {
                    currentLocation=new LatLng(locationResult.getData().getLatitude(),locationResult.getData().getLongitude());
                    Log.d(TAG, "getCurrentLocationnn: ");
                    FragmentUtils.addFrag(this,new CheckInFrag(currentLocation,status));

                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.Camera_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: "+status);
                getCurrentLocation();
            }else{
                permissionUtil.showDialog(this,this);
            }
        }
    }


}