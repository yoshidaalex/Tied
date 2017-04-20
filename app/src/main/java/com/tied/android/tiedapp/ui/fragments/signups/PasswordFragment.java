package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.SignUpLogin;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.signups.WalkThroughActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PasswordFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PasswordFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;

//    private Button continue_btn;
    private View back_btn;
    private RelativeLayout continue_btn;
    //LinearLayout alert_valid_password;
    private EditText password;

    private String passwordText;
    private View showPasswordBut;
    private ImageView showPasswordSelector;
    private Bundle bundle;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new PasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view) {

        bundle = getArguments();
        showPasswordBut=view.findViewById(R.id.show_password);
        showPasswordSelector=(ImageView)view.findViewById(R.id.show_password_selector);
        showPasswordBut.setOnClickListener(this);
        password = (EditText) view.findViewById(R.id.password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        back_btn =  view.findViewById(R.id.img_close);
        continue_btn = (RelativeLayout)view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
         back_btn.setOnClickListener(this);
        showPasswordSelector.setImageResource(R.drawable.empty_unchecked_icon);


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
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void continue_action() {
        DialogUtils.displayProgress(getActivity());
        Gson gson = new Gson();
        final String user_json = bundle.getString(Constants.USER_DATA);
        final User user = gson.fromJson(user_json, User.class);
        Log.d(TAG, user.toString());
        Call<SignUpLogin> response = MainApplication.createService(SignUpApi.class)
                .LoginSignUpUser(user.getEmail(), passwordText, Constants.PhoneAndFax);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<SignUpLogin>() {
            @Override
            public void onResponse(Call<SignUpLogin> call, Response<SignUpLogin> LoginResponse) {
                if (getActivity() == null) return;

                try {
                    SignUpLogin signUpLogin = LoginResponse.body();
                    try {
                        User loggedIn_user = signUpLogin.getUser();
                        Log.d(TAG, signUpLogin.toString());
                        if (loggedIn_user.getToken() != null) {
                            loggedIn_user.setSign_up_stage(Constants.PhoneAndFax);
                            loggedIn_user.setPassword(passwordText);
                            loggedIn_user.setAvatar(user.getAvatar());
                            loggedIn_user.setFirst_name(user.getFirst_name());
                            loggedIn_user.setLast_name(user.getLast_name());
                            boolean saved = loggedIn_user.save(getActivity().getApplicationContext());
                            if (saved) {
                                Bundle bundle = new Bundle();
                                Gson gson = new Gson();
                                String user_json = gson.toJson(loggedIn_user);
                                bundle.putString(Constants.USER_DATA, user_json);
                                //user.LogIn(getActivity());
                                WalkThroughActivity.getInstance().finish();
                                nextAction(Constants.PhoneAndFax, bundle);
                            } else {
                                //Toast.makeText(getActivity(), "user not save", Toast.LENGTH_LONG).show();
                                MyUtils.showToast(getString(R.string.connection_error));
                            }
                            DialogUtils.closeProgress();
                        } else {
                            DialogUtils.closeProgress();
                            MyUtils.showToast(getString(R.string.connection_error));
                            //Toast.makeText(getActivity(), "user not created", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e) {
                        MyUtils.showErrorAlert(getActivity(), signUpLogin.get_meta().getUser_message());
                    }
                }catch (Exception e) {
                    MyUtils.showToast(getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<SignUpLogin> checkEmailCall, Throwable t) {
                DialogUtils.closeProgress();
                //Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(getString(R.string.connection_error));
                Log.d(TAG + " onFailure", t.toString());
            }
        });
    }
boolean isShowingPassword=true;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                passwordText = password.getText().toString();
                if (passwordText.length() == 0) {
                   // alert_valid_password.setVisibility(View.VISIBLE);
                   // Utility.moveViewToScreenCenter( alert_valid_password, Utility.getResourceString(getActivity(), R.string.alert_valide_password));

                    MyUtils.showErrorAlert(getActivity(), getActivity().getString(R.string.alert_valide_password));
                } else if (passwordText.length() <6) {
                    MyUtils.showErrorAlert(getActivity(), "Password cannot be less than 6 characters");
                } else{
                        continue_action();

                }
                break;
            case R.id.img_close:
                getActivity().onBackPressed();
                break;
            case R.id.show_password:
                int post=password.getSelectionStart();
                if(!isShowingPassword) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowingPassword=true;
                    showPasswordSelector.setImageResource(R.drawable.check_icon);

                }else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowingPassword=false;
                    showPasswordSelector.setImageResource(R.drawable.empty_unchecked_icon);
                }
                password.setSelection(post);
                break;
        }
    }
}
