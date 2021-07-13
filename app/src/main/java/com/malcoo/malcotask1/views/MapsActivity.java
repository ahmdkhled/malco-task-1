package com.malcoo.malcotask1.views;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.ViewModels.MapsActivityVM;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationBottomSheet.OnActivateLocationClickedListener {

    private GoogleMap mMap;
    private MapsActivityVM mapsActivityVM;
    ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsActivityVM=new ViewModelProvider(this).get(MapsActivityVM.class);
        PermissionUtil permissionUtil=new PermissionUtil();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permissionUtil.requestPermission(this);
        checkLocation();
    }

    @Override
    public void onMapReady( GoogleMap googleMap) { mMap = googleMap; }

    void checkLocation(){
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("TAG", "checkLocation: ");
                        getCurrentLocation();

                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }else{
                Toast.makeText(this, "accessing location is mandatory please accept permission", Toast.LENGTH_SHORT).show();
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
            LatLng cordinates=latLngResult.data;
            if (cordinates!=null){
                mMap.addMarker(new MarkerOptions().position(cordinates));
                mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(cordinates, 10f));
            }else {
                Log.d("TAG", "checkLocation: no location ");
            }
        });

    }

    @Override
    public void onActivateLocationClickedListener() {
        launcher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}