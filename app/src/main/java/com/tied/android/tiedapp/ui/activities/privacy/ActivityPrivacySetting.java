package com.tied.android.tiedapp.ui.activities.privacy;

/**
 * Created by femi on 10/8/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.*;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.CoworkerApi;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ActivityPrivacySetting extends Activity
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


    private ArrayList clientsWithDistance;
    private ArrayList<String> selectedIDs=new ArrayList<String>(0);
    private ListView listView;

    private int[] range = {0,500,1000,2000,5000};
    private boolean[] added;

    // Pop up
    private EditText search;
    private MyClientLineAdapter adapter;
    private Bundle bundle;
    private User user;

    private TextView txt_continue, selectedCountText, titleTV;
    private View addLayout;
    View finishSelection;
    private boolean isMultiple=false;
    private String selectedIds;
    int objectType=SELECT_CLIENT_TYPE;
    String title, section;


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

            section=bundle.getString("section");

        }
        setContentView(R.layout.activity_sale_privacy);
      //  MyUtils.setFocus(findViewById(R.id.getFocus));
        initComponent();
    }
   /* public static void setType(int objectType, boolean isMultiple) {
      //  ACTIVITY_TYPE=objectType;
      //  IS_MULTIPLE=isMultiple;
       // this.objectType=objectType;
       // this.isMultiple=isMultiple
       // SelectClientActivity.IS_MULTIPLE=isMultiple;
    }*/



    public void initComponent() {
        clientsWithDistance = new ArrayList<Client>();
        listView = (ListView) findViewById(R.id.list);
    //   findViewById(R.id.clear_but).setOnClickListener(this);

//        txt_continue = (TextView) findViewById(R.id.txt_continue);

        search = (EditText) findViewById(R.id.search);
        listView.setOnItemClickListener(this);
        addLayout = findViewById(R.id.add_layout);

        if(!isMultiple) {
            addLayout.setVisibility(View.GONE);
        }else{
            addLayout.setVisibility(View.VISIBLE);
        }
        finishSelection=findViewById(R.id.add_button);
        finishSelection.setVisibility(View.VISIBLE);
        finishSelection.setOnClickListener(this);
        selectedCountText=(TextView)findViewById(R.id.selected_count);
        titleTV=(TextView)findViewById(R.id.title);




/*
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String searchData = search.getText().toString().trim().toLowerCase();
                //adapter.filter(searchData);
            }
        });*/




        //user = User.getCurrentUser(getActivity().getApplicationContext());//
        //

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        titleTV.setText(bundle.getString("title"));
        if(user==null) user=MyUtils.getUserLoggedIn();
        if(bundle!=null) {
            selectedObjects=(ArrayList<Object>)bundle.getSerializable(SELECTED_OBJECTS);
            Logger.write(selectedObjects.toString());
        }
        if(selectedObjects!=null) {
            int len=selectedObjects.size();
            selectedIDs=new ArrayList<>(len);
            for(int i=0; i<len; i++) {
                 selectedIDs.add(((CoWorker) selectedObjects.get(i)).getCoworker_id());


            }
        }else{
            selectedObjects=new ArrayList<Objects>();
        }
        //if( ACTIVITY_TYPE==SELECT_CLIENT_TYPE) {
        initClient();
        //  }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                finishSelection();
                break;
            case R.id.img_close:
                onBackPressed();
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

            User obj=(User)clientsWithDistance.get(position);
            if(selectedIDs.contains(obj.getId())) {
                selectedIDs.remove(obj.getId());
                selectedObjects.remove(obj);
            }else {
                selectedIDs.add(obj.getId());
                selectedObjects.add(obj);
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
            finishSelection.setVisibility(View.VISIBLE);
        }
        else {
            finishSelection.setVisibility(View.VISIBLE);
        }
    }
    private void finishSelection() {
        Intent intent = new Intent();
        Bundle b =new Bundle();
        //selectedObjects.clear();
       // selectedObjects=listView.getSelect
        if(!selectedObjects.isEmpty()) {
            if (isMultiple) {
                b.putSerializable("selected", selectedIDs);
            } else {
                b.putSerializable("selected", selectedIDs.get(0));
            }
        }
        b.putString("section", section);
        intent.putExtras(b);
        Logger.write("finishginnnnn.");
        setResult(RESULT_OK, intent);
        finishActivity(Constants.SELECT_USER);
        finish();
    }

    private void initClient(){

        DialogUtils.displayProgress(this);
        CoworkerApi coworkerApi = MainApplication.getInstance().getRetrofit().create(CoworkerApi.class);

        final Call<ResponseBody> request = coworkerApi.getCoworkers( user.getToken(), user.getId(), "i_added",  10, new RevenueFilter(), 1);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if ( this == null ) return;
                DialogUtils.closeProgress();
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response != null && response.isAuthFailed()) {
                        // User.LogOut(CoWorkerActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {
                        List<User> clients = response.getDataAsList("coworkers", User.class);
                        Log.d(TAG + "", clients.toString());
                        clientsWithDistance.addAll(clients);
                        adapter = new MyClientLineAdapter(clientsWithDistance, selectedIDs, ActivityPrivacySetting.this, isMultiple);
                        listView.setAdapter(adapter);
                        listView.setFastScrollEnabled(true);
                        updateNumSelected();
                    } else {
                        // Toast.makeText(com.tied.android.tiedapp.ui.activities.SelectClientActivity.this, clientRes.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
                Log.d(TAG + " onResponse", resResponse.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
    public void goBack(View v) {
        onBackPressed();
    }
}
