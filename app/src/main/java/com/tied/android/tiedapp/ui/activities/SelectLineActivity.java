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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerLinesActivity;
import com.tied.android.tiedapp.ui.adapters.ClientLinesAdapter;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.tab.LastVisitedClientListFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class SelectLineActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String TAG = LastVisitedClientListFragment.class
            .getSimpleName();


    public static final String SELECTED_OBJECTS="selected_ids";
    public static final String IS_MULTIPLE="is_multiple";

    public FragmentIterationListener mListener;
    ArrayList selectedObjects = new ArrayList();

    ArrayList<Line> search_data;
    ArrayList<Line> lineDataModels = new ArrayList<Line>();
    ArrayList<Line> lineDataModelsHolder = new ArrayList<Line>();
    private ListView listView;
    private ClientLinesAdapter adapter;
    private ArrayList<String> selectedIDs=new ArrayList<String>(0);

    // Pop up
    private EditText search;

    private Bundle bundle;
    private User user;

    private TextView txt_title, selectedCountText;
    private View addLayout;
    View finishSelection;
    View clearBut;
    private boolean isMultiple=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null ) {
            try{
                isMultiple=bundle.getBoolean(IS_MULTIPLE);

            }catch (Exception e) {}

            if(bundle!=null) {
                selectedObjects=(ArrayList<Object>)bundle.getSerializable(SELECTED_OBJECTS);
            }
            if(selectedObjects!=null) {
                int len=selectedObjects.size();
                selectedIDs=new ArrayList<>(len);
                for(int i=0; i<len; i++) {
               /* if( objectType==SELECT_CLIENT_TYPE) {
                    selectedIDs.add(((Client) selectedObjects.get(i)).getId());

                }
                else{*/
                    selectedIDs.add(((Line)selectedObjects.get(i)).getId());

                    // }
                }
            }else{
                selectedObjects=new ArrayList<Objects>();
            }
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
        finishSelection=findViewById(R.id.add_button);
        finishSelection.setOnClickListener(this);

        selectedCountText=(TextView)findViewById(R.id.selected_count);


            txt_title.setText("Select Line");
            search.setHint("Search Line by Name");
        clearBut =findViewById(R.id.clear_but);
        updateNumSelected();
        clearBut.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                search_data = new ArrayList<Line>();
                // TODO Auto-generated method stub
                for(int i = 0 ; i < lineDataModelsHolder.size() ; i++) {
                    Line model = (Line) lineDataModelsHolder.get(i);

                    String title = model.getName();
                    try {
                        if (title.matches("(?i).*" + search.getText().toString().trim() + ".*")) {
                            search_data.add(model);
                        }
                    }catch (NullPointerException npe) {

                    }
                }

                lineDataModels.clear();
                lineDataModels.addAll(search_data);
               // adapter = new ClientLinesAdapter(search_data, selectedIDs, SelectLineActivity.this, isMultiple);
                //listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        if(user==null) user=MyUtils.getUserLoggedIn();

        adapter = new ClientLinesAdapter( lineDataModels, selectedIDs, SelectLineActivity.this, isMultiple);
        listView.setAdapter(adapter);

        initLines();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                if(selectedIDs.size()==0) selectedObjects.clear();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("search", "here---------------- listener");
        if(!isMultiple) {
            selectedIDs.clear();
            selectedObjects.clear();
        }

            Line obj=(Line)lineDataModels.get(position);
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
        if(size==0) clearBut.setVisibility(View.GONE);
        else clearBut.setVisibility(View.VISIBLE);
       // selectedCountText.setText(size+" Selected");
//        if(size==0) finishSelection.setVisibility(View.GONE);
        //  else finishSelection.setVisibility(View.VISIBLE);
    }

    private void finishSelection() {
        Intent intent = new Intent();
        Bundle b =new Bundle();
        if(!selectedObjects.isEmpty()) {
            if (isMultiple) {
                b.putSerializable("selected", selectedObjects);
            } else {
                b.putSerializable("selected", (Line) selectedObjects.get(0));
            }
        }
        intent.putExtras(b);
        Logger.write("finishginnnnn.");
        setResult(RESULT_OK, intent);
        finishActivity(Constants.SELECT_LINE);
        finish();
    }


    public void initLines(){
        final LineApi lineApi =  MainApplication.createService(LineApi.class);
        Call<ResponseBody> response = lineApi.getUserLines(user.getId(), 1);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(SelectLineActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {

                        lineDataModels .addAll((ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class));
                        lineDataModelsHolder.clear();
                        lineDataModelsHolder.addAll(lineDataModels);
                        Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+lineDataModels.size());
                        adapter.notifyDataSetChanged();
                        if(lineDataModelsHolder.size()==0) {
                            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.no_results).setVisibility(View.GONE);
                        }
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
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