package com.tied.android.tiedapp.ui.activities.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.fragments.LinesFragment;
import com.tied.android.tiedapp.ui.fragments.TerritoriesFragment;
import com.tied.android.tiedapp.ui.fragments.client.ViewClientFragment;
import com.tied.android.tiedapp.ui.fragments.sales.SaleViewAllFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

public class ActivityClientProfile extends AppCompatActivity implements  View.OnClickListener, FragmentIterationListener {

    public static final String TAG = ActivityClientProfile.class
            .getSimpleName();

    public ViewPager mViewPager;
    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    LinearLayout lines_tab, territories_tab, tab_bar;
    ViewClientFragment linesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        //initComponent();
        setContentView(R.layout.activity_fragment_container);
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        linesFragment=(ViewClientFragment)ViewClientFragment.newInstance(bundle);
        MyUtils.addFragment(ft, null, linesFragment , ViewClientFragment.class.getName());
/**
 if (bundle.getBoolean(Constants.SHOW_TERRITORY)) {
 mViewPager.setCurrentItem(1);
 }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finishActivity(Constants.ClientDelete);
                finish();
                break;
            case R.id.lines_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.territories_tab:
                mViewPager.setCurrentItem(1);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        linesFragment.onActivityResult(requestCode, resultCode, data);
    }
}
