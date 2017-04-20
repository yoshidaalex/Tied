package com.tied.android.tiedapp.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ZuumaPC on 7/14/2016.
 */
public class ClientDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    String[] MONTHS_LIST = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String name_month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String month_name = MONTHS_LIST[view.getMonth()];

       // TextView tv1 = (TextView) getActivity().findViewById(R.id.birthday);
        //tv1.setText("" + view.getDayOfMonth() + " " + month_name + " " + view.getYear());

    }
}
