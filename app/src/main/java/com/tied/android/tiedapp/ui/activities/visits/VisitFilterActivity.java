package com.tied.android.tiedapp.ui.activities.visits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.objects.visit.VisitFilter;
import com.tied.android.tiedapp.ui.fragments.client.MapAndListFragment;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisitFilterActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = VisitFilterActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    Client client;
    VisitFilter visitFilter;

    LinearLayout back_layout;
    TextView txt_apply, txt_clear;
    SeekBar m_seekbar;
    TextView txt_miles, client_name;
    TextView txt_recent, txt_oldest, txt_nearest, txt_farthest, txt_month, txt_year;
    ImageView clientPhoto;
    int distance;
    String sortby, month, year;
    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_filter);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        try {
            visitFilter = (VisitFilter) bundle.getSerializable(Constants.VISIT_DATA);
        } catch (Exception e) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_apply = (TextView) findViewById(R.id.txt_apply);
        txt_apply.setOnClickListener(this);

        txt_clear = (TextView) findViewById(R.id.txt_clear);
        txt_clear.setOnClickListener(this);

        clientPhoto=(ImageView)findViewById(R.id.client_photo);
        client_name = (TextView) findViewById(R.id.client_name);
        txt_miles = (TextView) findViewById(R.id.txt_miles);
        setMiles(MapAndListFragment.distance);

        txt_month = (TextView)findViewById(R.id.txt_month_val);
        txt_year = (TextView)findViewById(R.id.txt_year_val);

        m_seekbar = (SeekBar) findViewById(R.id.seekBar);
        m_seekbar.setProgress(2000);
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

        txt_recent = (TextView) findViewById(R.id.txt_recent);
        txt_oldest = (TextView) findViewById(R.id.txt_oldest);
        txt_nearest = (TextView) findViewById(R.id.txt_nearest);
        txt_farthest = (TextView) findViewById(R.id.txt_farthest);

        if (visitFilter != null) {
            if (visitFilter.getClient() != null) {
                client_name.setText(MyUtils.getClientName(visitFilter.getClient()));
                MyUtils.Picasso.displayImage(visitFilter.getClient().getLogo(), clientPhoto);
            }

            m_seekbar.setProgress(visitFilter.getDistance());
            if(visitFilter.getMonth()==0) txt_month.setText("All");
            else txt_month.setText(MyUtils.MONTHS_LIST[visitFilter.getMonth() - 1]);
            txt_year.setText(String.valueOf(visitFilter.getYear()));
            setSortby(visitFilter.getSort());
        }else{

        }
    }

    private void setMiles(int miles) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedString = formatter.format(Double.valueOf(miles));
        txt_miles.setText(formattedString + " miles");
    }

    public void onSortBy(View v) {
        switch (v.getId()) {
            case R.id.txt_recent:
                sortby = "recent";
                break;
            case R.id.txt_oldest:
                sortby = "oldest";
                break;
            case R.id.txt_nearest:
                sortby = "nearest";
                break;
            case R.id.txt_farthest:
                sortby = "farthest";
                break;
        }

        setSortby(sortby);
    }

    private void setSortby(String sortby) {
        this.sortby=sortby;
        if (sortby.equals("recent")) {
            txt_recent.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_recent.setTextColor(Color.WHITE);

            txt_oldest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_oldest.setTextColor(Color.BLACK);
            txt_nearest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_nearest.setTextColor(Color.BLACK);
            txt_farthest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_farthest.setTextColor(Color.BLACK);
        } else if (sortby.equals("oldest")){
            txt_oldest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_oldest.setTextColor(Color.WHITE);

            txt_recent.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_recent.setTextColor(Color.BLACK);
            txt_nearest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_nearest.setTextColor(Color.BLACK);
            txt_farthest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_farthest.setTextColor(Color.BLACK);
        } else if (sortby.equals("nearest")) {
            txt_nearest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_nearest.setTextColor(Color.WHITE);

            txt_recent.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_recent.setTextColor(Color.BLACK);
            txt_oldest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_oldest.setTextColor(Color.BLACK);
            txt_farthest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_farthest.setTextColor(Color.BLACK);
        } else if (sortby.equals("farthest")) {
            txt_farthest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_farthest.setTextColor(Color.WHITE);

            txt_recent.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_recent.setTextColor(Color.BLACK);
            txt_oldest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_oldest.setTextColor(Color.BLACK);
            txt_nearest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_nearest.setTextColor(Color.BLACK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SELECT_CLIENT && resultCode == this.RESULT_OK) {
            client = (Client)(data.getSerializableExtra("selected"));
            client_name.setText(MyUtils.getClientName(client));
            MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
            Logger.write(client.toString());
        }
    }

    private void selectClient(Client client) {
        MyUtils.initiateClientSelector(this, client, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.select_client_layout:
                selectClient(client);
                break;
            case R.id.choose_month:
                showList(HelperMethods.MONTHS_LIST, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        month=HelperMethods.MONTHS_LIST[i];
                        txt_month.setText(month);
                        ad.dismiss();
                    }
                });
                break;
            case R.id.choose_year:
                int cyear=HelperMethods.getCurrentYear(HelperMethods.getTodayDate());
                final List<Integer> years = new ArrayList<>();
                for(int i=cyear; i>2014; i--) {
                    years.add(i);
                }
                String[] yearArray=new String[years.size()];
                for(int i=0; i<years.size(); i++) {
                    yearArray[i]=""+years.get(i);
                }
                showList(yearArray, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        year=""+years.get(i);
                        txt_year.setText(year);
                        ad.dismiss();
                    }
                });
                break;
            case R.id.txt_apply:
                if (client == null) {
                   // MyUtils.showErrorAlert(this, "please select a client");
                   // return;
                } else if (txt_month.getText().toString().isEmpty()) {
                    MyUtils.showErrorAlert(this, "please select a month");
                    return;
                } else if (txt_year.getText().toString().isEmpty()) {
                    MyUtils.showErrorAlert(this, "please select a year");
                    return;
                }

                visitFilter = new VisitFilter();
                visitFilter.setUnit("mi");
                visitFilter.setDistance(distance);
                visitFilter.setYear(Integer.valueOf(txt_year.getText().toString()).intValue());

                int endMonth = Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(month)+1;
                visitFilter.setMonth(endMonth);
                visitFilter.setClient(client);
                visitFilter.setSort(sortby);

                Intent intent = new Intent();
                intent.putExtra(Constants.VISIT_DATA, visitFilter);
                setResult(RESULT_OK, intent);
                finishActivity(Constants.VISIT_FILTER);
                finish();
                break;
            case R.id.txt_clear:
                txt_month.setText("");
                txt_year.setText("");
                setSortby("recent");
                break;
        }
    }

    private void showList(String[] items, AdapterView.OnItemClickListener onSelect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ListView lv=new ListView(this);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(onSelect);
        builder.setView(lv);
        ad=builder.create();
        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ad.dismiss();
            }
        });
        ad.show();
    }
}
