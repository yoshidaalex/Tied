package com.tied.android.tiedapp.ui.activities.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.AllScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.NextWeekScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.ThisMonthScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.ThisWeekScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.TodayScheduleFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleActivity extends FragmentActivity implements FragmentIterationListener, View.OnClickListener {

    public static final String TAG = ScheduleActivity.class
            .getSimpleName();

    public static ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;
    ImageView img_close;

    public FragmentIterationListener mListener;

    LinearLayout upcoming_tab, overdue_tab, completed_tab, tab_bar, alert_edit_msg;
    HorizontalScrollView tab_scroll;
    List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_timeline);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        fragmentList.add(new TodayScheduleFragment());
        fragmentList.add(new AllScheduleFragment());
        fragmentList.add(new ThisWeekScheduleFragment());

        initComponent();
    }

    public void initComponent() {
        // Set up the ViewPager with the sections adapter.

        bundle = getIntent().getExtras();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);
        upcoming_tab = (LinearLayout) findViewById(R.id.upcoming_tab);
        overdue_tab = (LinearLayout) findViewById(R.id.overdue_tab);
        completed_tab = (LinearLayout) findViewById(R.id.completed_tab);
        tab_scroll = (HorizontalScrollView) findViewById(R.id.tab_scroll);

        alert_edit_msg = (LinearLayout) findViewById(R.id.alert_edit_msg);
        // moveViewToScreenCenter( alert_edit_msg, "Your edit was successful");

        upcoming_tab.setOnClickListener(this);
        overdue_tab.setOnClickListener(this);
        completed_tab.setOnClickListener(this);


        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            selectTab(tab_bar, 0);
        }
        onCustomSelected(mViewPager);
        Logger.write("It is created again");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.upcoming_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.overdue_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.completed_tab:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    public static void moveViewToScreenCenter(final View view, String message) {
        view.setVisibility(View.VISIBLE);
        TextView txt_alert = (TextView) view.findViewById(R.id.info_msg);
        txt_alert.setText(message);

        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -130);
        anim.setFillAfter(true);
        anim.setDuration(2500);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
//                view.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);

    }

    public void onCustomSelected(ViewPager vpPager) {
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                //  Toast.makeText(getActivity(),"Selected page position: " + position, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "position real: " + position);
                selectTab(tab_bar, position);
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void selectTab(LinearLayout tab_bar, final int position) {
        int index = 0;
        for (int i = 0; i < tab_bar.getChildCount(); i++) {
            if (tab_bar.getChildAt(i) instanceof LinearLayout) {
                LinearLayout child = (LinearLayout) tab_bar.getChildAt(i);
                final TextView title = (TextView) child.getChildAt(0);

                View indicator = (View) child.getChildAt(1);
                if(position != index){
                    indicator.setVisibility(View.INVISIBLE);
                 title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                } else {
                    indicator.setVisibility(View.VISIBLE);
                    title.setTextColor(getResources().getColor(R.color.button_bg));
                    ViewTreeObserver vto = tab_scroll.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            tab_scroll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            if (position == 0) {
                                tab_scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                            } else {
                                tab_scroll.scrollTo((int) title.getScaleX(), 0);
                            }
                        }
                    });
                }
                index++;
            }
        }
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        mViewPager.setCurrentItem(action);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : " + position);

            fragment = fragmentList.get(position);

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private static int mCurCheckPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_SALES && resultCode == Activity.RESULT_OK) {

            Logger.write(data.getSerializableExtra("selected").toString());
        }
    }
}

