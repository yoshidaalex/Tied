package com.tied.android.tiedapp.ui.fragments.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.customs.model.ScheduleNotifyModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.activities.schedule.ScheduleDetailsActivitiy;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.ScheduleNotifyDialog;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.imanoweb.calendarview.CustomCalendarView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateAppointmentFragment extends Fragment implements View.OnClickListener,
        TimeRangePickerDialog.OnTimeRangeSelectedListener, ScheduleNotifyDialog.SelectedListener {

    public static final String TAG = CreateAppointmentFragment.class
            .getSimpleName();

    TextView txt_title, txt_description, txt_date, txt_time, txt_client_name, txt_client_company, txt_reminder, txt_date_selected;
    ImageView img_avatar, img_plus_date, img_plus1, img_location, img_reminder, img_close;
    private TextView locationTV;// street, city, zip, state;

    private Bundle bundle;
    private User user;

    private Schedule schedule;
    private Client client;
    private Location location;
    int notify_id = 1;
    private ScheduleNotifyModel scheduleNotifyModel;

    private String endTimeText, startTimeText, dateText, titleText, streetText, cityText, stateText, zipText;
    private TextView title, txt_create_schedule;
    RelativeLayout layout_date, layout_time, layout_reminder;

    ScheduleNotifyDialog alert;

    private FragmentIterationListener fragmentIterationListener;

    boolean isSelectClient = true;
    ImageView img_right_arrow;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new CreateAppointmentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public CreateAppointmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.gainFocus).requestFocus();
        view.setFocusableInTouchMode(true);

        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            fragmentIterationListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentIterationListener != null) {
            fragmentIterationListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void initComponent(View view) {

        layout_date = (RelativeLayout) view.findViewById(R.id.layout_date);
        layout_time = (RelativeLayout) view.findViewById(R.id.layout_time);
        layout_reminder = (RelativeLayout) view.findViewById(R.id.layout_reminder);

        layout_date.setOnClickListener(this);
        layout_time.setOnClickListener(this);
        layout_reminder.setOnClickListener(this);

        title = (TextView) view.findViewById(R.id.title);

        txt_create_schedule = (TextView) view.findViewById(R.id.txt_create_schedule);
        txt_create_schedule.setOnClickListener(this);

        txt_client_name = (TextView) view.findViewById(R.id.client_name);
        txt_client_company = (TextView) view.findViewById(R.id.client_company);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_description = (TextView) view.findViewById(R.id.txt_description);
        txt_date = (TextView) view.findViewById(R.id.date);
        txt_date_selected = (TextView) view.findViewById(R.id.date_selected);
        txt_time = (TextView) view.findViewById(R.id.time);
        txt_reminder = (TextView) view.findViewById(R.id.reminder);
        locationTV=(TextView) view.findViewById(R.id.txt_location);
        img_right_arrow = (ImageView) view.findViewById(R.id.img_right_arrow);

        view.findViewById(R.id.client_layout).setOnClickListener(this);

        /*street = (EditText) view.findViewById(R.id.street);
        state = (EditText) view.findViewById(R.id.state);
        city = (EditText) view.findViewById(R.id.city);
        zip = (EditText) view.findViewById(R.id.zip);*/

        img_close = (ImageView) view.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        img_avatar = (ImageView) view.findViewById(R.id.img_avatar_schedule);

        img_plus_date = (ImageView) view.findViewById(R.id.img_plus_date);
        img_plus_date.setOnClickListener(this);


        img_plus1 = (ImageView) view.findViewById(R.id.img_plus1);
        img_plus1.setOnClickListener(this);

        img_reminder = (ImageView) view.findViewById(R.id.img_reminder);
        img_reminder.setOnClickListener(this);

        view.findViewById(R.id.layout_location).setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            user = gson.fromJson(user_json, User.class);
            client = gson.fromJson(client_json, Client.class);
            isSelectClient = bundle.getBoolean("select_client", true);
//            txt_creative_co_op.setText(client.getCompany());

            if (!isSelectClient) img_right_arrow.setVisibility(View.GONE);

            String schedule_json = bundle.getString(Constants.SCHEDULE_DATA);
            Log.d(TAG, "schedule_json "+schedule_json);
            if(schedule_json != null){
                schedule = gson.fromJson(schedule_json, Schedule.class);
                txt_title.setText(schedule.getTitle());
               // street.setText(schedule.getLocation().getStreet());
               // city.setText(schedule.getLocation().getCity());
               // zip.setText(schedule.getLocation().getZip());
               // state.setText(schedule.getLocation().getState());
                location=schedule.getLocation();
                txt_time.setText(schedule.getTime_range().getRange());
                txt_date_selected.setText(schedule.getDate());
                txt_date.setText(HelperMethods.getFormatedDate(schedule.getDate()));
                txt_create_schedule.setText("UPDATE SCHEDULE");
                title.setText("Update Appointment");
                txt_description.setText(schedule.getDescription());
            }else{
               /* street.setText(client.getAddress().getStreet());
                city.setText(client.getAddress().getCity());
                zip.setText(client.getAddress().getZip());
                state.setText(client.getAddress().getState());*/
                title.setText("Create Appointment");
                if(client!=null) location=client.getAddress();

            }
            if(location!=null) locationTV.setText(location.getLocationAddress());

            if(client!=null) {
                txt_client_name.setText(client.getFull_name());
                txt_client_company.setText(client.getCompany());
                String logo = client.getLogo().equals("") ? null : client.getLogo();
                MyUtils.Picasso.displayImage(logo, img_avatar);
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            TimeRangePickerDialog tpd = (TimeRangePickerDialog) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(TAG);
            if (tpd != null) {
                tpd.setOnTimeRangeSetListener(this);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.img_close:
                /*intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);*/
                getActivity().onBackPressed();
                break;
            case R.id.img_plus_date:
//                nextAction(Constants.AppointmentCalendar, bundle);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.img_plus1:
                TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        CreateAppointmentFragment.this, false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TAG);
                break;
            case R.id.img_reminder:
                ScheduleNotifyDialog alert = new ScheduleNotifyDialog();
                alert.showDialog(this);
                break;
            case R.id.layout_date:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.layout_time:
                TimeRangePickerDialog timePickerDialog2 = TimeRangePickerDialog.newInstance(
                        CreateAppointmentFragment.this, false);
                timePickerDialog2.show(getActivity().getSupportFragmentManager(), TAG);
                break;
            case R.id.layout_reminder:
                ScheduleNotifyDialog alert2 = new ScheduleNotifyDialog();
                alert2.showDialog(this);
                break;
            case R.id.txt_create_schedule:
                if (validated()) {
                    if(schedule == null){
                        createAppointment();
                    }else {
                        updateAppointment();
                    }
//                    new GeocodeAsyncTask().execute();
                }
                break;
            case R.id.client_layout:
                if (isSelectClient) {
                    MyUtils.initiateClientSelector(getActivity(), null, false);
                }
                break;
            case R.id.layout_location:
                MyUtils.showAddressDialog(getActivity(), "Appointment Location", location, new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        if(response!=null) {
                            location=(Location)response;
                            locationTV.setText(location.getLocationAddress());
                        }else{
                            locationTV.setText("Click to enter location");
                        }
                    }
                });
                break;
        }
    }

    public String timeFormat(String time){
        String[] split_hour_min = time.split(":");
        String hour = String.format("%02d", Integer.parseInt(split_hour_min[0]));
        String min = String.format("%02d", Integer.parseInt(split_hour_min[1]));
        return hour+":"+min;
    }

    public boolean validated() {
        dateText = txt_date_selected.getText().toString();
        titleText = txt_title.getText().toString();
       // streetText = street.getText().toString();
      //  cityText = city.getText().toString();
       // zipText = zip.getText().toString();
        //stateText = state.getText().toString();
        if(client==null) {
            MyUtils.showErrorAlert(getActivity(), "You must select a client");
            return false;
        }
        if(titleText.trim().isEmpty()) {
            MyUtils.showErrorAlert(getActivity(), "Schedule title is required");
            return false;
        }
        if(dateText.trim().isEmpty()) {
            MyUtils.showErrorAlert(getActivity(), "Schedule date is required");
            return false;
        }
        if(location==null) {
            MyUtils.showErrorAlert(getActivity(), "Schedule location is required");
            return false;
        }


        //location = new Location(cityText, zipText, stateText, streetText);


        String range = txt_time.getText().toString();
        String[] time = range.split("-");
        if(time.length == 2){
            String from = time[0].replace(" ", "");
            String to = time[1].replace(" ", "");
            startTimeText = timeFormat(from);
            endTimeText = timeFormat(to);
        }
        if(startTimeText==null || startTimeText.isEmpty() || endTimeText==null || endTimeText.isEmpty()) {
            MyUtils.showErrorAlert(getActivity(), "You must enter the time for the schedule");
            return false;
        }

        return dateText != null && endTimeText!= null && startTimeText != null;
    }

    @Override
    public void selectedNow(ScheduleNotifyModel scheduleNotifyModel) {
        String text_notify = "Notify me " + scheduleNotifyModel.getTxt_notify() + " before time";
        txt_reminder.setText(text_notify);
        notify_id = scheduleNotifyModel.getId();
    }

    class GeocodeAsyncTask extends MyAddressAsyncTask {

        String errorMessage = "";
        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected Address doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            try {
                Log.d(TAG, location.getLocationAddress());
                addresses = geocoder.getFromLocationName(location.getLocationAddress(), 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if (getActivity() == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
                if(schedule == null){
                    createAppointment();
                }else {
                    updateAppointment();
                }
            } else {
                DialogUtils.closeProgress();
                Toast.makeText(getActivity(), "sorry location cannot be found in map", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createAppointment() {
        final Schedule schedule = new Schedule();
        schedule.setTitle(titleText);
        if(client!=null)    schedule.setClient_id(client.getId());
        schedule.setUser_id(MyUtils.getUserLoggedIn().getId());
        schedule.setVisited(false);
        schedule.setReminder(notify_id);
        TimeRange timeRange = new TimeRange(startTimeText, endTimeText);
        schedule.setTime_range(timeRange);
        schedule.setEnd_time(endTimeText);
        schedule.setDate(dateText);
        schedule.setLocation(location);
        schedule.setDescription(txt_description.getText().toString());

        Log.d(TAG + " schedule", schedule.toString());

        DialogUtils.displayProgress(getActivity());
        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.createSchedule(user.getToken(), schedule);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> scheduleResResponse) {
                if (getActivity() == null) return;
                try {
                    DialogUtils.closeProgress();

                    ScheduleRes scheduleRes = scheduleResResponse.body();
                    Log.d(TAG + " onFailure", scheduleRes.toString());
                    if (scheduleRes.isAuthFailed()) {
                        User.LogOut(getActivity());
                    } else if (scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 201) {
//                        if (isSelectClient) {
//                            Log.d(TAG + " Schedule", scheduleRes.getSchedule().toString());
//                            Gson gson = new Gson();
//                            Schedule mainSchedule = scheduleRes.getSchedule();
//                            bundle.putSerializable(Constants.SCHEDULE_DATA, mainSchedule);
//                            if (client != null)
//                                bundle.putSerializable(Constants.CLIENT_DATA, client);
//                            Schedule.scheduleCreated(getActivity().getApplicationContext());
//                            bundle.putBoolean(Constants.NO_SCHEDULE_FOUND, false);
//                            DialogUtils.closeProgress();
//                            //nextAction(Constants.ActivitySchedule, bundle);
//                            MyUtils.startActivity(getActivity(), ScheduleDetailsActivitiy.class, bundle);
//                        } else {
                            /*Intent intent = new Intent();
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finishActivity(Constants.ScheduleDetailsActivitiy);*/
                        MyUtils.showMessageAlert(getActivity(), "Schedule Created!");
                        txt_date.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bundle.putSerializable(Constants.SCHEDULE_DATA, schedule);
                                bundle.putSerializable(Constants.CLIENT_DATA, client);
                                MyUtils.startActivity(getActivity(), ScheduleDetailsActivitiy.class, bundle);
                            }
                        }, 1000);

//                        }
                        getActivity().finish();
                    } else {
                        DialogUtils.closeProgress();
                        nextAction(Constants.CreateSchedule, bundle);
                        MyUtils.showToast(scheduleRes.getMessage());
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(getActivity());
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> ScheduleResponseCall, Throwable t) {
               // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showConnectionErrorToast(getActivity());
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public void updateAppointment() {
        schedule.setTitle(titleText);
       if(client!=null) schedule.setClient_id(client.getId());
        //schedule.setUser_id(user.getId());
        schedule.setVisited(false);
        schedule.setReminder(notify_id);
        TimeRange timeRange = new TimeRange(startTimeText, endTimeText);
        schedule.setTime_range(timeRange);
        schedule.setEnd_time(endTimeText);
        schedule.setDate(dateText);
        schedule.setLocation(location);
        schedule.setDescription(txt_description.getText().toString());

        Log.d(TAG + " schedule", schedule.toString());

        DialogUtils.displayProgress(getActivity());
        final ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.updateSchedule(user.getToken(), schedule.getId(), schedule);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> scheduleResResponse) {
                if (getActivity() == null) return;
                try {
                    ScheduleRes scheduleRes = scheduleResResponse.body();
                    if (scheduleRes != null && scheduleRes.isAuthFailed()) {
                        DialogUtils.closeProgress();
                        User.LogOut(getActivity());
                    } else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                        Log.d(TAG + " Schedule", scheduleRes.getSchedule().toString());
                        Gson gson = new Gson();
                        Schedule updatedSchedule = scheduleRes.getSchedule();
                        if (updatedSchedule.getId().equals(schedule.getId())) {
                           // String schedule_string = gson.toJson(schedule, Schedule.class);
//                            bundle.putSerializable(Constants.SCHEDULE_DATA, schedule);
//                            bundle.putBoolean(Constants.NO_SCHEDULE_FOUND, false);
//                            bundle.putBoolean(Constants.SCHEDULE_EDITED, true);
//                            bundle.putSerializable(Constants.CLIENT_DATA, client);

                           // MyUtils.startActivity(getActivity(), MainActivity.class, bundle);

//                            getActivity().finish();
//                        Schedule.scheduleCreated(getActivity().getApplicationContext());
//                        DialogUtils.closeProgress();
//                        nextAction(Constants.ScheduleSuggestions, bundle);

                            Intent intent = new Intent();
                            intent.putExtra(Constants.SCHEDULE_DATA, schedule);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finishActivity(Constants.ViewSchedule);
                            getActivity().finish();
                        }
                    } else {
                        //Toast.makeText(getActivity(), scheduleRes.toString(), Toast.LENGTH_LONG).show();
                        MyUtils.showErrorAlert(getActivity(), scheduleRes.getMessage());
                        DialogUtils.closeProgress();
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(getActivity());
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> ScheduleResponseCall, Throwable t) {
               // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showConnectionErrorToast(getActivity());
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        startTimeText = startHour + ":" +String.format("%02d", startMin);
        endTimeText = endHour + ":" + String.format("%02d", endMin);
        txt_time.setText(startTimeText + " - " + endTimeText);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_CLIENT && resultCode == Activity.RESULT_OK) {
            //location=(
            if(data.getSerializableExtra("selected")!=null) {

                client = (Client) data.getSerializableExtra("selected");
                String logo = client.getLogo().equals("") ? null : client.getLogo();
                MyUtils.Picasso.displayImage(logo, img_avatar);
                txt_client_name.setText(MyUtils.getClientName(client));
                txt_client_company.setText(client.getCompany());
                if(location==null) {
                    location=client.getAddress();
                    locationTV.setText(location.getLocationAddress());
                }


                Logger.write(client.toString());
            }else{
                client=null;
                img_avatar.setImageResource(R.drawable.client_photo);
                txt_client_name.setText("");
            }
        }
    }
}