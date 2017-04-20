package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.GoalApi;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.GregorianCalendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@SuppressWarnings("ValidFragment")
public class LineAddGoalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineAddGoalActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;
    private EditText goal_name, how_much, note;
    private TextView end_date, date_selected, title, targetLabel;
    private Client client;

    
    private String name, set_goal, dateText, noteText;

    LinearLayout back_layout, layout_date, ok_but;
    private Goal goal;
    private Line line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_add_goal);
        bundle = getIntent().getExtras();
        goal = (Goal) bundle.getSerializable(Constants.GOAL_DATA);
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        client = (Client)bundle.getSerializable(Constants.CLIENT_DATA);

        title = (TextView) findViewById(R.id.title);
        goal_name = (EditText) findViewById(R.id.goal_name);
        how_much = (EditText) findViewById(R.id.how_much);
        end_date = (TextView) findViewById(R.id.date);
        date_selected = (TextView) findViewById(R.id.date);
        note = (EditText) findViewById(R.id.note);
        targetLabel = (TextView)findViewById(R.id.target_label);
        if(client!=null) targetLabel.equals("HOW MANY CLIENTS YOU INTEND TO VISIT");



        if (goal != null){
            goal_name.setText(goal.getTitle());
            how_much.setText(goal.getValue());
            end_date.setText(HelperMethods.getFormatedDate(goal.getDate()));
            date_selected.setText(goal.getDate());
            note.setText(goal.getDescription());
            title.setText("Edit Goal");
        }
        else{
            goal = new Goal();
        }

        user = MyUtils.getUserFromBundle(bundle);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        layout_date = (LinearLayout) findViewById(R.id.layout_date);
        ok_but = (LinearLayout) findViewById(R.id.ok_but);

        layout_date.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        ok_but.setOnClickListener(this);
    }
    
    public boolean validate(){
        name = goal_name.getText().toString().trim();
        set_goal = how_much.getText().toString().trim();
        //dateText = date_selected.getText().toString().trim();
        noteText = note.getText().toString().trim();

        if(name.isEmpty()) {
            MyUtils.showErrorAlert(this, "You must provide a Goal title");
            return false;
        }
        if(set_goal.isEmpty()) {
            MyUtils.showErrorAlert(this, "You must set a goal target");
            return false;
        }
        if(dateText.isEmpty()) {
            MyUtils.showErrorAlert(this,  "You must provide end date");
            return false;
        }

        //if(noteText.isEmpty()) {
         //   MyUtils.showErrorAlert(this,  "You must provide end date");
         //   return false;
       // }

        goal.setTitle(name);
        if(line!=null) {
            goal.setItem_id(line.getId());
            goal.setGoal_type(Goal.SALES_TYPE);
        }
        else if(client!=null) {
            goal.setItem_id(client.getId());
            goal.setGoal_type(Goal.VISIT_TYPE);
        }else{
            goal.setGoal_type(Goal.CLIENT_TYPE);
        }
        goal.setTarget(set_goal);
        goal.setDescription(noteText);
        goal.setDate(dateText+" 23:59:59");
        goal.setUser_id(user.getId());

        return true;

    }

    public void createGoal(){
        if(!validate()) return;

        GoalApi goalApi = MainApplication.createService(GoalApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = goalApi.createGoal(goal);
        Logger.write(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(LineAddGoalActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {
                       // DialogUtils.closeProgress();
                        final Goal the_line = response.getData(Constants.GOAL_DATA, Goal.class);
                        MyUtils.showMessageAlert(LineAddGoalActivity.this, "Goal added successfully!");
                        date_selected.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Logger.write("the_line: "+the_line.toString());
                                DialogUtils.closeProgress();
                                bundle.putSerializable(Constants.GOAL_DATA, the_line);
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                finishActivity(Constants.GOAL_REQUEST);
                                finish();

                            }
                        }, 1200);

                        //MainApplication.goals.clear();
                       // MyUtils.startActivity(LineAddGoalActivity.this, LineGoalActivity.class, bundle);
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }
                catch (Exception jo) {
                    DialogUtils.closeProgress();
                    MyUtils.showToast("An error was encountered. Please try again");

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getMessage());
                MyUtils.showConnectionErrorToast(LineAddGoalActivity.this);
                DialogUtils.closeProgress();
            }
        });
        
    }

    private void updateGoal(final Goal goal) {

        if(!validate()) return;

        GoalApi goalApi = MainApplication.createService(GoalApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = goalApi.updateGoal(goal.getId(), goal);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(LineAddGoalActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        Goal the_goal = response.getData(Constants.GOAL_DATA, Goal.class);
                        if(the_goal.getId().equals(goal.getId())){
                            Logger.write("Update goal id +"+the_goal.getId());
                            bundle.putSerializable(Constants.GOAL_DATA, the_goal);
                            MyUtils.startActivity(LineAddGoalActivity.this, LineViewGoalActivity.class, bundle);
                        }
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.layout_date:

                DialogFragment dateFragment = new DatePickerFragment() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // Do something with the date chosen by the user
                        String month_name = MyUtils.MONTHS_LIST[view.getMonth()];
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(view.getYear(), view.getMonth(), view.getDayOfMonth() - 1);

                        int dayOfWeek = gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
                        String dayOfWeekName = WEEK_LIST[dayOfWeek];

                        date_selected.setText("" + dayOfWeekName + " " + month_name + " " + view.getDayOfMonth() + ", " + view.getYear());

                        dateText = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);

                    }
                };
                Bundle bd=new Bundle();
                bd.putString("date", dateText);
                dateFragment.setArguments(bundle);
                dateFragment.show(this.getSupportFragmentManager(), "datePicker");
                break;
            case  R.id.ok_but:
                if (goal.getId() == null){
                    createGoal();
                }else {
                    updateGoal(goal);
                }
                break;
        }
    }
}
