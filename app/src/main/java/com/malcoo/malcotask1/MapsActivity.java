package com.malcoo.malcotask1;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.ViewModels.MapsActivityVM;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapsActivityVM mapsActivityVM;


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

    }


    @Override
    public void onMapReady( GoogleMap googleMap) {
        mMap = googleMap;

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
        LatLng cordinates =mapsActivityVM.getLocation();
        if (cordinates!=null){
            mMap.addMarker(new MarkerOptions().position(cordinates));
            mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(cordinates, 10f));
        }
    }
}