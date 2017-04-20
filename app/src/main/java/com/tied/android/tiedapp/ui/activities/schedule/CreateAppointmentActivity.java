package com.tied.android.tiedapp.ui.activities.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.schedule.AppointmentCalendarFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateAppointmentFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleSuggestionFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ViewScheduleFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 5/3/2016.
 */
public class CreateAppointmentActivity extends FragmentActivity implements FragmentIterationListener {
    public static final String TAG = CreateAppointmentFragment.class
            .getSimpleName();


    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    public Coordinate coordinate;
    int currentFragmentID=0;


    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    User user;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_fragment_container);

        user = User.getCurrentUser(getApplicationContext());
        Client client = (Client) getIntent().getSerializableExtra(Constants.CLIENT_DATA);

        bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }

        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        String client_json = gson.toJson(client);

        Schedule schedule = (Schedule) getIntent().getSerializableExtra(Constants.SCHEDULE_DATA);
        if(schedule != null){
            String schedule_json = gson.toJson(schedule);
            bundle.putString(Constants.SCHEDULE_DATA, schedule_json);
        }

        bundle.putString(Constants.USER_DATA, user_json);
        bundle.putString(Constants.CLIENT_DATA, client_json);
        launchFragment(Constants.CreateAppointment, bundle);
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " OnFragmentInteractionListener " + action);
        launchFragment(action, bundle);
    }

    @Override
    public void onBackPressed() {
       /* if(fragment_index == Constants.CreateAppointment){
            finish();
        }*/
       // super.onBackPressed();
        finish();
    }

    Fragment currentFragment=null;
    public void launchFragment(int pos, Bundle bundle) {
        fragment = null;
        fragment_index = pos;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID=pos;
        switch (pos) {
            case Constants.CreateAppointment:
                if(fragments.get(pos)==null) {
                    fragments.put(pos, CreateAppointmentFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.CreateSchedule:
                if(fragments.get(pos)==null) {
                    fragments.put(pos, CreateScheduleFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.AppointmentCalendar:
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  AppointmentCalendarFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ScheduleSuggestions:
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  ScheduleSuggestionFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ViewSchedule:
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  ViewScheduleFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            default:
                finish();
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            Logger.write("TAGGGG: "+ fragment.getClass().getName());
            addFragment(ft, currentFragment, fragment, fragment.getClass().getName());
        }
        currentFragment=fragment;
    }
    static long backPressed=0;
    public void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(currentFragment!=null) transaction.hide(currentFragment);
        // use a fragment tag, so that later on we can find the currently displayed fragment
        if(targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        }else {
            transaction.add(R.id.fragment_place, targetFragment, tag)
                   // .addToBackStack(tag)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
