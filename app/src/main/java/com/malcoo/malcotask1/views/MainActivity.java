package com.malcoo.malcotask1.views;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.PermissionUtil;
import com.malcoo.malcotask1.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PermissionUtil permissionUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        permissionUtil=new PermissionUtil();

        permissionUtil.requestCameraPermission(this);

        CameraUtil.getInstance().setOnBarcodeScannedListener(barcode -> {
            Log.d("BAR_CODE", "result : "+barcode.getRawValue());
            binding.barcodeValue.setText(barcode.getRawValue());
        });




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.Camera_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraUtil.getInstance().startCamera(this,this,binding.previewView);
            }else{
                permissionUtil.showDialog(this,this);
            }
        }
    }
}