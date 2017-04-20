package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddBossNowFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddBossNowFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;

    private RelativeLayout continue_btn;
    private TextView txt_add_later;

    public ImageView img_user_picture;
    private Bundle bundle;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new AddBossNowFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public AddBossNowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_add_boss_now, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view){

        txt_add_later = (TextView) view.findViewById(R.id.txt_add_later);
        txt_add_later.setOnClickListener(this);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void continue_action() {
        if(validated()){
            DialogUtils.displayProgress(getActivity());

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            final User user = gson.fromJson(user_json, User.class);
            user.setSign_up_stage(Constants.CoWorkerCount);

            Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    if(getActivity() == null) return;
                    ServerRes ServerRes = ServerResponseResponse.body();
                    Log.d(TAG +" onFailure", ServerResponseResponse.body().toString());
                    if(ServerRes.isSuccess()){
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if(saved){
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            nextAction(Constants.CoWorkerCount, bundle);
                        }else{
                            DialogUtils.closeProgress();
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        DialogUtils.closeProgress();
                        Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG +" onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validated(){
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.txt_add_later:
                nextAction(Constants.CoWorkerCount, bundle);
                break;
        }
    }
}