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
import com.malcoo.malcotask1.Utils.FragmentUtils;
import com.malcoo.malcotask1.Utils.LogSystem;
import com.malcoo.malcotask1.databinding.FragCheckedInBinding;

import static com.malcoo.malcotask1.Utils.LogSystem.CHECK_IN;
import static com.malcoo.malcotask1.Utils.LogSystem.CHECK_OUT;

public class CheckedInFrag extends Fragment {
    private static final String TAG = "CheckedInFrag";
    FragCheckedInBinding binding;
    private final int status;
    private  long timeStamp;

    public CheckedInFrag(int status, LatLng currentLocation, long timeStamp, boolean forceCheckout) {
        this.status = status;
        this.timeStamp = timeStamp;
        this.currentLocation = currentLocation;
        this.forceCheckout = forceCheckout;
    }

    LatLng currentLocation;
    boolean forceCheckout=false;



    CheckedInFrag(int status,LatLng currentLocation,boolean forceCheckout){
        this.status=status;
        this.forceCheckout=forceCheckout;
        this.currentLocation=currentLocation;
    }
    CheckedInFrag(int status,LatLng currentLocation,long timeStamp){
        this.status=status;
        this.timeStamp=timeStamp;
        this.currentLocation=currentLocation;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_checked_in,container,false);

        // TODO: 05/08/2021 get location from location api
        binding.setStatus(status);
        Log.d("TAGGG", " check in frag status: "+status);
        binding.setForceCheckout(forceCheckout);
        binding.time.setText(LogSystem.toTime(timeStamp));


        binding.checkout.setOnClickListener(v->{
            if (status==CHECK_OUT){
                CheckInFrag checkInFrag=new CheckInFrag(currentLocation,CHECK_IN,forceCheckout);
                FragmentUtils.replaceFragment(getContext(),checkInFrag);
            }
            else if (status==CHECK_IN&&!forceCheckout){
                requireActivity().finish();
                Log.d(TAG, "force checkout close app :(: ");

            }else {

                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                Log.d(TAG, "force checkout close frag only (: ");


            }
        });

        return binding.getRoot();
    }
}
