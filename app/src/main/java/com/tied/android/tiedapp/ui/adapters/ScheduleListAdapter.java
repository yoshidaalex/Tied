package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.schedule.ScheduleDetailsActivitiy;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;

import java.util.ArrayList;
import java.util.List;

import com.tied.android.tiedapp.util.MyUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleListAdapter extends BaseAdapter{
    public static final String TAG = ScheduleListAdapter.class
            .getSimpleName();

    public List<ScheduleDataModel> _data;
    Activity _c;
    ViewHolder v;
    Bundle bundle;
    private Client client;
    User currentUser;

    public ScheduleListAdapter(List<ScheduleDataModel> schedules, Activity context, Bundle bundle) {
        _data = schedules;
        _c = context;
        this.bundle = bundle;
        if (client != null){
            filterForClient();
        }
        currentUser= MyUtils.getUserLoggedIn();
    }

    public void filterForClient(){
        for (int i = 0; i < _data.size(); i++){
            ArrayList<Schedule> schedules = _data.get(i).getSchedules();
            for (int j = 0; j < schedules.size(); j++){
                if(!schedules.get(j).getClient_id().equals(client.getId())){
                    _data.get(i).getSchedules().remove(j);
                    if (_data.get(i).getSchedules().size() == 0){
                        _data.remove(i);
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void remove(String id){
        for (int i = 0; i < _data.size(); i++){
            ArrayList<Schedule> schedules = _data.get(i).getSchedules();
            for (int j = 0; j < schedules.size(); j++){
                if(schedules.get(j).getId().equals(id)){
                    _data.get(i).getSchedules().remove(j);
                    if (_data.get(i).getSchedules().size() == 0){
                        _data.remove(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
        //ScheduleAppoientsFragment.mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void updateStatus(String id, int status){
        outerloop:
        for (int i = 0; i < _data.size(); i++){
            ArrayList<Schedule> schedules = _data.get(i).getSchedules();
            for (int j = 0; j < schedules.size(); j++){
                if(schedules.get(j).getId().equals(id)){
                    _data.get(i).getSchedules().get(j).setStatus(status);
                    break outerloop;
                }
            }
        }
        notifyDataSetChanged();
       //ScheduleAppointmentsFragment.mViewPager.getAdapter().notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ScheduleDataModel data = (ScheduleDataModel) _data.get(i);
        Log.d(TAG, data.toString());
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.schedule_timeline_list_item, viewGroup, false);

        } else {
            view = convertView;
        }

        v = new ViewHolder();
        v.day = (TextView) view.findViewById(R.id.day);
        v.week_day = (TextView) view.findViewById(R.id.week_day);
        v.temperature = (TextView) view.findViewById(R.id.temperature);
        v.weather = (TextView) view.findViewById(R.id.weather);

        v.day.setText(data.getDay());
        v.week_day.setText(data.getWeek_day().toUpperCase());
        v.temperature.setText(data.getTemperature());
        v.weather.setText(data.getWeather());
        v.timeLine = (LinearLayout) view.findViewById(R.id.timeline);

        v.timeLine.removeAllViews();
        ArrayList<Schedule> schedules = data.getSchedules();
        boolean showDivider = false;
        for (final Schedule schedule : schedules) {
            View schedule_view = LayoutInflater.from(_c).inflate(R.layout.schedule_list_item, null);
            LinearLayout linearLayout = (LinearLayout) schedule_view.findViewById(R.id.schedule);
            TextView time = (TextView) linearLayout.findViewById(R.id.time);
            View divider =linearLayout.findViewById(R.id.divider);
            if(showDivider) divider.setVisibility(View.VISIBLE);
            else divider.setVisibility(View.GONE);
            showDivider=true;
            int color = getStatusColor(schedule.getStatus());
            time.setBackgroundColor(color);
            if(schedule.getUser_id().equals(currentUser.getId())) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putBoolean(Constants.NO_SCHEDULE_FOUND, false);
                        bundle.putString("fragment", ScheduleDetailsActivitiy.class.getName());
                        doAction(schedule,ScheduleDetailsActivitiy.class, bundle);

//                        DialogScheduleEventOptions alert = new DialogScheduleEventOptions();
//                        alert.showDialog(schedule, ScheduleListAdapter.this, _c, bundle);
                    }
                });
            }
            TextView message = (TextView) linearLayout.findViewById(R.id.message);
            String timeRange = getTimeRange(schedule);
            time.setText(timeRange);
            message.setText(schedule.getTitle());
            v.timeLine.addView(linearLayout);

            getWeather(schedules.get(0), v.temperature, v.weather);
        }
        view.setTag(data);
        return view;
    }

    public void doAction(final Schedule schedule,  final Class aClass, Bundle bundle){
        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        User user = gson.fromJson(user_json, User.class);

        Log.d(TAG + "schedule", schedule.toString());

        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientWithId(user.getToken(), schedule.getClient_id());
        response.enqueue(new retrofit2.Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, retrofit2.Response<ClientRes> resResponse) {
                if (_c == null) return;
                Log.d(TAG + "ClientRes", resResponse.toString());
                DialogUtils.closeProgress();
                try {
                    ClientRes ClientRes = resResponse.body();
                    if (ClientRes != null && ClientRes.isAuthFailed()) {
                        User.LogOut(_c);
                    } else if (ClientRes != null && ClientRes.get_meta() != null && ClientRes.get_meta().getStatus_code() == 200) {
                        Client client = ClientRes.getClient();
                        Log.d(TAG + "client", client.toString());
                        Gson gson = new Gson();
                        Intent intent = new Intent(_c, aClass);
                        intent.putExtra(Constants.CLIENT_DATA, client);
                        intent.putExtra(Constants.SCHEDULE_DATA, schedule);
                        _c.startActivityForResult(intent, Constants.ViewSchedule);
                    } else {
                        MyUtils.showConnectionErrorToast(_c);
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(_c);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    static class ViewHolder {
        TextView day, week_day, temperature, weather;
        LinearLayout timeLine;
    }

    public void getWeather(Schedule schedule, final TextView temperature, final TextView cloud){
        final RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat(schedule.getLocation().getCoordinate().getLat()+"");
        request.setLng(schedule.getLocation().getCoordinate().getLon()+"");
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                for(DataPoint dataPoint : weatherResponse.getDaily().getData()){
                    Log.d(TAG, dataPoint.getIcon() +" -on- "+dataPoint.getSummary());
                }
                int temp_max = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                String icon_string = weatherResponse.getDaily().getData().get(0).getIcon();
                Log.d(TAG, "temperature : "+weatherResponse.getDaily().getData().get(0).getTemperature()+"");
                temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);

                temperature.setText(temp_max+"°");
                cloud.setText(icon_string);
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
            }
        });
    }


    public int getStatusColor(int status){
        int color = _c.getResources().getColor(R.color.semi_transparent_black);
//        SampleText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        switch (status){
            case 0:
                color = _c.getResources().getColor(R.color.red_color);
               break;
            case 1:
                color = _c.getResources().getColor(R.color.green_color);
                break;
            case 2:
                color = _c.getResources().getColor(R.color.orange_color);
                break;
        }
        return color;
    }

    public String getTimeRange(Schedule schedule){
        String from = schedule.getTime_range().getStart_time();
        String to = schedule.getTime_range().getEnd_time();

        String range =  MyUtils.formatTime(from) +" - "+ MyUtils.formatTime(to);
        long diff = HelperMethods.getTimeDifference(from,to);
        int abs_difference = Math.abs((int)diff);

        if(abs_difference > 15){
            range = "All Day";
        }else if(abs_difference < 1){
            range =  MyUtils.formatTime(from);
        }
        return range;
    }

    public String getMeridianString(String hour){
        String[] hour_min = hour.split(":");
        int hour_int = Integer.parseInt(hour_min[0]);
        String str = "am";
        if(hour_int > 12){
            str = "pm";
        }
        return str;
    }

    public String getMeridianTime(String hour){
        String[] hour_min = hour.split(":");
        int hour_int = Integer.parseInt(hour_min[0]);

        String new_hour = hour;
        if(hour_int > 12){
            new_hour = String.format("%02d", hour_int - 12) +":"+ hour_min[1];
        }
        return new_hour;
    }
}
