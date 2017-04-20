package com.tied.android.tiedapp.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tied.android.tiedapp.R;


public class ThirdFragment extends Fragment {


    public ThirdFragment() {
        // Required empty public constructor
    }

    public static ThirdFragment newInstance() {
        ThirdFragment fragmentThird = new ThirdFragment();
        return fragmentThird;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_notifications, container, false);
    }

}
