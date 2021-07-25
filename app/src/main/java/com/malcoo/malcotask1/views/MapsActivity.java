package com.malcoo.malcotask1.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Repo.LocationRepo;
import com.malcoo.malcotask1.Utils.LogSystem;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.Utils.Timer;
import com.malcoo.malcotask1.ViewModels.MapsActivityVM;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,LocationBottomSheet.OnActivateLocationClickedListener ,
        Timer.OnTimerFinished
{
    private static final String TAG = "MapsActivityyy";
    private GoogleMap mMap;
    private MapsActivityVM mapsActivityVM;
    ActivityResultLauncher<Intent> launcher;
    MapUtil mapUtil=new MapUtil();
    PermissionUtil permissionUtil;
    LogSystem logSystem;

    // random warehouse coordinates outside circle
    //LatLng warehouse=new LatLng(30.0742382,31.2856253);

    // random warehouse coordinates inside circle
    LatLng warehouse=new LatLng(30.073859,31.3012522);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsActivityVM=new ViewModelProvider(this).get(MapsActivityVM.class);
        permissionUtil=new PermissionUtil();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permissionUtil.requestPermission(this);
        checkLocation();

        

    }

    @Override
    public void onMapReady( GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().title("ware house").position(warehouse));
        // dynamic radius as required
        mapUtil.drawCircle(500,warehouse,mMap);
    }

    void checkLocation(){
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("TAG", "checkLocation: ");
                        getCurrentLocation();
                });
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }else{
                permissionUtil.showDialog(this,this);
            }
        }
    }

    private void getCurrentLocation(){
        if (!mapsActivityVM.isLocationEnabled()){
            new LocationBottomSheet(this)
                    .show(getSupportFragmentManager(),"");
            return;
        }
        mapsActivityVM.getLocation().observe(this, latLngResult -> {
            Location location=latLngResult.getData();
            if (latLngResult.isSuccess()&&location!=null){
                LatLng coordinates=LocationRepo.toLatLng(location);

                boolean inCircle=mapUtil.isInCircle(coordinates);
                mapUtil.addCurrentLocationMarker(mMap,coordinates);
                mapUtil.alert(inCircle,this);
                Log.d("Timer", "getCurrentLocation: called "+inCircle);
                if (inCircle){
                    Log.d("LOG_TIME", "entering time : "+System.currentTimeMillis());
                    logSystem.addEnteringTime(System.currentTimeMillis());
                    Timer.getInstance(this,30000,1000).start();
                    LocationRepo.getInstance(this).stopLocationUpdate();
                }else {
                    Log.d("LOG_TIME", "exit time : "+System.currentTimeMillis());
                    logSystem.addLeavingTime(System.currentTimeMillis());
                }

            }else {
                Toast.makeText(this, "failed to get Location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivateLocationClickedListener() {
        launcher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onTimerFinished() {
        getCurrentLocation();
    }
}