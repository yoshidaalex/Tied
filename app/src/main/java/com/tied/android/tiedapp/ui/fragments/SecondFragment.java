package com.tied.android.tiedapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tied.android.tiedapp.R;

/**
 * Created by Emmanuel on 6/17/2016.
 */
public class SecondFragment extends Fragment {
    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance() {
        SecondFragment fragmentSecond = new SecondFragment();
        return fragmentSecond;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_more, container, false);
    }
}
