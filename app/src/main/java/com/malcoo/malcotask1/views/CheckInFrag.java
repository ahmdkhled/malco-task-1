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
import com.google.android.material.snackbar.Snackbar;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Repo.CheckInRepo;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.FragmentUtils;
import com.malcoo.malcotask1.Utils.LogSystem;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.databinding.FragCheckInBinding;

import static com.malcoo.malcotask1.Utils.LogSystem.CHECK_IN;
import static com.malcoo.malcotask1.Utils.LogSystem.CHECK_OUT;

public class CheckInFrag extends Fragment {

    FragCheckInBinding binding;
    LatLng currentLocation;
    LogSystem logSystem;

    int status;
    private static final String TAG = "CheckInFrag";


    CheckInFrag(LatLng currentLocation,int status){
        this.currentLocation=currentLocation;
        this.status=status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_check_in,container,false);
        logSystem=LogSystem.getInstance(getContext());
        observeQrCode();


        Log.d(TAG, " checked in frag status : "+status);
        return binding.getRoot();
    }

    private void observeQrCode(){
        CameraUtil.getInstance().startCamera(getContext(),this,binding.previewView);
        CameraUtil.getInstance().setOnBarcodeScannedListener(barcode -> {
            String value=barcode.getRawValue();

            Log.d("BAR_CODE", "result : "+barcode.getRawValue());
            LatLng coordinates= MapUtil.getCoordinates(value);
            //Log.d(TAG, "string: "+value);
            if (coordinates==null){
                binding.barcodeValue.setText(R.string.wrong_qr);
                return;
            }
            float distance = MapUtil.getDistanceBetween(currentLocation,coordinates);
            Log.d(TAG, "distsance: "+distance);
            if (distance<=200){
                CameraUtil.getInstance().stopAnalyzer();
                if (status==CHECK_IN) checkOut();
                else if (status==CHECK_OUT) checkIn();
            }else {
                Log.d(TAG, "distsance: you are out of circle man");
                Snackbar.make(getView(),"you are away from warehouse ",Snackbar.LENGTH_LONG)
                        .show();
                CameraUtil.lastValue="";
            }

        });
    }

    private void checkIn(){
        long currentTime=System.currentTimeMillis();
        boolean b=logSystem.addEnteringTime(currentTime);
        FragmentUtils.replaceFragment(getContext(),new CheckedInFrag(status,currentTime));
        CheckInRepo.getInstance()
                .setCheckInStatus(CHECK_IN);
        LogSystem.getInstance(getContext()).setLastStatus(CHECK_IN);
    }

    private void checkOut() {
        long currentTime=System.currentTimeMillis();
        logSystem.addLeavingTime(currentTime);
        FragmentUtils.replaceFragment(getContext(),new CheckedInFrag(status,currentTime));
        CheckInRepo.getInstance()
                .setCheckInStatus(CHECK_OUT);
        LogSystem.getInstance(getContext()).setLastStatus(CHECK_OUT);

    }
}
