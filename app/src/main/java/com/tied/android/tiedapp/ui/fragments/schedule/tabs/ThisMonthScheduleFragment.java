package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class ThisMonthScheduleFragment extends SchedulesFragment implements View.OnClickListener {

    public static final String TAG = TodayScheduleFragment.class
            .getSimpleName();

    protected void initComponent(View view) {
        timeRange = new TimeRange("00:00","23:59");

        Pair<String, String> range = MyUtils.getDateRange();
        dateRange = new DateRange(range.first, range.second);
        scheduleDate = new ScheduleDate(timeRange, dateRange);
        super.initComponent(view);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Constants.CreateSchedule || requestCode == Constants.ViewSchedule) && resultCode == Activity.RESULT_OK) {
            //Logger.write("helllllllllllllllllllllllllllllllllll inited");
            loadSchedule();
            Logger.write("helllllllllllllllllllllllllllllllllll inited");
        }
    }
}
