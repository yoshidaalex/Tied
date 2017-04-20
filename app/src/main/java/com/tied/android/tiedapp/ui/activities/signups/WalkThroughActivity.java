package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DemoData;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.lib.coverflow.CoverFlow;
import com.tied.lib.coverflow.core.PagerContainer;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by yuweichen on 16/4/30.
 */
public class WalkThroughActivity extends Activity implements View.OnClickListener{

    public static final String TAG = WalkThroughActivity.class
            .getSimpleName();

    static WalkThroughActivity walkThroughActivity;
    private FragmentIterationListener mListener;

    private TextView register, sign_in;

    public static WalkThroughActivity getInstance() {
        return walkThroughActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walkThroughActivity=this;
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.fragment_welcome);

        boolean returning = MyUtils.getSharedPreferences().getBoolean(Constants.RETURNING_USER, false);
        if(returning){
            MyUtils.startActivity(this, SignInActivity.class);
        }else{
            initComponent();
        }
    }

    public void initComponent(){

        register = (TextView) findViewById(R.id.register);
        sign_in = (TextView) findViewById(R.id.signin);

        register.setOnClickListener(this);
        sign_in.setOnClickListener(this);

        PagerContainer container = (PagerContainer) findViewById(R.id.pager_container);
        ViewPager pager = container.getViewPager();
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        pager.setAdapter(new MyPagerAdapter());
        indicator.setViewPager(pager);

        pager.setClipChildren(false);
        //
        pager.setOffscreenPageLimit(15);

        new CoverFlow.Builder()
                .with(pager)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                .spaceSize(0f)
                .build();
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(WalkThroughActivity.this).inflate(R.layout.item_cover,null);
            String[] str_walkthrough = getResources().getStringArray(R.array.str_walkthrough);

            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
            imageView.setImageDrawable(getResources().getDrawable(DemoData.covers[position]));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            container.addView(view);

            TextView txt_walthrough = (TextView) view.findViewById(R.id.txt_walkthrough);
            txt_walthrough.setText(str_walkthrough[position]);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return DemoData.covers.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                MyUtils.startActivity(this, SignUpActivity.class);
                break;
            case R.id.signin:
                MyUtils.startActivity(this, SignInActivity.class);
                break;
        }
    }
}
