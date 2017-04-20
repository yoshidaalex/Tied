package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class AllScheduleFragment extends SchedulesFragment implements View.OnClickListener{

    public static final String TAG = AllScheduleFragment.class
            .getSimpleName();
    int page=1;
    Parcelable mListViewState;

    public AllScheduleFragment() {
        super();

    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if(mListViewState != null) {
//             Logger.write("Restoring the listview's state.");
//            listView.onRestoreInstanceState(mListViewState);
//        }
//
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

//    protected void initComponent(View view) {
//        super.initComponent(view);
//    }

    public void initSchedule() {
        ScheduleApi scheduleApi = MainApplication.createService(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getSchedule(user.getId(), 1);
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
}
