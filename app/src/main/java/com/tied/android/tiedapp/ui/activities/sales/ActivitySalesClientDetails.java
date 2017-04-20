package com.tied.android.tiedapp.ui.activities.sales;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.SaleLineDetailsListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivitySalesClientDetails extends FragmentActivity implements  View.OnClickListener{

    public static final String TAG = ActivitySalesClientDetails.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private ImageView img_back, img_filter, img_plus;

    private ListView clients_listview;
    private SaleLineDetailsListAdapter line_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_client_details);

        bundle = getIntent().getExtras();

        initComponent();
    }
    private void initComponent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green_color1));
        }

        img_back = (ImageView) findViewById(R.id.img_back);
        img_filter = (ImageView) findViewById(R.id.img_filter);
        img_plus = (ImageView) findViewById(R.id.img_plus);

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);

        clients_listview = (ListView) findViewById(R.id.clients_listview);

        ArrayList<LineDataModel> lineDataModels = new ArrayList<>();

        for (int i = 0 ; i < 7 ; i++) {
            LineDataModel lineDataModel = new LineDataModel();

            if (i < 2) {
                lineDataModel.setLine_name("Last Year (YTD)");
                lineDataModel.setLine_date("Monthly numbers from last year");
            }
            else  {
                lineDataModel.setLine_name("CREATIVE CO-OP");
                lineDataModel.setLine_date("50 sales");
            }

            lineDataModel.setPercent("48");
            lineDataModel.setPrice("$1,200,400");

            lineDataModels.add(lineDataModel);
        }

        line_adapter = new SaleLineDetailsListAdapter(lineDataModels, this);
        clients_listview.setAdapter(line_adapter);
        line_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_filter:
                MyUtils.startActivity(this, ActivitySalesFilter.class);
                break;
            case R.id.img_plus:
                MyUtils.startActivity(this, ActivityAddSales.class);
                break;
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}