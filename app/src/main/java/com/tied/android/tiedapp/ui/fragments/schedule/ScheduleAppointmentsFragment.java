package com.tied.android.tiedapp.ui.fragments.schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.*;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleAppointmentsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ScheduleAppointmentsFragment.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    public FragmentIterationListener mListener;

    LinearLayout all_tab, today_tab, this_week_tab, next_week_tab, tab_bar, this_month, alert_edit_msg;
    HorizontalScrollView tab_scroll;
    List<SchedulesFragment> fragmentList = new ArrayList<SchedulesFragment>();
    ImageView img_plus;

    Fragment fragment = null;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new ScheduleAppointmentsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_timeline, null);

        initComponent(view);

        Log.d(TAG, "AM HERE AGAIN");
        if (savedInstanceState != null) {
            int  position= savedInstanceState.getInt(key);
            mViewPager.setCurrentItem(position);
        }
        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        bundle = getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blue_status_bar));
        }

        img_plus = (ImageView) view.findViewById(R.id.img_plus);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tab_bar = (LinearLayout) view.findViewById(R.id.tab_bar);
        all_tab = (LinearLayout) view.findViewById(R.id.all_tab);
        today_tab = (LinearLayout) view.findViewById(R.id.today_tab);
        this_week_tab = (LinearLayout) view.findViewById(R.id.this_week_tab);
        next_week_tab = (LinearLayout) view.findViewById(R.id.next_week_tab);
        this_month = (LinearLayout) view.findViewById(R.id.this_month);
        tab_scroll = (HorizontalScrollView) view.findViewById(R.id.tab_scroll);

        alert_edit_msg = (LinearLayout) getActivity().findViewById(R.id.alert_edit_msg);
        // moveViewToScreenCenter( alert_edit_msg, "Your edit was successful");

        img_plus.setOnClickListener(this);
        all_tab.setOnClickListener(this);
        today_tab.setOnClickListener(this);
        this_week_tab.setOnClickListener(this);
        next_week_tab.setOnClickListener(this);
        this_month.setOnClickListener(this);


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
            case R.id.img_plus:
                MyUtils.startRequestActivity(getActivity(), CreateAppointmentActivity.class, Constants.CreateSchedule, bundle);
                break;
            case R.id.all_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.today_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.this_week_tab:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.next_week_tab:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.this_month:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagerAdapter.notifyDataSetChanged();
        Logger.write("It has resumed oooooooooooooooh");
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*if(mPagerAdapter == null){
            mPagerAdapter = new PagerAdapter(getFragmentManager());
        }
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    public void refresh() {
        Logger.write("Refreshing");
        final int current_item=mViewPager.getCurrentItem();
        mViewPager.setAdapter(null);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(current_item);
            }
        }, 300);
        MainActivity.getInstance().refresh.setRefreshing(false);
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
                            if (position == 4 || position == 3) {
                                tab_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            } else if (position == 0) {
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

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Log.d(TAG, "position : " + position);
            switch (position) {
                        case 0: fragment = new TodayScheduleFragment(); break;
                        case 1: fragment = new  AllScheduleFragment(); break;
                        case 2: fragment = new ThisWeekScheduleFragment(); break;
                        case 3: fragment = new NextWeekScheduleFragment(); break;
                        case 4: fragment = new ThisMonthScheduleFragment(); break;
            }

            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    private static int mCurCheckPosition = 0;
    private String key="curTab";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(key, mViewPager.getCurrentItem());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        // Logger.write("femiiiiiiiiiiiiiiii");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_SALES && resultCode == Activity.RESULT_OK) {
            Logger.write(data.getSerializableExtra("selected").toString());
        }

        if ((requestCode == Constants.ViewSchedule || requestCode == Constants.CreateSchedule) && resultCode == Activity.RESULT_OK) {
            //Fragment currentFrag= mPagerAdapter.getItem(mViewPager.getCurrentItem());
            //if(currentFrag instanceof TodayScheduleFragment)
                //((TodayScheduleFragment)mPagerAdapter.getItem(mViewPager.getCurrentItem())).initSchedule();
            mViewPager.setAdapter(null);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.getAdapter().notifyDataSetChanged();
            Logger.write("lkasdafa;lskdf;laksdf alsdkj adapter");
        }
    }
}

