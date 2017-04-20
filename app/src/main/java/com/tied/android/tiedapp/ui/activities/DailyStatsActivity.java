package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.fragments.DailyStatsFragment;
import com.tied.android.tiedapp.ui.fragments.HelpActivityFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by femi on 8/26/2016.
 */
public class DailyStatsActivity extends FragmentActivity implements FragmentIterationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_daily_fragment);
        launchFragment();

    }
    public void launchFragment() {

        Bundle bundle = new Bundle();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, DailyStatsFragment.newInstance(bundle))
                   //  .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();

    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }
}
