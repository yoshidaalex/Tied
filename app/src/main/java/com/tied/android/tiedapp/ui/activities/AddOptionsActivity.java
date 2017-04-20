package com.tied.android.tiedapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Femi on 7/31/2016.
 */
public class AddOptionsActivity  extends AppCompatActivity implements  View.OnClickListener {
    AppCompatActivity view;
    Bundle bundle;
    User user;
    TextView txt_done;
    RelativeLayout sale_layout, schedule_layout, client_layout, line_layout, employee_layout, goal_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_options);
        view=this;
        initComponent();
    }
    public void initComponent(){

        bundle = getIntent().getExtras();
        if (bundle != null) {
           user= MyUtils.getUserFromBundle(bundle);
        }

      //  txt_done = (TextView) findViewById(R.id.txt_done);
       // txt_done.setOnClickListener(this);

        sale_layout = (RelativeLayout) view.findViewById(R.id.sale_layout);
        sale_layout.setOnClickListener(this);
        sale_layout.setVisibility(View.GONE);

        schedule_layout = (RelativeLayout) view.findViewById(R.id.schedule_layout);
        schedule_layout.setOnClickListener(this);

        client_layout = (RelativeLayout) view.findViewById(R.id.client_layout);
        client_layout.setOnClickListener(this);
//        client_layout.setVisibility(View.GONE);

        line_layout = (RelativeLayout) view.findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);
//        line_layout.setVisibility(View.GONE);

        employee_layout = (RelativeLayout) view.findViewById(R.id.employee_layout);
        employee_layout.setOnClickListener(this);
        employee_layout.setVisibility(View.GONE);

        goal_layout = (RelativeLayout) view.findViewById(R.id.goal_layout);
        goal_layout.setOnClickListener(this);
        goal_layout.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txt_done:
               // nextAction(Constants.CreateSchedule, bundle);
                break;
            case R.id.sale_layout:

                break;
            case R.id.schedule_layout:
                MyUtils.startActivity(this, CreateAppointmentActivity.class);
                break;
            case R.id.client_layout:
                MyUtils.startActivity(this, AddClientActivity.class);
                break;
            case R.id.employee_layout:

                break;
            case R.id.line_layout:
                MyUtils.startActivity(this, AddLinesActivity.class, bundle);
                break;
            case R.id.goal_layout:

                break;
        }
        finish();
    }
    public void goBack(View view) {
        onBackPressed();
    }
}
