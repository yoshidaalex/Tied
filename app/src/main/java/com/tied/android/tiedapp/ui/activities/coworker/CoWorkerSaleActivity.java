package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.adapters.SaleLineListAdapter;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 9/9/2016.
 */
public class CoWorkerSaleActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = CoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;

    public FragmentIterationListener mListener;

    private ImageView img_back, img_plus, img_filter;
    private LinearLayout top_layout;

    private SaleLineListAdapter line_adapter;

    private ListView lines_listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sale_view_all);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        bundle = new Bundle();

        top_layout = (LinearLayout) findViewById(R.id.top_layout);
        top_layout.setBackgroundResource(R.drawable.background_gradient);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_filter = (ImageView) findViewById(R.id.img_filter);
        img_plus = (ImageView) findViewById(R.id.img_plus);

//        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);

        lines_listview = (ListView) findViewById(R.id.lines_listview);

        ArrayList<Line> lineDataModels = new ArrayList<>();

        for (int i = 0 ; i < 6 ; i++) {
            Line lineDataModel = new Line();

            if (i < 2) {
                lineDataModel.setName("Last Year (YTD)");
                //lineDataModel.setLine_date("Monthly numbers from last year");
            }
            else  {
                lineDataModel.setName("Creative Co-op");
                //lineDataModel.setLine_date("Last sale : 5 days ago");
            }

            //lineDataModel.setPercent("48");
            lineDataModel.setTotal_revenue(233333);

            lineDataModels.add(lineDataModel);
        }

        line_adapter = new SaleLineListAdapter(1, lineDataModels, this);
        lines_listview.setAdapter(line_adapter);
        line_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_filter:
                bundle.putInt(Constants.SHOW_FILTER, 2);
                MyUtils.startActivity(this, ActivitySalesFilter.class, bundle);
                break;
            case R.id.img_plus:
                bundle.putInt(Constants.SHOW_SALE, 2);
                MyUtils.startActivity(this, ActivityAddSales.class, bundle);
                break;
        }
    }
}
