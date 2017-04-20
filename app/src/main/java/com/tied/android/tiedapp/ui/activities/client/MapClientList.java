package com.tied.android.tiedapp.ui.activities.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.adapters.MapClientListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapClientList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = MapClientList.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    LinearLayout back_layout;
    protected ListView listView;

    protected BaseAdapter adapter;
    protected ArrayList clientsList;
    ImageView map_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_list_layout);

        user = MyUtils.getUserLoggedIn();

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        map_layout = (ImageView) findViewById(R.id.map_layout);

        if (back_layout != null) {
            back_layout.setOnClickListener(this);
        }
        map_layout.setOnClickListener(this);

        clientsList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        if (listView != null) {
            listView.setOnItemClickListener(MapClientList.this);
        }

      if (clientsList.size() == 0) {
            initClient();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");
        if (clientsList.get(position) instanceof Client) {
            Client data = (Client) clientsList.get(position);

            Intent intent = new Intent(this, AddClientActivity.class);
            intent.putExtra(Constants.CLIENT_DATA, data);
            startActivity(intent);
        }
    }


    protected void initClient() {

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("0km");
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if (coordinate == null) {
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        final ClientApi clientApi =  MainApplication.createService(ClientApi.class, user.getToken());
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), 1, clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if (clientRes.isAuthFailed()) {
                    User.LogOut(MapClientList.this);
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                    ArrayList<Client> clients = clientRes.getClients();
                    Log.d(TAG + "", clients.toString());
                    if (clients.size() > 0) {
                        initFormattedClient(clients);
                    } else {
                        bundle.putBoolean(Constants.NO_CLIENT_FOUND, true);
                        MyUtils.startActivity(MapClientList.this, MainActivity.class, bundle);
                    }
                } else {
                    Toast.makeText(MapClientList.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }
                Log.d(TAG + " onResponse", resResponse.body().toString());
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });

    }

    public void initFormattedClient(ArrayList<Client> clients) {
        clientsList = clients;
        adapter = new MapClientListAdapter(clientsList, this);
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                MyUtils.startActivity(this, MainActivity.class);
                break;
            case R.id.map_layout:
                MyUtils.startActivity(this, ActivityClient.class);
                break;
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
