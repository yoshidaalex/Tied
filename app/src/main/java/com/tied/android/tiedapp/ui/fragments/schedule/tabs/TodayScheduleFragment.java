package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Emmanuel on 7/15/2016.
 */


public class TodayScheduleFragment extends SchedulesFragment implements View.OnClickListener {
    public static ArrayList<ScheduleDataModel> todayScheduleDataModels;
    public static final String TAG = TodayScheduleFragment.class
            .getSimpleName();

    public static SchedulesFragment getInstance(ArrayList<ScheduleDataModel> data) {
        TodayScheduleFragment sf=new TodayScheduleFragment();
        if(data != null) {
            sf.scheduleDataModels=data;
        }
        return sf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    public void initComponent(View view) {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String  today = sdf.format(now);
        timeRange = new TimeRange("00:00","23:59");
        dateRange = new DateRange(today, today);
        scheduleDate = new ScheduleDate(timeRange, dateRange);
        super.initComponent(view);
        Logger.write("I am attaching");
    }

    @Override
    protected ArrayList<ScheduleDataModel> parseSchedules(ArrayList<Schedule> scheduleArrayList) {

        Log.d(TAG + " parseSchedules", scheduleArrayList.toString());

        ArrayList<ScheduleDataModel> scheduleDataModels = new ArrayList<>();
        int size=scheduleArrayList.size();
        for (int i = 0; i < size; i++) {
            Schedule schedule = scheduleArrayList.get(i);

            ScheduleDataModel scheduleDataModel = new ScheduleDataModel();

            ArrayList<Schedule> schedules = new ArrayList<Schedule>();
            schedules.add(schedule);

            String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
            String week_day = HelperMethods.getDayOfTheWeek(schedule.getDate());

            scheduleDataModel.setSchedules(schedules);
            scheduleDataModel.setTemperature("80");
            scheduleDataModel.setWeather("cloudy");
            scheduleDataModel.setDay(day);
            scheduleDataModel.setWeek_day(week_day);

            scheduleDataModels.add(scheduleDataModel);
        }

        return scheduleDataModels;
    }

}
