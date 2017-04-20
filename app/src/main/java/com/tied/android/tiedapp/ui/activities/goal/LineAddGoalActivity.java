package com.tied.android.tiedapp.ui.activities.goal;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;

public class LineAddGoalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineAddGoalActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    ImageView img_close;
    LinearLayout sale_layout, clients_layout, visit_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_add_goal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = new Bundle();

        sale_layout = (LinearLayout) findViewById(R.id.sale_layout);
        sale_layout.setOnClickListener(this);

        clients_layout = (LinearLayout) findViewById(R.id.clients_layout);
        clients_layout.setOnClickListener(this);

        visit_layout = (LinearLayout) findViewById(R.id.visit_layout);
        visit_layout.setOnClickListener(this);

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.sale_layout:
                MyUtils.startActivity(this, GoalMoreSaleActivity.class);
                break;
            case R.id.clients_layout:
                bundle.putInt(Constants.SHOW_MORE_CLIENT, 0);
                MyUtils.startActivity(this, GoalMoreClientActivity.class, bundle);
                break;
            case R.id.visit_layout:
                bundle.putInt(Constants.SHOW_MORE_CLIENT, 1);
                MyUtils.startActivity(this, GoalMoreClientActivity.class, bundle);
                break;

        }
    }
}
