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
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.adapters.SaleClientDetailsListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivityUniqueSales extends FragmentActivity implements  View.OnClickListener{

    public static final String TAG = ActivityUniqueSales.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private ImageView img_back, img_filter, img_plus;

    private ListView client_sales_listview;
    private SaleClientDetailsListAdapter client_sale_adapter;
    ArrayList<Revenue> revenueList=new ArrayList<Revenue>();
    ArrayList<Line> lines = new ArrayList<Line>();

    int page=1;
    TextView totalRevenue, title;
    Line line;
    Client client;
    String type="line";
    RevenueFilter filter;
    TextView periodLabelTV, no_results;
    String line_id, client_id;
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
            line_id=bundle.getString(Constants.LINE_ID);
        }catch (Exception e){

        }
        try{
            client_id=bundle.getString(Constants.CLIENT_ID);
        }catch (Exception e){

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
        title=(TextView)findViewById(R.id.title) ;

        no_results = (TextView) findViewById(R.id.no_results) ;
        no_results.setOnClickListener(this);

        if(client!=null) title.setText(MyUtils.getClientName(client));
        else if(line!=null) title.setText(line.getName());
        else title.setText("Sales");

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);


        client_sales_listview = (ListView) findViewById(R.id.client_sales_listview);

        client_sale_adapter = new SaleClientDetailsListAdapter(revenueList, null, this);
        client_sales_listview.setAdapter(client_sale_adapter);
        client_sale_adapter.notifyDataSetChanged();

        //if(line_id!=null) filter.setLine_id(line_id);
        //else if(client_id!=null) filter.setClient_id(client_id);

        setLineTotalRevenue();
        loadData();
        updateSalesLabel();

        client_sales_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Revenue item = revenueList.get(position);
               // bundle.putString("revenue_id", item.getId());
                bundle.putSerializable(Constants.REVENUE_DATA, item);
                MyUtils.startRequestActivity(ActivityUniqueSales.this, ActivitySaleDetails.class, Constants.REVENUE_LIST, bundle);
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
        Call<ResponseBody> response = null;

        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        RevenueApi revenueApi = MainApplication.createService(RevenueApi.class);
        if (client == null && line == null) {

            response = revenueApi.getUserAllRevenues(user.getId(), pageNumber, filter);
        }
        else {
            if (line != null) {

                response = revenueApi.getUniqueLineRevenues(line.getId(), pageNumber, filter);
            } else if (client != null) {
                response = revenueApi.getUniqueClientRevenues(client.getId(), pageNumber, filter);
            }
        }

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
                        User.LogOut(ActivityUniqueSales.this);
                        return;
                    }
                    Logger.write("************************** "+response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {

                        numPages=meta.getPage_count();
                        if(pageNumber==1)  {
                            revenueList.clear();
                            lines.clear();
                        }
                        revenueList.addAll( (ArrayList) response.getDataAsList("revenues", Revenue.class));

                        if(pageNumber==1 && revenueList.size()==0) {
                            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        }

                        if (client == null && line == null) {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONObject client_obj = jsonObject.getJSONObject("lines");
                            Line line;
                            Gson gson = new Gson();
                            for (int i = 0; i < revenueList.size(); i++) {
                                Revenue item = revenueList.get(i);
                                line = gson.fromJson(client_obj.getJSONObject(item.getLine_id()).toString(), Line.class);
                                lines.add(line);
                            }
                        }
                        //client_sale_adapter.setLines(lines);
                        client_sale_adapter.notifyDataSetChanged();

                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(ActivityUniqueSales.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(ActivityUniqueSales.this);
                DialogUtils.closeProgress();
            }
        });

        // DialogUtils.displayProgress(this);
    }

    public void setLineTotalRevenue() {
        RevenueApi revenueApi = MainApplication.createService(RevenueApi.class);
        Call<ResponseBody> response = null;

        if (client == null && line == null) {
            response = revenueApi.getTotalRevenues(user.getId(), filter);
        }
        else {
            if (line != null) {
                response = revenueApi.getTotalRevenues("line", line.getId(), filter);
            } else if (client != null) {
                response = revenueApi.getTotalRevenues("client", client.getId(), filter);
            }
        }

        response.enqueue(new Callback<ResponseBody>() {
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
                        User.LogOut(ActivityUniqueSales.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();
                        if(line!=null) {
                            line.setTotal_revenue(response.getData("line", Line.class).getTotal_revenue());
                            totalRevenue.setText(MyUtils.moneyFormat(line.getTotal_revenue()));
                        } else if (client != null){
                            client.setTotal_revenue(response.getData("line", Client.class).getTotal_revenue());
                            totalRevenue.setText(MyUtils.moneyFormat(client.getTotal_revenue()));
                        } else {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            totalRevenue.setText(MyUtils.moneyFormat(jsonObject.getDouble("total")));
                        }

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
                MyUtils.showConnectionErrorToast(ActivityUniqueSales.this);
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

        if((requestCode== Constants.ADD_SALES || requestCode==Constants.REVENUE_LIST) && resultCode==RESULT_OK) {
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
            periodLabelTV.setText("Sales for");
        }
        periodLabelTV.setText(periodLabelTV.getText()+" "+filter.getYear());

    }
}