package com.tied.android.tiedapp.ui.activities.schedule;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.dialogs.ConfirmScheduleActionDialog;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;

import com.tied.android.tiedapp.objects.client.Client;

import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Emmanuel on 6/23/2016.
 */
public class ScheduleDetailsActivitiy extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    public static final String TAG = ScheduleDetailsActivitiy.class
            .getSimpleName();

    GoogleMap myMap;
    private Location mLocation;
    private User user;
    private Client client;
    private Schedule schedule;
    private String schedule_id;
    TextView dayTV, weekTV, timeRange;
    View callClient;
    Bundle bundle;

    private TextView description, temperature, schedule_title, weatherInfo, txt_complete, txt_delete;
    private LinearLayout description_layout;
    View line;

    MapFragment mapFragment;
    ConfirmScheduleActionDialog confirmScheduleActionDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_view);
        bundle = getIntent().getExtras();

        user = MyUtils.getUserFromBundle(bundle);
        try {
            client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
        } catch (Exception e) {

        }
        schedule = (Schedule) bundle.getSerializable(Constants.SCHEDULE_DATA);
        schedule_id = (String) bundle.getSerializable("schedule_id");

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(ScheduleDetailsActivitiy.this);

        schedule_title = (TextView) findViewById(R.id.schedule_title);
        description = (TextView) findViewById(R.id.description);
        temperature = (TextView) findViewById(R.id.weather);

        txt_complete = (TextView) findViewById(R.id.txt_complete);
        txt_delete = (TextView) findViewById(R.id.txt_delete);

        description_layout = (LinearLayout) findViewById(R.id.description_layout);
        line = (View) findViewById(R.id.line);

        dayTV = (TextView) findViewById(R.id.day);
        weekTV = (TextView) findViewById(R.id.week_day);
        timeRange = (TextView) findViewById(R.id.time_range);
        weatherInfo = (TextView) findViewById(R.id.weather_info);

        if (schedule != null) {
            initValue();
        } else if (!schedule_id.isEmpty()){
            getSchedule(schedule_id);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;

        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        final LatLng location = new LatLng(schedule.getLocation().getCoordinate().getLat(), schedule.getLocation().getCoordinate().getLon());
        Marker melbourne = myMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng location2 = new LatLng(location.latitude + 0.002, location.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 15));

        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        melbourne.showInfoWindow();

        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                MyUtils.setCurrentLocation(new Coordinate(33.894212, -84.231574));
                Coordinate coordinate = MyUtils.getCurrentLocation();

                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f&daddr=%f,%f", coordinate.getLat(), coordinate.getLon(), location.latitude, location.longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finishActivity(Constants.ViewSchedule);
                finish();
                break;
            case R.id.img_edit:
                MyUtils.startRequestActivity(this, CreateAppointmentActivity.class, Constants.ViewSchedule, bundle);
                break;
            case R.id.call_client:
                String number = "tel:" + client.getPhone().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.txt_complete:
                confirmScheduleActionDialog = new ConfirmScheduleActionDialog(schedule, client, ScheduleDetailsActivitiy.this, bundle, 1);
                confirmScheduleActionDialog.showDialog();
                break;
            case R.id.txt_delete:
                confirmScheduleActionDialog = new ConfirmScheduleActionDialog(schedule, client, ScheduleDetailsActivitiy.this,bundle, 3);
                confirmScheduleActionDialog.showDialog();
                break;
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        boolean not_first_time_showing_info_window;

        MyInfoWindowAdapter() {
            ContextThemeWrapper cw = new ContextThemeWrapper(
                   getApplicationContext(), R.style.Transparent);

            LayoutInflater inflater = (LayoutInflater) cw
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myContentsView = inflater.inflate(R.layout.schedule_map_info_window, null);
        }

        //// TODO: 9/2/2016  avater should be clients

        @Override
        public View getInfoWindow(Marker marker) {


            TextView line = ((TextView) myContentsView.findViewById(R.id.line));
            TextView address = ((TextView) myContentsView.findViewById(R.id.address));
            TextView distance = ((TextView) myContentsView.findViewById(R.id.distance));
            final ImageView image = ((ImageView) myContentsView.findViewById(R.id.image));

            try {
                line.setText(MyUtils.getClientName(client));
                address.setText(schedule.getLocation().getLocationAddress());
                distance.setText(""+MyUtils.getDistance(MyUtils.getCurrentLocation(), schedule.getLocation().getCoordinate()));

                
                if(client.getLogo()!=null && client.getLogo()!="") MyUtils.Picasso.displayImage(client.getLogo(), image);

            } catch (Exception e) {

            }
            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private void initValue() {
        schedule_title.setText(schedule.getTitle());
        description.setText(schedule.getDescription());
        if (schedule.getDescription() == null || schedule.getDescription().isEmpty()) {
            description.setVisibility(View.GONE);
            description_layout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        long diff_in_date = HelperMethods.getDateDifferenceWithToday(schedule.getDate());

        String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));

        dayTV.setText(MyUtils.toNth(day));

        long epoch = 0;
        Logger.write(schedule.getDate() + " " + schedule.getTime_range().getStart_time());
        if(schedule.getTime_range().getStart_time().equalsIgnoreCase(schedule.getTime_range().getEnd_time())) {
            timeRange.setText(MyUtils.formatTime(schedule.getTime_range().getStart_time()));
        }else {
            timeRange.setText(MyUtils.formatTime(schedule.getTime_range().getStart_time()) + " - " + MyUtils.formatTime(schedule.getTime_range().getEnd_time()));
        }
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(schedule.getDate() + " " + schedule.getTime_range().getStart_time()).getTime();
            System.out.println(epoch);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Logger.write(e);
        }

        weekTV.setText(MyUtils.getWeekDay(schedule).toUpperCase());

        if(schedule.getUser_id().equals(user.getId())) {
            if (schedule.getStatus() != 1) {
                txt_complete.setVisibility(View.VISIBLE);
            }
            txt_delete.setVisibility(View.VISIBLE);
        }

        setTemperature();
    }

    private void setTemperature() {
        final RequestBuilder weather = new RequestBuilder();
        Request request = new Request();
        request.setLat("" + schedule.getLocation().getCoordinate().getLat());
        request.setLng("" + schedule.getLocation().getCoordinate().getLon());
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "temperature : " + weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax() + "");
                int temp_max = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max + "°");
                weatherInfo.setText(weatherResponse.getDaily().getData().get(0).getSummary());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
            }
        });
    }

    private void getSchedule(String schedule_id) {
        final ScheduleApi scheduleApi =  MainApplication.createService(ScheduleApi.class);
        Call<ResponseBody> response = scheduleApi.getScheduleID(schedule_id);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(ScheduleDetailsActivitiy.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        schedule = ( (Schedule) response.getData("schedules", Schedule.class));

                        initValue();
                        getClientObject(schedule.getClient_id());

                    } else {
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                } catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Logger.write(" onFailure", t.toString());
            }
        });
    }

    private void getClientObject(String client_id) {
        final ClientApi clientApi =  MainApplication.createService(ClientApi.class);
        Call<ResponseBody> response = clientApi.getClient(client_id);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(ScheduleDetailsActivitiy.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        client = ( (Client) response.getData("client", Client.class));
                        mapFragment.getMapAsync(ScheduleDetailsActivitiy.this);

                    } else {
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                } catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Logger.write(" onFailure", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ViewSchedule && resultCode == RESULT_OK) {
            schedule = (Schedule) data.getSerializableExtra(Constants.SCHEDULE_DATA);
            initValue();
        }
    }
}
