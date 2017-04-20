package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.adapters.PagerAdapter;

public class SwipeActivity extends AppCompatActivity implements View.OnClickListener{

    FragmentPagerAdapter adapterViewPager;
    LinearLayout first, second, third, fourth;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        initComponent();
        adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        if (vpPager != null) {
            vpPager.setAdapter(adapterViewPager);
        }
    }

    public void initComponent(){

        first = (LinearLayout) findViewById(R.id.first);
        first.setOnClickListener(this);

        second = (LinearLayout) findViewById(R.id.second);
        second.setOnClickListener(this);

        third = (LinearLayout) findViewById(R.id.third);
        third.setOnClickListener(this);

        fourth = (LinearLayout) findViewById(R.id.fourth);
        fourth.setOnClickListener(this);

        onCustomSelected(vpPager);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first:
                vpPager.setCurrentItem(0);
                break;
            case R.id.second:
                vpPager.setCurrentItem(1);
                break;
            case R.id.third:
                vpPager.setCurrentItem(2);
                break;
            case R.id.fourth:
                vpPager.setCurrentItem(3);
                break;
        }
    }

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(SwipeActivity.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();
                switch(position){
                    case 0:
                        first.setBackgroundResource(R.drawable.tab_selected);
                        second.setBackgroundResource(0);
                        third.setBackgroundResource(0);
                        fourth.setBackgroundResource(0);
                        break;
                    case 1:
                        first.setBackgroundResource(0);
                        second.setBackgroundResource(R.drawable.tab_selected);
                        third.setBackgroundResource(0);
                        fourth.setBackgroundResource(0);
                        break;
                    case 2:
                        first.setBackgroundResource(0);
                        second.setBackgroundResource(0);
                        third.setBackgroundResource(R.drawable.tab_selected);
                        fourth.setBackgroundResource(0);
                        break;
                    case 3:
                        first.setBackgroundResource(0);
                        second.setBackgroundResource(0);
                        third.setBackgroundResource(0);
                        fourth.setBackgroundResource(R.drawable.tab_selected);
                        break;
                }
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


}
