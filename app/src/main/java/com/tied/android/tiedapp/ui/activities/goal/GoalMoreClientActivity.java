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
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;

import org.w3c.dom.Text;

public class GoalMoreClientActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = GoalMoreClientActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    ImageView img_close;
    LinearLayout top_layout, layout_date;
    TextView txt_title, target_label, txt_set_my_goal;
    EditText goal_name, how_much, note;
    int page_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_client);

        bundle = getIntent().getExtras();
        page_index = bundle.getInt(Constants.SHOW_MORE_CLIENT);

        top_layout = (LinearLayout) findViewById(R.id.top_layout);
        txt_title = (TextView) findViewById(R.id.txt_title);
        target_label = (TextView) findViewById(R.id.target_label);

        layout_date = (LinearLayout) findViewById(R.id.layout_date);
        layout_date.setOnClickListener(this);

        txt_set_my_goal = (TextView) findViewById(R.id.txt_set_my_goal);
        txt_set_my_goal.setOnClickListener(this);

        goal_name = (EditText) findViewById(R.id.goal_name);
        how_much = (EditText) findViewById(R.id.how_much);
        note = (EditText) findViewById(R.id.note);

        if (page_index == 0) {
            top_layout.setBackgroundResource(R.drawable.add_more_clients);
            txt_title.setText("Add More Clients");
            target_label.setText("How clients more would you be adding?");
        } else {
            top_layout.setBackgroundResource(R.drawable.make_more_visit);
            txt_title.setText("Make More Visits to Clients");
            target_label.setText("How many visits would you like to make?");
        }

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
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
        }
    }
}
