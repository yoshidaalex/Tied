package com.tied.android.tiedapp.ui.fragments.signins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class DoneResetFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = DoneResetFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;
    TextView txt_login;
    LinearLayout back_layout;
    ImageView img_check;

    public DoneResetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in_reset_done, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initComponent(view);
    }

    public void initComponent(View view){

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_login = (TextView) view.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_layout:
                nextAction(Constants.SignInUser, null);
                break;
            case R.id.txt_login:
                nextAction(Constants.SignInUser, null);
                break;
        }
    }
}