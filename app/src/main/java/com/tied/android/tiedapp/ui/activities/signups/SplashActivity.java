package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.ConfigApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.ActivityMaintenance;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by yuweichen on 16/4/30.
 */
public class SplashActivity extends Activity {

    public static final String TAG = SplashActivity.class
            .getSimpleName();

    Context context;
    protected boolean _active = true;
    protected int _splashTime = 3000;

    Typeface typeFace;
    private static boolean activityStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);

        if (   activityStarted
                && getIntent() != null
                && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) {
            finish();
            return;
        }

        activityStarted = true;
        setContentView(R.layout.activity_splash);
        final SplashActivity sPlashScreen = this;
        Fabric.with(this, new Crashlytics());
        context = this;

        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean done = mPrefs.getBoolean(Constants.SPLASH_SCREEN_DONE, false);
        if (done) {
            if (User.isUserLoggedIn(getApplicationContext())) {
                MyUtils.startActivity(this, MainActivity.class);
                finish();
            } else {
                MyUtils.startActivity(sPlashScreen, WalkThroughActivity.class);
                finish();
            }

        } else {
            final Thread splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        while (_active && waited < _splashTime) {
                            sleep(100);
                            if (_active) {
                                waited += 100;
                            }
                        }
                    } catch (final InterruptedException e) {
                        // do nothing
                    } finally {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                MyUtils.initIndustryList();
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                prefsEditor.putBoolean(Constants.SPLASH_SCREEN_DONE, true);
                                prefsEditor.apply();
                                MyUtils.startActivity(sPlashScreen, WalkThroughActivity.class);
                                finish();
                            }
                        });
                    }
                }
            };
            splashTread.start();
        }
    }
    public static void checkForUpdateAndSaveServerSettings(final Activity context) {
        checkForUpdate( context);
    }
    private static void saveServerSettings(final Activity context, JSONObject jo) {
        SharedPreferences sp=MyUtils.getSharedPreferences();
        SharedPreferences.Editor  editor=sp.edit();
        try {
            editor.putInt(Constants.PROXIMITY_REMINDER_DISTANCE, jo.getInt("proximity_reminder_distance"));
        }catch (Exception e) {
            Logger.write(e);
        }
        editor.apply();
    }
    public static void checkForUpdate(final Activity context) {

        ConfigApi clientApi = MainApplication.getInstance().getRetrofit().create(ConfigApi.class);
        Call<ResponseBody> response;
            response = clientApi.getSetting("");

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
          try {

                    JSONObject jo=new JSONObject(response.toString());
                    JSONObject vJO=new JSONObject(jo.getString("version_android"));
                    saveServerSettings(context, jo);
                    final boolean isRequired = vJO.getBoolean("required");
                    if ( !isUptoDate(context, vJO.getString("code").trim())) {
                        android.app.AlertDialog c=null;
                        Logger.write("Latest ................ "+response);
                        final android.app.AlertDialog.Builder ad=new android.app.AlertDialog.Builder(context, R.style.customDialog);
                        // View v=getLayoutInflater().inflate(R.layout.update_dialog, null);
                        // TextView updateTV=(TextView)v.findViewById(R.id.update_note);
                        ad.setMessage("A new version ("+vJO.getString("code")+") of "+context.getString(R.string.app_name)+" is now available. You should update to continue.");

                        ad.setNeutralButton(null, null);
                        ad.setCancelable(false);
                        ad.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               openPlayStore(context);
                                context.finish();
                            }
                        });
                        ad.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ad.create().dismiss();
                               if(isRequired) context.finish();
                                return;
                            }
                        });
                        //ad.setView(v, 0,0,0,0);
                        c=ad.create();
                        c.show();
                        return;
                    }
                    if(jo.getBoolean("maintenance_mode")==true) {
                        try {
                            ActivityMaintenance.MESSAGE = jo.getString("maintenance_message");
                        }catch (Exception e) {

                        }
                        MyUtils.startActivity(context, ActivityMaintenance.class);
                        context.finish();
                        return;
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }


            }catch (Exception E) {

                }
        }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }

        });
    }

    private static boolean isUptoDate(Context c, String version) throws Exception {
        PackageManager manager = c.getPackageManager();
        PackageInfo info = manager.getPackageInfo(
                c.getPackageName(), 0);
        String vi= info.versionName;
        if(version.equalsIgnoreCase(vi)) return true;
        String[] release=version.split("\\.");
        String[] installed = vi.split("\\.");
        // int len=Math.min(release.length, installed.length);
        // for(int i=0; i<len; i++) {

        try {
            if (Integer.parseInt(release[0]) > Integer.parseInt(installed[0])) {
                return false;
            }
        }catch (IndexOutOfBoundsException iob) {

        }
        try {
            if ( (Integer.parseInt(release[0]) == Integer.parseInt(installed[0])) && Integer.parseInt(release[1]) > Integer.parseInt(installed[1])) {
                return false;
            }
        }catch (IndexOutOfBoundsException iob) {

        }
        Logger.write(vi);
        Logger.write(Integer.valueOf(release[2])+"" +
                "="+Integer.valueOf(installed[2]));
        try {
            if ( (Integer.parseInt(release[0]) == Integer.parseInt(installed[0]))
                    && Integer.parseInt(release[1]) == Integer.parseInt(installed[1])
                    && Integer.parseInt(release[2]) > Integer.parseInt(installed[2])) {
                return false;
            }
        }catch (IndexOutOfBoundsException iob) {

        }
        try {
            if ( (Integer.parseInt(release[0]) == Integer.parseInt(installed[0]))
                    && Integer.parseInt(release[1]) == Integer.parseInt(installed[1])
                    && Integer.parseInt(release[2])== Integer.parseInt(installed[2])
                    && Integer.parseInt(release[3]) > Integer.parseInt(installed[3])) {
                return false;
            }
        }catch (IndexOutOfBoundsException iob) {

        }
        return true;
    }
    public static void openPlayStore(Context c) {
        final String appPackageName = c.getPackageName(); // getPackageName() from Context or Activity object
        try {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
