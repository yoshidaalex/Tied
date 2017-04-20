package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public abstract class SchedulesFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemClickListener{
    protected static final String TAG = SchedulesFragment.class
            .getSimpleName();

    public FragmentIterationListener mListener;

    protected ScheduleDate scheduleDate;

    protected ArrayList<ScheduleDataModel> scheduleDataModels;
    protected ListView listView;

    protected TimeRange timeRange = null;
    protected DateRange dateRange = null;

    protected View emptyScheduleMessage;

    protected ScheduleListAdapter adapter;
    protected Bundle bundle;
    protected User user;

    public int num;

    protected ProgressBar pb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    protected void initComponent(View view) {
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        emptyScheduleMessage=view.findViewById(R.id.empty_schedule);
        emptyScheduleMessage.setVisibility(View.GONE);
        pb=(ProgressBar)view.findViewById(R.id.progress_bar);
        pb.setVisibility(View.GONE);
        bundle = getArguments();

        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user =MyUtils.getUserFromBundle(bundle);
           // initSchedule();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Logger.write("shedule fragment resumed");
        //if(scheduleDataModels==null || scheduleDataModels.isEmpty())
       // initSchedule();
        user =MyUtils.getUserFromBundle(bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d("schedules at ", position +"here---------------- "+scheduleDataModels.toString());
      /*  Bundle bundle=new Bundle();
        ScheduleDataModel data=scheduleDataModels.get(position);
        bundle.putSerializable(Constants.SCHEDULE_DATA, data.toJSONString());
        if(data.cclient!=null) bundle.putSerializable(Constants.CLIENT_DATA, gson.toJson(client, Client.class));
        Schedule.scheduleCreated(getActivity().getApplicationContext());
        bundle.putBoolean(Constants.NO_SCHEDULE_FOUND, false);
        DialogUtils.closeProgress();
        nextAction(Constants.ScheduleSuggestions, bundle);*/
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSchedule();
    }

    public void initSchedule() {
//        Log.d(TAG + " scheduinitScheduleleDate", scheduleDate.toString());
        loadSchedule();
    }

    public void loadSchedule() {
//        Log.d(TAG + " scheduinitScheduleleDate", scheduleDate.toString());
        if(user==null) user =MyUtils.getUserFromBundle(bundle);
        Logger.write("Userrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr  "+user.toString());
        pb.setVisibility(View.VISIBLE);
        emptyScheduleMessage.setVisibility(View.GONE);
        ScheduleApi scheduleApi = MainApplication.createService(ScheduleApi.class);
        Logger.write("Userrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr  "+user.toString());
        Call<ScheduleRes> response = scheduleApi.getScheduleByDate(user.getId(), scheduleDate,1);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> resResponse) {
                if (getActivity() == null) return;
                pb.setVisibility(View.GONE);
                try {
                    ScheduleRes scheduleRes = resResponse.body();
                    Logger.write(scheduleRes.toString());
                    if (scheduleRes != null && scheduleRes.isAuthFailed()) {
                        User.LogOut(getActivity());
                    } else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                        ArrayList<Schedule> scheduleArrayList = scheduleRes.getSchedules();
                        scheduleDataModels = parseSchedules(scheduleArrayList);
                        Log.d(TAG + "scheduleDataModels", scheduleDataModels.toString());
                        bundle.putBoolean(Constants.NO_SCHEDULE_FOUND, false);
                        adapter = new ScheduleListAdapter(scheduleDataModels, getActivity(), bundle);
                        listView.setAdapter(adapter);
                        if(scheduleArrayList.size()==0) {
                            emptyScheduleMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                pb.setVisibility(View.GONE);
                MyUtils.showToast(getString(R.string.connection_error));
                DialogUtils.closeProgress();
            }
        });
    }


    protected ArrayList<ScheduleDataModel> parseSchedules(ArrayList<Schedule> scheduleArrayList) {
        Log.d(TAG + " parseSchedules", scheduleArrayList.toString());
        ArrayList<ScheduleDataModel> scheduleDataModels = new ArrayList<>();
        for (int i = 0; i < scheduleArrayList.size(); i++) {
            Schedule schedule = scheduleArrayList.get(i);
            ScheduleDataModel scheduleDataModel = new ScheduleDataModel();
            ArrayList<Schedule> schedules = new ArrayList<Schedule>();
            schedules.add(schedule);
            for (int j = i + 1; j < scheduleArrayList.size(); j++) {
                Schedule this_schedule = scheduleArrayList.get(j);
                try {
                    if (MyUtils.isSameDay(schedule.getDate(), this_schedule.getDate())) {
                        schedules.add(this_schedule);
                        Log.d(TAG, "SAME " + schedule.getTitle() + " and " + this_schedule.getTitle());
                        scheduleArrayList.remove(j--);
                    }
                }catch (Exception e) {

                }
            }
            try {
                long diff_in_date = HelperMethods.getDateDifferenceWithToday(schedule.getDate());
            }catch (Exception e) {
                continue;
            }

            String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
            String week_day = MyUtils.getWeekDay(schedule);

            scheduleDataModel.setSchedules(schedules);
            scheduleDataModel.setDay(day);
            scheduleDataModel.setWeek_day(week_day);

            scheduleDataModels.add(scheduleDataModel);
        }
        Collections.reverse(scheduleDataModels);
        return scheduleDataModels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Constants.CreateSchedule || requestCode == Constants.ViewSchedule) && resultCode == Activity.RESULT_OK) {
            //Logger.write("helllllllllllllllllllllllllllllllllll inited");
           // loadSchedule();
           // Logger.write("helllllllllllllllllllllllllllllllllll inited");
        }
    }
}
