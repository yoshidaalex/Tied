package com.tied.android.tiedapp.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DailyStatsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = DailyStatsFragment.class
            .getSimpleName();

    private Bundle bundle;

    private ImageView img_user_picture;

    private TextView btn_got, date, greeting, temperature;
    private User user;

    private FragmentIterationListener fragmentIterationListener;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new DailyStatsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_landing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        bundle = getArguments();

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        temperature = (TextView) view.findViewById(R.id.weather);
        btn_got = (TextView) view.findViewById(R.id.btn_got);
        greeting = (TextView) view.findViewById(R.id.greeting);
        date = (TextView) view.findViewById(R.id.date);

        btn_got.setOnClickListener(this);
        date.setOnClickListener(this);
        img_user_picture.setOnClickListener(this);

        Gson gson = new Gson();
       // String user_json = MyUtils.getUserLoggedIn();
        user = MyUtils.getUserLoggedIn();
        Logger.write(user.toString());

        MyUtils.Picasso.displayImage(user.getAvatar(), img_user_picture);

        Calendar cal = Calendar.getInstance();

        String greet = getTimeOfTheDay() + ", " + (""+user.getFirst_name().charAt(0)).toUpperCase()+user.getFirst_name().substring(1);
        greeting.setText(greet);

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        final String date_string = format.format(Date.parse(cal.getTime().toString()));
        date.setText(date_string);

        final RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat(""+MyUtils.getCurrentLocation().getLat());
        request.setLng(""+MyUtils.getCurrentLocation().getLon());
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
                int temp = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                String icon = weatherResponse.getDaily().getData().get(0).getIcon();

                Log.d(TAG, "temperature : "+weatherResponse.getDaily().getData().get(0).getTemperature()+"");
                temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max+"°");
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
            }
        });
    }

    private String getTimeOfTheDay() {
        String time_of_the_day = "";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            time_of_the_day = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            time_of_the_day = "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            time_of_the_day = "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            time_of_the_day = "Good Night";
        }
        return time_of_the_day;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_got:
              //  nextAction(Constants.AppointmentList, bundle);
                getActivity().finish();
                break;
        }
    }



}