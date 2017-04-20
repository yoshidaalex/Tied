package com.tied.android.tiedapp.ui.activities.sales;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.SalePrintListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivitySalesPrint extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;

    private ImageView img_back, img_filter;

    private ListView month_listview;
    private SalePrintListAdapter line_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_print_report);

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

        img_back.setOnClickListener(this);

        month_listview = (ListView) findViewById(R.id.month_listview);

        ArrayList<LineDataModel> lineDataModels = new ArrayList<LineDataModel>();

        for (int i = 0 ; i < 7 ; i++) {
            LineDataModel lineDataModel = new LineDataModel();

            lineDataModel.setLine_name("January");
            lineDataModel.setLine_date("$35,000");
            lineDataModel.setPrice(":35 Clients");

            lineDataModels.add(lineDataModel);
        }

        line_adapter = new SalePrintListAdapter(lineDataModels, this);
        month_listview.setAdapter(line_adapter);
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
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}