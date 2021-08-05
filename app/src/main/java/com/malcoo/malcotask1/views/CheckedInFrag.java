package com.malcoo.malcotask1.views;

import android.app.Activity;
import android.os.Bundle;
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
import com.malcoo.malcotask1.databinding.FragCheckedInBinding;

public class CheckedInFrag extends Fragment {

    private int status;
    FragCheckedInBinding binding;
    LatLng currentLocation;
    CheckedInFrag(int status){
        this.status=status;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_checked_in,container,false);
        currentLocation=((CheckInActivity) getActivity()).currentLocation;
        binding.setStatus(status);
        binding.checkout.setOnClickListener(v->{
            if (status==CheckInFrag.CHECK_IN)
            FragmentUtils.replaceFragment(getContext(),new CheckInFrag(currentLocation,CheckInFrag.CHECK_OUT));
            else if (status==CheckInFrag.CHECK_OUT)
                getActivity().finish();
        });

        return binding.getRoot();
    }
}
