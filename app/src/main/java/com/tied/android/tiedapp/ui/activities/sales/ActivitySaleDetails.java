package com.tied.android.tiedapp.ui.activities.sales;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.activities.visits.ActivityAddVisits;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.DialogYesNo;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 8/4/2016.
 */
@SuppressWarnings("ValidFragment")
public class ActivitySaleDetails extends AppCompatActivity implements  View.OnClickListener{

    public static final String TAG = ActivitySaleDetails.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    Client client;
    Line line;
    String revenue_id;
    Revenue revenue;

    ImageView clientPhoto;
    TextView clientNameTV;
    TextView dscription, date, txt_delete, txt_amount, txt_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);

        bundle = getIntent().getExtras();
        user=MyUtils.getUserLoggedIn();

        revenue = (Revenue) bundle.getSerializable(Constants.REVENUE_DATA);
        revenue_id = revenue.getId();

        initComponent();
    }
    private void initComponent() {

        clientPhoto=(ImageView)findViewById(R.id.avatar);
        clientNameTV=(TextView)findViewById(R.id.client_name);

        dscription=(TextView)findViewById(R.id.txt_description);
        txt_line=(TextView)findViewById(R.id.txt_line);
        txt_amount=(TextView)findViewById(R.id.txt_ammount);
        date=(TextView)findViewById(R.id.date);
        txt_delete=(TextView)findViewById(R.id.txt_delete);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }
        txt_amount.setText(MyUtils.moneyFormat(revenue.getValue()));
        try {
            date.setText(MyUtils.formatDate(MyUtils.parseDate(revenue.getDate_sold())));
        }catch (Exception e) {

        }
        setVisibile();
        getSaleDetails();
    }
    @Override
    public void onClick(View v) {
        int color = this.getResources().getColor(R.color.schedule_title_bg_color);

        switch (v.getId()) {
            case R.id.img_edit:
                bundle.putSerializable(Constants.CLIENT_DATA, client);
                bundle.putSerializable(Constants.LINE_DATA, line);
                bundle.putSerializable(Constants.REVENUE_DATA, revenue);
                MyUtils.startRequestActivity(ActivitySaleDetails.this, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
            case R.id.txt_delete:
                color = this.getResources().getColor(R.color.alert_bg_color);
                DialogYesNo alert_delete = new DialogYesNo(ActivitySaleDetails.this, revenue, "DELETE SALE","Are you sure want to delete this sale","YES DELETE!",color,3);
                alert_delete.showDialog();
                break;
        }
    }

    public void goBack(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finishActivity(Constants.ADD_SALES);
        finish();
        return;
    }

    private void getSaleDetails() {
        final RevenueApi revenueApi =  MainApplication.createService(RevenueApi.class);
        Call<ResponseBody> response = revenueApi.getSaleDetails(revenue_id);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(ActivitySaleDetails.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {

                        revenue = ( (Revenue) response.getData("revenue", Revenue.class));
                        try {
                            client = ((Client) response.getData("client", Client.class));
                            MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
                            clientNameTV.setText(MyUtils.getClientName(client));
                        }catch (Exception e) {

                        }
                        try {
                            line = ((Line) response.getData("line", Line.class));
                            txt_line.setText(line.getName());
                        }catch (Exception e) {

                        }
                        Logger.write(revenue.toString());
                        dscription.setText(revenue.getTitle());

                        txt_amount.setText(MyUtils.moneyFormat(revenue.getValue()));
                        String[] strdate = revenue.getDate_sold().split("-");
                        date.setText(MyUtils.MONTHS_LIST[Integer.valueOf(strdate[1]).intValue() - 1] + " " + strdate[2] + ", " + strdate[0]);

                        setVisibile();

                    } else {
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                } catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                MyUtils.showConnectionErrorToast(ActivitySaleDetails.this);
        }
        });
    }

    private void setVisibile() {

        if (user.getId().equals(revenue.getUser_id())) {
            txt_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ADD_SALES && resultCode == RESULT_OK) {

            bundle = data.getExtras();
            revenue = (Revenue) bundle.getSerializable("revenue");
            revenue_id = revenue.getId();
            getSaleDetails();
        }
    }
}