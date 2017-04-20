package com.tied.android.tiedapp.ui.activities.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.CoWorker;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.CoworkerApi;
import com.tied.android.tiedapp.ui.activities.coworker.ViewCoWorkerActivity;
import com.tied.android.tiedapp.ui.activities.privacy.ActivityPrivacySetting;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitendra on 9/9/2016.
 */
public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlSales, rlDailyActivities, rlClients, rlTeritorry, rlLine, rlIndustry;
    ImageView img_close;

    User currentUser;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_privacy);

        bundle = getIntent().getExtras();
        currentUser=User.getCurrentUser(this);
        init();
    }

    private void init() {
        rlSales = (RelativeLayout) findViewById(R.id.rlSales);
        rlSales.setOnClickListener(this);
        rlDailyActivities = (RelativeLayout) findViewById(R.id.rlDailyActivities);
        rlDailyActivities.setOnClickListener(this);
        rlClients = (RelativeLayout) findViewById(R.id.rlClients);
        rlClients.setOnClickListener(this);
        rlTeritorry = (RelativeLayout) findViewById(R.id.rlTeritorry);
        rlTeritorry.setOnClickListener(this);
        rlLine = (RelativeLayout) findViewById(R.id.rlLine);
        rlLine.setOnClickListener(this);
        rlIndustry = (RelativeLayout) findViewById(R.id.rlIndustry);
        rlIndustry.setOnClickListener(this);
        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        bundle.putBoolean(ActivityPrivacySetting.IS_MULTIPLE, true);
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.rlSales:
                bundle.putString("title", "Sales Privacy");
                getCoworkersCanSee("sales");
                break;
            case R.id.rlDailyActivities:
                bundle.putString("title", "Activities Privacy");
                getCoworkersCanSee("activities");
                break;
            case R.id.rlClients:
                bundle.putString("title", "Clients Privacy");
                getCoworkersCanSee("clients");
                break;
            case R.id.rlTeritorry:
                bundle.putString("title", "Territory Privacy");
                getCoworkersCanSee("territory");
                break;
            case R.id.rlLine:
                bundle.putString("title", "Line Privacy");
                getCoworkersCanSee("line");
                break;
            case R.id.rlIndustry:
                bundle.putString("title", "Industry Privacy");
                getCoworkersCanSee("industry");
                break;
        }

    }
    public void getCoworkersCanSee(final String section) {
        //super.loadData();
        //if(addLinesActivity.getLine()==null) return;
        bundle.putString("section", section);
        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        CoworkerApi coworkerApi = MainApplication.createService(CoworkerApi.class);

        final Call<ResponseBody> response = coworkerApi.canSeeSection( currentUser.getId(), section);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    Logger.write("******************** "+response.toString());
                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(PrivacyActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        ArrayList<CoWorker> coworkers= (ArrayList)response.getDataAsList("coworkers", CoWorker.class);
                        //ArrayList<String> cws=new ArrayList<String>(coworkers.size());
                        //for(CoWorker cw:coworkers) {
                          //  cws.add(cw.toJSONString());
                        //}
                        Logger.write("&&&&&&&&&&&&&&&&&&&&&&&&&&& "+coworkers.size());
                        bundle.putSerializable(ActivityPrivacySetting.SELECTED_OBJECTS, coworkers);
                        MyUtils.startRequestActivity(PrivacyActivity.this, ActivityPrivacySetting.class, Constants.SELECT_USER, bundle);

                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(PrivacyActivity.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(PrivacyActivity.this);
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write(requestCode+" "+resultCode);
        if(requestCode==Constants.SELECT_USER && resultCode==RESULT_OK) {
          //   ArrayList<User> selectedUsers= (ArrayList)(data.getSerializableExtra("selected"));
            final ArrayList<String> selected = new ArrayList<>();
            /*for(Object obj:selectedUsers) {
                if(obj instanceof User) {
                    selected.add(((User)obj).getId());
                }
                if(obj instanceof CoWorker) {
                    selected.add(((CoWorker)obj).getCoworker_id());
                }

            }*/
            Logger.write(selected.toString());

            CoworkerApi coworkerApi = MainApplication.createService(CoworkerApi.class);

            String section=data.getStringExtra("section");
            final Call<ResponseBody> response = coworkerApi.updatePrivacy( currentUser.getId(), section, (ArrayList)(data.getSerializableExtra("selected")));
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                    if (this == null) return;
                    DialogUtils.closeProgress();
                    try {
                        //Logger.write(resResponse.body().string());
                        //  JSONObject response = new JSONObject(resResponse.body().string());
                        GeneralResponse response=new GeneralResponse(resResponse.body());
                        Logger.write(response.toString());
                        if (response != null && response.isAuthFailed()) {
                            User.LogOut(PrivacyActivity.this);
                            return;
                        }
                        Logger.write(response.toString());
                        _Meta meta=response.getMeta();
                        if(meta !=null && (meta.getStatus_code()==201 || meta.getStatus_code()==200)) {
                            MyUtils.showMessageAlert(PrivacyActivity.this, "Privacy updated successfully!");
                           // ArrayList<CoWorker> coworkers= (ArrayList)response.getDataAsList("coworkers", CoWorker.class);
                            //ArrayList<String> cws=new ArrayList<String>(coworkers.size());
                            //for(CoWorker cw:coworkers) {
                            //  cws.add(cw.toJSONString());
                            //}
                            //bundle.putSerializable(ActivityPrivacySetting.SELECTED_OBJECTS, coworkers);
                            //MyUtils.startRequestActivity(PrivacyActivity.this, ActivityPrivacySetting.class, Constants.SELECT_USER, bundle);

                        } else {
                            MyUtils.showToast(getString(R.string.connection_error));
                        }
                    }catch (Exception e) {
                        MyUtils.showConnectionErrorToast(PrivacyActivity.this);
                        Logger.write(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //Log.d(TAG + " onFailure", t.toString());
                    Logger.write(t.getMessage());
                    MyUtils.showConnectionErrorToast(PrivacyActivity.this);
                    DialogUtils.closeProgress();
                }
            });


        }

    }
}
