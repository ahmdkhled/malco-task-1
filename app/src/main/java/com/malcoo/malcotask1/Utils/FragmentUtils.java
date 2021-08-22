package com.malcoo.malcotask1.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.malcoo.malcotask1.R;

import de.mateware.snacky.Snacky;

public class FragmentUtils {

    public static void addFrag(Context context, Fragment fragment,String tag ){
        Fragment frag=((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag(tag);
        if (frag!=null)return;
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

    public static void showErrorMessage(Activity activity, String errorMessage, View.OnClickListener onClick){
        Snacky.builder()
                .setActivity(activity)
                .error()
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setText(errorMessage)
                .setAction("retry",onClick)
                .show();

    }
}
