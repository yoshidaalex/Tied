package com.tied.android.tiedapp.ui.activities.territories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.*;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.TerritoryApi;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.ui.adapters.TerritoryAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HTTPConnection;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by femi on 10/14/2016.
 */
public class ActivityAddTerritory extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener{

    private Bundle bundle;
    private User user;

    ListView territories_listview;
    LineTerritoriesAdapter territoriesAdapter;
    ImageView img_close, img_edit;
    int page_index;
    ArrayList<Territory> myTerritories =new ArrayList<>();
    TextView txt_client_info, txt_description;
    EditText search;
    String selectedLocation="", selectedCoord="";
    Spinner stateSpinner;
    ArrayList<Territory> territoryModels = new ArrayList<Territory>();
    ArrayList<Territory> selected = new ArrayList<>();
    View addBut;

    String searchKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_territory);

        bundle = getIntent().getExtras();
        MyUtils.setColorTheme(this, bundle.getInt(Constants.SOURCE), findViewById(R.id.main_layout));
        initComponents();
    }

    private void initComponents() {
        search=(EditText)findViewById(R.id.search);
        myTerritories=(ArrayList)bundle.getSerializable("my_territories");
        addBut=findViewById(R.id.img_add);

        stateSpinner=(Spinner)findViewById(R.id.stateSpinner);
        List<String> states = MyUtils.States.asArrayList();
        states.add(0, "State...");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_item, states);
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown);
        stateSpinner.setAdapter(adapter);
        territories_listview=(ListView)findViewById(R.id.listView) ;

        //territoriesAdapter=new LineTerritoriesAdapter(territories, this);
        //territories_listview.setAdapter(territoriesAdapter);

        territoriesAdapter = new LineTerritoriesAdapter(territoryModels, this, selected);
        territories_listview.setOnItemClickListener(this);
        territories_listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();

    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.img_add:
                finishSelection();
                break;
            case R.id.search_button:
                doSearch();
                break;
        }
    }
    public void doSearch() {
        searchKey=search.getText().toString().trim();
        territoriesAdapter.notifyDataSetChanged();

        final TerritoryApi territoryApi =  MainApplication.createService(TerritoryApi.class);
        Call<ResponseBody> response = territoryApi.getTerritoriesDatabase(searchKey);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityAddTerritory.this);
                        return;
                    }
                    _Meta meta=response.getMeta();


                    if(meta !=null && meta.getStatus_code() == 200) {
                        territoryModels.clear();
                        territoryModels.addAll( (ArrayList) response.getDataAsList(Constants.TerritoryData, Territory.class));
                        //Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+territories.size());
                        territoriesAdapter.notifyDataSetChanged();
                        if( territoryModels.isEmpty()) MyUtils.showNoResults(ActivityAddTerritory.this.findViewById(R.id.main_layout), R.id.no_results);
                        else findViewById(R.id.no_results).setVisibility(View.GONE);
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

//        DialogUtils.displayProgress(this);
//
//        new HTTPConnection(new HTTPConnection.AjaxCallback() {
//            @Override
//            public void run(int code, String response) {
//
//                DialogUtils.closeProgress();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        territoryModels.clear();
//                        territoriesAdapter.notifyDataSetChanged();
//                    }
//                });
//
//                if(code==0) {
//                    Logger.write("no response");
//                    return;
//                }else{
//
//
//                    try{
//
//                        JSONObject jo=new JSONObject(response);
//                        JSONArray ja=new JSONArray(jo.getString("results"));
//                        int lent=ja.length();
//
//
//                        for(int k=0; k<lent; k++) {
//                            JSONObject addrJO=new JSONObject(new JSONArray(jo.getString("results")).get(k).toString());
//                            JSONArray addressCompJA=new JSONArray(addrJO.getString("address_components"));
//                            int len=addressCompJA.length();
//                            String city="", county="", state="", country="";
//                            state=null; city=state=county=null;
//                            for(int i=0; i<len; i++) {
//                                JSONObject component=new JSONObject(addressCompJA.get(i).toString());
//                                JSONArray types=new JSONArray(component.getString("types"));
//
//                                for(int j=0; j<types.length(); j++) {
//                                    if(types.getString(j).equalsIgnoreCase("locality")) {
//                                        city=component.getString("long_name");
//                                        break;
//                                    }
//                                    if(types.getString(j).equalsIgnoreCase("administrative_area_level_1")) {
//                                        state=component.getString("short_name");
//                                        break;
//                                    }
//                                    if(types.getString(j).equalsIgnoreCase("administrative_area_level_2")) {
//                                        county=component.getString("long_name");
//                                        break;
//                                    }
//                                    if(types.getString(j).equalsIgnoreCase("country")) {
//                                        country=component.getString("short_name");
//                                        break;
//                                    }
//                                }
//
//                                if(state!=null && city!=null && country!=null) break;
//                            }
//                            if(county==null) continue;
//                            if((state==null && city==null && country==null) || (state.equalsIgnoreCase("null") && country.equalsIgnoreCase("null")  && city.equalsIgnoreCase("null"))) continue;
//
//                            String location="";
//
//                            if(city!=null && !city.equalsIgnoreCase("null") && !city.trim().isEmpty()) location+=city;
//                            if(state!=null && !state.equalsIgnoreCase("null") && !state.trim().isEmpty())  location+=(location.isEmpty()?state:", "+state);
//                            if(country!=null && !country.equalsIgnoreCase("null") && !country.trim().isEmpty()) location+=(location.isEmpty()?country:", "+country);
//
//                            if(location.isEmpty()) continue;
//                            final Territory territory=new Territory();
//                            territory.setCity(city);
//                            territory.setCounty(county);
//                            territory.setCountry(country);
//                            territory.setState(state);
//                            runOnUiThread(new Runnable() {
//                                              @Override
//                                              public void run() {
//                                                  if (!territoryModels.contains(territory))
//                                                      territoryModels.add(territory);
//                                              }
//                                          });
//
//                            // Logger.write(territories.toString());
//
//                            //JSONObject coordCompObj = new JSONObject(addrJO.getString("geometry"));
//                            //JSONObject locObj=new JSONObject(coordCompObj.getString("location"));
//                            //coord.add(locObj.getDouble("lat")+","+locObj.getDouble("lng"));
//                        }
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Logger.write("updating..................");
//                                territoriesAdapter.notifyDataSetChanged();
//                            }
//                        });
//
//                    }catch (JSONException je) {
//                        MyUtils.showConnectionErrorToast(ActivityAddTerritory.this);
//                        Logger.write(je);
//                    }catch (Exception e) {
//                        Logger.write(e);
//                    }
//                }
//
//            }
//        }).load(Constants.GOOGLE_REVERSE_GEOCODING_URL +"&address="+searchKey+" county, USA");

    }
    private void toggleAddButton() {
        if(selected.size()>0) addBut.setVisibility(View.VISIBLE);
        else addBut.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Territory territory=territoryModels.get(i);
        //Logger.write(id);

        int count = 0;
        for (int index = 0 ; index < myTerritories.size() ; index++) {
            Territory item = myTerritories.get(index);

            if (item.getCounty().equals(territory.getCounty())) {
                MyUtils.showToast("Territory - "+territory.getCounty()+" "+territory.getState()+" already exists!");
                return;
            }
        }

        if(selected.contains(territory)) {
            selected.remove(selected.indexOf(territory));
        }else{
            selected.add(territory);
        }
        territoriesAdapter.notifyDataSetChanged();
        toggleAddButton();
    }

    private void finishSelection() {
        saveTerritories();

    }

    private void saveTerritories() {
        final TerritoryApi territoryApi =  MainApplication.createService(TerritoryApi.class);
        Call<ResponseBody> response = territoryApi.create(selected);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityAddTerritory.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 201) {
                        Intent intent = new Intent();
                        Bundle b =new Bundle();
                        if(!selected.isEmpty()) {
                            b.putSerializable("selected", selected);

                        }
                        intent.putExtras(b);
                        Logger.write("finishginnnnn.");
                        setResult(RESULT_OK, intent);
                        finishActivity(Constants.ADD_TERRITORY);
                        finish();
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

}
