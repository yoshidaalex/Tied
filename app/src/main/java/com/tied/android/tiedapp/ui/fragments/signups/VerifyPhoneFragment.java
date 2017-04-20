package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class VerifyPhoneFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = VerifyPhoneFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;

    private RelativeLayout continue_btn;
    private EditText code;
    private LinearLayout back_btn;

    private String codeText;

    public VerifyPhoneFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_verify_code, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
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

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view){

        back_btn = (LinearLayout) view.findViewById(R.id.back_layout);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    public void continue_action(){
        if(validated()){
            //Todo Verify Code from Server request should be sent here.
            Bundle bundle = getArguments();
            nextAction(Constants.OfficeAddress, bundle);

        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validated(){
        String code_sent = getArguments().getString(Constants.CODE);
        codeText = code.getText().toString();
        return codeText.equals(code_sent) && codeText.length() == 5;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.back_layout:
                Bundle bundle = getArguments();
                nextAction(Constants.PhoneAndFax, bundle);
                break;
        }
    }
}