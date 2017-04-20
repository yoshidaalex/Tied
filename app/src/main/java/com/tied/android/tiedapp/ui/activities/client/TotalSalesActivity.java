package com.tied.android.tiedapp.ui.activities.client;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ClientSaleDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.adapters.ExpendableActivitiesListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by femi on 9/4/2016.
 */
public class TotalSalesActivity extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;

    int page=1;

    ImageView img_back, img_filter, img_plus;
    TextView name, totalRevenue, txt_last_year, txt_this_year;
    ExpandableListView expandableListView;

    ExpendableActivitiesListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, ArrayList<ClientSaleDataModel>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_sales);

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

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareListData();

        listAdapter = new ExpendableActivitiesListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);

        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<ClientSaleDataModel>>();

        // Adding child data
        listDataHeader.add("May 2016");
        listDataHeader.add("July 2016");

        ArrayList<ClientSaleDataModel> clientSaleDataModels = new ArrayList<ClientSaleDataModel>();

        for (int i = 0 ; i < 5 ; i++) {
            ClientSaleDataModel clientSaleDataModel = new ClientSaleDataModel();

            clientSaleDataModel.setPrice("+ 230 USD");
            clientSaleDataModel.setDate("19 February 2016");
            clientSaleDataModel.setSummary("This is a note added by the user");

            clientSaleDataModels.add(clientSaleDataModel);
        }

        listDataChild.put(listDataHeader.get(0), clientSaleDataModels); // Header, Child data
        listDataChild.put(listDataHeader.get(1), clientSaleDataModels); // Header, Child data

    }
}
