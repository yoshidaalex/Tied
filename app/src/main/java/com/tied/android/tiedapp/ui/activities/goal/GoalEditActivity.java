package com.tied.android.tiedapp.ui.activities.goal;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;

public class GoalEditActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = GoalEditActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    ImageView img_close;
    TextView date, txt_save;
    EditText goal_name, how_much, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getIntent().getExtras();

        date = (TextView) findViewById(R.id.date);
        date.setOnClickListener(this);

        goal_name = (EditText) findViewById(R.id.goal_name);
        how_much = (EditText) findViewById(R.id.how_much);
        note = (EditText) findViewById(R.id.note);

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_save.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.txt_save:

                break;
            case R.id.date:

                break;
        }
    }
}
