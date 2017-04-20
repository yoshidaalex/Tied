package com.tied.android.tiedapp.ui.activities.coworker;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.CoworkerApi;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.profile.PrivacyActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityLineClientSales;
import com.tied.android.tiedapp.ui.adapters.CoWorkerGAdapter;
import com.tied.android.tiedapp.ui.adapters.CoWorkerHAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoWorkerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    public static final String TAG = CoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private CoWorkerHAdapter horizontalAdapter;
    private RecyclerView horizontalView;
    LinearLayoutManager horizontalManager;
    List<User> coworkersIAdded=new ArrayList<>();
    List<User> coworkersThatAddedMe=new ArrayList<>();

    private CoWorkerGAdapter gridAdapter;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        bundle.putInt(Constants.SOURCE, Constants.COWORKER_SOURCE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }
        View focus=findViewById(R.id.getFocus);
        focus.requestFocus();
        focus.setFocusableInTouchMode(true);
        focus.setNextFocusDownId(R.id.search);


        //set horizontal LinearLayout as layout manager to creating horizontal list view
        horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalView = (RecyclerView) findViewById(R.id.horizontal_list);
        horizontalView.setLayoutManager(horizontalManager);

        horizontalAdapter = new CoWorkerHAdapter(coworkersIAdded, this);
        horizontalView.setAdapter(horizontalAdapter);





        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new CoWorkerGAdapter(coworkersThatAddedMe, this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);


//        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
//        layoutParams.height = 800;
//        gridView.setLayoutParams(layoutParams);
        loadWhoCanViewMyStuff();
        loadWhoICanSee();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        User user = (User) coworkersThatAddedMe.get(position);
        bundle.putSerializable(Constants.USER_DATA, user);
        MyUtils.startActivity(this, ViewCoWorkerActivity.class, bundle);
    }
    public void loadWhoCanViewMyStuff() {
        //super.loadData();
        //if(addLinesActivity.getLine()==null) return;
        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        CoworkerApi coworkerApi = MainApplication.getInstance().getRetrofit().create(CoworkerApi.class);

        final Call<ResponseBody> response = coworkerApi.getCoworkers( user.getToken(), user.getId(), "i_added",  10, new RevenueFilter(), 1);
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
                        User.LogOut(CoWorkerActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        coworkersIAdded.addAll(response.getDataAsList("coworkers", User.class));
                        horizontalAdapter.notifyDataSetChanged();
                        if(coworkersIAdded.size()==0) {
                            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.no_results).setVisibility(View.GONE);
                        }
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(CoWorkerActivity.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(CoWorkerActivity.this);
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }

    public void loadWhoICanSee() {
        //super.loadData();
        //if(addLinesActivity.getLine()==null) return;
        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        CoworkerApi coworkerApi = MainApplication.getInstance().getRetrofit().create(CoworkerApi.class);
        String group="added_me";
        final Call<ResponseBody> response = coworkerApi.getCoworkers( user.getToken(), user.getId(), "added_me",  10, new RevenueFilter(), 1);
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
                        User.LogOut(CoWorkerActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        coworkersThatAddedMe.addAll(response.getDataAsList("coworkers", User.class));
                        gridAdapter.notifyDataSetChanged();
                        if(coworkersThatAddedMe.size()==0) {
                            findViewById(R.id.no_results_2).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.no_results_2).setVisibility(View.GONE);
                        }
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(CoWorkerActivity.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(CoWorkerActivity.this);
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.add:
                MyUtils.startActivity(this, InviteCoWorkerActivity.class, bundle);
                break;
            case R.id.privacy_button:
                MyUtils.startActivity(this, PrivacyActivity.class, bundle);
                break;
        }
    }
}
