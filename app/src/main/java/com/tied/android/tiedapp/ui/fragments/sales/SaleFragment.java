package com.tied.android.tiedapp.ui.fragments.sales;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
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
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.report.ReportActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityGroupedSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesPrint;
import com.tied.android.tiedapp.ui.adapters.*;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class SaleFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {

    public static final String TAG = SaleFragment.class
            .getSimpleName();

    private Bundle bundle;

    public FragmentIterationListener mListener;

    private ImageView img_segment, img_printer, img_plus, img_filter;
    private LinearLayout sub_layout, line_layout, client_layout;

    private SaleLineListAdapter line_adapter;
    private SaleClientListAdapter client_adapter;

    private ListView lines_listview, client_listview;

    private TextView txt_view_all;
    private PieChart mChart;
    List<Line> lineDataModels = new ArrayList<>();
    ArrayList<Client> clientDataModels = new ArrayList<>();
    User user;
    boolean bLine = true;
    String start, stop;
    String month, year;
    String group_by="line";
    RevenueFilter filter=MyUtils.initializeFilter();

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D"
    };

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new SaleFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sale_home, null);

        bundle=getArguments();
        user=MyUtils.getUserFromBundle(bundle);

        String today=HelperMethods.getTodayDate();
        start=HelperMethods.getMonthOfTheYear(today)+" "+today;
        Logger.write("8888888888888888888888 "+today);

        this.month = (HelperMethods.getMonthOfTheYear(today) );
        this.year = today.substring(0, 4);

        Log.d(TAG, "AM HERE AGAIN");
        view.findViewById(R.id.img_segment).postDelayed(new Runnable() {
            @Override
            public void run() {
                initComponent(view);
            }
        }, 800);

        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        bundle = getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.green_color1));
        }

        img_segment = (ImageView) view.findViewById(R.id.img_segment);
        img_segment.setBackgroundResource(R.drawable.line);
        img_segment.setOnClickListener(this);

        img_printer = (ImageView) view.findViewById(R.id.img_printer);
        img_filter = (ImageView) view.findViewById(R.id.img_filter);
        img_plus = (ImageView) view.findViewById(R.id.img_plus);

        img_printer.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);

        sub_layout = (LinearLayout) view.findViewById(R.id.sub_layout);
        sub_layout.setVisibility(View.GONE);

        line_layout = (LinearLayout) view.findViewById(R.id.line_layout);
        client_layout = (LinearLayout) view.findViewById(R.id.client_layout);
        client_layout.setVisibility(View.GONE);

        txt_view_all = (TextView) view.findViewById(R.id.txt_view_all);
        txt_view_all.setOnClickListener(this);
        txt_view_all.setVisibility(View.INVISIBLE);

        lines_listview = (ListView) view.findViewById(R.id.lines_listview);
        client_listview = (ListView) view.findViewById(R.id.client_listview);
        loadData();

        line_adapter = new SaleLineListAdapter(0, lineDataModels, getActivity());
        lines_listview.setAdapter(line_adapter);
        line_adapter.notifyDataSetChanged();



        client_adapter = new SaleClientListAdapter(clientDataModels, getActivity());

        mChart = (PieChart) view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDrawSliceText(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

//        setData(3, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        Logger.write("It is created again");
    }


    @Override
    public void onClick(View v) {
        final Bundle bundle=new Bundle();
        bundle.putInt(Constants.SOURCE, Constants.SALES_SOURCE);
        switch (v.getId()) {
            case R.id.img_segment:
                bLine = !bLine;
                if (bLine) {
                    //line_layout.setVisibility(View.VISIBLE);

                    //sub_layout.setVisibility(View.GONE);
                   // client_layout.setVisibility(View.GONE);
                    this.lines_listview.setAdapter(this.line_adapter);
                    group_by="line";
                    loadData();
                    img_segment.setBackgroundResource(R.drawable.line);
                } else {
                    //line_layout.setVisibility(View.GONE);

                   // sub_layout.setVisibility(View.VISIBLE);
                   // client_layout.setVisibility(View.VISIBLE);
                    group_by="client";
                    this.lines_listview.setAdapter(this.client_adapter);
                    loadClientData();
                    img_segment.setBackgroundResource(R.drawable.clients);
                }
                break;
            case R.id.img_filter:


                MyUtils.startActivity(getActivity(), ActivitySalesFilter.class, bundle);
                break;
            case R.id.img_printer:
               // MyUtils.startActivity(getActivity(), ActivitySalesPrint.class, bundle);
                MyUtils.startActivity(getActivity(), ReportActivity.class);
                break;
            case R.id.img_plus:
               MyUtils.initiateAddSales(getActivity(), bundle);
                break;
            case R.id.txt_view_all:
                bundle.putString("group_by", group_by);

                //((MainActivity) getActivity()).launchFragment(Constants.SaleViewAll, bundle);
                MyUtils.startActivity(getActivity(), ActivityGroupedSales.class,  bundle);
                break;
        }
    }

    List<Float> topRevenues = new ArrayList<>();
    List<String> topRevenuesName = new ArrayList<>();
    private void setData() {

        //float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (Float revenue:topRevenues) {
            yVals1.add(new Entry(revenue, topRevenues.indexOf(revenue)));
        }
        int count=topRevenues.size();

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            try{
                xVals.add(topRevenuesName.get(i).substring(0, 10));
            }catch (Exception e) {
                xVals.add(topRevenuesName.get(i));
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Total Revenues");
        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(String line1, String line2) {

        if(line2==null) line2="Total sales for "+month.substring(0, 3)+" "+year;
        int len = line1.length();
        SpannableString s = new SpannableString(line1+"\n"+line2);
        s.setSpan(new RelativeSizeSpan(15f/len), 0, len, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), len, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), len, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.8f), len, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
    public void loadData() {
        // super.loadData();
        // if(addLinesActivity.getLine()==null) return;
        // Logger.write("Loading data");
        DialogUtils.displayProgress(getActivity());
        RevenueApi lineApi = MainApplication.createService(RevenueApi.class);
        final Call<ResponseBody> response = lineApi.getTopLineRevenues(user.getId(), "line", filter);
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
                        User.LogOut(getActivity());
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {

                        List<Object> keys=response.getKeys();
                        JSONObject map=response.getKeyObjects();
                        int size=keys.size();
                        if(size==0) txt_view_all.setVisibility(View.INVISIBLE);
                        else  txt_view_all.setVisibility(View.VISIBLE);
                        List<Line> lines = new ArrayList<Line>(size);
                        Gson gson = new Gson();
                        topRevenues.clear();
                        topRevenuesName.clear();
                        double total=0;
                        int count=0;
                        Line others=new Line();
                        others.setName("Others");
                        others.setId("others");
                        others.setTotal_revenue(0);
                        for(Object keyObject:keys) {
                            Map<String, Object> obj = MyUtils.MapObject.create(keyObject.toString());
                            //Logger.write(map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")).toString());
                            // lines.add((Line)map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")));
                            Float val=Float.parseFloat(""+(Double)obj.get("value"));
                            Line line;
                            try {
                                line = gson.fromJson(map.getString(obj.get("key").toString()), Line.class);
                                line.setTotal_revenue(val);
                            }catch (Exception e) {
                                line=new Line();
                                line.setName("<Deleted Line>");
                                line.setTotal_revenue(val);
                                line.setId(obj.get("key").toString());
                            }
                            total=total+val;

                            if(count<4) {
                                lines.add(line);
                                topRevenues.add(val);
                                topRevenuesName.add(line.getName());
                            }
                            else others.setTotal_revenue(others.getTotal_revenue()+val);
                            count++;
                        }
                        topRevenues.add(Float.valueOf(""+others.getTotal_revenue()));
                        topRevenuesName.add("Others");
                        setData();
                        mChart.setCenterText(generateCenterSpannableText(MyUtils.moneyFormat(total), null));
                        //Map mapObject = MyUtils.MapObject.create(response.toString());
                        //Logger.write(.toString());
                        lineDataModels.clear();
                        lineDataModels.addAll(lines);
                        if(others.getTotal_revenue()>0) lineDataModels.add(others);
                        //
                        line_adapter.notifyDataSetChanged();
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

    public void refresh() {
        Logger.write(this.getClass().getCanonicalName());
        if( group_by=="client") {
            loadClientData();
        }else{
            loadData();
        }
        MainActivity.getInstance().refresh.setRefreshing(false);
    }
    public void loadClientData() {
        // super.loadData();
        // if(addLinesActivity.getLine()==null) return;
        // Logger.write("Loading data");
        DialogUtils.displayProgress(getActivity());
        RevenueApi lineApi = MainApplication.getInstance().getRetrofit().create(RevenueApi.class);

        final Call<ResponseBody> response = lineApi.getTopLineRevenues(user.getId(), "client", filter);
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
                        int size=keys.size();
                        if(size==0) txt_view_all.setVisibility(View.INVISIBLE);
                        else  txt_view_all.setVisibility(View.VISIBLE);
                        List<Client> clients = new ArrayList<Client>(size);
                        Gson gson = new Gson();
                        topRevenues.clear();
                        topRevenuesName.clear();
                        double total=0;
                        int count=0;
                        Client others=new Client();
                        others.setCompany("Others");
                        others.setId("others");
                        others.setTotal_revenue(0);
                        for(Object keyObject:keys) {
                            Map<String, Object> obj = MyUtils.MapObject.create(keyObject.toString());
//                            Logger.write(map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")).toString());
                            // lines.add((Line)map.get(MyUtils.MapObject.create(keyObject.toString()).get("key")));
                            Float val=Float.parseFloat(""+(Double)obj.get("value"));
                            Client client;
                            try {
                                client =gson.fromJson(map.getString(obj.get("key").toString()), Client.class);
                                client.setTotal_revenue(val);
                            }catch (Exception e) {
                                client=new Client();
                                client.setCompany("<Deleted Client>");
                                client.setTotal_revenue(val);
                                client.setId(obj.get("key").toString());
                            }

                            total=total+val;


                            if(count<4){
                                clients.add(client);
                                topRevenues.add(val);
                                topRevenuesName.add(MyUtils.getClientName(client));
                            }
                            else others.setTotal_revenue(others.getTotal_revenue()+val);
                            count ++;
                        }
                        topRevenues.add(Float.valueOf(""+others.getTotal_revenue()));
                        topRevenuesName.add("Others");
                        setData();
                        mChart.setCenterText(generateCenterSpannableText(MyUtils.moneyFormat(total), null));
                        //Map mapObject = MyUtils.MapObject.create(response.toString());
                        //Logger.write(.toString());
                        clientDataModels.clear();
                        clientDataModels.addAll(clients);
                        if(others.getTotal_revenue()>0) clientDataModels.add(others);
                        //
                        client_adapter.notifyDataSetChanged();
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
}
