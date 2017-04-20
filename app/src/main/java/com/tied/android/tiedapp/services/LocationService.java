package com.tied.android.tiedapp.services;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by femi on 8/10/2016.
 */
public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;
    ArrayList<String> foundClients=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 4, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 4, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
       // Log.v("STOP_SERVICE", "DONE");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }



    private long lastChecked=new Date().getTime();
    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location loc)
        {

            if(isBetterLocation(loc, previousBestLocation)) {
                /*loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);*/
                Coordinate co=new Coordinate(loc.getLatitude(), loc.getLongitude());

                Log.i("CURRENT LOCATION", co.toString());
                MyUtils.setCurrentLocation(co);
                if(new Date().getTime()-lastChecked>(0.1*60*1000)) {
                    User user=MyUtils.getUserLoggedIn();
                    if(user.getNotification().getProximity().getPush()) {//if he wants proximity checks
                        doProximityCheck(user, co);
                    }
                    lastChecked=new Date().getTime();
                }

            }
        }

        private void doProximityCheck(User user, final Coordinate coordinate) {
            ClientFilter clientFilter=new ClientFilter();
            clientFilter.setCoordinate(coordinate);
            clientFilter.setPage_size(2);
            clientFilter.setOrder("asc");
            clientFilter.setOrder_by("distance");
            clientFilter.setDistance(MyUtils.getSharedPreferences().getInt(Constants.PROXIMITY_REMINDER_DISTANCE, 400));
            clientFilter.setUnit("mi");
            Logger.write("*********************"+clientFilter.getDistance()+"");
            ClientApi clientApi = MainApplication.createService(ClientApi.class);
            Call<ClientRes> response = clientApi.getClientsFilter(user.getId(), 1, clientFilter);

            response.enqueue(new Callback<ClientRes>() {
                @Override
                public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                    if (getApplicationContext()== null ) {
                        // Logger.write("null activity");
                        return;
                    }
                    //Logger.write("(((((((((((((((((((((((((((((999999");
                    DialogUtils.closeProgress();
                    ClientRes clientRes = resResponse.body();
                    Logger.write(clientRes.toString());
                    try {
                        if (clientRes.isAuthFailed()) {
                            User.LogOut(getApplicationContext());
                        } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                            Client client =clientRes.getClients().get(0);

                                createNotification(client, coordinate);

                        } else {
                            Logger.write("Error onResponse", clientRes.getMessage());
                        }
                    }catch (Exception e) {
                        Logger.write(e);
                    }
                }

                @Override
                public void onFailure(Call<ClientRes> call, Throwable t) {
                    Logger.write(" onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }
        static final int mId=9230923;
        public void onProviderDisabled(String provider)
        {
            //Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }

        private void loadFoundClients() {
            SharedPreferences sp=MyUtils.getSharedPreferences();
            try {
                JSONArray ja = new JSONArray(
                        sp.getString(Constants.NEARBY_CLIENTS, new JSONArray().toString())
                );

                for (int i=0; i<ja.length(); i++) {
                    foundClients.add(ja.getString(i));
                }
            }catch (JSONException je) {
                foundClients=new ArrayList<>();
            }
        }

        private final String PROXIMITY_IS_TODAYS_DATE="proximity_is_newday";
        String savedDate=null;

        private boolean isNewDay() {
            if(savedDate==null) {
                SharedPreferences sp = MyUtils.getSharedPreferences();
                sp.getString(PROXIMITY_IS_TODAYS_DATE, "");
            }

            boolean isNew= !(HelperMethods.getTodayDate().equalsIgnoreCase(savedDate)) ;
            if(isNew) {
                SharedPreferences sp = MyUtils.getSharedPreferences();
                SharedPreferences.Editor e=sp.edit();
                e.putString(PROXIMITY_IS_TODAYS_DATE,  HelperMethods.getTodayDate());
                e.apply();
            }
            return isNew;
        }
        private void saveFoundClients() {
            JSONArray ja=new JSONArray();
            for (int j=0; j<foundClients.size(); j++) {
                ja.put(foundClients.get(j));
            }

            SharedPreferences sp=MyUtils.getSharedPreferences();
            SharedPreferences.Editor e=sp.edit();
            e.putString(Constants.NEARBY_CLIENTS, ja.toString());
            e.apply();
        }

        void createNotification(Client client, Coordinate coordinate) {
            if(foundClients==null || foundClients.size()==0) loadFoundClients();
            if(foundClients.contains(client.getId())) return;
            if(foundClients.size()<5) {
                foundClients.add(client.getId());
            }else  {
               foundClients.remove(0);
                foundClients.add(client.getId());
            }

            saveFoundClients();

            Logger.write(foundClients.toString());
            String clientName=MyUtils.getClientName(client);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                            .setContentTitle("Visit "+clientName)
                            .setContentText("You are only "+MyUtils.getDistance(client.getAddress().getCoordinate(), coordinate, true)+" away.");
            // Creates an explicit intent for an Activity in your app
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.PROXIMITY_CLIENT_DATA, client);
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            resultIntent.putExtras(bundle);
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            //Vibration
           // mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //LED
            //mBuilder.setLights(Color.BLUE, 3000, 3000);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
           // Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
           // mBuilder.setSound(alarmSound);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(mId, mBuilder.build());
        }


        public void onProviderEnabled(String provider)
        {
           // Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}