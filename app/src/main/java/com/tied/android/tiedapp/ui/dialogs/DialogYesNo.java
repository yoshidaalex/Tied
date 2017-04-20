package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.util.MyUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZuumaPC on 7/7/2016.
 */
public class DialogYesNo {
    private TextView heading, content, cancel, yes;

    Activity activity;
    String txt_heading;
    String txt_content;
    String txt_yes;
    int color, type;
    Object object;

    public DialogYesNo(Activity activity, Object object, String txt_heading, String txt_content, String txt_yes, int color, int type){
        this.activity = activity;
        this.color = color;
        this.type = type;
        this.txt_content = txt_content;
        this.txt_yes = txt_yes;
        this.object = object;
        this.txt_heading = txt_heading;
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_yes_no);

        heading = (TextView) dialog.findViewById(R.id.txt_heading);
        content = (TextView) dialog.findViewById(R.id.txt_content);
        yes = (TextView) dialog.findViewById(R.id.yes);

        heading.setText(txt_heading);
        content.setText(txt_content);
        yes.setText(txt_yes);
//        yes.setTextColor(activity.getResources().getColor(R.color.semi_transparent_black));
        yes.setTextColor(color);

        cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (type){
                    case 0: //ActivityClientProfoile
                        MyUtils.showToast("Delete Successfully");
                        deleteClient((Client)object);
                        break;
                    case 1: //ViewLineActivity
                        deleteLine((Line)object);
                        break;
                    case 2: //ActivityVisitDetails - Delete visit
                        deleteVisit((Visit) object);
                        break;
                    case 3: //ActivitySaleDetails- Delete sale
                        deleteRevenue((Revenue) object);
                        break;
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void deleteLine(final Line line){

        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        DialogUtils.displayProgress(activity);
        Call<ResponseBody> response = lineApi.deleteLine(line.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (activity == null) return;

                DialogUtils.closeProgress();
                GeneralResponse generalResponse = new GeneralResponse(resResponse.body());
                try {
                    _Meta meta = generalResponse.getMeta();
                    if (meta.getStatus_code() == 200){
                        Intent intent = new Intent();
                        activity.setResult(activity.RESULT_OK, intent);
                        activity.finishActivity(Constants.LineDelete);
                        activity.finish();
                    }else{
                        MyUtils.showToast(meta.getUser_message());
                    }
                } catch (Exception e) {
                    MyUtils.showToast("Error Encounted");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtils.closeProgress();
            }
        });
    }

    public void deleteClient(final Client client){

        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        DialogUtils.displayProgress(activity);
        Call<ResponseBody> response = clientApi.deleteClient(client.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (activity == null) return;

                DialogUtils.closeProgress();
                GeneralResponse generalResponse = new GeneralResponse(resResponse.body());
                try {
                    _Meta meta = generalResponse.getMeta();
                    if (meta.getStatus_code() == 200){
                        Intent intent = new Intent();
                        activity.setResult(activity.RESULT_OK, intent);
                        activity.finishActivity(Constants.ClientDelete);
                        activity.finish();
                    }else{
                        MyUtils.showToast(meta.getUser_message());
                    }
                } catch (Exception e) {
                    MyUtils.showToast("Error encountered");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtils.closeProgress();
            }
        });
    }

    public void deleteVisit(final Visit visit){

        VisitApi visitApi = MainApplication.getInstance().getRetrofit().create(VisitApi.class);
        DialogUtils.displayProgress(activity);
        Call<ResponseBody> response = visitApi.deleteVisit(visit.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (activity == null) return;

                DialogUtils.closeProgress();
                GeneralResponse generalResponse = new GeneralResponse(resResponse.body());
                try {
                    _Meta meta = generalResponse.getMeta();
                    if (meta.getStatus_code() == 200){
                        Intent intent = new Intent();
                        activity.setResult(activity.RESULT_OK, intent);
                        activity.finishActivity(Constants.VISIT_LIST);
                        activity.finish();
                    }else{
                        MyUtils.showToast(meta.getUser_message());
                    }
                } catch (Exception e) {
                    MyUtils.showToast("Error encountered");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogUtils.closeProgress();
            }
        });
    }

    public void deleteRevenue(final Revenue revenue){

        RevenueApi revenueApi = MainApplication.getInstance().getRetrofit().create(RevenueApi.class);
        DialogUtils.displayProgress(activity);
        Call<ResponseBody> response = revenueApi.deleteSale(revenue.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (activity == null) return;

                DialogUtils.closeProgress();
                GeneralResponse generalResponse = new GeneralResponse(resResponse.body());
                try {
                    _Meta meta = generalResponse.getMeta();
                    if (meta.getStatus_code() == 200){
                        Intent intent = new Intent();
                        activity.setResult(activity.RESULT_OK, intent);
                        activity.finishActivity(Constants.REVENUE_LIST);
                        activity.finish();
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
}
