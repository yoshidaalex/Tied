package com.tied.android.tiedapp.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ConfigApi;
import com.tied.android.tiedapp.retrofits.services.PaymentApi;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.ProgressIndicator;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Femi on 7/31/2016.
 */
public class SubscriptionActivity extends AppCompatActivity implements  View.OnClickListener {
    AppCompatActivity view;
    Bundle bundle;
    User user;
    ImageView img_month, img_year;
    Double yearPrice, monthPrice;
    TextView monthPriceTV, yearPriceTV;
    String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        view=this;
        initComponent();
    }
    public void initComponent(){

        bundle = getIntent().getExtras();
        if (bundle != null) {
           user= MyUtils.getUserFromBundle(bundle);
        }
        monthPriceTV=(TextView )findViewById(R.id.monthPrice);
        yearPriceTV=(TextView)findViewById(R.id.yearPrice);
        monthPriceTV.setText("");
        yearPriceTV.setText("");
        img_month = (ImageView) findViewById(R.id.img_month);
       // img_month.setOnClickListener(this);

        img_year = (ImageView) findViewById(R.id.img_year);
       // img_year.setOnClickListener(this);
        getPrices();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_month:
                initiatePayment("month");
               // completePayment("Adsfasdfasdf", "month");
                break;
            case R.id.img_year:
                initiatePayment("year");
                break;
        }
    }
    public void goBack(View view) {
        onBackPressed();
    }

    private void getPrices() {

        ConfigApi clientApi = MainApplication.getInstance().getRetrofit().create(ConfigApi.class);
        Call<ResponseBody> response;
        response = clientApi.getSetting("");

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {

                    if(SubscriptionActivity.this==null) return;
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    try {

                        JSONObject jo=new JSONObject(response.toString());
                        JSONObject vJO=new JSONObject(jo.getString("subscription_cost"));
                        yearPrice=vJO.getDouble("year");
                        monthPrice=vJO.getDouble("month");
                        monthPriceTV.setText(MyUtils.moneyFormat(monthPrice)+"/month");
                        yearPriceTV.setText(MyUtils.moneyFormat(yearPrice)+"/year");

                    }catch (Exception e) {
                        Logger.write(e);
                    }


                }catch (Exception E) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }

        });
    }
    String clientToken;
    static int REQUEST_CODE=9023;
    private void initiatePayment(String duration) {
        this.duration=duration;
        DialogUtils.displayProgress(this);
        PaymentApi clientApi = MainApplication.getInstance().getRetrofit().create(PaymentApi.class);
        Call<ResponseBody> response;
        response = clientApi.getClientToken();

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    DialogUtils.closeProgress();
                    if(SubscriptionActivity.this==null) return;
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    try {
                        SubscriptionActivity.this.clientToken = new JSONObject(response.toString()).getString("token");
                        PaymentRequest paymentRequest = new PaymentRequest()
                                .clientToken(SubscriptionActivity.this.clientToken);
                        startActivityForResult(paymentRequest.getIntent(SubscriptionActivity.this), REQUEST_CODE);

                    }catch (Exception e) {
                        Logger.write(e);
                    }


                }catch (Exception E) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
               // String nonce = paymentMethodNonce.getNonce();
                completePayment( paymentMethodNonce.getNonce(), duration);
            }else{
                MyUtils.showErrorAlert(SubscriptionActivity.this, "Error, Unable to complete payment");
            }
        }
    }
    private void completePayment(String nonce, String duration) {
        DialogUtils.displayProgress(this);
        PaymentApi clientApi = MainApplication.getInstance().getRetrofit().create(PaymentApi.class);
        Call<ResponseBody> response;
        Map<String, String> body=new HashMap<>();
        body.put("nonce", nonce);
        body.put("duration", duration);
        response = clientApi.sendNonce(body);

        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    DialogUtils.closeProgress();
                    if(SubscriptionActivity.this==null) return;
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    try {
                        JSONObject resp=new JSONObject(response.toString());
                        if(resp.getBoolean("success")) {
                            MyUtils.showMessageAlert(SubscriptionActivity.this, "Your subscription has been upgraded. Thank you!");
                        }else{

                            MyUtils.showErrorAlert(SubscriptionActivity.this, "Error, Unable to complete payment");
                        }
                    }catch (Exception e) {
                        Logger.write(e);
                        MyUtils.showErrorAlert(SubscriptionActivity.this, "Error, Unable to complete payment");

                    }


                }catch (Exception E) {
                    MyUtils.showErrorAlert(SubscriptionActivity.this, "Error, Unable to complete payment");

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }

        });
    }

}
