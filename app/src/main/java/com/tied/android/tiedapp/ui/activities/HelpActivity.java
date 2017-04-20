package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.fragments.HelpActivityFragment;

public class HelpActivity extends FragmentActivity {

    public static final String TAG = HelpActivity.class
            .getSimpleName();

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        launchFragment(Constants.Help,null);
    }

    @Override
    public void onBackPressed() {
        if(fragment_index == Constants.Help){
            finish();
        }
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        Log.d(TAG, "position "+pos);

        switch (pos) {
            case Constants.Help:
                fragment = new HelpActivityFragment();
                break;
            default:
                finish();
        }

        fragment.setArguments(bundle);

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
