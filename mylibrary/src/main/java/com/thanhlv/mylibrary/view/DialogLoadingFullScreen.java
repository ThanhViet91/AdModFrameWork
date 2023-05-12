package com.thanhlv.mylibrary.view;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.thanhlv.mylibrary.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by Le Viet Thanh on 07/12/22.
 */

public class DialogLoadingFullScreen extends DialogFragment {

    public int getLayout() {
        return R.layout.loading_full_screen;
    }

    @Override
    @Nullable
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return requireActivity().getLayoutInflater().inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (getDialog() != null && getDialog().getWindow() != null) {
                getDialog().getWindow()
                        .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                getDialog().getWindow().setGravity(Gravity.CENTER);
                getDialog().setCanceledOnTouchOutside(false);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateUI() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                //Hide your keyboard here!!!
                updateUI();
                dismissAllowingStateLoss();
                return true; // pretend we've processed it
            } else
                return false; // pass on to be processed as normal
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    public static void handleException(Exception e) {
        try {
            e.printStackTrace();
            Log.e("Error", "handleException", e);
        } catch (Exception ex) {
            Log.e("Error Exception", Objects.requireNonNull(ex.getMessage()));
        }
    }
}
