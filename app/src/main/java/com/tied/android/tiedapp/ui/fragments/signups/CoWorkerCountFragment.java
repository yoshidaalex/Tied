package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class CoWorkerCountFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CoWorkerCountFragment.class
            .getSimpleName();

    private RelativeLayout continue_btn;

    ImageView img_worker1, img_worker2, img_worker3, img_worker4;
    LinearLayout worker_layout, worker_layout1, worker_layout2, worker_layout3;
    TextView txt_worker1, txt_worker2, txt_worker3, txt_worker4;

    private String coWorkerCountText;

    private FragmentIterationListener mListener;

    public ImageView img_user_picture;
    private Bundle bundle;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new CoWorkerCountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public CoWorkerCountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_co_worker_count, container, false);
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

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.AddOptions, bundle);
        }
    }

    public void initComponent(View view) {
        txt_worker1 = (TextView) view.findViewById(R.id.txt_worker1);
        txt_worker2 = (TextView) view.findViewById(R.id.txt_worker2);
        txt_worker3 = (TextView) view.findViewById(R.id.txt_worker3);
        txt_worker4 = (TextView) view.findViewById(R.id.txt_worker4);
        img_worker1 = (ImageView) view.findViewById(R.id.img_worker1);
        img_worker2 = (ImageView) view.findViewById(R.id.img_worker2);
        img_worker3 = (ImageView) view.findViewById(R.id.img_worker3);
        img_worker4 = (ImageView) view.findViewById(R.id.img_worker4);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        worker_layout = (LinearLayout) view.findViewById(R.id.worker_layout);
        worker_layout.setOnClickListener(this);

        worker_layout1 = (LinearLayout) view.findViewById(R.id.worker_layout1);
        worker_layout1.setOnClickListener(this);

        worker_layout2 = (LinearLayout) view.findViewById(R.id.worker_layout2);
        worker_layout2.setOnClickListener(this);

        worker_layout3 = (LinearLayout) view.findViewById(R.id.worker_layout3);
        worker_layout3.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);

        setSelectType(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.worker_layout:
                setSelectType(0);
                break;
            case R.id.worker_layout1:
                setSelectType(1);
                break;
            case R.id.worker_layout2:
                setSelectType(2);
                break;
            case R.id.worker_layout3:
                setSelectType(3);
                break;
        }
    }


    public void continue_action(){
        DialogUtils.displayProgress(getActivity());

        Bundle bundle = getArguments();

        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        final User user = gson.fromJson(user_json, User.class);
        user.setCo_workers(coWorkerCountText);
        user.setSign_up_stage(Constants.AddOptions);

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
                        user.LogIn(getActivity().getApplicationContext());
                    }else{
                        DialogUtils.closeProgress();
                        Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                    }
                }else{
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
    }

    private void setSelectType(int index) {
        if(index == 0) {
            img_worker1.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_worker2.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker3.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker4.setBackgroundResource(R.mipmap.dot_unchecked_icon);

            txt_worker1.setTextColor(Color.WHITE);
            txt_worker2.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker3.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker4.setTextColor(getResources().getColor(R.color.text_disable_color));

        } else if (index == 1){
            img_worker1.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker2.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_worker3.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker4.setBackgroundResource(R.mipmap.dot_unchecked_icon);

            txt_worker1.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker2.setTextColor(Color.WHITE);
            txt_worker3.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker4.setTextColor(getResources().getColor(R.color.text_disable_color));
        } else if (index == 2) {
            img_worker1.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker2.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker3.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_worker4.setBackgroundResource(R.mipmap.dot_unchecked_icon);

            txt_worker1.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker2.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker3.setTextColor(Color.WHITE);
            txt_worker4.setTextColor(getResources().getColor(R.color.text_disable_color));
        } else {
            img_worker1.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker2.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker3.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_worker4.setBackgroundResource(R.mipmap.dot_checked_icon);

            txt_worker1.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker2.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker3.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_worker4.setTextColor(Color.WHITE);
        }
    }
}
