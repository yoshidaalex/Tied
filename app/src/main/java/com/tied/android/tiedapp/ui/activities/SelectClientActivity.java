package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientFilter;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class SelectClientActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String TAG = LastVisitedClientListFragment.class
            .getSimpleName();


    public static final String SELECTED_OBJECTS="selected_ids";
    public static final String OBJECT_TYPE="object_type";
    public static final String IS_MULTIPLE="is_multiple";
    public static final int SELECT_CLIENT_TYPE=100;
   // public static final int SELECT_CLIENT_TYPE_MULTIPLE=102;
    public static final int SELECT_LINE_TYPE=200;
   // public static final int SELECT_LINE_TYPE_MULTIPLE=201;
    //public static int ACTIVITY_TYPE=SELECT_CLIENT_TYPE;
    //public static boolean IS_MULTIPLE=true;

    public FragmentIterationListener mListener;
    ArrayList selectedObjects = new ArrayList();


    private ArrayList clientsWithDistance = new ArrayList<Client>();
    private ArrayList clientsWithDistanceHolder = new ArrayList<Client>();
    ArrayList search_data = new ArrayList<>();

    private ArrayList<String> selectedIDs=new ArrayList<String>(0);
    private ListView listView;

    private int[] range = {0,500,1000,2000,5000};
    private boolean[] added;

    // Pop up
    private EditText search;
    private MyClientLineAdapter adapter;
    private Bundle bundle;
    private User user;

    private TextView txt_title, selectedCountText;
    private View addLayout;
    View finishSelection;
    private boolean isMultiple=false;
    private String selectedIds;
    int objectType=SELECT_CLIENT_TYPE;

    int numPages=1;
    private int preLast;
    public int pageNumber=1;

    View clearBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null ) {
           try{
               isMultiple=bundle.getBoolean(IS_MULTIPLE);

           }catch (Exception e) {}
            try{
                objectType=bundle.getInt(OBJECT_TYPE);
            }catch (Exception e) {}
           /* try{
                selectedIDs=bundle.getStringArrayList(SELECTED_IDS);
                if(selectedIDs==null) selectedIDs=new ArrayList<String>(0);
            }catch (Exception e) {}*/
        }
        setContentView(R.layout.schedule_select_client_list_general);
        MyUtils.setFocus(findViewById(R.id.getFocus));
        initComponent();
    }

    public void initComponent() {

        txt_title = (TextView) findViewById(R.id.txt_title);
        listView = (ListView) findViewById(R.id.list);

        search = (EditText) findViewById(R.id.search);
        listView.setOnItemClickListener(this);
        addLayout = findViewById(R.id.add_layout);

        if(!isMultiple) {
            addLayout.setVisibility(View.GONE);
        }

        clearBut = findViewById(R.id.clear_but);
        clearBut.setOnClickListener(this);
        finishSelection=findViewById(R.id.add_button);
        finishSelection.setVisibility(View.GONE);
        finishSelection.setOnClickListener(this);
        selectedCountText=(TextView)findViewById(R.id.selected_count);

        if( objectType==SELECT_CLIENT_TYPE) {
            txt_title.setText("Select Client");
            search.setHint("Search Client by Name");
        } else {
            txt_title.setText("Select Line");
            search.setHint("Search Line by Name");
        }

        updateNumSelected();

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                search_data = new ArrayList<>();
                // TODO Auto-generated method stub
                for(int i = 0 ; i < clientsWithDistanceHolder.size() ; i++) {
                    Client model = (Client) clientsWithDistanceHolder.get(i);

                    String title = MyUtils.getClientName(model);
                    if(title.matches("(?i).*" + search.getText().toString() + ".*")) {
                        search_data.add(model);
                    }
                }
                clientsWithDistance.clear();
                clientsWithDistance.addAll(search_data);
                adapter.notifyDataSetChanged();
                //adapter = new MyClientLineAdapter(clientsWithDistance, selectedIDs, SelectClientActivity.this, isMultiple);
                //listView.setAdapter(adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

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

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        if(user==null) user=MyUtils.getUserLoggedIn();
        if(bundle!=null) {
            selectedObjects=(ArrayList<Object>)bundle.getSerializable(SELECTED_OBJECTS);
        }
        if(selectedObjects!=null) {
            int len=selectedObjects.size();
            selectedIDs=new ArrayList<>(len);
            for(int i=0; i<len; i++) {
                if( objectType==SELECT_CLIENT_TYPE) {
                    selectedIDs.add(((Client) selectedObjects.get(i)).getId());
                }
                else{
                    selectedIDs.add(((Line)selectedObjects.get(i)).getId());
                }
            }
        }else{
            selectedObjects=new ArrayList<Objects>();
        }
        //if( ACTIVITY_TYPE==SELECT_CLIENT_TYPE) {
        adapter = new MyClientLineAdapter(clientsWithDistance, selectedIDs, SelectClientActivity.this, isMultiple);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initClient();
      //  }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                finishSelection();
                break;
            case R.id.clear_but:
                MyUtils.showClearWarning(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedIDs.clear();
                        selectedObjects.clear();
                        adapter.notifyDataSetChanged();
                        // finishSelection();
                        updateNumSelected();
                    }
                });
                break;
        }
    }
    private void showClearWarning() {
        final AlertDialog ad= new AlertDialog.Builder(this).create();
        ad.setMessage("This will clear all selections. Are you sure you want to proceed?");
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedIDs.clear();
                selectedObjects.clear();
                finishSelection();
            }
        });
        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");

       // if(clientsWithDistance.get(position))
        if(!isMultiple) {
            selectedIDs.clear();
            selectedObjects.clear();
        }

        if( objectType==SELECT_CLIENT_TYPE) {
            Client obj=(Client)clientsWithDistance.get(position);

            if(selectedIDs.contains(obj.getId())) {
                selectedIDs.remove(obj.getId());
                selectedObjects.remove(obj);
            }else {
                selectedIDs.add(obj.getId());
                selectedObjects.add(obj);
            }
        }else{
            Line obj=(Line)clientsWithDistance.get(position);
            if(selectedIDs.contains(obj.getId())) {
                selectedIDs.remove(obj.getId());
                selectedObjects.remove(obj);
            }else {
                selectedIDs.add(obj.getId());
                selectedObjects.add(obj);
            }
        }
       /* Intent intent = new Intent();
        Bundle b =new Bundle();
        b.putSerializable("selected", selectedObjects);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();*/
        if(isMultiple) {
            adapter.setSelected(selectedIDs);
            adapter.notifyDataSetChanged();
        }else{
            finishSelection();
            return;
        }
       updateNumSelected();

    }
    private void updateNumSelected() {
        int size=selectedIDs.size();
        selectedCountText.setText(size+" Selected");
        if(size==0) {
            finishSelection.setVisibility(View.GONE);
            clearBut.setVisibility(View.GONE);
            }
        else{
            finishSelection.setVisibility(View.VISIBLE);
            clearBut.setVisibility(View.VISIBLE);
        }
    }
    private void finishSelection() {
        Intent intent = new Intent();
        Bundle b =new Bundle();
        if(!selectedObjects.isEmpty()) {
            if (isMultiple) {
                b.putSerializable("selected", selectedObjects);
            } else {
                b.putSerializable("selected", (Client) selectedObjects.get(0));
            }
        }
        intent.putExtras(b);
        Logger.write("finishginnnnn.");
        setResult(RESULT_OK, intent);
        finishActivity(Constants.SELECT_CLIENT);
        finish();
    }

    private void initClient(){

        DialogUtils.displayProgress(this);
        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("100000"+MyUtils.getPreferredDistanceUnit());
        MyUtils.setCurrentLocation(new Coordinate(33.894212, -84.231574));
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        ClientApi clientApi =  MainApplication.createService(ClientApi.class, user.getToken());
        Call<ClientRes> response = clientApi.getClientsFilter(user.getId(), pageNumber, new ClientFilter());
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
                    numPages=clientRes.get_meta().getPage_count();
                    ArrayList<Client> clients = clientRes.getClients();
                    //Log.d(TAG + "", clients.toString());

                    if(pageNumber==1) {
                        clientsWithDistance.clear();
                        clientsWithDistanceHolder.clear();
                    }
                    clientsWithDistance.addAll(clients);
                    clientsWithDistanceHolder.addAll(clientsWithDistance);

                    if(pageNumber==1 && clientsWithDistance.size()==0) {
                        findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.no_results).setVisibility(View.GONE);
                    }

                    listView.setFastScrollEnabled(true);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(SelectClientActivity.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
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

    public void initAdded(){
        added = new boolean[range.length-1];
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(user==null) {
            finish();
        }
    }
    public void goBack(View v) {
        onBackPressed();
    }
}
