package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by femi on 8/28/2016.
 */
public class SuperActivity extends FragmentActivity implements View.OnClickListener, FragmentIterationListener {

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_fragment_container);
        bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        Fragment fragment = Fragment.instantiate(this, bundle.getString("fragment"), bundle);
        addFragment(getSupportFragmentManager().beginTransaction(), null, fragment, bundle.getString("fragment"));
    }
    public void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

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
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }
}
