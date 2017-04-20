package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class NameFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = NameFragment.class
            .getSimpleName();

    private EditText first_name, last_name;
    //    private Button continue_btn;
    private RelativeLayout continue_btn;
   // LinearLayout alert_valid;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    String firstNameText, lastNameText;

    private FragmentIterationListener mListener;

    private Bundle bundle;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new NameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public NameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view) {

        SignUpActivity.setStage(view, 5);
        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);

        //alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
       // alert_valid.setVisibility(View.GONE);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);
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


    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.OfficeAddress, bundle);
        }
    }

    public void continue_action() {
        DialogUtils.displayProgress(getActivity());
        String user_json = bundle.getString(Constants.USER_DATA);
        final Gson gson = new Gson();
        final User user = gson.fromJson(user_json, User.class);
        user.setFirst_name(firstNameText);
        user.setLast_name(lastNameText);
        user.setSign_up_stage(Constants.OfficeAddress);
        Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if (getActivity() == null) return;
                try {
                    ServerRes ServerRes = ServerResResponse.body();
                    Log.d(TAG + " onFailure", ServerResResponse.body().toString());
                    if (ServerRes.isSuccess()) {
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            nextAction(bundle);
                        } else {
                            DialogUtils.closeProgress();
                            MyUtils.showToast(getString(R.string.connection_error));
                        }
                    } else {
                        MyUtils.showErrorAlert(getActivity(), ServerRes.getMessage());
                    }
                }catch (Exception e) {
                    MyUtils.showToast(getString(R.string.connection_error));
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                MyUtils.showToast(getString(R.string.connection_error));
                Logger.write(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                firstNameText = first_name.getText().toString();
                lastNameText = last_name.getText().toString();
                boolean first_name_match = firstNameText.matches("^[\\p{L} .'-]+$");
                if (firstNameText.length() == 0) {
                    // alert_valid.setVisibility(View.VISIBLE);
                    MyUtils.showErrorAlert(getActivity(), "First name is required");
                    //Utility.moveViewToScreenCenter(alert_valid, Utility.getResourceString(getActivity(), R.string.alert_valide_name_empty));
                } else if (!first_name_match) {
                  //  alert_valid.setVisibility(View.VISIBLE);
                    MyUtils.showErrorAlert(getActivity(), getActivity().getString(R.string.alert_valide_first_name));
                   // Utility.moveViewToScreenCenter(alert_valid, );
                } else  {
                    continue_action();
                }
                break;
        }
    }
}
