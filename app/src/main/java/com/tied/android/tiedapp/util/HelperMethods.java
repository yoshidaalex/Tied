package com.tied.android.tiedapp.util;

import android.util.Log;

import com.tied.android.tiedapp.customs.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Emmanuel on 7/3/2016.
 */
public  class HelperMethods {

    protected static final String TAG = HelperMethods.class
            .getSimpleName();

    public static String[] MONTHS_LIST = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String[] WEEK_LIST = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public static String getFormatedDate(String string_date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar c = Calendar.getInstance();
            Date date = sdf.parse(string_date);
            c.setTime(date);

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            String month_name=MyUtils.MONTHS_LIST[month];
            GregorianCalendar gregorianCalendar = getGCalendar(string_date);

            int dayOfWeek=gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
            String dayOfWeekName= WEEK_LIST[dayOfWeek];

            return ""+ dayOfWeekName +", " + month_name + " " + day + ", " + year;

        } catch (ParseException e) {
            return null;
        }
    }

    public static void getWeatherIcon(String icon){
        switch (icon){
            case Constants.CLEAR_DAY:
                break;
            case Constants.CLEAR_NIGHT:
                break;
            case Constants.PARTLY_CLOUDY_DAY:
                break;
            case Constants.PARTLY_CLOUDY_NIGHT:
                break;
            case Constants.RAIN:
                break;
            case Constants.FOG:
                break;
            case Constants.SLEET:
                break;
            case Constants.SNOW:
                break;
            case Constants.WIND:
                break;
        }
    }

    // Converts to celcius
    public static float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    // Converts to fahrenheit
    public static float convertCelciusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
    }

    public static double kilometerToMile(double km) {
        double miles;
        miles = km * 0.621371192;
        return miles;
    }

    public static String getDayOfTheWeek(String string_date) {
        GregorianCalendar gregorianCalendar = getGCalendar(string_date);
        int dayOfWeek = gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
        String dayOfWeekName = WEEK_LIST[dayOfWeek];
        return dayOfWeekName;
    }

    public static GregorianCalendar getGCalendar(String string_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Calendar c = Calendar.getInstance();
            Date date = sdf.parse(string_date);
            c.setTime(date);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day - 1);
            return gregorianCalendar;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getMonthOfTheYear(String string_date) {
        String montOfyearName="";
        //String dtStart = "2010-10-15";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(string_date);
            System.out.println(date);
            montOfyearName=getMonthOfTheYear(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block

        }
        return montOfyearName;
    }
    public static String getMonthOfTheYear(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int montOfyear = calendar.get(calendar.MONTH);
       // Logger.write("month of the yeah in "+string_date+" is "+montOfyear);
        String montOfyearName = MyUtils.MONTHS_LIST[montOfyear];
        return montOfyearName;
    }
    public static int getNumericMonthOfTheYear(String string_date) {
        GregorianCalendar gregorianCalendar = getGCalendar(string_date);
        return gregorianCalendar.get(gregorianCalendar.MONTH)+1;

    }
    public static int getCurrentYear(String string_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int year=2016;
        try {
            Date date = format.parse(string_date);
            System.out.println(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            year = calendar.get(calendar.YEAR);
            // Logger.write("month of the yeah in "+string_date+" is "+montOfyear);
          //  String montOfyearName = MONTHS_LIST[montOfyear];
        } catch (ParseException e) {
            // TODO Auto-generated catch block

        }



        return year;
    }

    public static int getDayFromSchedule(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.DAY_OF_MONTH);
            return month;
        } catch (ParseException e) {
            return 0;
        }
    }



    public static long getDateDifference(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date s_date = sdf.parse(startDate);
            Date e_date = sdf.parse(endDate);
            //milliseconds
            long different = s_date.getTime() - e_date.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            return elapsedDays;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getTimeDifference(String startDate, String endDate) {
        try {
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String today = sdf.format(now);

            String date1 = today +" "+startDate+":00";
            String date2 = today +" "+endDate+":00";

            SimpleDateFormat this_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date s_date = this_sdf.parse(date1);
            Date e_date = this_sdf.parse(date2);

            long mills = e_date.getTime() - s_date.getTime();

            long Hours = mills / (1000 * 60 * 60);
            long Mins = mills % (1000 * 60 * 60);

            Log.d(TAG, "difference in time between " + date1 + " and " + date2 + " is "+Hours);

            return Hours;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "ParseException HERE");
            return 0;
        }
    }

    public static long getDateDifferenceWithToday(String date) {
        return getDateDifference(date, getTodayDate());
    }

    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(now);
        return today;
    }
}