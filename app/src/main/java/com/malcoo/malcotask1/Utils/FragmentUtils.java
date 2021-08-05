package com.malcoo.malcotask1.Utils;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.malcoo.malcotask1.R;

public class FragmentUtils {

    public static void addFrag(Context context, Fragment fragment){
        ((FragmentActivity)context)
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,fragment)
                .commit();
    }

    public static void replaceFragment(Context context, Fragment fragment){
        ((FragmentActivity)context)
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }
}
