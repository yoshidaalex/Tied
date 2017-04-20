package com.tied.android.tiedapp.ui.activities.report;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.ReportFilter;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ReportApi;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.activities.visits.ActivityVisits;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Upworker on 11/4/2016.
 */

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Bundle bundle;
    private User user;
    TextView txt_year, txt_month, txt_user_email;
    ImageView img_csv, img_pdf, img_sales, img_visits, img_clients;
    Boolean isCSV = true;
    int iReportContent = 1;

    AlertDialog ad;
    int month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        month=HelperMethods.getNumericMonthOfTheYear(HelperMethods.getTodayDate());
        year=HelperMethods.getCurrentYear(HelperMethods.getTodayDate());
        initComponents();
    }

    public void initComponents() {


        txt_user_email = (TextView) findViewById(R.id.txt_user_email);
        txt_user_email.setText(String.format("Send Report to %s", user.getEmail()));

        txt_year = (TextView) findViewById(R.id.txt_year);
        txt_month = (TextView) findViewById(R.id.txt_month);

        img_csv = (ImageView) findViewById(R.id.img_csv);
        img_csv.setBackgroundResource(R.drawable.circle_check2);

        img_pdf = (ImageView) findViewById(R.id.img_pdf);

        img_sales = (ImageView) findViewById(R.id.img_sales);
        img_sales.setBackgroundResource(R.drawable.circle_check2);

        img_visits = (ImageView) findViewById(R.id.img_visits);
        img_clients = (ImageView) findViewById(R.id.img_clients);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.year_layout:
                int cyear= HelperMethods.getCurrentYear(HelperMethods.getTodayDate());
                final List<Integer> years = new ArrayList<>();
                for(int i=cyear; i>2014; i--) {
                    years.add(i);
                }
                String[] yearArray=new String[years.size()];
                for(int i=0; i<years.size(); i++) {
                    yearArray[i]=""+years.get(i);
                }
                showList(yearArray, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        year=i;
                        txt_year.setText(years.get(i));
                        ad.dismiss();
                    }
                });
                break;
            case R.id.month_layout:
                showList(HelperMethods.MONTHS_LIST, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        month=i;
                        txt_month.setText(HelperMethods.MONTHS_LIST[i]);
                        ad.dismiss();
                    }
                });
                break;
            case R.id.csv_layout:
                isCSV = true;
                img_csv.setBackgroundResource(R.drawable.circle_check2);
                img_pdf.setBackgroundResource(R.drawable.circle_uncheck);
                break;
            case R.id.pdf_layout:
                isCSV = false;
                img_csv.setBackgroundResource(R.drawable.circle_uncheck);
                img_pdf.setBackgroundResource(R.drawable.circle_check2);
                break;
            case R.id.sales_layout:
                iReportContent = 1;
                img_sales.setBackgroundResource(R.drawable.circle_check2);
                img_visits.setBackgroundResource(R.drawable.circle_uncheck);
                img_clients.setBackgroundResource(R.drawable.circle_uncheck);
                break;
            case R.id.visits_layout:
                iReportContent = 2;
                img_sales.setBackgroundResource(R.drawable.circle_uncheck);
                img_visits.setBackgroundResource(R.drawable.circle_check2);
                img_clients.setBackgroundResource(R.drawable.circle_uncheck);
                break;
            case R.id.clients_layout:
                iReportContent = 3;
                img_sales.setBackgroundResource(R.drawable.circle_uncheck);
                img_visits.setBackgroundResource(R.drawable.circle_uncheck);
                img_clients.setBackgroundResource(R.drawable.circle_check2);
                break;
            case R.id.txt_report:
                sendReport();
                break;
        }
    }

    private void showList(String[] items, AdapterView.OnItemClickListener onSelect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ListView lv=new ListView(this);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(onSelect);
        builder.setView(lv);
        ad=builder.create();
        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ad.dismiss();
            }
        });
        ad.show();
    }

    private void sendReport() {
        ReportFilter reportFilter = new ReportFilter();
        reportFilter.setYear(Integer.valueOf(year));
        reportFilter.setMonth(month);

        String type = (isCSV) ? "csv" : "pdf";
        reportFilter.setType(type);
DialogUtils.displayProgress(this);
        String content = (iReportContent == 1) ? "sales" : ((iReportContent == 2) ? "clients" : "visits");
        reportFilter.setContent(content);
        Logger.write(reportFilter.toString());

        final ReportApi reportApi =  MainApplication.createService(ReportApi.class);
        Call<ResponseBody> response = reportApi.report(user.getId(), reportFilter);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
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
                        User.LogOut(ReportActivity.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {
                        MyUtils.showMessageAlert(ReportActivity.this, "Report sent. Thank you.");
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

                Logger.write(" onFailure", t.toString());
            }
        });
    }
}
