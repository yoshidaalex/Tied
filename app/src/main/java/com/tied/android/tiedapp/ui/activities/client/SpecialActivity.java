package com.tied.android.tiedapp.ui.activities.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ClientSaleDataModel;
import com.tied.android.tiedapp.customs.model.SpecialDataModel;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.ExpendableActivitiesListAdapter;
import com.tied.android.tiedapp.ui.adapters.ExpendableSpecialListAdapter;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;
import com.tied.android.tiedapp.ui.fragments.client.ViewClientFragment;
import com.tied.android.tiedapp.ui.fragments.signups.TerritoryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 5/3/2016.
 */
public class SpecialActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = SpecialActivity.class
            .getSimpleName();


    private User user;
    private Bundle bundle;

    ExpandableListView expandableListView;
    ExpendableSpecialListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, ArrayList<SpecialDataModel>> listDataChild;

    ImageView img_close, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);

        setContentView(R.layout.activity_special);
        bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareListData();

        listAdapter = new ExpendableSpecialListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);

        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.add:
                break;
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<SpecialDataModel>>();

        // Adding child data
        listDataHeader.add("ACTIVE");
        listDataHeader.add("ENDED");

        ArrayList<SpecialDataModel> specialDataModels = new ArrayList<SpecialDataModel>();

        for (int i = 0 ; i < 3 ; i++) {
            SpecialDataModel specialDataModel = new SpecialDataModel();

            specialDataModel.setTitle("Name of Special goes here");
            specialDataModel.setDate_range("Jul 8 - Aug 10");
            specialDataModel.setClient_count("20");

            specialDataModels.add(specialDataModel);
        }

        ArrayList<SpecialDataModel> specialDataModels1 = new ArrayList<SpecialDataModel>();

        for (int i = 0 ; i < 3 ; i++) {
            SpecialDataModel specialDataModel = new SpecialDataModel();

            specialDataModel.setTitle("Name overflow looks like this...");
            specialDataModel.setDate_range("Jul 11 - Jul 22");
            specialDataModel.setClient_count("30");

            specialDataModels1.add(specialDataModel);
        }


        listDataChild.put(listDataHeader.get(0), specialDataModels); // Header, Child data
        listDataChild.put(listDataHeader.get(1), specialDataModels1); // Header, Child data

    }
}

