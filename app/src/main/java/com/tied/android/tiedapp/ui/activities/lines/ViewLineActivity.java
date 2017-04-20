package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.*;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.sales.ActivityLineClientSales;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.DialogYesNo;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.ui.activities.goal.LineGoalActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Femi on 7/30/2016.
 */
public class ViewLineActivity extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    LinearLayout back_layout;
    RelativeLayout clients_layout, goals_layout;

    private TextView name, description, totalRevenueHeaderTV, totalRevenueBodyTV, addressTV, numClients, numGoalsTV;
    private Line line;
    Location location;
    View nameEditor, descriptionEditor;
    private static ViewLineActivity viewLineActivity;
    TextView txt_delete;

    public static ViewLineActivity getInstance() {
        return viewLineActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_view);
        viewLineActivity=this;

        bundle = getIntent().getExtras();
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        user = MyUtils.getUserFromBundle(bundle);

        final LineApi lineApi = MainApplication.createService(LineApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        final Call<ResponseBody> response = lineApi.getLineWithId(line.getId(), line.getId());

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response=new GeneralResponse(resResponse.body());

                    if(line!=null) {
                        Line the_line = response.getData("lines", Line.class);
                        Logger.write("Request failedsdddddddddddddd: "+the_line.toString());
                        ViewLineActivity.this.line=the_line;
                        bundle.putSerializable(Constants.LINE_DATA, line);
                        initComponent(line);
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                        finish();
                    }

                }catch (Exception ioe) {
                    DialogUtils.closeProgress();
                    Logger.write(ioe);
                    finish();
                }

                DialogUtils.closeProgress();
            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                MyUtils.showConnectionErrorToast(ViewLineActivity.this);
                DialogUtils.closeProgress();
                finish();
            }
        });


        setLineTotalRevenue();

    }

    private void initComponent(Line line) {

        name = (TextView) findViewById(R.id.name);
        name.setText(line.getName());

        description = (TextView) findViewById(R.id.description);
        description.setText(line.getDescription());
        clients_layout = (RelativeLayout) findViewById(R.id.clients_layout);
        clients_layout.setOnClickListener(this);

       // goals_layout = (RelativeLayout) findViewById(R.id.goals_layout);
//        goals_layout.setOnClickListener(this);

        findViewById(R.id.ship_layout).setOnClickListener(this);

        totalRevenueHeaderTV = (TextView) findViewById(R.id.total_revenue_txt);
        totalRevenueBodyTV = (TextView) findViewById(R.id.total_revenue);
        totalRevenueHeaderTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));
        totalRevenueBodyTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));

        numClients=(TextView) findViewById(R.id.num_clients);
       // numGoalsTV = (TextView) findViewById(R.id.num_goals);

        addressTV=(TextView) findViewById(R.id.ship_from);
        location=line.getAddress();
        if(location!=null)
            addressTV.setText(location.getLocationAddress());
        //numClients.setText(""+line.getNum_clients());

        nameEditor = findViewById(R.id.name_editor);
        nameEditor.setOnClickListener(this);
        descriptionEditor = findViewById(R.id.description_editor);
        descriptionEditor.setOnClickListener(this);

        txt_delete = (TextView) findViewById(R.id.txt_delete);
        txt_delete.setOnClickListener(this);

//        setLineNumGoals();

        if (user.getId().equals(line.getUser_id())) {
            txt_delete.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.revenue_layout:
                bundle.putInt(Constants.SOURCE, Constants.LINE_SOURCE);
                MyUtils.startRequestActivity(this, ActivityLineClientSales.class, Constants.ADD_SALES, bundle);
                break;
            case R.id.clients_layout:
                bundle.putString(Constants.CLIENT_LIST, "line");
                MyUtils.startActivity(this, LineClientListActivity.class, bundle);
                break;
            case R.id.info_layout:
                MyUtils.showLinesRelevantInfoDialog(this,"Relevant Information", line, new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        line=line;
                        updateLine(line);
                    }
                });
                break;
            case R.id.goals_layout:
                MyUtils.startActivity(this, LineGoalActivity.class, bundle);
                break;
            case R.id.ship_layout:
                MyUtils.showAddressDialog(this, "Shipping information",location, new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                        line.setAddress(((Location)response));
                        addressTV.setText(line.getAddress().getLocationAddress());
                        updateLine(line);
                    }
                });
                break;
            case R.id.name_editor:
                MyUtils.showEditTextDialog(this, "Line Name", line.getName(), new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        line.setName((String)response);
                        name.setText((String)response);
                        updateLine(line);
                    }
                });
                break;
            case R.id.description_editor:
                MyUtils.showEditTextDialog(this, "Line Description", line.getDescription(), new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        line.setDescription((String)response);
                        name.setText((String)response);
                        updateLine(line);
                    }
                });
                break;
            case R.id.img_edit:
                MyUtils.startRequestActivity(this, AddLinesActivity.class, Constants.Lines, bundle);
                break;
            case R.id.txt_delete:
                int color = this.getResources().getColor(R.color.alert_bg_color);
                DialogYesNo alert_delete = new DialogYesNo(ViewLineActivity.this, line, "DELETE LINE","All sales related to this line will also be deleted. Do you want to continue?","YES DELETE!",color,1);
                alert_delete.showDialog();
                break;
        }
    }

    boolean revenueUpdated=false;
    @Override
    public void onBackPressed() {
//        if(revenueUpdated) {
            Intent intent = new Intent();
            Bundle b =new Bundle();
            b.putSerializable(Constants.LINE_DATA, line);

            intent.putExtras(b);
            setResult(RESULT_OK, intent);
            finishActivity(Constants.LineDelete);
            finish();
            return;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(line==null) finish();

        setLineNumClients();
    }

    private void updateLine(final Line line) {

        LineApi lineApi = MainApplication.createService(LineApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = lineApi.updateLine(line.getId(), line);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(ViewLineActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        Line the_line = response.getData(Constants.LINE_DATA, Line.class);
                        Logger.write("Request failed: "+the_line.toString());
                        if(the_line.getId().equals(line.getId())){
                            Logger.write("Update line id +"+the_line.getId());
                            name.setText(line.getName());
                            description.setText(line.getDescription());
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
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                DialogUtils.closeProgress();
            }
        });
    }

    public void setLineTotalRevenue() {
        RevenueApi lineApi = MainApplication.createService(RevenueApi.class);
        RevenueFilter rf=new RevenueFilter();
        rf.setMonth(0);
        rf.setYear(HelperMethods.getCurrentYear(HelperMethods.getTodayDate()));
        rf.setQuarter(0);
        final Call<ResponseBody> response2 = lineApi.getTotalRevenues("line", line.getId(), rf);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                   // Logger.write("RESPONSSSSSSSSSSSSSSSSSSSS "+response.toString());
                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(ViewLineActivity.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();
                        line.setTotal_revenue(response.getData("line", Line.class).getTotal_revenue());
                        totalRevenueHeaderTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));
                        totalRevenueBodyTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));
                        updateLine(line);

                    } else {
                       // MyUtils.showToast(getString(R.string.connection_error));
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
                MyUtils.showConnectionErrorToast(ViewLineActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }

    public void setLineNumClients() {
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        final Call<ResponseBody> response2 = lineApi.getClientCount(user.getToken(), line.getId());
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {

                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    Logger.write("RESPONSSSSSSSSSSSSSSSSSSSS "+response.toString());

                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(ViewLineActivity.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();
                        //line.setNum_clients(response.getData("line", Line.class).getNum_clients());
                        //Logger.write("num clientsss "+line.getNum_clients());
                       // numClients.setText(""+line.getNum_clients());


                    } else {
                        // MyUtils.showToast(getString(R.string.connection_error));
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
                MyUtils.showConnectionErrorToast(ViewLineActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }

    public void setLineNumGoals() {
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        final Call<ResponseBody> response2 = lineApi.getNumLineGoals(user.getToken(), line.getId());
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {

                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    Logger.write("RESPONSSSSSSSSSSSSSSSSSSSS "+response.toString());

                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(ViewLineActivity.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();
                        //line.setNum_goals(response.getData("line", Line.class).getNum_goals());
                        //Logger.write("num clientsss "+line.getNum_goals());
                        //numGoalsTV.setText(""+line.getNum_goals());


                    } else {
                        // MyUtils.showToast(getString(R.string.connection_error));
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
                MyUtils.showConnectionErrorToast(ViewLineActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write(requestCode+" : "+resultCode);
        if(requestCode==Constants.ADD_SALES && RESULT_OK==resultCode) {
            setLineTotalRevenue();
            revenueUpdated=true;
        } else if (requestCode == Constants.Lines && resultCode == RESULT_OK) {
            bundle = data.getExtras();
            line = (Line) bundle.getSerializable(Constants.LINE_DATA);
            setLineTotalRevenue();
        }
    }
}
