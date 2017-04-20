package com.tied.android.tiedapp.ui.activities.sales;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.adapters.SaleClientDetailsListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivityLineClientSales extends FragmentActivity implements  View.OnClickListener{

    public static final String TAG = ActivityLineClientSales.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private ImageView img_back, img_filter, img_plus;

    private ListView client_sales_listview;
    private SaleClientDetailsListAdapter client_sale_adapter;
    ArrayList revenueList=new ArrayList<Revenue>();
    TextView totalRevenue, title;
    Line line;
    Client client;
    String type="line";
    RevenueFilter filter;
    TextView periodLabelTV, no_results;
    int source=Constants.SALES_SOURCE;
    private boolean revenueAdded=false;

    int numPages=1;
    private int preLast;
    public int pageNumber=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_client_sale_details);

        bundle = getIntent().getExtras();
        user=MyUtils.getUserFromBundle(bundle);
        try{
            line=(Line)bundle.getSerializable(Constants.LINE_DATA);
            type="line";
        }catch (Exception e){

        }
        try{
            client=(Client)bundle.getSerializable(Constants.CLIENT_DATA);
            if(client!=null) type="client";
        }catch (Exception e){

        }
        try{
            filter=(RevenueFilter)bundle.getSerializable(Constants.FILTER);
        }catch (Exception e){

        }
        if(filter==null) {
            filter=MyUtils.initializeFilter();
        }
        try{
            source=bundle.getInt(Constants.SOURCE);
        }catch (Exception e){

        }


        initComponent();
    }
    private void initComponent() {

         MyUtils.setColorTheme(this, source, findViewById(R.id.main_layout));


        img_back = (ImageView) findViewById(R.id.img_back);
        img_filter = (ImageView) findViewById(R.id.img_filter);
        img_plus = (ImageView) findViewById(R.id.img_plus);
        totalRevenue = (TextView)findViewById(R.id.txt_total_sales);
        periodLabelTV = (TextView) findViewById(R.id.period_label);
        title = (TextView)findViewById(R.id.title);
        no_results = (TextView)findViewById(R.id.no_results);
        no_results.setOnClickListener(this);
        if(client!=null) title.setText(MyUtils.getClientName(client));
        if(line!=null) title.setText(line.getName());

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);


        client_sales_listview = (ListView) findViewById(R.id.client_sales_listview);

        client_sale_adapter = new SaleClientDetailsListAdapter(revenueList, null, this);
        client_sales_listview.setAdapter(client_sale_adapter);
        client_sale_adapter.notifyDataSetChanged();

        loadData();
        updateSalesLabel();
        setLineTotalRevenue();

        client_sales_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                Revenue revenue=(Revenue)revenueList.get(position);
                if (client == null) {
                    Client client=new Client();
                    client.setId(revenue.getClient_id());
                    client.setCompany(revenue.getTitle());
                    bundle.putSerializable(Constants.CLIENT_DATA, client);
                    filter.setLine_id(line.getId());

                } else {
                    Line line=new Line();
                    line.setId(revenue.getLine_id());
                    line.setName(revenue.getTitle());
                    bundle.putSerializable(Constants.LINE_DATA, line);
                   filter.setClient_id(client.getId());
                }
                bundle.putSerializable(Constants.FILTER, filter);

                MyUtils.startActivity(ActivityLineClientSales.this, ActivityUniqueSales.class, bundle);
            }
        });

        client_sales_listview.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                            loadData();
                        }

                        preLast = lastItem;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_filter:
                bundle.putSerializable(Constants.FILTER, filter);
                MyUtils.startRequestActivity(this, ActivitySalesFilter.class, Constants.FILTER_CODE, bundle);
                break;
            case R.id.img_plus:
                MyUtils.startRequestActivity(this, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
            case R.id.no_results:
                MyUtils.startRequestActivity(this, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
        }
    }

    public void loadData() {
        //super.loadData();
        //if(addLinesActivity.getLine()==null) return;
        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        RevenueApi revenueApi = MainApplication.createService(RevenueApi.class);
        String id=(client==null?line.getId():client.getId());
        String type=(client==null?"line":"client");
        final Call<ResponseBody> response = revenueApi.getLineRevenues( type, id, pageNumber, filter);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());

                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(ActivityLineClientSales.this);
                        return;
                    }
                    Logger.write("************************** "+response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        numPages=meta.getPage_count();
                        //revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        //client_sale_adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                       // topRevenues.clear();
                        //topRevenuesName.clear();
                        List<Object> keys=response.getKeys();
                        JSONObject map=response.getKeyObjects();
                        //double total=0;
                        for(Object keyObject:keys) {
                            Map<String, Object> obj = MyUtils.MapObject.create(keyObject.toString());
//                            Logger.write(map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")).toString());
                            // lines.add((Line)map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")));
                            Revenue revenue = new Revenue();
                            if(line==null) {
                                Line mLine;
                                try {
                                    mLine = gson.fromJson(map.getString(obj.get("key").toString()), Line.class);
                                }catch (JSONException je) {
                                    mLine=new Line();
                                    mLine.setName("<Deleted>");
                                    mLine.setId(obj.get("key").toString());
                                }
                                Float val=0.0f;
                                try {
                                     val = Float.parseFloat("" + (Double) obj.get("value"));
                                }catch (Exception e) {

                                }
                                mLine.setTotal_revenue(val);
                                //total=total+val;
                                //topRevenues.add(val);
                                //topRevenuesName.add(MyUtils.getClientName(client));
                                //clients.add(client);
                                Logger.write("" + val);

                                revenue.setClient_id(client.getId());
                                revenue.setValue(val);
                                revenue.setDate_sold("");
                                revenue.setLine_id(mLine.getId());
                                revenue.setTitle(mLine.getName());
                            }else {
                                Client client;
                                try {
                                    client = gson.fromJson(map.getString(obj.get("key").toString()), Client.class);
                                } catch (JSONException je) {
                                    client = new Client();
                                    client.setCompany("<Deleted>");
                                    client.setId(obj.get("key").toString());
                                }
                                Float val=0.0f;
                                try {
                                  val = Float.parseFloat("" + (Double) obj.get("value"));
                                }catch (Exception e) {

                                }
                                client.setTotal_revenue(val);
                                //total=total+val;
                                //topRevenues.add(val);
                                //topRevenuesName.add(MyUtils.getClientName(client));
                                //clients.add(client);
                                Logger.write("" + val);

                                revenue.setClient_id(client.getId());
                                revenue.setValue(val);
                                revenue.setDate_sold("");
                                revenue.setLine_id(line.getId());
                                revenue.setTitle(MyUtils.getClientName(client));
                            }

                            revenueList.add(revenue);
                        }
                        client_sale_adapter.notifyDataSetChanged();

                        if(pageNumber==1 && revenueList.size()==0) {
                            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        }
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(ActivityLineClientSales.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(ActivityLineClientSales.this);
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }

    public void setLineTotalRevenue() {
        RevenueApi revenueApi = MainApplication.createService(RevenueApi.class);
        String id=(client==null?line.getId():client.getId());
        String type=(client==null?"line":"client");
        final Call<ResponseBody> response2 = revenueApi.getTotalRevenues(type, id,filter);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    // Logger.write("RESPONSSSSSSSSSSSSSSSSSSSS "+response.toString());
                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(ActivityLineClientSales.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();
                        if(line!=null) {
                            line.setTotal_revenue(response.getData("line", Line.class).getTotal_revenue());
                            totalRevenue.setText(MyUtils.moneyFormat(line.getTotal_revenue()));
                        }else{
                            client.setTotal_revenue(response.getData("line", Client.class).getTotal_revenue());
                            totalRevenue.setText(MyUtils.moneyFormat(client.getTotal_revenue()));
                        }
                        //totalRevenueBodyTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));

                    } else {
                        // MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    // MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                    //Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(ActivityLineClientSales.this);
                DialogUtils.closeProgress();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
       // revenueList.clear();
       // loadData();
       // setLineTotalRevenue();
    }

    public void goBack(View v) {
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
     // if(revenueAdded) {
            Intent intent = new Intent();
            Bundle b =new Bundle();
            b.putSerializable(Constants.LINE_DATA, line);

            intent.putExtras(b);
            setResult(RESULT_OK, intent);
            finishActivity(Constants.ADD_SALES);
            finish();
            return;
       // }
        //super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== Constants.ADD_SALES && resultCode==RESULT_OK) {
           // Logger.write("REgdlkadf ajsdpfjasdf "+requestCode+":"+RESULT_OK);
            revenueAdded=true;
            revenueList.clear();
            client_sale_adapter.notifyDataSetChanged();
            loadData();
            setLineTotalRevenue();
        }
        Logger.write("REgdlkadf ajsdpfjasdf "+requestCode+":"+RESULT_OK);
        if(requestCode==Constants.FILTER_CODE && resultCode== Activity.RESULT_OK) {
            Logger.write("REgdlkadf ajsdpfjasdf "+requestCode+":"+RESULT_OK);
            revenueList.clear();
            client_sale_adapter.notifyDataSetChanged();
            filter = (RevenueFilter) (data.getSerializableExtra(Constants.FILTER));
            updateSalesLabel();
            loadData();
            setLineTotalRevenue();
        }
    }
    private void updateSalesLabel() {
        if(filter.getQuarter()==0 && filter.getMonth()!=0) {
            //int position= Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(endMonth)-1;


            periodLabelTV.setText(HelperMethods.MONTHS_LIST[filter.getMonth()-1]);
        }else if(filter.getQuarter()!=0 && filter.getMonth()==0) {
            //int position= Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(endMonth)-1;
            switch(filter.getQuarter()) {
                case 1:
                    periodLabelTV.setText(HelperMethods.MONTHS_LIST[0]+"-"+HelperMethods.MONTHS_LIST[2]);
                    break;
                case 2:
                    periodLabelTV.setText(HelperMethods.MONTHS_LIST[3]+"-"+HelperMethods.MONTHS_LIST[5]);
                    break;
                case 3:
                    periodLabelTV.setText(HelperMethods.MONTHS_LIST[6]+"-"+HelperMethods.MONTHS_LIST[8]);
                    break;
                case 4:
                    periodLabelTV.setText(HelperMethods.MONTHS_LIST[9]+"-"+HelperMethods.MONTHS_LIST[11]);
                    break;
            }

        }else{
            periodLabelTV.setText("All time sales");
        }
        periodLabelTV.setText(periodLabelTV.getText()+" "+filter.getYear());

    }
}