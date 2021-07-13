package com.malcoo.malcotask1.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.ViewModels.MapsActivityVM;
import com.tapadoo.alerter.Alerter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationBottomSheet.OnActivateLocationClickedListener {

    private GoogleMap mMap;
    private MapsActivityVM mapsActivityVM;
    ActivityResultLauncher<Intent> launcher;
    MapUtil mapUtil=new MapUtil();
    PermissionUtil permissionUtil;
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
        mapUtil.drawCircle(warehouse,mMap);
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
            LatLng cordinates=latLngResult.getData();
            if (latLngResult.isSuccess()&&cordinates!=null){
                mMap.addMarker(new MarkerOptions().position(cordinates)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(cordinates, 12f));
                boolean inCircle=mapUtil.isInCircle(cordinates);
                String message=mapUtil.getMessage(inCircle);
                Alerter.create(this).setTitle("your status")
                        .setText(message).
                        setBackgroundColorRes(mapUtil.getColor(inCircle)).show();

            }else {
                Log.d("TAG", "checkLocation: no location ");
                Toast.makeText(this, "failed to get Location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivateLocationClickedListener() {
        launcher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}