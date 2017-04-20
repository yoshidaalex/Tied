package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.GoalsAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

public class CoWorkergGoalsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = CoWorkergGoalsActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    TextView txt_title;

    protected ListView goals_listview;
    protected GoalsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_goals);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        ArrayList<Goal> goalDataModels = new ArrayList<Goal>();
        goals_listview = (ListView) findViewById(R.id.goals_listview);

        for (int i = 0 ; i < 5 ; i++) {
            Goal goalDataModel = new Goal();

            goalDataModel.setTitle("Make More Revenue");
            goalDataModel.setValue("200");
            goalDataModel.setTarget("50");

            goalDataModels.add(goalDataModel);
        }

        adapter = new GoalsAdapter(goalDataModels, this);
        goals_listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
        }
    }
}
