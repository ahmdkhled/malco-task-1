package com.malcoo.malcotask1.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PermissionUtil permissionUtil;
    private static final String TAG = "MainActivityyy";
    public static final String COORDINATES_KEY="coordinates";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        permissionUtil=new PermissionUtil();

        permissionUtil.requestCameraPermission(this);

        CameraUtil.getInstance().setOnBarcodeScannedListener(barcode -> {
            Log.d("BAR_CODE", "result : "+barcode.getRawValue());
            String value=barcode.getRawValue();
            LatLng coordinates= MapUtil.getCoordinates(value);
            Log.d(TAG, "string: "+value);
            if (coordinates==null){
                binding.barcodeValue.setText(R.string.wrong_qr);

            }else{
                CameraUtil.getInstance().stopAnalyzer();
                Intent intent=new Intent(this,MapsActivity.class);
                intent.putExtra(COORDINATES_KEY,coordinates);
                startActivity(intent);
                finish();
            }


        });




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.Camera_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraUtil.getInstance().startCamera(this,this,binding.previewView);
                Log.d(TAG, "onRequestPermissionsResult: ");
            }else{
                permissionUtil.showDialog(this,this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}