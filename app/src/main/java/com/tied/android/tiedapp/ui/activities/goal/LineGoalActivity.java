package com.tied.android.tiedapp.ui.activities.goal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.lines.ActiveGoalFragment;
import com.tied.android.tiedapp.ui.fragments.lines.PastGoalFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

public class LineGoalActivity extends AppCompatActivity implements  View.OnClickListener {

    public static final String TAG = LineGoalActivity.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;
    private Goal goal;

    ImageView img_segment, img_close, img_add;
    boolean bActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_goal_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        initComponent(goal);
    }

    private void initComponent(Goal goal) {
        img_segment = (ImageView) findViewById(R.id.img_segment);
        img_segment.setOnClickListener(this);

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new PagerAdapter(LineGoalActivity.this.getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);

                mViewPager.setCurrentItem(0);
                selectTab(0);
        }
        onCustomSelected(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.img_add:
                MyUtils.startRequestActivity(this, LineAddGoalActivity.class, Constants.GOAL_REQUEST, bundle);
                break;
            case R.id.img_segment:
                bActive = !bActive;
                if (bActive) {
                    mViewPager.setCurrentItem(0);
                    img_segment.setBackgroundResource(R.drawable.goal_active);
                } else {
                    mViewPager.setCurrentItem(1);
                    img_segment.setBackgroundResource(R.drawable.goal_past);
                }
                break;
        }
    }

    public void selectTab(int position){
        switch (position){
            case 0:
                bActive = true;
                img_segment.setBackgroundResource(R.drawable.goal_active);
                break;
            case 1:
                bActive = false;
                img_segment.setBackgroundResource(R.drawable.goal_past);
                break;
            default:;
        }
    }

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new ActiveGoalFragment();
                    break;
                case 1:
                    fragment = new PastGoalFragment();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.write("requesssssssssssssssssssssssssssst "+requestCode);
    }
}
