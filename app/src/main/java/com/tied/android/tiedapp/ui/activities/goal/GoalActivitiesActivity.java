package com.tied.android.tiedapp.ui.activities.goal;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ClientSaleDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.ui.adapters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Femi on 7/30/2016.
 */
public class GoalActivitiesActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = GoalActivitiesActivity.class.getSimpleName();
    private Bundle bundle;
    private User user;

    ExpandableListView expandableListView;
    ImageView img_close;

    ExpendableActivitiesListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, ArrayList<ClientSaleDataModel>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_goal_activities);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        initComponent();
    }

    private void initComponent() {

        img_close =(ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
        }
    }

}
