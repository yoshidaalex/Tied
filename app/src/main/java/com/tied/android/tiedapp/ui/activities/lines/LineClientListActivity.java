package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.TerritoryApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.adapters.LineClientAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineClientListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = LineClientListActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;
    Line line;

    LinearLayout back_layout;
    protected ListView listView;

    protected LineClientAdapter adapter;
    protected ArrayList<Client> clientsList;
    protected ArrayList<Client> clientsListHolder = new ArrayList<>();
    protected Territory territory;
    ArrayList search_data = new ArrayList<>();

    TextView txt_title, num_clients, no_results;
    EditText search;
    ImageView img_plus;
    String client_list;

    int numPages=1;
    private int preLast;
    public int pageNumber=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_client_list);

        user = MyUtils.getUserLoggedIn();
        bundle = getIntent().getExtras();
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        territory = (Territory) bundle.getSerializable(Constants.TERRITORY_DATA);
        client_list = bundle.getString(Constants.CLIENT_LIST);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        img_plus = (ImageView) findViewById(R.id.img_plus);
        img_plus.setOnClickListener(this);

        if (back_layout != null) {
            back_layout.setOnClickListener(this);
        }

        clientsList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        if (clientsList.size() == 0) {
            initClient();
        }

        no_results = (TextView) findViewById(R.id.no_results);
        no_results.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        if (client_list.equals("line")) {
            txt_title.setText(String.format("Clients for %s", line.getName()));
        } else if (client_list.equals("territory")) {
            txt_title.setText(String.format("Clients in %s", territory.getCounty()));
            img_plus.setVisibility(View.GONE);
        }

        num_clients = (TextView) findViewById(R.id.num_clients);
        adapter = new LineClientAdapter(clientsList, this);
        search = (EditText) findViewById(R.id.search);
        listView.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                search_data.clear();
                // TODO Auto-generated method stub
                for(int i = 0 ; i < clientsListHolder.size() ; i++) {
                    Client model = (Client) clientsListHolder.get(i);

                    String title = MyUtils.getClientName(model);
                    if(title.matches("(?i).*" + search.getText().toString() + ".*")) {
                        search_data.add(model);
                    }
                }
                clientsList.clear();
                clientsList.addAll(search_data);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        if(pageNumber<numPages) {
                            pageNumber++;
                            initClient();
                        }

                        preLast = lastItem;
                    }
                }
            }
        });

    }


    protected void initClient() {

        DialogUtils.displayProgress(this);
        Call<ClientRes> response;
        if (client_list.equals("line")) {
            ClientLocation clientLocation = new ClientLocation();
            clientLocation.setDistance("0km");
            Coordinate coordinate = MyUtils.getCurrentLocation();
            if (coordinate == null) {
                coordinate = user.getOffice_address().getCoordinate();
            }
            clientLocation.setCoordinate(coordinate);

            final ClientApi clientApi = MainApplication.createService(ClientApi.class, user.getToken());
            response = clientApi.getLineClients(user.getToken(), line.getId(), pageNumber, clientLocation);
        } else {
            final TerritoryApi territoryApi = MainApplication.createService(TerritoryApi.class, user.getToken());
            response = territoryApi.getTerritoryClient(territory, pageNumber);
        }
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    ClientRes clientRes = resResponse.body();
                    Logger.write(clientRes.toString());
                    if (clientRes.isAuthFailed()) {
                       // User.LogOut(LineClientListActivity.this);
                        Logger.write(clientRes.toString());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        numPages=clientRes.get_meta().getPage_count();
                        ArrayList<Client> clients = clientRes.getClients();

                        if(pageNumber==1 && clients.size()==0) {
                            bundle.putBoolean(Constants.NO_CLIENT_FOUND, true);
                            num_clients.setVisibility(View.GONE);
                            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        } else {
                            initFormattedClient(clients);
                        }
                    } else {
                        Logger.write(clientRes.getMessage());
                    }
                    Log.d(TAG + " onResponse", resResponse.body().toString());
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Logger.write(TAG + " onFailure", t.toString());
                MyUtils.showConnectionErrorToast(LineClientListActivity.this);
                DialogUtils.closeProgress();
            }
        });

    }

    public void initFormattedClient(ArrayList<Client> clients) {


        num_clients.setText(String.format("%d Clients", clients.size()));
        clientsList.clear();
        clientsList.addAll(clients);

        adapter.notifyDataSetChanged();
        listView.setFastScrollEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.img_plus:
                MyUtils.initiateClientSelector(this, null, false);
                break;
            case R.id.no_results:
                MyUtils.initiateClientSelector(this, null, false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "here---------------- listener");
        //Client client = clients.get(position);
        Bundle bundle =new Bundle();
        bundle.putSerializable(Constants.USER_DATA, user);
        bundle.putSerializable(Constants.CLIENT_DATA, clientsList.get(position));
        MyUtils.startRequestActivity(this, ActivityClientProfile.class, Constants.ClientDelete, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write(requestCode+" "+resultCode);
        if(requestCode==Constants.SELECT_CLIENT && resultCode==RESULT_OK) {
            Client client = (Client)(data.getSerializableExtra("selected"));

            int count = 0;
            for (int i = 0 ; i < clientsList.size() ; i++) {
                Client item = (Client) clientsList.get(i);

                if (item.getId().equals(client.getId())) {
                    count++;
                }
            }

            if (count == 0) {
                clientsList.add(client);

                num_clients.setText(String.format("%d Clients", clientsList.size()));

                adapter = new LineClientAdapter(clientsList, this);
                listView.setAdapter(adapter);

                //integrate of api
                addLineClient(client);
            } else {
                MyUtils.showMessageAlert(this, "Already existing!");
            }
        } else if (requestCode==Constants.ClientDelete && resultCode==RESULT_OK) {
            initClient();
        }
    }

    public void addLineClient(Client client) {
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        final Call<ResponseBody> response2 = lineApi.addLineClient(line.getId(), client);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    GeneralResponse response=new GeneralResponse(resResponse.body());

                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(LineClientListActivity.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {
                        MyUtils.showToast("Add Successfully");
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    // MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                    //Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(LineClientListActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }
}
