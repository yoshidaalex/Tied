package com.tied.android.tiedapp.ui.activities.client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.fragments.client.MapAndListFragment;
import com.tied.android.tiedapp.util.MyUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ClientFilterActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = ClientFilterActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    TextView txt_apply, txt_clear;
    RelativeLayout parent, territory_layout, line_layout;
    int page_index;
    SeekBar m_seekbar;
    TextView txt_miles, txt_justme, txt_co_worker;
    TextView txt_all, txt_1w, txt_2w, txt_3w, txt_4w, txt_2m;
    TextView txt_distance, txt_sale, txt_highest, txt_lowest, txt_num_lines, txt_num_territories;

    String group = "all";
    int last_visited = 0;
    String order = "asc";
    int distance = MapAndListFragment.distance;
    String orderby = "distance";
    ArrayList<Territory> selectedTerritories = new ArrayList<Territory>();
    ArrayList<String> selectedLines = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_filter);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        page_index = bundle.getInt(Constants.SHOW_FILTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (page_index == 0) {
                window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.gradient));
            }
        }

        parent = (RelativeLayout) findViewById(R.id.parent);
        if (page_index == 0) {
            parent.setBackgroundResource(R.drawable.background_blue);
        } else {
            parent.setBackgroundResource(R.drawable.background_gradient);
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_apply = (TextView) findViewById(R.id.txt_apply);
        txt_apply.setOnClickListener(this);

        txt_clear = (TextView) findViewById(R.id.txt_clear);
        txt_clear.setOnClickListener(this);

        territory_layout = (RelativeLayout) findViewById(R.id.territory_layout);
        territory_layout.setOnClickListener(this);

        line_layout = (RelativeLayout) findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

        txt_miles = (TextView) findViewById(R.id.txt_miles);
        setMiles(MapAndListFragment.distance);

        txt_num_lines=(TextView) findViewById(R.id.num_lines);
        txt_num_territories =(TextView) findViewById(R.id.num_territories);

        m_seekbar = (SeekBar) findViewById(R.id.seekBar);
        m_seekbar.setProgress(MapAndListFragment.distance);
        m_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_seekbar.setProgress(progress);
                distance = progress;

                setMiles(distance);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txt_justme = (TextView) findViewById(R.id.txt_justme);
        txt_co_worker = (TextView) findViewById(R.id.txt_co_worker);

        txt_all = (TextView) findViewById(R.id.txt_all);
        txt_1w = (TextView) findViewById(R.id.txt_1w);
        txt_2w = (TextView) findViewById(R.id.txt_2w);
        txt_3w = (TextView) findViewById(R.id.txt_3w);
        txt_4w = (TextView) findViewById(R.id.txt_4w);
        txt_2m = (TextView) findViewById(R.id.txt_2m);

        txt_distance = (TextView) findViewById(R.id.txt_distance);
        txt_sale = (TextView) findViewById(R.id.txt_sale);

        txt_highest = (TextView) findViewById(R.id.txt_highest);
        txt_lowest = (TextView) findViewById(R.id.txt_lowest);

        setGroup(MapAndListFragment.group);

        setLastVisited(MapAndListFragment.last_visited);
        setOrderby(MapAndListFragment.orderby);
        setOrder(MapAndListFragment.order);
    }

    private void setMiles(int miles) {
        if(miles==0 || miles>=MapAndListFragment.distance) {
            txt_miles.setText("Any");
        }else {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedString = formatter.format(Double.valueOf(miles));
            txt_miles.setText(formattedString + " miles");
        }
    }

    public void onClient(View v) {
        switch (v.getId()) {

            case R.id.txt_justme:
                group = "me";
                break;
            case R.id.txt_co_worker:
                group = "coworker";
                break;
        }
        setGroup(group);
    }

    private void setGroup(String groupName) {
        if (groupName.equals("me")) {
            txt_justme.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_justme.setTextColor(Color.WHITE);

            txt_co_worker.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_co_worker.setTextColor(Color.BLACK);
        } else {
            txt_co_worker.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_co_worker.setTextColor(Color.WHITE);

            txt_justme.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_justme.setTextColor(Color.BLACK);
        }
    }

    public void onLastVisited(View v) {
        switch (v.getId()) {
            case R.id.txt_all:
                last_visited = 0;
                break;
            case R.id.txt_1w:
                last_visited = 1;
                break;
            case R.id.txt_2w:
                last_visited = 2;
                break;
            case R.id.txt_3w:
                last_visited = 3;
                break;
            case R.id.txt_4w:
                last_visited = 4;
                break;
            case R.id.txt_2m:
                last_visited = 8;
                break;
        }

        setLastVisited(last_visited);
    }

    private void setLastVisited(int index) {
        last_visited=index;
        switch (index) {
            case 0:
                txt_all.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
                txt_all.setTextColor(Color.WHITE);

                txt_1w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_1w.setTextColor(Color.BLACK);

                txt_2w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_3w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_4w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_2m.setBackgroundResource(R.drawable.white_fill_grey_stroke);

                txt_2w.setTextColor(Color.BLACK);
                txt_3w.setTextColor(Color.BLACK);
                txt_4w.setTextColor(Color.BLACK);
                txt_2m.setTextColor(Color.BLACK);
                break;
            case 1:
                txt_1w.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
                txt_1w.setTextColor(Color.WHITE);

                txt_all.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_all.setTextColor(Color.BLACK);

                txt_2w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_3w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_4w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_2m.setBackgroundResource(R.drawable.white_fill_grey_stroke);

                txt_2w.setTextColor(Color.BLACK);
                txt_3w.setTextColor(Color.BLACK);
                txt_4w.setTextColor(Color.BLACK);
                txt_2m.setTextColor(Color.BLACK);
                break;
            case 2:
                txt_2w.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
                txt_2w.setTextColor(Color.WHITE);

                txt_all.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_all.setTextColor(Color.BLACK);
                txt_1w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_3w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_4w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_2m.setBackgroundResource(R.drawable.white_fill_grey_stroke);

                txt_1w.setTextColor(Color.BLACK);
                txt_3w.setTextColor(Color.BLACK);
                txt_4w.setTextColor(Color.BLACK);
                txt_2m.setTextColor(Color.BLACK);
                break;
            case 3:
                txt_all.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_all.setTextColor(Color.BLACK);
                txt_3w.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
                txt_3w.setTextColor(Color.WHITE);

                txt_2w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_1w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_4w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_2m.setBackgroundResource(R.drawable.white_fill_grey_stroke);

                txt_2w.setTextColor(Color.BLACK);
                txt_1w.setTextColor(Color.BLACK);
                txt_4w.setTextColor(Color.BLACK);
                txt_2m.setTextColor(Color.BLACK);
                break;
            case 4:
                txt_all.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_all.setTextColor(Color.BLACK);
                txt_4w.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
                txt_4w.setTextColor(Color.WHITE);

                txt_2w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_3w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_1w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_2m.setBackgroundResource(R.drawable.white_fill_grey_stroke);

                txt_2w.setTextColor(Color.BLACK);
                txt_3w.setTextColor(Color.BLACK);
                txt_1w.setTextColor(Color.BLACK);
                txt_2m.setTextColor(Color.BLACK);
                break;
            case 8:

                txt_2m.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
                txt_2m.setTextColor(Color.WHITE);

                txt_all.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_all.setTextColor(Color.BLACK);
                txt_2w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_3w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_4w.setBackgroundResource(R.drawable.white_fill_grey_stroke);
                txt_1w.setBackgroundResource(R.drawable.white_fill_grey_stroke);

                txt_2w.setTextColor(Color.BLACK);
                txt_3w.setTextColor(Color.BLACK);
                txt_4w.setTextColor(Color.BLACK);
                txt_1w.setTextColor(Color.BLACK);
                break;
        }
    }

    public void onOrderBy(View v) {
        switch (v.getId()) {
            case R.id.txt_distance:
                orderby = "distance";
                break;
            case R.id.txt_sale:
                orderby = "sales";
                break;
        }

        setOrderby(orderby);
    }

    private void setOrderby(String orderby) {
        this.orderby=orderby;
        if (orderby.equals("distance")) {
            txt_distance.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_distance.setTextColor(Color.WHITE);

            txt_sale.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_sale.setTextColor(Color.BLACK);
        } else {
            txt_sale.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_sale.setTextColor(Color.WHITE);

            txt_distance.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_distance.setTextColor(Color.BLACK);
        }
    }

    public void onOrder(View v) {

        switch (v.getId()) {
            case R.id.txt_highest:
                order = "desc";
                break;
            case R.id.txt_lowest:
                order = "asc";
                break;
        }

        setOrder(order);
    }

    private void setOrder(String order) {
        this.order=order;
        if (order.equals("desc")) {
            txt_highest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_highest.setTextColor(Color.WHITE);

            txt_lowest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_lowest.setTextColor(Color.BLACK);
        } else {
            txt_lowest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_lowest.setTextColor(Color.WHITE);

            txt_highest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_highest.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.txt_apply:
                MapAndListFragment.isClientFilter = true;
                MapAndListFragment.group = group;
                MapAndListFragment.distance = distance;
                MapAndListFragment.last_visited = last_visited;
                MapAndListFragment.order = order;
                MapAndListFragment.orderby = orderby;
                MapAndListFragment.selectedTerritories = selectedTerritories;
                MapAndListFragment.selectedLines = selectedLines;

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finishActivity(Constants.ClientFilter);
                finish();
                break;
            case R.id.txt_clear:
                MapAndListFragment.isClientFilter = false;
                MapAndListFragment.isClear = true;
                intent = new Intent();
                setResult(RESULT_OK, intent);
                finishActivity(Constants.ClientFilter);
                finish();
                break;
            case R.id.territory_layout:
                bundle.putInt(Constants.SHOW_TERRITORY, 1);
                bundle.putBoolean("single", false);
                MyUtils.initiateTerritorySelector(this, selectedTerritories, true);
                break;
            case R.id.line_layout:
                bundle.putInt(Constants.SHOW_LINE, 1);
                MyUtils.initiateLineSelector(this, selectedLines ,true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SELECT_TERRITORY && resultCode == this.RESULT_OK) {
            selectedTerritories = (ArrayList<Territory>) (data.getSerializableExtra("selected"));
            try {
                txt_num_territories.setText(selectedTerritories.size()+" selected");
            }catch (Exception e) {
                txt_num_territories.setText( "0 selected");
            }
        } else if (requestCode == Constants.SELECT_LINE && resultCode == this.RESULT_OK) {
            selectedLines = (ArrayList<String>) (data.getSerializableExtra("selected"));
            try {
                txt_num_lines.setText(selectedLines.size() + " selected");
            }catch (Exception e) {
                txt_num_lines.setText( "0 selected");
            }
        }
    }
}
