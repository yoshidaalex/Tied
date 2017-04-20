package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.LinesFragment;
import com.tied.android.tiedapp.util.MyUtils;

public class LinesListActivity extends AppCompatActivity implements  View.OnClickListener {

    public static final String TAG = LinesListActivity.class
            .getSimpleName();

    public ViewPager mViewPager;
    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    LinearLayout lines_tab, territories_tab, tab_bar;
    LinesFragment linesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        //initComponent();

        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        linesFragment=(LinesFragment)LinesFragment.newInstance(bundle);
        String tag =linesFragment.getClass().getName();
        ft.replace(R.id.fragment_place,linesFragment, tag)
                .addToBackStack(tag)
                .commit();
/**
        if (bundle.getBoolean(Constants.SHOW_TERRITORY)) {
            mViewPager.setCurrentItem(1);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == Constants.ADD_LINE || requestCode == Constants.LineDelete) && resultCode == RESULT_OK) {
            linesFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
