package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.adapters.ClientTerritoriesAdapter;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Femi on 7/30/2016.
 */
public class LineAddClientActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = LineAddClientActivity.class.getSimpleName();
    private Bundle bundle;
    private User user;

    TextView txt_add, selected_count, txt_title;
    ImageView img_close, img_all;
    EditText search;
    LinearLayout add_layout, all_select_layout;

    ListView line_listview;
    private MyClientLineAdapter adapter;
    private ArrayList<Client> clientsWithDistance;

    int total_selected_cnt = 0;
    boolean bAll = false;
    int page_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.line_add_client);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        page_index = bundle.getInt(Constants.SHOW_CLIENT);

        if(user == null) user = MyUtils.getUserLoggedIn();

        initComponent();
    }

    private void initComponent() {

        img_close =(ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_add = (TextView) findViewById(R.id.txt_add);
        search = (EditText) findViewById(R.id.search);

        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        selected_count = (TextView) findViewById(R.id.selected_count);

        all_select_layout = (LinearLayout) findViewById(R.id.all_select_layout);
        all_select_layout.setOnClickListener(this);

        if (page_index == 0) {
            txt_title.setText("Add clients to Line");
            all_select_layout.setVisibility(View.VISIBLE);
        } else {
            txt_title.setText("Add clients to Goal");
            all_select_layout.setVisibility(View.GONE);
        }

        img_all = (ImageView) findViewById(R.id.img_all);

        line_listview = (ListView) findViewById(R.id.list);

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clientsWithDistance.get(position).getCheckStatus()) {
                    clientsWithDistance.get(position).setCheckStatus(false);
                    total_selected_cnt--;
                } else {
                    clientsWithDistance.get(position).setCheckStatus(true);
                    total_selected_cnt++;
                }

                adapter.notifyDataSetChanged();

                if (total_selected_cnt > 0) {
                    add_layout.setVisibility(View.VISIBLE);
                    selected_count.setText(String.format("Add %d clients to line", total_selected_cnt));
                } else {
                    add_layout.setVisibility(View.GONE);
                }
            }
        });

        initClient();
    }

    private void initClient(){

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("100000"+MyUtils.getPreferredDistanceUnit());
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
                    Log.d(TAG + "", clients.toString());
                    clientsWithDistance = clients;
                    adapter = new MyClientLineAdapter(clientsWithDistance,  LineAddClientActivity.this, false);
                    line_listview.setAdapter(adapter);
                    line_listview.setFastScrollEnabled(true);
                }else{
                    Toast.makeText(LineAddClientActivity.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
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

    private void allClientSelect() {
        for (int i = 0 ; i < clientsWithDistance.size() ; i++) {

            if (bAll) {
                clientsWithDistance.get(i).setCheckStatus(true);
            } else {
                clientsWithDistance.get(i).setCheckStatus(false);
            }
        }

        if (bAll) {
            add_layout.setVisibility(View.VISIBLE);
            selected_count.setText(String.format("Add %d clients to line", clientsWithDistance.size()));
            img_all.setBackgroundResource(R.drawable.circle_check2);
        } else {
            add_layout.setVisibility(View.GONE);
            img_all.setBackgroundResource(R.drawable.unselectd_bg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_add:

                break;
            case R.id.img_close:
                finish();
                break;
            case R.id.all_select_layout:
                bAll = !bAll;
                allClientSelect();
                break;
        }
    }

}
