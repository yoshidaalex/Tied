package com.tied.android.tiedapp.ui.fragments.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.SchedulesFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class ClientScheduleFragment extends SchedulesFragment implements View.OnClickListener{

    public static final String TAG = ClientScheduleFragment.class
            .getSimpleName();
    int page=1;
    Parcelable mListViewState;
    private Client client;
    Bundle bundle;

    public ClientScheduleFragment() {
        super();
    }

    public void initSchedule() {
        bundle = getArguments();
        client = (Client)bundle.getSerializable(Constants.CLIENT_DATA);

        ScheduleApi scheduleApi = MainApplication.createService(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getClientSchedule(client.getId(), 1);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + "ScheduleRes", resResponse.toString());
                DialogUtils.closeProgress();
                ScheduleRes scheduleRes = resResponse.body();
                if (scheduleRes != null && scheduleRes.isAuthFailed()) {
                    User.LogOut(getActivity());
                } else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                    ArrayList<Schedule> scheduleArrayList = scheduleRes.getSchedules();

                    scheduleDataModels = parseSchedules(scheduleArrayList);
                    adapter = new ScheduleListAdapter(scheduleDataModels, getActivity(), bundle);
                    listView.setAdapter(adapter);
                    if(scheduleArrayList.size()==0) {
                        emptyScheduleMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), "encountered error with server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {

        mListViewState = listView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CreateSchedule && resultCode == Activity.RESULT_OK) {
            initSchedule();
        }
    }
}
