package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.adapters.SaleLineDetailsListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 9/4/2016.
 */
public class LineNewRevenueActivity extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;

    int page=1;

    ImageView img_back, img_filter, img_plus;
    TextView name, totalRevenue, txt_last_year, txt_this_year, txt_add_sale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_new_revenue);

        bundle = new Bundle();
        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        initComponents();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_plus:
                bundle.putInt(Constants.SHOW_SALE, 1);
                MyUtils.startRequestActivity(this, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
            case R.id.img_filter:
                bundle.putInt(Constants.SHOW_FILTER, 1);
                MyUtils.startActivity(this, ActivitySalesFilter.class, bundle);
                break;
            case R.id.txt_last_year:
                /*MyUtils.showLineNewRevenueDialog(this, "Last", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                    }
                });*/
                break;
            case R.id.txt_this_year:
                /*MyUtils.showLineNewRevenueDialog(this, "This", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                    }
                });*/
                break;
            case R.id.txt_add_new_sale:
                bundle.putInt(Constants.SHOW_SALE, 1);
                MyUtils.startRequestActivity(this, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
        }
    }

    public void initComponents() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        img_filter = (ImageView) findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this);

        img_plus = (ImageView) findViewById(R.id.img_plus);
        img_plus.setOnClickListener(this);

        name = (TextView) findViewById(R.id.name);
        totalRevenue = (TextView) findViewById(R.id.total_revenue_txt);

        txt_last_year = (TextView) findViewById(R.id.txt_last_year);
        txt_last_year.setOnClickListener(this);

        txt_this_year = (TextView) findViewById(R.id.txt_this_year);
        txt_this_year.setOnClickListener(this);

        txt_add_sale = (TextView) findViewById(R.id.txt_add_new_sale);
        txt_add_sale.setOnClickListener(this);
    }

}
