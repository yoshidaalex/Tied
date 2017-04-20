package com.tied.android.tiedapp.ui.activities.visits;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.objects.visit.VisitFilter;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerLinesActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityLineClientSales;
import com.tied.android.tiedapp.ui.adapters.MapClientListAdapter;
import com.tied.android.tiedapp.ui.adapters.VisitListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Upworker on 11/3/2016.
 */

public class ActivityVisits extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ActivityVisits.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    protected ListView listView;
    List<Visit> visits = new ArrayList<Visit>();
    protected VisitListAdapter adapter;
    VisitFilter visitFilter;
    Map<String, Client> clients=new HashMap<String, Client>();
    TextView no_result;
    int numPages=1;
    private int preLast;
    public int pageNumber=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);

        user = MyUtils.getUserLoggedIn();

        initComponent();
    }

    private void initComponent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        no_result = (TextView)  findViewById(R.id.no_results);
        no_result.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.visits_listview);

        adapter = new VisitListAdapter(visits, clients, this);
        listView.setAdapter(adapter);
        setDefaultVisitFilter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Visit visit = visits.get(position);
                Client client = clients.get(visit.getClient_id());
                bundle = new Bundle();
                bundle.putSerializable(Constants.CLIENT_DATA, client);
                bundle.putSerializable(Constants.VISIT_DATA, visit);
                MyUtils.startRequestActivity(ActivityVisits.this, ActivityVisitDetails.class, Constants.VISIT_LIST, bundle);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        if(pageNumber<numPages) {
                            pageNumber++;
                            loadVisits(visitFilter);
                        }

                        preLast = lastItem;
                    }
                }
            }
        });
    }

    private void setDefaultVisitFilter() {
        visitFilter = new VisitFilter();
        visitFilter.setClient(null);
        //visitFilter.setMonth(HelperMethods.getNumericMonthOfTheYear(HelperMethods.getTodayDate()));
        visitFilter.setMonth(0);
        visitFilter.setYear(HelperMethods.getCurrentYear(HelperMethods.getTodayDate()));
        visitFilter.setDistance(5000);
        visitFilter.setUnit("mi");
        visitFilter.setSort("recent");

        loadVisits(visitFilter);
    }

    private void loadVisits(VisitFilter visitFilter) {
        DialogUtils.displayProgress(this);
        final VisitApi visitApi =  MainApplication.createService(VisitApi.class);
        Call<ResponseBody> response = visitApi.getUserVisits(user.getId(), pageNumber, visitFilter);
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
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityVisits.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {
                        JSONObject jsonObject=new JSONObject(response.toString());
                        JSONObject client_obj = jsonObject.getJSONObject("clients");

                        //Logger.write(client_obj.toString());
                        Gson gson = new Gson();

                        Iterator<String> iter = client_obj.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                               clients.put(key, gson.fromJson(client_obj.getString(key), Client.class));
                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                        }
                        Logger.write(clients.toString());
                        adapter.setClients(clients);

                        numPages=meta.getPage_count();
                        if(pageNumber==1)  visits.clear();
                        visits.addAll( (ArrayList) response.getDataAsList(Constants.VISITS_lIST, Visit.class));

                        if(pageNumber==1 && visits.size()==0) {
                            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.no_results).setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_filter:
                bundle = new Bundle();
                bundle.putSerializable(Constants.VISIT_DATA, visitFilter);
                MyUtils.startRequestActivity(this, VisitFilterActivity.class, Constants.VISIT_FILTER, bundle);
                break;
            case R.id.img_plus:
                MyUtils.startRequestActivity(this, ActivityAddVisits.class, Constants.Visits);
                break;
            case R.id.no_results:
                MyUtils.startRequestActivity(this, ActivityAddVisits.class, Constants.Visits);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Constants.Visits || requestCode == Constants.VISIT_LIST) && resultCode == this.RESULT_OK) {
            setDefaultVisitFilter();
        } else if(requestCode == Constants.VISIT_FILTER && resultCode == this.RESULT_OK) {
            visitFilter = (VisitFilter) data.getExtras().getSerializable(Constants.VISIT_DATA);
            loadVisits(visitFilter);
        }
    }
}
