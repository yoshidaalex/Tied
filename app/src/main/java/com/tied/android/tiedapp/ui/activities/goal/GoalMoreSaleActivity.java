package com.tied.android.tiedapp.ui.activities.goal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.lines.LineAddClientActivity;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.util.MyUtils;

public class GoalMoreSaleActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = GoalMoreSaleActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    ImageView img_close;
    RelativeLayout line_layout, client_layout;
    TextView txt_set_my_goal;
    EditText goal_name, how_much, note;
    LinearLayout layout_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_sale);
        bundle = new Bundle();

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        layout_date = (LinearLayout) findViewById(R.id.layout_date);
        layout_date.setOnClickListener(this);

        txt_set_my_goal = (TextView) findViewById(R.id.txt_set_my_goal);
        txt_set_my_goal.setOnClickListener(this);

        line_layout = (RelativeLayout) findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

        client_layout = (RelativeLayout) findViewById(R.id.client_layout);
        client_layout.setOnClickListener(this);

        goal_name = (EditText) findViewById(R.id.goal_name);
        how_much = (EditText) findViewById(R.id.how_much);
        note = (EditText) findViewById(R.id.note);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.txt_set_my_goal:
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;
            case R.id.layout_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(this.getSupportFragmentManager(), "datePicker");
                break;
            case R.id.line_layout:
               // MyUtils.startActivity(this, GoalAddLineActivity.class);
                break;
            case R.id.client_layout:
                bundle.putInt(Constants.SHOW_CLIENT, 1);
                MyUtils.startActivity(this, LineAddClientActivity.class, bundle);
                break;
        }
    }
}
