package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.onesignal.*;
import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Notification;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.retrofits.services.UserApi;
import com.tied.android.tiedapp.services.LocationService;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerActivity;
import com.tied.android.tiedapp.ui.activities.coworker.ViewCoWorkerActivity;
import com.tied.android.tiedapp.ui.activities.goal.LineGoalActivity;

import com.tied.android.tiedapp.ui.activities.lines.LinesListActivity;
import com.tied.android.tiedapp.ui.activities.report.ReportActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityGroupedSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivityUniqueSales;
import com.tied.android.tiedapp.ui.activities.schedule.ScheduleDetailsActivitiy;
import com.tied.android.tiedapp.ui.activities.signups.SplashActivity;
import com.tied.android.tiedapp.ui.activities.territories.ActivityTerritories;
import com.tied.android.tiedapp.ui.activities.visits.ActivityVisitDetails;
import com.tied.android.tiedapp.ui.activities.visits.ActivityVisits;
import com.tied.android.tiedapp.ui.dialogs.DialogAddNewItem;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.DailyStatsFragment;
import com.tied.android.tiedapp.ui.fragments.LinesFragment;
import com.tied.android.tiedapp.ui.fragments.client.ClientAddFragment;
import com.tied.android.tiedapp.ui.fragments.client.MapAndListFragment;
import com.tied.android.tiedapp.ui.fragments.notification.NotificationFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment1;
import com.tied.android.tiedapp.ui.fragments.sales.*;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleAppointmentsFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleSuggestionFragment;
import com.tied.android.tiedapp.ui.fragments.signups.IndustryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.ui.listeners.ImageReadyForUploadListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import io.fabric.sdk.android.Fabric;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Daniel on 5/3/2016.
 */
public class MainActivity extends FragmentActivity implements FragmentIterationListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MainActivity.class
            .getSimpleName();

    public ImageView img_user_picture, add, drawerUserPicture;
    private ImageReadyForUploadListener imageReadyForUploadListener;

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;
    //LinearLayout relativeLayout , tab_bar;
    private LinearLayout  map_tab,  activity_layout, add_layout, more_layout, tab_actvity_schedule,  sale_tab;
    private RelativeLayout invite_menu, notification_menu, subscription_menu;
    private TextView txt_schedules, txt_activities, info_msg, drawerFullName, drawerEmail;

    public Bitmap bitmap;

    public Uri imageUri = null, outputUri = null;
    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    User user;
    public Bundle bundle;

    //
    private final int BRAIN_REQUEST_CODE = 10010;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public Retrofit retrofit;
    public SignUpApi service;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    int currentFragmentID=0;


    public static MainActivity mainActivity;
    public View notificationCircle;
    public TextView numNotifications;
    public SwipeRefreshLayout refresh;

    ImageView schedule_icon, map_icon, sale_icon, more_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        mainActivity=this;

        SplashActivity. checkForUpdateAndSaveServerSettings(this);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_main);
        OneSignal.sendTag("user_id", MyUtils.getUserLoggedIn().getId());
        checkSubscription();
        user = User.getCurrentUser(getApplicationContext());
        if(user == null){
            User.LogOut(getApplicationContext());
            finish();
        }

        startService(new Intent(this, LocationService.class));
        refresh=(SwipeRefreshLayout)findViewById(R.id.swiperefresh) ;
        refresh.setOnRefreshListener(this);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        numNotifications=(TextView)findViewById(R.id.num_new_alerts);
        notificationCircle=findViewById(R.id.notification_circle);
        notificationCircle.setVisibility(View.GONE);
        //showNumAlerts(null);


        //relativeLayout = (LinearLayout) findViewById(R.id.linearLayout);
       // tab_bar = (LinearLayout) findViewById(R.id.tab_bar);
        //alert_edit_msg = (LinearLayout) findViewById(R.id.alert_edit_msg);
       //tab_actvity_schedule = (LinearLayout) findViewById(R.id.tab_activity_schedule);

        more_layout = (LinearLayout) findViewById(R.id.more);
        activity_layout = (LinearLayout) findViewById(R.id.activity);
        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        invite_menu = (RelativeLayout) findViewById(R.id.invite_menu);
        map_tab = (LinearLayout) findViewById(R.id.map);
        sale_tab = (LinearLayout) findViewById(R.id.sales);

        add = (ImageView) findViewById(R.id.add);
       // txt_activities = (TextView) findViewById(R.id.txt_activities);
       // txt_schedules = (TextView) findViewById(R.id.txt_schedules);

        img_user_picture = (ImageView) findViewById(R.id.img_user_picture);
        drawerUserPicture = (ImageView) findViewById(R.id.user_picture_iv);
        notification_menu = (RelativeLayout) findViewById(R.id.notification_menu);
        subscription_menu = (RelativeLayout) findViewById(R.id.subscription_menu);

        schedule_icon = (ImageView) findViewById(R.id.schedule_icon);
        map_icon = (ImageView) findViewById(R.id.map_icon);
        sale_icon = (ImageView) findViewById(R.id.sale_icon);
        more_icon = (ImageView) findViewById(R.id.more_icon);

        more_layout.setOnClickListener(this);
        add.setOnClickListener(this);
//        txt_schedules.setOnClickListener(this);
     //   txt_activities.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
//        img_user_picture.setOnClickListener(this);
        invite_menu.setOnClickListener(this);
        map_tab.setOnClickListener(this);
        sale_tab.setOnClickListener(this);
        notification_menu.setOnClickListener(this);
        subscription_menu.setOnClickListener(this);
        drawerFullName=(TextView)findViewById(R.id.full_name_tv);
        drawerEmail=(TextView)findViewById(R.id.email_tv);
        drawerEmail.setText(user.getEmail());
        drawerFullName.setText(user.getFirst_name()+" "+user.getLast_name());

       // Log.d(TAG, "Avatar Url : " + Constants.GET_AVATAR_ENDPOINT + "avatar_" + user.getId() + ".jpg");
        //String avatarURL =Constants.GET_AVATAR_ENDPOINT + "avatar_" + user.getId() + ".jpg";
       // MyUtils.Picasso.displayImage(user.getAvatarURL(), img_user_picture);
           MyUtils.Picasso.displayImage(user.getAvatar(), drawerUserPicture);
        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);

        //Picasso.with(this).load(user.getAvatar()).into(drawerUserPicture);

        bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();

        }

        if(bundle.getBoolean(Constants.SCHEDULE_EDITED)){
            MyUtils.showMessageSuccess(this, "Schedule successfully updated");
        }

        if(bundle.getBoolean(Constants.CLIENT_EDITED)){
            MyUtils.showMessageSuccess(this, "Client successfully updated");
        }


        Gson gson = new Gson();
        String user_json = gson.toJson(user);

        bundle.putString(Constants.USER_DATA, user_json);

        if(bundle.getSerializable(Constants.PROXIMITY_CLIENT_DATA) !=null) {
            Bundle b=new Bundle();
            b.putSerializable(Constants.CLIENT_DATA, bundle.getSerializable(Constants.PROXIMITY_CLIENT_DATA) );
            MyUtils.startActivity(this, ActivityClientProfile.class, b);

        }
        if ((new Date().getTime() - MyUtils.getLastTimeAppRan()) > 24*60*60*1000) {
            //launchFragment(Constants.HomeSchedule, bundle);
            //MyUtils.startActivity(this, DailyStatsActivity.class);
            MyUtils.setLastTimeAppRan(new Date().getTime());
           // return;
        }
       /* else if(bundle.getBoolean(Constants.NO_CLIENT_FOUND)){
            launchFragment(Constants.ClientAdd, bundle);
        }
        else if(bundle.getBoolean(Constants.NO_SCHEDULE_FOUND)){
            launchFragment(Constants.CreateSchedule, bundle);
        }
        else {*/
            launchFragment(Constants.AppointmentList, bundle);
        //}
        activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivity=this;

        showNumAlerts(null);
    }
    public static MainActivity getInstance() {
        return mainActivity;
    }

    Fragment currentFragment=null;
    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

       // tab_bar.setVisibility(View.VISIBLE);
        more_layout.setBackground(null);
        activity_layout.setBackground(null);
        sale_tab.setBackground(null);
        add_layout.setBackground(null);
        map_tab.setBackground(null);
        //relativeLayout.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID=pos;
        switch (pos) {
            case Constants.CreateSchedule:
                tab_actvity_schedule.setBackgroundResource(R.drawable.base_schedule);
                if(fragments.get(pos)==null) {
                    fragments.put(pos,CreateScheduleFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ClientAdd:
               //// relativeLayout.setVisibility(View.GONE);
                tab_actvity_schedule.setBackgroundResource(R.drawable.base_schedule);
                if(fragments.get(pos)==null) {
                    fragments.put(pos,ClientAddFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ActivitySchedule:
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  ClientAddFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.HomeSchedule:
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
               // tab_bar.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, DailyStatsFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ScheduleSuggestions:
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ScheduleSuggestionFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.AddScheduleActivity:
                add_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                MyUtils.startActivity(this, AddOptionsActivity.class);
                return;
            case Constants.Profile:
                setActiveIcon(3);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ProfileFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
               // tab_bar.setVisibility(View.VISIBLE);
                break;
          /*  case Constants.EditProfile:
                //relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, EditProfileFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ProfileAddress:
                //relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  AddressFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);

                break;*/
            case Constants.Notification:
                setActiveIcon(3);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, NotificationFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;

            case Constants.HomeSale:
                setActiveIcon(2);
                sale_tab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                //relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, SaleFragment.newInstance(bundle) );
                    Logger.write("it is new");
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;
            case Constants.SaleViewAll:
                sale_tab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
               // relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, SaleViewAllFragment.newInstance(bundle) );
                    Logger.write("it is new");
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;

            case Constants.Industry:
                //tab_bar.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, IndustryFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ActivityFragment:
                setActiveIcon(0);
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ScheduleAppointmentsFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.AppointmentList:
                setActiveIcon(0);
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ScheduleAppointmentsFragment.newInstance(bundle) );
                    Logger.write("it is new");
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;

            case Constants.LineAndTerritory:
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                //relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, LinesFragment.newInstance(bundle) );


                    Logger.write("it is new");
                }
                fragment = fragments.get(pos);
               // tab_bar.setVisibility(View.VISIBLE);
                break;
            case Constants.MapFragment:
                setActiveIcon(1);
                map_tab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
               // relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, MapAndListFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;

            default:
                finish();
        }

        if (fragment != null) {
           // Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            //Logger.write("TAGGGG: "+ fragment.getClass().getName());
            addFragment(ft, currentFragment, fragment, fragment.getClass().getName());
        }
        currentFragment=fragment;
    }

    static long backPressed=0;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }
        if(System.currentTimeMillis()-backPressed < 5000) {
            finish();
        }else{
            MyUtils.showToast("Press back again to exit");
        }
        //this.moveTaskToBack(true);
        backPressed=System.currentTimeMillis();
        return;
    }

    private void handleCrop(Uri outputUri) {
        imageReadyForUploadListener = (AvatarProfileFragment) profileFragment;
        ImageView avatar = ((AvatarProfileFragment) profileFragment).image;
        bundle.putString(Constants.AVATAR_STATE_SAVED, outputUri.toString());
        avatar.setImageBitmap(null);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
            avatar.setImageBitmap(bitmap);
            imageReadyForUploadListener.imageReadyUri(outputUri);
        } catch (IOException e) {
            MyUtils.showToast("An error occurred. Please try again.");
        }
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " OnFragmentInteractionListener " + action);
        launchFragment(action, bundle);
    }

    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            handleCrop(outputUri);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            outputUri = Uri.fromFile(new File(getFilesDir(), "cropped.jpg"));
            Uri selectedImage = imageUri;
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        } else if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            outputUri = Uri.fromFile(new File(getFilesDir(), "cropped.jpg"));
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        }

        if (requestCode == BRAIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                // Send the nonce to your server.
            }
        }

        if ((requestCode == Constants.ClientFilter || requestCode == Constants.ClientDelete || requestCode == Constants.ViewSchedule || requestCode == Constants.CreateSchedule) && resultCode == Activity.RESULT_OK) {
//            launchFragment(Constants.MapAndListFragment, bundle);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onBraintreeSubmit(View v) {
//        To do
        //https://developers.braintreepayments.com/start/hello-client/android/v2
    }

    @Override
    public void onClick(View v) {
//        android:background="@color/semi_transparent_black"
        Logger.write("ID "+v.getId());
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        switch (v.getId()) {
            case R.id.add:
                //launchFragment(Constants.AddScheduleActivity, bundle);
//                MyUtils.startActivity(this, AddOptionsActivity.class);
                DialogAddNewItem alert = new DialogAddNewItem();
                alert.showDialog(this, bundle);
                break;
           /* case R.id.txt_activities:
                if(currentFragmentID==Constants.ActivityFragment) return;
                tab_actvity_schedule.setBackgroundResource(R.drawable.base_activities);
                launchFragment(Constants.ActivityFragment, bundle);
                break;
            case R.id.txt_schedules:
                if(currentFragmentID==Constants.AppointmentList) return;
                tab_actvity_schedule.setBackgroundResource(R.drawable.base_schedule);
                launchFragment(Constants.AppointmentList, bundle);
                break;*/
            case R.id.more:
                toggelDrawer();
                break;
            case R.id.activity:
                if(currentFragmentID==Constants.ActivityFragment) return;
              //  tab_actvity_schedule.setBackgroundResource(R.drawable.base_activities);
                launchFragment(Constants.ActivityFragment, bundle);
                break;
            case Constants.Profile:
                fragment = new ProfileFragment1();
                launchFragment(Constants.Profile, bundle);
                break;
            case Constants.EditProfile:
//                fragment = new EditProfileFragment();
                break;
            case Constants.ProfileAddress:
//                fragment = new AddressActivity();
                break;
            //case R.id.lines_menu:
               // MyUtils.startActivity(MainActivity.this, LinesListActivity.class,bundle);

            case R.id.lines_menu:
                MyUtils.startActivity(MainActivity.this, LinesListActivity.class,bundle);
                //if(currentFragmentID==Constants.LineAndTerritory) return;
               // launchFragment(Constants.LineAndTerritory, bundle);

                break;
            case R.id.coworker_menu:
                MyUtils.startActivity(MainActivity.this, CoWorkerActivity.class,bundle);
                break;
           /* case R.id.client_menu:
                bundle.putBoolean(Constants.CLIENT_LIST, true);
                MyUtils.startActivity(MainActivity.this, ClientMapAndListActivity.class,bundle);
                break;*/
            case R.id.specials_menu:
                bundle.putBoolean(Constants.CLIENT_LIST, true);
                MyUtils.startActivity(MainActivity.this, ActivityGroupedSales.class,bundle);
                break;


            case R.id.img_user_picture :
            case R.id.user_picture_iv:
            case R.id.profile_menu:
                if(currentFragmentID==Constants.ProfileFragment) return;
                launchFragment(Constants.Profile, bundle);

//                MyUtils.startActivity(MainActivity.this, ProfileFragment.class, bundle);
                break;
            case R.id.territories_menu:
                MyUtils.startActivity(MainActivity.this, ActivityTerritories.class,bundle);
                break;



            case R.id.invite_menu:
               // MyUtils.startActivity(MainActivity.this, SendInviteActivity.class, bundle);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try Tied for Android!");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm using Tied for Android and I recommend it to manage your business. Click here: http://www.gettied.com");

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
                break;
            case R.id.logout:
                User.LogOut(this);
                finish();
                break;
            case R.id.map:
               // MyUtils.startActivity(this, ClientMapAndListActivity.class, bundle);
                bundle.putBoolean(Constants.CLIENT_LIST, false);
                bundle.putInt(Constants.SHOW_FILTER, 0);

                if(currentFragmentID==Constants.MapFragment) return;
                launchFragment(Constants.MapFragment, bundle);
                break;
            case R.id.goal_menu:
                MyUtils.startActivity(this, LineGoalActivity.class);
                break;
            case R.id.sales:
                if(currentFragmentID==Constants.HomeSale) return;
                launchFragment(Constants.HomeSale, bundle);
                break;
            case R.id.notification_menu:
                if(currentFragmentID==Constants.Notification) return;
                launchFragment(Constants.Notification, bundle);
                break;
            case R.id.subscription_menu:
                MyUtils.startActivity(this, SubscriptionActivity.class);
                break;
            case R.id.visits:
                MyUtils.startActivity(this, ActivityVisits.class);
                break;
            case R.id.reports_menu:
                MyUtils.startActivity(this, ReportActivity.class);
                break;
            case R.id.sales_menu:
                MyUtils.startActivity(this, ActivityUniqueSales.class, bundle);
                break;
        }
    }
    private void toggelDrawer() {
        OneSignal.sendTag("user_id", MyUtils.getUserLoggedIn().getId());
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);

            user = User.getCurrentUser(getApplicationContext());
            drawerEmail.setText(user.getEmail());
            drawerFullName.setText(user.getFirst_name()+" "+user.getLast_name());
            MyUtils.Picasso.displayImage(user.getAvatar(), drawerUserPicture);
/*
            Picasso.with(this).load(user.getAvatar())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(drawerUserPicture);*/
        }
    }

    private void clearTabs() {
        activity_layout.setBackground(null);
        map_tab.setBackground(null);
    }



    public void addFragment(FragmentTransaction transaction, Fragment cCurrentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(cCurrentFragment!=null) {
            Logger.write("removing current "+cCurrentFragment.getClass().getName());
            transaction.hide(cCurrentFragment);
        }
        // use a fragment tag, so that later on we can find the currently displayed fragment
      if(targetFragment.isAdded()) {
            Logger.write("was added");
            transaction.show(targetFragment).commit();
        }else {
            if(cCurrentFragment==null) {
                transaction.replace(R.id.fragment_place, targetFragment, tag)
                        //.addToBackStack(tag)
                        .commit();
            }else {
                transaction.add(R.id.fragment_place, targetFragment, tag)
                        .addToBackStack(tag)
                        .commit();
            }
        }
        //currentFragment=targetFragment;
    }

    @Override
    public void onRefresh() {
        Fragment visibleFragment = getVisibleFragment();

        if (visibleFragment != null) {

            if (visibleFragment instanceof MapAndListFragment) {
                ((MapAndListFragment) fragment).refresh();
                return;
            }
             else if (visibleFragment instanceof ScheduleAppointmentsFragment) {
                ((ScheduleAppointmentsFragment) fragment).refresh();
                return;
            } else if (visibleFragment instanceof SaleFragment) {
                Logger.write(visibleFragment.getClass().getCanonicalName());
                ((SaleFragment) fragment).refresh();
                return;
            } else if (visibleFragment instanceof NotificationFragment) {
                ((NotificationFragment) fragment).refresh();
                return;
            } else if (visibleFragment instanceof ProfileFragment) {
                ((ProfileFragment) fragment).refresh();
                return;
            }
        }
        refresh.setRefreshing(false);
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public static class OneSignalNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        Context context;
        public OneSignalNotificationOpenedHandler(Context c)  {
            context=c;
        }



        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;
            Logger.write("OneSignalExample", "customkey set with value: " + result.notification.toString());
            final Bundle bundle=new Bundle();
            if (data != null) {
                try {
                    Logger.write(data.toString());
                    final Notification model=Notification.fromJSONString(data.toString());
                    getUIHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch (model.getObject()) {
                                case "user":

                                    if (MyUtils.getUserLoggedIn().getId().equals(model.getUser_id())) {
                                        MainActivity.getInstance().launchFragment(Constants.Profile, bundle);
                                    } else {
                                        bundle.putString("user_id", model.getObject_id());
                                        MyUtils.startActivity(context, ViewCoWorkerActivity.class, bundle, true);
                                    }
                                    break;
                                case "visit":
                                    bundle.putString("visit_id", model.getId());
                                    MyUtils.startActivity(context, ActivityVisitDetails.class, bundle, true);
                                    break;
                                case "client":
                                    bundle.putString("client_id", model.getId());
                                    MyUtils.startActivity(context, ActivityClientProfile.class, bundle, true);
                                    break;
                                case "schedule":
                                    bundle.putString("schedule_id", model.getId());
                                    MyUtils.startActivity(context, ScheduleDetailsActivitiy.class, bundle, true);
                                    break;
                            }
                        }
                    }, 500);

                }catch (Exception e) {
                    Logger.write(e);
                }

            }


            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);



        }
    }
    public static Handler UIHandler = new Handler(Looper.getMainLooper());
    public static Handler getUIHandler() {
        if(UIHandler==null) UIHandler = new Handler(Looper.getMainLooper());
        return UIHandler;
    }
    private static final String NEW_MESSAGE_TYPE="message";
    private static final String SEEN_MESSAGE_TYPE="seen_message";
    private static final String  NEW_NOTIFICATION_TYPE="notification";
    private static final String SEEN_NOTIFICATION_TYPE="seen_notification";
    static int numNewAlert;
    public static class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

        Context context;
        public MyNotificationReceivedHandler(Context c)  {
            context=c;
        }

        @Override
        public void notificationReceived(OSNotification notification) {


           ////Logger.write("OneSignalExample", "customkey set with value: " +notification.payload.additionalData.toString());
            JSONObject data = notification.payload.additionalData;
           // Logger.write("Custome " +data.toString());
           // String customKey;
           // ShortcutBadger.applyCount(MainApplication.getInstance().getApplicationContext(), 3);
            if (data != null) {
                //customKey = data.optString("customkey", null);
               // if (customKey != null) {
                 //   Logger.write("OneSignalExample", "customkey set with value: " + customKey);
                    try {

                        SharedPreferences sp = MyUtils.getSharedPreferences();
                        SharedPreferences.Editor ed = sp.edit();
                /*
                ed.putString(Constants.PREFS_ALERT, intent.getBundleExtra("data").getString("alert").toString());
                ed.apply();*/
                          numNewAlert = sp.getInt(Constants.PREFS_NEW_NOTIFICATION_COUNT, 0) + sp.getInt(Constants.PREFS_NEW_MESSAGE_COUNT, 0);

                                numNewAlert++;

                                ed.putInt(Constants.PREFS_NEW_NOTIFICATION_COUNT, numNewAlert);


                        ed.commit();
                        ShortcutBadger.applyCount(MainApplication.getInstance().getApplicationContext(), numNewAlert);

                        try {
                          getUIHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.getInstance().showNumAlerts(numNewAlert);
                                    try {
                                        MainActivity.getInstance().showNumAlerts(numNewAlert);
                                    }catch (NullPointerException npe) {

                                    }
                                }
                            }, 500);
                        } catch (Exception e) {
                            Logger.write(e);
                        }
                    } catch (Exception je) {
                        Logger.write(je);
                    }
               // }
            }
        }
    }
    public void showNumAlerts(Integer numAlerts) {
        if(numAlerts==null) {
            SharedPreferences sp = MyUtils.getSharedPreferences();
            numAlerts = sp.getInt(Constants.PREFS_NEW_NOTIFICATION_COUNT, 0) + sp.getInt(Constants.PREFS_NEW_MESSAGE_COUNT, 0);
        }
        if(numAlerts>0) {
            notificationCircle.setVisibility(View.VISIBLE);
        }else{
            notificationCircle.setVisibility(View.GONE);

            try {
                ShortcutBadger.applyCount(this, 0);
            }catch(Exception e) {

            }
        }
        numNotifications.setText(""+numAlerts);
    }

    public void clearNewAlertCount() {
        SharedPreferences sp = MyUtils.getSharedPreferences();
        numNewAlert = sp.getInt(Constants.PREFS_NEW_NOTIFICATION_COUNT, 0) + sp.getInt(Constants.PREFS_NEW_MESSAGE_COUNT, 0);
        if(numNewAlert > 0) {
            SharedPreferences.Editor ed = sp.edit();
            ed.remove(Constants.PREFS_NEW_NOTIFICATION_COUNT);
            ed.commit();
        }
        notificationCircle.setVisibility(View.GONE);
        ShortcutBadger.applyCount(MainApplication.getInstance().getApplicationContext(), 0);
    }


    private void setActiveIcon(int index) {
        switch (index) {
            case 0:
                schedule_icon.setBackgroundResource(R.drawable.analytics_active);

                map_icon.setBackgroundResource(R.drawable.maps);
                sale_icon.setBackgroundResource(R.drawable.sales);
                more_icon.setBackgroundResource(R.drawable.lists);
                break;
            case 1:
                map_icon.setBackgroundResource(R.drawable.maps_active);

                schedule_icon.setBackgroundResource(R.drawable.analytics);
                sale_icon.setBackgroundResource(R.drawable.sales);
                more_icon.setBackgroundResource(R.drawable.lists);
                break;
            case 2:
                sale_icon.setBackgroundResource(R.drawable.sales_active);

                schedule_icon.setBackgroundResource(R.drawable.analytics);
                map_icon.setBackgroundResource(R.drawable.maps);
                more_icon.setBackgroundResource(R.drawable.lists);
                break;
            case 3:
                more_icon.setBackgroundResource(R.drawable.lists_active);

                schedule_icon.setBackgroundResource(R.drawable.analytics);
                map_icon.setBackgroundResource(R.drawable.maps);
                sale_icon.setBackgroundResource(R.drawable.sales);
                break;
        }
    }
    public static class NotificationExtender extends NotificationExtenderService {
        @Override
        protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
            // Read properties from result.
            if(MyUtils.getUserLoggedIn()==null) return true;
            // Return true to stop the notification from displaying.
            return false;
        }
    }
    private void checkSubscription() {
        final UserApi userApi =  MainApplication.createService(UserApi.class);
        Call<ResponseBody> response = userApi.getUser(MyUtils.getUserLoggedIn().getId());
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
                        User.LogOut(MainActivity.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        User user = ( (User) response.getData("user", User.class));
                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                        format.setTimeZone(TimeZone.getDefault());
                        Date expDate=format.parse(user.getSub_expiration_date());

                        Calendar c= Calendar.getInstance();
                        c.setTime(expDate);
                        long days=HelperMethods.getDateDifferenceWithToday(c.get(Calendar.YEAR)+"-"+(1+c.get(Calendar.MONTH))+"-"+c.get(Calendar.DAY_OF_MONTH));
                        TextView packageTV=(TextView)findViewById(R.id.package_expiry);
                        if(days>0) {
                            packageTV.setText("Expires in "+days+" days");
                            if(days<10) packageTV.setTextColor(getResources().getColor(R.color.red_color));
                        }else{
                            packageTV.setText("Expired");
                            packageTV.setTextColor(getResources().getColor(R.color.red_color));
                        }

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
}

