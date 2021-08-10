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

    private final int status;
    private final long timeStamp;
    FragCheckedInBinding binding;
    LatLng currentLocation;
    CheckedInFrag(int status,long timeStamp){
        this.status=status;
        this.timeStamp=timeStamp;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_checked_in,container,false);
        // TODO: 05/08/2021 get location from location api
        currentLocation=((CheckInActivity) requireActivity()).currentLocation;
        binding.setStatus(status);
        Log.d("TAGGG", " check in frag status: "+status);

        binding.time.setText(LogSystem.toTime(timeStamp));
        binding.checkout.setOnClickListener(v->{
            if (status==CHECK_OUT){
                FragmentUtils.replaceFragment(getContext(),new CheckInFrag(currentLocation,CHECK_IN));
            }
            else if (status==CHECK_IN){
                requireActivity().finish();

            }
        });

        return binding.getRoot();
    }
}
