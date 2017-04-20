package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;

public class LineViewGoalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineViewGoalActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;
    private Goal goal;

    LinearLayout back_layout;
    ImageView img_add, img_edit;
    TextView goal_title, goal_progress, txt_expire, description, lines_client_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_goal_view);

        txt_expire = (TextView) findViewById(R.id.expire);
        goal_progress = (TextView) findViewById(R.id.goal_progress);
        goal_title = (TextView) findViewById(R.id.goal_title);
        description = (TextView) findViewById(R.id.description);
        lines_client_count = (TextView) findViewById(R.id.lines_client_count);

        bundle = getIntent().getExtras();
        goal = (Goal) bundle.getSerializable(Constants.GOAL_DATA);
        user = MyUtils.getUserFromBundle(bundle);


        goal_title.setText(goal.getTitle());
        description.setText(goal.getDescription());
        goal_progress.setText(goal.getProgress());
        txt_expire.setText(goal.getExpirationDate());
        lines_client_count.setText(goal.getClientLinesCountString());

        user = MyUtils.getUserLoggedIn();

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        img_edit = (ImageView) findViewById(R.id.img_edit);
        img_add = (ImageView) findViewById(R.id.img_add);

        img_edit.setOnClickListener(this);
        img_add.setOnClickListener(this);
        back_layout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.img_add:
                MyUtils.startActivity(this, LineAddGoalActivity.class,bundle);
                break;
            case R.id.img_edit:
                MyUtils.startActivity(this, LineAddGoalActivity.class, bundle);
                break;
        }
    }

}
