package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.lines.ActiveGoalFragment;
import com.tied.android.tiedapp.ui.fragments.lines.LineGoalsFragment;
import com.tied.android.tiedapp.ui.fragments.lines.PastGoalFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineGoalActivity extends AppCompatActivity implements  View.OnClickListener {

    public static final String TAG = LineGoalActivity.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;
    private Goal goal;

    LinearLayout back_layout;
    TextView active_goals, past_goals, goal_title, goal_progress, txt_expire, description, lines_client_count;
    ImageView img_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_goal_layout);


            bundle = getIntent().getExtras();
            //goal = (Goal) bundle.getSerializable(Constants.GOAL_DATA);
            user = MyUtils.getUserFromBundle(bundle);


        initComponent(goal);

    }

    private void initComponent(Goal goal) {
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);
        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setOnClickListener(this);

       // active_goals = (TextView) findViewById(R.id.active_goals);
      //  past_goals = (TextView) findViewById(R.id.past_goals);

       // active_goals.setOnClickListener(this);
       // past_goals.setOnClickListener(this);

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
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.img_add:
                MyUtils.startRequestActivity(this, LineAddGoalActivity.class, Constants.GOAL_REQUEST, bundle);
                break;
           /* case R.id.active_goals:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.past_goals:
                mViewPager.setCurrentItem(1);
                break; */
        }
    }

    public void selectTab(int position){
        switch (position){
            case 0:
                active_goals.setBackgroundResource(R.drawable.left_curve_white_background);
                active_goals.setTextColor(getResources().getColor(R.color.button_bg));

                past_goals.setBackgroundResource(R.drawable.right_curve_white_stroke);
                past_goals.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                active_goals.setBackgroundResource(R.drawable.left_curve_white_stroke);
                active_goals.setTextColor(getResources().getColor(R.color.white));

                past_goals.setBackgroundResource(R.drawable.right_curve_white_background);
                past_goals.setTextColor(getResources().getColor(R.color.button_bg));
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
