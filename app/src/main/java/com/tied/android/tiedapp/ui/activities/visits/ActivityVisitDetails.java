package com.tied.android.tiedapp.ui.activities.visits;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.objects.visit.Duration;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.DialogYesNo;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 8/4/2016.
 */
@SuppressWarnings("ValidFragment")
public class ActivityVisitDetails extends AppCompatActivity implements  View.OnClickListener{

    public static final String TAG = ActivityVisitDetails.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    Client client;
    Visit visit;
    String visit_id;
    boolean updateMade=false;

    ImageView clientPhoto;
    TextView clientNameTV, locationTV;
    TextView distance, dscription, date, time, txt_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);

        bundle = getIntent().getExtras();
        user=MyUtils.getUserLoggedIn();
        client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
        visit = (Visit) bundle.getSerializable(Constants.VISIT_DATA);
        visit_id = (String) bundle.getSerializable("visit_id");

        initComponent();
    }
    private void initComponent() {

        clientPhoto=(ImageView)findViewById(R.id.avatar);
        clientNameTV=(TextView)findViewById(R.id.client_name);

        dscription=(TextView)findViewById(R.id.txt_description);
        locationTV = (TextView)findViewById(R.id.txt_location);
        distance=(TextView)findViewById(R.id.distance);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        txt_delete=(TextView)findViewById(R.id.txt_delete);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        if (client != null) {
            MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
            clientNameTV.setText(MyUtils.getClientName(client));
        }

        if (visit != null) {
            setVisitData(visit);
        } else {
            getVisitDetails();
        }
    }
    @Override
    public void onClick(View v) {
        int color = this.getResources().getColor(R.color.schedule_title_bg_color);

        switch (v.getId()) {
            case R.id.img_edit:
                bundle.putSerializable(Constants.CLIENT_DATA, client);
                bundle.putSerializable(Constants.VISIT_DATA, visit);
                MyUtils.startRequestActivity(ActivityVisitDetails.this, ActivityAddVisits.class, Constants.Visits, bundle);
                break;
            case R.id.txt_delete:
                color = this.getResources().getColor(R.color.alert_bg_color);
                DialogYesNo alert_delete = new DialogYesNo(ActivityVisitDetails.this, visit, "DELETE VISIT","Are you sure want to delete this visit","YES DELETE!",color,2);
                alert_delete.showDialog();
                break;
        }
    }

    public void goBack(View v) {
        if(updateMade) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finishActivity(Constants.VISIT_LIST);
        }
        finish();
    }

    private void getVisitDetails() {
        final VisitApi visitApi =  MainApplication.createService(VisitApi.class);
        Call<ResponseBody> response = visitApi.getVisit(visit_id);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityVisitDetails.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        visit = ( (Visit) response.getData("visit", Visit.class));

                        getClientObject(visit.getClient_id());

                        setVisitData(visit);

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
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityVisitDetails.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        client = ( (Client) response.getData("client", Client.class));

                        MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
                        clientNameTV.setText(MyUtils.getClientName(client));

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

    private void setVisitData(Visit visit) {
        locationTV.setText(visit.getAddress().getStreet() + ", " + visit.getAddress().getCity() + ", " + visit.getAddress().getState() + ", " + visit.getAddress().getZip());
        distance.setText(MyUtils.formatDistance(visit.getDistance())+MyUtils.getPreferredDistanceUnit());
        dscription.setText(visit.getTitle());
        String[] strdate = visit.getVisit_date().split("-");
        date.setText(MyUtils.MONTHS_LIST[Integer.valueOf(strdate[1]).intValue() - 1] + " " + strdate[2] + ", " + strdate[0]);

        time.setText(MyUtils.formatTime(visit.getVisit_time()));

        if (user.getId().equals(visit.getUser_id())) {
            txt_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.Visits && resultCode == RESULT_OK) {

            bundle = data.getExtras();
            client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
            visit = (Visit) bundle.getSerializable(Constants.VISIT_DATA);

            MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
            clientNameTV.setText(MyUtils.getClientName(client));
            updateMade=true;
            setVisitData(visit);
        }
    }
}