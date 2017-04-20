package com.tied.android.tiedapp.ui.fragments.sales;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ClientDataModel;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesPrint;
import com.tied.android.tiedapp.ui.adapters.SaleClientListAdapter;
import com.tied.android.tiedapp.ui.adapters.SaleLineListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class SaleViewAllFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = SaleViewAllFragment.class
            .getSimpleName();

    private Bundle bundle;

    public  RevenueFilter  filter =null;
    private static SaleViewAllFragment saleViewAllFragment;
    public FragmentIterationListener mListener;

    private ImageView img_back, img_plus, img_filter;
    private LinearLayout line_layout;

    private SaleLineListAdapter line_adapter;
    private SaleClientListAdapter client_adapter;
    private ListView lines_listview;
    String group_by="line";
    ArrayList<Line> lineDataModels = new ArrayList<>();
    ArrayList<Client> clientDataModels = new ArrayList<>();
    User user, userLoggedIn;
    TextView titleTV, totalSalesTV, totalSalesLabelTV;

    int numPages=1;
    private int preLast;
    public int pageNumber=1;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new SaleViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SaleViewAllFragment  getInstance() {
        return saleViewAllFragment;
    }
    public void setFilter(RevenueFilter f) {
        filter=f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        saleViewAllFragment=this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_view_all, null);


        bundle=getArguments();
        MyUtils.setColorTheme(getActivity(), bundle.getInt(Constants.SOURCE), view.findViewById(R.id.main_layout));
        user = MyUtils.getUserFromBundle(bundle);

        group_by=bundle.getString("group_by");
        if(group_by==null) group_by="line";

        String todaysDate=HelperMethods.getTodayDate();
        filter=MyUtils.initializeFilter();
        userLoggedIn=MyUtils.getUserLoggedIn();
        initComponent(view);
        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        //img_back = (ImageView) view.findViewById(R.id.img_back);
        img_filter = (ImageView) view.findViewById(R.id.img_filter);
        img_plus = (ImageView) view.findViewById(R.id.img_plus);

        //img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);
        titleTV=(TextView) view.findViewById(R.id.title);
        totalSalesTV = (TextView)  view.findViewById(R.id.totalSales);
        totalSalesLabelTV = (TextView) view.findViewById(R.id.totalSalesLabel);
        totalSalesLabelTV.setText("All time sales");


        line_layout = (LinearLayout) view.findViewById(R.id.line_layout);

        lines_listview = (ListView) view.findViewById(R.id.lines_listview);
        lines_listview.setOnItemClickListener(this);

        lines_listview.setOnScrollListener(new AbsListView.OnScrollListener() {

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

        if(group_by.equals("line")) {
            line_adapter = new SaleLineListAdapter(1, lineDataModels, getActivity(), Constants.SALES_SOURCE);
            lines_listview.setAdapter(line_adapter);
            titleTV.setText("Sales by Line");
            loadData();

        }else{
            client_adapter=new SaleClientListAdapter(clientDataModels, getActivity(), Constants.SALES_SOURCE);
            lines_listview.setAdapter(client_adapter);
            titleTV.setText("Sales by Client");
            loadClientData();
        }
        updateSalesLabel();

    }
    public void load() {
        if(group_by.equals("line")) {
            //line_adapter = new SaleLineListAdapter(1, lineDataModels, getActivity());
            lines_listview.setAdapter(line_adapter);

            loadData();

        }else{
            //client_adapter=new SaleClientListAdapter(clientDataModels, getActivity());
            lines_listview.setAdapter(client_adapter);
            //titleTV.setText("Sales by Client");
            loadClientData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                //((MainActivity) getActivity()).launchFragment(Constants.HomeSale, bundle);
                break;
            case R.id.img_filter:
                //Bundle bundle2=new Bundle();
                bundle.putSerializable(Constants.FILTER, filter);
                MyUtils.startRequestActivity(getActivity(), ActivitySalesFilter.class, Constants.FILTER_CODE, bundle);
                break;
            case R.id.img_printer:
                MyUtils.startActivity(getActivity(), ActivitySalesPrint.class);
                break;
            case R.id.img_plus:
                MyUtils.startActivity(getActivity(), ActivityAddSales.class, bundle);
                break;
        }
    }

    public void loadData() {
        // super.loadData();
        // if(addLinesActivity.getLine()==null) return;
        // Logger.write("Loading data");
        DialogUtils.displayProgress(getActivity());
        RevenueApi lineApi = MainApplication.createService(RevenueApi.class);
        final Call<ResponseBody> response = lineApi.getRevenueByGroup( user.getId(), "line", filter);
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
                        //User.LogOut(LineRevenueActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {

                        numPages=meta.getPage_count();

                        List<Object> keys=response.getKeys();
                        JSONObject map=response.getKeyObjects();

                        List<Line> lines = new ArrayList<Line>(keys.size());
                        Gson gson = new Gson();

                        double total=0;
                        for(Object keyObject:keys) {
                            try {
                                Map<String, Object> obj = MyUtils.MapObject.create(keyObject.toString());
//                            Logger.write(map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")).toString());
                                // lines.add((Line)map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")));
                                Line line = gson.fromJson(map.getString(obj.get("key").toString()), Line.class);
                                Float val = Float.parseFloat("" + (Double) obj.get("value"));
                                line.setTotal_revenue(val);
                                total = total + val;

                                lines.add(line);
                            }catch (Exception e) {
                                Logger.write(e);
                            }
                        }
                        totalSalesTV.setText(MyUtils.moneyFormat(total));

                        lineDataModels.addAll(lines);

                        line_adapter.notifyDataSetChanged();
                        if(pageNumber==1 && lineDataModels.size()==0) {
                            MyUtils.showToast("No sales recorded for this period");
                        }

                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(getActivity());
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(getActivity());
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }
    public void loadClientData() {
        // super.loadData();
        // if(addLinesActivity.getLine()==null) return;
        // Logger.write("Loading data");
        DialogUtils.displayProgress(getActivity());
        RevenueApi lineApi = MainApplication.createService(RevenueApi.class);

        final Call<ResponseBody> response = lineApi.getRevenueByGroup(user.getId(), "client", filter);
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
                        //User.LogOut(LineRevenueActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {

                        List<Object> keys=response.getKeys();
                        JSONObject map=response.getKeyObjects();

                        List<Client> clients = new ArrayList<Client>(keys.size());
                        Gson gson = new Gson();

                        double total=0;
                        for(Object keyObject:keys) {
                            Map<String, Object> obj = MyUtils.MapObject.create(keyObject.toString());
                            // Logger.write(map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")).toString());
                            // lines.add((Line)map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")));
                            Client client =gson.fromJson(map.getString(obj.get("key").toString()), Client.class);
                            Float val=Float.parseFloat(""+(Double)obj.get("value"));
                            client.setTotal_revenue(val);
                            total=total+val;

                            clients.add(client);
                        }
                        totalSalesTV.setText(MyUtils.moneyFormat(total));
                        clientDataModels.addAll(clients);
                        //
                        client_adapter.notifyDataSetChanged();
                        if(clientDataModels.size()==0) {
                            MyUtils.showToast("No sales recorded for this period");
                        }
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(getActivity());
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(getActivity());
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }

    private void updateSalesLabel() {
        if(filter.getQuarter()==0 && filter.getMonth()!=0) {
           //int position= Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(endMonth)-1;


                totalSalesLabelTV.setText(HelperMethods.MONTHS_LIST[filter.getMonth()-1]);
        }else if(filter.getQuarter()!=0 && filter.getMonth()==0) {
            //int position= Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(endMonth)-1;
            switch(filter.getQuarter()) {
                case 1:
                    totalSalesLabelTV.setText(HelperMethods.MONTHS_LIST[0]+"-"+HelperMethods.MONTHS_LIST[2]);
                    break;
                case 2:
                    totalSalesLabelTV.setText(HelperMethods.MONTHS_LIST[3]+"-"+HelperMethods.MONTHS_LIST[5]);
                    break;
                case 3:
                    totalSalesLabelTV.setText(HelperMethods.MONTHS_LIST[6]+"-"+HelperMethods.MONTHS_LIST[9]);
                    break;
                case 4:
                    totalSalesLabelTV.setText(HelperMethods.MONTHS_LIST[10]+"-"+HelperMethods.MONTHS_LIST[12]);
                    break;
            }

        }else{
            totalSalesLabelTV.setText("All time sales");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.FILTER_CODE && resultCode== Activity.RESULT_OK) {
            lineDataModels.clear();
            clientDataModels.clear();
            filter = (RevenueFilter) (data.getSerializableExtra(Constants.FILTER));
            updateSalesLabel();
            load();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
