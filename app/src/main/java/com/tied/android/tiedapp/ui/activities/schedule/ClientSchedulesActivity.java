package com.tied.android.tiedapp.ui.activities.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.fragments.client.ClientScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.client.ViewClientFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.AllScheduleFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by femi on 10/10/2016.
 */
public class ClientSchedulesActivity extends AppCompatActivity implements FragmentIterationListener, View.OnClickListener{
    Bundle bundle;


    ClientScheduleFragment scheduleFragment;
    private TextView description, temperature,  schedule_title, weatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_schedules);
        bundle=getIntent().getExtras();
        MyUtils.setColorTheme(this, bundle.getInt(Constants.SOURCE), findViewById(R.id.main_layout));


        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        scheduleFragment=new  ClientScheduleFragment();
        scheduleFragment.setArguments(bundle);
        ft.replace(R.id.fragment_place , scheduleFragment, scheduleFragment.getClass().getName());
        ft.commit();
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.ok_but:
                bundle.putBoolean("select_client", false);
                MyUtils.startRequestActivity(this, CreateAppointmentActivity.class, Constants.CreateSchedule, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        scheduleFragment.onActivityResult(requestCode, resultCode, data);
    }
}
