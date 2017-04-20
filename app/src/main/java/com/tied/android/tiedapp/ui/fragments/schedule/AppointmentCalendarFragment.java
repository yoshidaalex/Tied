package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class AppointmentCalendarFragment extends Fragment implements View.OnClickListener , CalendarView.OnDateChangeListener{

    public static final String TAG = AppointmentCalendarFragment.class
            .getSimpleName();

    String[] WEEK_LIST = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

    private Bundle bundle;
    private CalendarView calendarView;

    private TextView temperature, date, time_of_day, txt_cancel, txt_select;

    private FragmentIterationListener fragmentIterationListener;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new AppointmentCalendarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public AppointmentCalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_choose_date, container, false);
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
        txt_select = (TextView) view.findViewById(R.id.txt_select);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_select.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        temperature = (TextView) view.findViewById(R.id.weather);
        time_of_day = (TextView) view.findViewById(R.id.time_of_day);
        time_of_day = (TextView) view.findViewById(R.id.time_of_day);
        date = (TextView) view.findViewById(R.id.date);

        Calendar cal = Calendar.getInstance();

        time_of_day.setText(getTimeOfTheDay());
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        final String date_string = format.format(Date.parse(cal.getTime().toString()));
        date.setText(date_string);

        final RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat("32.00");
        request.setLng("-81.00");
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "temperature : "+weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax()+"");
                int temp_max = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                temp_max = (int) convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max+"°");
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
            }
        });

        bundle = getArguments();
    }

    // Converts to celcius
    private float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    // Converts to fahrenheit
    private float convertCelciusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
    }


    private String getTimeOfTheDay() {
        String time_of_the_day = "";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            time_of_the_day = "Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            time_of_the_day = "Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            time_of_the_day = "Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            time_of_the_day = "Night";
        }
        return time_of_the_day;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_select:
                    nextAction(Constants.CreateAppointment, bundle);
                break;
            case R.id.txt_cancel:
                    nextAction(Constants.CreateAppointment, bundle);
                break;
        }
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        // Do something with the date chosen by the user

        String month_name= MyUtils.MONTHS_LIST[month];
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, dayOfMonth-1);

        int dayOfWeek=gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
        String dayOfWeekName= WEEK_LIST[dayOfWeek];
        String selected = year +"-"+String.format("%02d", month)+"-"+String.format("%02d", dayOfMonth);
        String show_selected = ""+ dayOfWeekName +" " + month_name + " " + dayOfMonth + ", " + year;
        bundle.putString(Constants.SELECTED_DATE, selected);
        bundle.putString(Constants.SHOW_SELECTED_DATE, show_selected);
        Toast.makeText(getActivity(),selected, Toast.LENGTH_LONG).show();
    }
}