package com.malcoo.malcotask1.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.FragmentUtils;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.databinding.FragCheckInBinding;

public class CheckInFrag extends Fragment {

    FragCheckInBinding binding;
    LatLng currentLocation;
    private static final String TAG = "CheckInFrag";

    CheckInFrag(LatLng currentLocation){
        this.currentLocation=currentLocation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_check_in,container,false);

        checkIn();
        return binding.getRoot();
    }

    private void checkIn(){
        CameraUtil.getInstance().startCamera(getContext(),this,binding.previewView);
        CameraUtil.getInstance().setOnBarcodeScannedListener(barcode -> {
            Log.d("BAR_CODE", "result : "+barcode.getRawValue());
            String value=barcode.getRawValue();
            LatLng coordinates= MapUtil.getCoordinates(value);
            Log.d(TAG, "string: "+value);
            if (coordinates==null){
                binding.barcodeValue.setText(R.string.wrong_qr);
            }else{

                float distance = MapUtil.getDistanceBetween(currentLocation,coordinates);
                if (distance<=200){
                    binding.barcodeValue.setText(R.string.checked_in);
                    CameraUtil.getInstance().stopAnalyzer();
                    FragmentUtils.replaceFragment(getContext(),new CheckedInFrag());

                }

            }


        });
    }
}
