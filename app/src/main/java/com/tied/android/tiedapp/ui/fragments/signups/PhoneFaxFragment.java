package com.tied.android.tiedapp.ui.fragments.signups;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
public class PhoneFaxFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = PhoneFaxFragment.class
            .getSimpleName();

    private EditText phone,fax;
    private RelativeLayout continue_btn;
   // LinearLayout alert_valid;

    String phoneText, faxText;

    // Reference to our image view we will use
    public ImageView img_user_picture;
    private Bundle bundle;

    private FragmentIterationListener mListener;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new PhoneFaxFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PhoneFaxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_phone_fax, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initComponent(View view){
        SignUpActivity.setStage(view, 3);
        phone = (EditText) view.findViewById(R.id.phone);
        fax = (EditText) view.findViewById(R.id.fax);




        continue_btn = (RelativeLayout)view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
//        fax.addTextChangedListener(new FaxNumberTextWatcher(fax));

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

            mListener.OnFragmentInteractionListener(Constants.EnterCode, bundle);
        }
    }
    
    public void continue_action(){
        phoneText = phone.getText().toString();
        phoneText = phoneText.replaceAll("[)(-]","").replace(" ","");
        if(phoneText.charAt(0) == '0'){
            phoneText = "+1" + phoneText.substring(1);
        }
        if(phoneText.charAt(0) != '+'){
            phoneText = "+1" +phoneText;
        }

        faxText = fax.getText().toString();


        DialogUtils.displayProgress(getActivity());
        bundle = getArguments();
        final Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        final User user = gson.fromJson(user_json, User.class);
        user.setPhone(phoneText);
        user.setFax(faxText);
        bundle.putString(Constants.USER_DATA, gson.toJson(user));
        user.setSign_up_stage(Constants.EnterCode);

        Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if(getActivity() == null) return;
                ServerRes ServerRes = ServerResResponse.body();
                if(ServerRes != null && ServerRes.isSuccess()){
                    Gson gson = new Gson();
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if(saved){
                        String user_json = bundle.getString(Constants.USER_DATA);
                        User user = gson.fromJson(user_json, User.class);
                        Log.d(TAG +" number", phoneText);
                        call_send_phone_vc(user);
                    }else{
                        DialogUtils.closeProgress();
                        //MyUtils.showToast("user info  was not updated");
                    }
                }else{
                    MyUtils.showToast(ServerRes.getMessage());
                    DialogUtils.closeProgress();
                }
                //DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                MyUtils.showToast("On failure : error encountered");
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    private void call_send_phone_vc(User user) {
        DialogUtils.displayProgress(getActivity());
        Call<ServerRes> response = MainApplication.createService(SignUpApi.class).sendPhoneCode(user.getId(), phoneText);
        Logger.write(phoneText);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> serverResResponse) {
                if(getActivity() == null) return;
                try {
                    ServerRes serverRes = serverResResponse.body();
                    Logger.write("Am here "+serverRes);
                    if (serverRes.isSuccess()) {
                        Logger.write(TAG +" Sms enter", serverResResponse.body().toString());
                        DialogUtils.closeProgress();
                        nextAction(bundle);
                    } else {
                        Logger.write("Am here ");
                        MyUtils.showToast("An error occurred. Please try again");
                    }
                }catch (Exception e){
                    Logger.write(e);
                   // nextAction(bundle);
                    MyUtils.showToast("An error occurred. Please try again");
                }
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                MyUtils.showToast("On failure : error encountered");
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                if (phone.getText().length() == 0) {
                   // alert_valid.setVisibility(View.VISIBLE);
                    //Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(getActivity(), R.string.alert_valide_phone_number));

                    MyUtils.showErrorAlert(getActivity(), getActivity().getString(R.string.alert_valide_phone_number));
                } else {
                    continue_action();
                }
                break;
        }
    }
}
