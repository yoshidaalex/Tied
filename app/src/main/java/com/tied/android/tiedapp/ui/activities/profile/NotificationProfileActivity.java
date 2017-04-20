package com.tied.android.tiedapp.ui.activities.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import android.widget.Switch;
import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.Notification;
import com.tied.android.tiedapp.objects.user.NotificationAction;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class NotificationProfileActivity extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener{

    public FragmentIterationListener mListener;

    private Bundle bundle;
    private ImageView img_close;
    User user;
    Notification notification;
    List<Switch> switches=new ArrayList<>();
    Switch updateSwitchEmail, updateSwitchPush, coworkerSwitchEmail, coworkerSwitchPush, specialSwitchEmail, specialSwitchPush, proximitySwitchEmail, proximitySwitchPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_notifications);
        user= MyUtils.getUserLoggedIn();
        initComponent();
    }

    public void initComponent() {
        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
        updateSwitchEmail=(Switch)findViewById(R.id.update_email); switches.add(updateSwitchEmail);
        updateSwitchPush =(Switch)findViewById(R.id.update_phone); switches.add(updateSwitchPush);
        coworkerSwitchEmail=(Switch)findViewById(R.id.coworker_email); switches.add(coworkerSwitchEmail);
        coworkerSwitchPush=(Switch)findViewById(R.id.coworker_push); switches.add(coworkerSwitchPush);
        specialSwitchEmail=(Switch)findViewById(R.id.special_email); switches.add(specialSwitchEmail);
        specialSwitchPush=(Switch)findViewById(R.id.special_push); switches.add(specialSwitchPush);
        proximitySwitchEmail=(Switch)findViewById(R.id.proximity_email); switches.add(proximitySwitchEmail);
        proximitySwitchPush=(Switch)findViewById(R.id.proximity_push); switches.add(proximitySwitchPush);

        for(Switch sw : switches) {
            sw.setOnCheckedChangeListener(this);
        }

        if(user.getNotification()==null) {
            notification=new Notification();
            notification.setAccount_update(new NotificationAction(true, true));
            notification.setCoworker(new NotificationAction(false, false));
            notification.setSpecial(new NotificationAction(true, true));
            notification.setProximity(new NotificationAction(false, true));
            user.setNotification(notification);
            user.save(this);
        }else notification=user.getNotification();
        updateSwitches();
    }
    void updateSwitches() {
        updateSwitchEmail.setChecked(notification.getAccount_update().getEmail());
        updateSwitchPush.setChecked(notification.getAccount_update().getPush());
        coworkerSwitchEmail.setChecked(notification.getCoworker().getEmail());
        coworkerSwitchPush.setChecked(notification.getCoworker().getPush());
        specialSwitchEmail.setChecked(notification.getSpecial().getEmail());
        specialSwitchPush.setChecked(notification.getSpecial().getPush());
        proximitySwitchPush.setChecked(notification.getProximity().getPush());
        proximitySwitchEmail.setChecked(notification.getProximity().getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()) {
            case R.id.update_email:
                notification.getAccount_update().setEmail(b);
                break;
            case R.id.update_phone:
                notification.getAccount_update().setPush(b);
                break;
            case R.id.coworker_email:
                notification.getCoworker().setEmail(b);
                break;
            case R.id.coworker_push:
                notification.getCoworker().setPush(b);
                break;
            case R.id.special_email:
                notification.getSpecial().setEmail(b);
                break;
            case R.id.special_push:
                notification.getSpecial().setPush(b);
                break;
            case R.id.proximity_email:
                notification.getProximity().setEmail(b);
                break;
            case R.id.proximity_push:
                notification.getProximity().setPush(b);
                break;

        }
        user.setNotification(notification);
        Logger.write(notification.toJSONString());
        Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if (this == null) return;
                try {
                    ServerRes ServerRes = ServerResResponse.body();
                    Logger.write(" onFailure", ServerResResponse.body().toString());
                    if (ServerRes.isSuccess()) {
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(NotificationProfileActivity.this);
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                           // DialogUtils.closeProgress();
                           // MyUtils.showMessageAlert(NotificationProfileActivity.this, "Notif");
                        } else {
                            //DialogUtils.closeProgress();
                            MyUtils.showToast(getString(R.string.connection_error));
                        }
                    } else {
                        MyUtils.showErrorAlert(NotificationProfileActivity.this, ServerRes.getMessage());
                    }
                }catch (Exception e) {
                    MyUtils.showToast(getString(R.string.connection_error));
                }
                ///DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                MyUtils.showToast(getString(R.string.connection_error));
                //Logger.write(TAG + " onFailure", t.toString());
               // DialogUtils.closeProgress();
            }
        });
    }
}
