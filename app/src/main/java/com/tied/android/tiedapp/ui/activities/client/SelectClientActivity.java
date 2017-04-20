package com.tied.android.tiedapp.ui.activities.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.client.tab.ClientAlphabeticalListFragment;
import com.tied.android.tiedapp.ui.fragments.client.tab.ClientDistanceListFragment;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.util.DemoData;

public class SelectClientActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = SelectClientActivity.class
            .getSimpleName();

    private int[] range = {0,500,1000,2000,5000};
    private boolean[] added;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;
    protected BaseAdapter adapter;

    LinearLayout tab_alpha, tab_last_visited,tab_distance, tab_bar;
    TextView alpha_indicator, last_visited_indicator,distance_indicator;
    TextView txt_alpha, txt_last_visited,txt_distance;

    private ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_select_client);

        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        tab_alpha = (LinearLayout) findViewById(R.id.tab_alpha);
        tab_alpha.setOnClickListener(this);

        tab_last_visited = (LinearLayout) findViewById(R.id.tab_last_visited);
        tab_last_visited.setOnClickListener(this);

        tab_distance = (LinearLayout) findViewById(R.id.tab_distance);
        tab_distance.setOnClickListener(this);

        alpha_indicator = (TextView) findViewById(R.id.alpha_indicator);
        last_visited_indicator = (TextView) findViewById(R.id.last_visited_indicator);
        distance_indicator = (TextView) findViewById(R.id.distance_indicator);

        txt_distance = (TextView) findViewById(R.id.txt_distance);
        txt_alpha = (TextView) findViewById(R.id.txt_alpha);
        txt_last_visited = (TextView) findViewById(R.id.txt_last_visited);

        bundle = getIntent().getExtras();
        if (bundle == null){
            bundle = new Bundle();
        }
        user = User.getCurrentUser(getApplicationContext());
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER_DATA, user_json);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            alpha_indicator.setVisibility(View.VISIBLE);
            txt_alpha.setTextColor(getResources().getColor(R.color.button_bg));
        }
        onCustomSelected(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_last_visited:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_distance:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_alpha:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.img_close:
               onBackPressed();
                break;
        }
    }


    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
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

    public void selectTab(LinearLayout tab_bar, int position){
        int index = 0;
        for(int i = 0; i < tab_bar.getChildCount(); i++){
            if(tab_bar.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) tab_bar.getChildAt(i);
                TextView title = (TextView) child.getChildAt(0);
                TextView indicator = (TextView) child.getChildAt(1);
                if(position != index){
                    indicator.setVisibility(View.INVISIBLE);
                    title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                }else{
                    indicator.setVisibility(View.VISIBLE);
                    title.setTextColor(getResources().getColor(R.color.button_bg));
                }
                index++;
            }
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : "+position);
            switch (position){
                case 0:
                    fragment = new ClientAlphabeticalListFragment();
                    break;

                case 1:
                    fragment = new ClientDistanceListFragment();
                    break;
                case 2:
                    fragment = new LastVisitedClientListFragment();
                    break;

            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return DemoData.select_client_layout.length;
        }
    }

}
