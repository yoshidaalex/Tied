package com.tied.android.tiedapp.ui.activities.territories;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.TerritoryApi;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.lines.LineClientListActivity;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

import static com.tied.android.tiedapp.customs.Constants.Territory;

/**
 * Created by femi on 10/4/2016.
 */
public class ActivityTerritories extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener{

        public static final String TAG = ActivityTerritories.class
            .getSimpleName();

        private Bundle bundle;
        private User user;

        ListView territories_listview;
        LineTerritoriesAdapter territoriesAdapter;

        ImageView img_close, img_edit;
        int page_index;
        TextView no_results;

        ArrayList<Territory> territoryModels = new ArrayList<Territory>();
        TextView txt_client_info, txt_description;

        int numPages=1;
        private int preLast;
        public int pageNumber=1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_territory);

                bundle = getIntent().getExtras();
                user=MyUtils.getUserFromBundle(bundle);
                page_index = bundle.getInt(Constants.SHOW_LINE);

                MyUtils.setColorTheme(this, bundle.getInt(Constants.SOURCE), findViewById(R.id.main_layout));

                initComponents();
        }

        @Override
        public void onClick(View view) {
                int id=view.getId();
                switch (id) {
                    case R.id.img_close:
                        finish();
                        break;
                    case R.id.img_add:
                        bundle.putSerializable("my_territories", territoryModels);
                        MyUtils.startRequestActivity(this, ActivityAddTerritory.class, Constants.ADD_TERRITORY, bundle);
                        break;
                    case R.id.no_results:
                        bundle.putSerializable("my_territories", territoryModels);
                        MyUtils.startRequestActivity(this, ActivityAddTerritory.class, Constants.ADD_TERRITORY, bundle);
                        break;
                }
        }

        public void initComponents() {

            img_close = (ImageView) findViewById(R.id.img_close);
            img_close.setOnClickListener(this);

           // img_edit = (ImageView) findViewById(R.id.img_edit);
           // img_edit.setOnClickListener(this);

            txt_client_info = (TextView) findViewById(R.id.txt_client_info);
            txt_description = (TextView) findViewById(R.id.txt_description);

            no_results = (TextView) findViewById(R.id.no_results);
            no_results.setOnClickListener(this);

            txt_client_info.setText("Territories");
            txt_description.setText("You currently serve 20 territories for");

            territories_listview = (ListView) findViewById(R.id.listView);
            territories_listview.setOnItemClickListener(this);

            territoriesAdapter = new LineTerritoriesAdapter(territoryModels, this);
            territories_listview.setAdapter(territoriesAdapter);
            territoriesAdapter.notifyDataSetChanged();
            initTerritories();

            territories_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int color = getResources().getColor(R.color.schedule_title_bg_color);
                Territory item = territoryModels.get(position);

                alertDialog(item, color);

                return false;
                }
            });

            territories_listview.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                                initTerritories();
                            }

                            preLast = lastItem;
                        }
                    }
                }
            });
        }

    private void alertDialog(final Territory territory, int color){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_yes_no);

        TextView heading = (TextView) dialog.findViewById(R.id.txt_heading);
        TextView content = (TextView) dialog.findViewById(R.id.txt_content);
        TextView yes = (TextView) dialog.findViewById(R.id.yes);

        heading.setText("DELETE TERRITORY");
        content.setText("All sales, schedule and visits related to this territory with be deleted. Are you sure want to continue?");
        yes.setText("YES DELETE!");
        yes.setTextColor(color);

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTerritory(territory);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void deleteTerritory(final Territory territory){

        TerritoryApi territoryApi = MainApplication.getInstance().getRetrofit().create(TerritoryApi.class);
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = territoryApi.deleteTerritory(territory.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;

                DialogUtils.closeProgress();
                GeneralResponse generalResponse = new GeneralResponse(resResponse.body());
                try {
                    _Meta meta = generalResponse.getMeta();
                    if (meta.getStatus_code() == 200){
                        MyUtils.showMessageAlert(ActivityTerritories.this, "Territory successfully deleted!");
                        territoryModels.clear();
                        territoriesAdapter.notifyDataSetChanged();
                        territories_listview.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initTerritories();
                            }
                        }, 200);
                    }else{
                        MyUtils.showToast("Error encountered");
                    }
                } catch (Exception e) {
                    MyUtils.showToast(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtils.closeProgress();
            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                //super.onActivityResult(requestCode, resultCode, data);
                Logger.write("REgdlkadf ajsdpfjasdf "+requestCode);
                if(requestCode==Constants.ADD_TERRITORY && resultCode==RESULT_OK) {
                    MyUtils.showMessageAlert(this, "Territory successfully added!");
                    territoryModels.clear();
                    territoriesAdapter.notifyDataSetChanged();
                    territories_listview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initTerritories();
                        }
                    }, 200);

                }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "here---------------- listener");
            //Client client = clients.get(position);
            Bundle bundle =new Bundle();
            bundle.putSerializable(Constants.USER_DATA, user);
            bundle.putSerializable(Constants.TERRITORY_DATA, territoryModels.get(position));
            bundle.putString(Constants.CLIENT_LIST, "territory");
            MyUtils.startActivity(this, LineClientListActivity.class, bundle);
        }
        public void initTerritories(){
            final TerritoryApi territoryApi =  MainApplication.createService(TerritoryApi.class);
            Call<ResponseBody> response = territoryApi.getTerritories(user.getId(), pageNumber);
            response.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                    try {
                        GeneralResponse response = new GeneralResponse(resResponse.body());
                        Logger.write(response.toString());
                        if (response.isAuthFailed()) {
                            User.LogOut(ActivityTerritories.this);
                            return;
                        }
                        _Meta meta=response.getMeta();


                        if(meta !=null && meta.getStatus_code() == 200) {
                            numPages=meta.getPage_count();
                            if(pageNumber==1)  territoryModels.clear();
                            territoryModels.addAll( (ArrayList) response.getDataAsList(Constants.TerritoryData, Territory.class));

                            if(pageNumber==1 && territoryModels.size()==0) {
                                MyUtils.showNoResults(ActivityTerritories.this.findViewById(R.id.main_layout), R.id.no_results);
                            }

                            territoriesAdapter.notifyDataSetChanged();
                        }else{
                            MyUtils.showToast("Error encountered");
                            DialogUtils.closeProgress();
                        }
                        if(territoryModels.size()>0) findViewById(R.id.no_results).setVisibility(View.GONE);
                        else findViewById(R.id.no_results).setVisibility(View.VISIBLE);

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
