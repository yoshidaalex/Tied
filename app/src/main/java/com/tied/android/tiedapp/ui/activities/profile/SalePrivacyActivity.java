package com.tied.android.tiedapp.ui.activities.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.adapters.ClientLinesAdapter;
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
public class SalePrivacyActivity extends AppCompatActivity implements View.OnClickListener{

    public FragmentIterationListener mListener;

    private Bundle bundle;
    private ImageView img_close;

    ListView line_listview;
    private MyClientLineAdapter adapter;
    private ArrayList<Client> clientsWithDistance;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_privacy);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        if(user == null) user = MyUtils.getUserLoggedIn();
        initComponent();
    }

    public void initComponent() {
        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        line_listview = (ListView) findViewById(R.id.list);

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clientsWithDistance.get(position).getCheckStatus()) {
                    clientsWithDistance.get(position).setCheckStatus(false);
                } else {
                    clientsWithDistance.get(position).setCheckStatus(true);
                }

                adapter.notifyDataSetChanged();
            }
        });

        initClient();
    }

    private void initClient(){

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("100000"+ MyUtils.getPreferredDistanceUnit());
        MyUtils.setCurrentLocation(new Coordinate(33.894212, -84.231574));
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        ClientApi clientApi =  MainApplication.createService(ClientApi.class, user.getToken());
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), 1, clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if ( this == null ) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if(clientRes.isAuthFailed()){
                    User.LogOut(getApplicationContext());
                }
                else if(clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200){
                    ArrayList<Client> clients = clientRes.getClients();
                    clientsWithDistance = clients;
                    adapter = new MyClientLineAdapter(clientsWithDistance, SalePrivacyActivity.this, true);
                    line_listview.setAdapter(adapter);
                    line_listview.setFastScrollEnabled(true);
                }else{
                    Toast.makeText(SalePrivacyActivity.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                finish();
                break;
        }
    }
}
