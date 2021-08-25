package com.malcoo.malcotask1.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.databinding.LayoutLocationBottonsheetBinding;

public class LocationBottomSheet extends BottomSheetDialogFragment {

    OnActivateLocationClickedListener onActivateLocationClickedListener;

    public LocationBottomSheet(OnActivateLocationClickedListener onActivateLocationClickedListener) {
        this.onActivateLocationClickedListener = onActivateLocationClickedListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutLocationBottonsheetBinding binding= DataBindingUtil
                .inflate(inflater,R.layout.layout_location_bottonsheet,container,false);

        binding.activateLocation.setOnClickListener(v -> {
            onActivateLocationClickedListener.onActivateLocationClicked();
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        onActivateLocationClickedListener.onActivateLocationDismissed();
    }

    interface OnActivateLocationClickedListener{
        void onActivateLocationClicked();
        void onActivateLocationDismissed();
    }
}
