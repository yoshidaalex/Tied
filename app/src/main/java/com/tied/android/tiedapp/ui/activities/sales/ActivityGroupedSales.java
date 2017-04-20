package com.tied.android.tiedapp.ui.activities.sales;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.fragments.sales.SaleViewAllFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by femi on 9/28/2016.
 */
public class ActivityGroupedSales extends FragmentActivity implements FragmentIterationListener {
    SaleViewAllFragment saleViewAllFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        saleViewAllFragment=(SaleViewAllFragment)SaleViewAllFragment.newInstance(getIntent().getExtras());
        addFragment(ft, null, saleViewAllFragment , SaleViewAllFragment.class.getName());
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View v) {
        finish();
    }
    public void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(currentFragment!=null) transaction.hide(currentFragment);
        // use a fragment tag, so that later on we can find the currently displayed fragment
        if(targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        }else {
            transaction.add(R.id.fragment_place, targetFragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        saleViewAllFragment.onActivityResult(requestCode, resultCode, data);
    }
}
