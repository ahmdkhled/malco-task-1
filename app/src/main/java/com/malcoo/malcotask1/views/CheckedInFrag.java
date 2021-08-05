package com.malcoo.malcotask1.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.databinding.FragCheckedInBinding;

public class CheckedInFrag extends Fragment {

    FragCheckedInBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_checked_in,container,false);



        return binding.getRoot();
    }
}
