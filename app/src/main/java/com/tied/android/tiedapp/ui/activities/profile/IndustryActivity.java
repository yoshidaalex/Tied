package com.tied.android.tiedapp.ui.activities.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.adapters.ClientTerritoriesAdapter;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class IndustryActivity extends AppCompatActivity implements View.OnClickListener{

    public FragmentIterationListener mListener;

    private Bundle bundle;
    private TextView txt_done;

    ListView line_listview;
    ClientTerritoriesAdapter territoriesAdapter;
    ArrayList<Territory> territoryModels = new ArrayList<Territory>();
    ArrayList selectedObjects = new ArrayList();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        initComponent();
    }

    public void initComponent() {
        txt_done = (TextView) findViewById(R.id.txt_done);
        txt_done.setOnClickListener(this);

        line_listview = (ListView) findViewById(R.id.list);

//        for (int i = 0 ; i < 10; i++) {
//            Territory territoryModel = new Territory();
//
//            territoryModel.set.setTerritory_name("lroko Technologies LLC");
//
//            territoryModels.add(territoryModel);
//        }

        territoriesAdapter = new ClientTerritoriesAdapter(territoryModels, selectedObjects , this, false);
        line_listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if (territoryModels.get(position).isCheck_status()) {
                    territoryModels.get(position).setCheck_status(false);
                } else {
                    territoryModels.get(position).setCheck_status(true);
                }*/

                territoriesAdapter.notifyDataSetChanged();
            }
        });

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* if (territoryModels.get(position).isCheck_status()) {
                    territoryModels.get(position).setCheck_status(false);
                } else {
                    territoryModels.get(position).setCheck_status(true);
                }
 */
                territoriesAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_done:
                finish();
                break;
        }
    }
}
