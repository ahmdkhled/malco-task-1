package com.malcoo.malcotask1.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Repo.CheckInRepo;
import com.malcoo.malcotask1.Repo.LocationRepo;
import com.malcoo.malcotask1.Utils.LogSystem;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.ViewModels.MapsActivityVM;
import com.malcoo.malcotask1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,LocationBottomSheet.OnActivateLocationClickedListener
{
    private static final String TAG = "MapsActivityyy";
    private GoogleMap mMap;
    private MapsActivityVM mapsActivityVM;
    ActivityResultLauncher<Intent> launcher;
    MapUtil mapUtil=new MapUtil();
    PermissionUtil permissionUtil;
    LogSystem logSystem;
    ActivityMapsBinding binding;
    LatLng coordinates;
    public static final String CHECKIN_STATUS_KEY="checkin_key";
    private int checkInStatus;
    // random warehouse coordinates outside circle
    //LatLng warehouse=new LatLng(24.689332,46.711770);

    // random warehouse coordinates inside circle
    LatLng warehouse=new LatLng(30.073859,31.3012522);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_maps);
        mapsActivityVM=new ViewModelProvider(this).get(MapsActivityVM.class);
        permissionUtil=new PermissionUtil();
        logSystem=LogSystem.getInstance(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permissionUtil.requestPermission(this);
        checkLocation();
        checkInStatus=mapsActivityVM.getLastStatus();
        binding.statusFooter.setStatus(checkInStatus);
        CheckInRepo.getInstance()
                .getCheckInStatus()
                .observe(this, status -> {
                    binding.statusFooter.setStatus(status);
                    checkInStatus=status;
                });

        binding.statusFooter.checkIn.setOnClickListener(v->{

            Intent intent=new Intent(this,CheckInActivity.class);
            intent.putExtra(CHECKIN_STATUS_KEY,checkInStatus);
            startActivity(intent);
        });


        String log=logSystem.logToday();


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
                        getCurrentLocation();
                });
        CheckInRepo.getInstance().getLastLocation()
                .observe(this, latLng -> {
                    coordinates=latLng;
                    boolean inCircle=mapUtil.isInCircle(coordinates);
                    mapUtil.addCurrentLocationMarker(mMap,coordinates);
                    mapUtil.update(inCircle,binding);
                    if (inCircle){
                        binding.statusFooter.setInside(true);
                    }
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
                coordinates=LocationRepo.toLatLng(location);

                boolean inCircle=mapUtil.isInCircle(coordinates);
                mapUtil.addCurrentLocationMarker(mMap,coordinates);
                mapUtil.update(inCircle,binding);
                if (inCircle){
                    binding.statusFooter.setInside(true);
                    LocationRepo.getInstance(this).stopLocationUpdate();
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




}