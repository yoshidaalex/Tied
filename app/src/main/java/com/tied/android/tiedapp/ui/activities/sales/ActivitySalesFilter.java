package com.tied.android.tiedapp.ui.activities.sales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.SalePrintListAdapter;

import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivitySalesFilter extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    RevenueFilter revenueFilter;
    String month, year, quarter="";
    String[] quarters = new String[] { "Any", "First", "Second", "Third", "Fourth"};
    String sort="desc";
    AlertDialog ad;



    private TextView txt_reset, txt_cancel, txt_default, txt_highest, txt_lowest, txt_filter, txt_month, txt_year, txt_quater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_filter);


        bundle = getIntent().getExtras();
        if(bundle!=null )
            revenueFilter=(RevenueFilter)bundle.getSerializable(Constants.FILTER);
        if(revenueFilter==null) {
            revenueFilter=new RevenueFilter();
          /*  String today= HelperMethods.getTodayDate();
            revenueFilter.setStart_date(HelperMethods.getCurrentYear(today)+"-"+HelperMethods.getNumericMonthOfTheYear(today)+"-01");
            int nextMonth=HelperMethods.getNumericMonthOfTheYear(today)+1;
            if(nextMonth>12) nextMonth=1;
            revenueFilter.setEnd_date(HelperMethods.getCurrentYear(today)+"-"+nextMonth+"-01");*/
            revenueFilter.setMonth(0);
            revenueFilter.setYear(HelperMethods.getCurrentYear(HelperMethods.getTodayDate()));
            revenueFilter.setQuarter(0);
        }


        initComponent();
    }

    private void initComponent() {
        MyUtils.setColorTheme(this, bundle.getInt(Constants.SOURCE), findViewById(R.id.main_layout));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green_color1));
        }
        if(revenueFilter.getSort()!=null && !revenueFilter.getSort().isEmpty()) {
            sort=revenueFilter.getSort();
        }

        /*
        try {
            quarter = quarters[revenueFilter.getQuarter()+1];
            month="";
        }catch (Exception e) {
            try {
                    month = HelperMethods.MONTHS_LIST[revenueFilter.getMonth() - 1];//HelperMethods.getMonthOfTheYear(revenueFilter.getStart_date());
            }catch (Exception ee) {
                month="All";
            }
            quarter="Any";
        }
        try {
            year = "" + revenueFilter.getYear();
        }catch (Exception e) {
           // try {
              //  year = "" + HelperMethods.getCurrentYear(HelperMethods.getTodayDate());
          //  }catch (Exception ee) {
                year="";
           // }
        }*/

        txt_reset = (TextView) findViewById(R.id.txt_reset);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        txt_default = (TextView) findViewById(R.id.txt_default);
        txt_highest = (TextView) findViewById(R.id.txt_highest);
        txt_lowest = (TextView) findViewById(R.id.txt_lowest);
        txt_filter = (TextView) findViewById(R.id.txt_filter);



        txt_month=(TextView)  findViewById(R.id.txt_month_val);
        txt_month.setText(month);
        txt_year=(TextView)  findViewById(R.id.txt_year_val);
        txt_year.setText(year);
        txt_quater=(TextView)  findViewById(R.id.txt_quarter_val);
        txt_quater.setText(quarter);
        //txt_month.setText(year);
        //txt_year.setText(HelperMethods.getCurrentYear(revenueFilter.getStart_date()));

        try {
            quarter = quarters[revenueFilter.getQuarter()];
        }catch (Exception e) {

        }
        txt_quater.setText(quarter);

        txt_reset.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);
        txt_default.setOnClickListener(this);
        txt_highest.setOnClickListener(this);
        txt_filter.setOnClickListener(this);
Logger.write(revenueFilter.toJSONString());
        year =""+revenueFilter.getYear();
        if(revenueFilter.getMonth()>0) {
            month=HelperMethods.MONTHS_LIST[revenueFilter.getMonth() - 1];

        }else{
            month="";
            txt_month.setText("");
        }
        txt_month.setText(month);
        if(revenueFilter.getQuarter()>0) {
            quarter=quarters[revenueFilter.getQuarter()];
            txt_quater.setText(quarter);
        }else{
            txt_quater.setText("");
        }
        txt_year.setText(year);

        toggleSortButs(sort);

    }
    private  void toggleSortButs(String sel) {
        if(sel.equals("asc")) {
            txt_lowest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_highest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_highest.setTextColor(getResources().getColor(R.color.grey));
        }
        if(sel.equals("desc")) {
            txt_lowest.setBackgroundResource(R.drawable.white_fill_grey_stroke);
            txt_highest.setBackgroundResource(R.drawable.blue_fill_grey_stroke);
            txt_lowest.setTextColor(getResources().getColor(R.color.grey));
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_cancel:
                finish();
                break;
            case R.id.txt_reset:
                year="";
                month="";
                quarter="";
                txt_month.setText("");
                txt_year.setText("");
                txt_quater.setText("");
                sort="desc";
                toggleSortButs(sort);
                break;
            case R.id.txt_default:
                revenueFilter.setSort("desc");
                break;
            case R.id.txt_highest:
                sort ="desc";
                revenueFilter.setSort(sort);
                toggleSortButs(sort);
                break;
            case R.id.txt_lowest:
                sort="asc";
                revenueFilter.setSort(sort);
                toggleSortButs(sort);
                break;
            case R.id.txt_filter:
                if(year.isEmpty()) {
                    MyUtils.showErrorAlert(this, "You must select a year");
                    return;
                }
                if(!month.isEmpty() ) {
                    revenueFilter.setMonth(1+(Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(month)));
                    revenueFilter.setQuarter(0);
                }else{
                    if(!quarter.isEmpty()) {

                        revenueFilter.setQuarter(Arrays.asList(quarters).indexOf(quarter));
                        revenueFilter.setMonth(0);
                    }
                }
                finishSelection();

                break;
            case R.id.choose_month:
                final String[] months = new String[13];
                months[0]="All";
                System.arraycopy( HelperMethods.MONTHS_LIST, 0, months, 1, HelperMethods.MONTHS_LIST.length );
                showList(months, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        month=months[i];
                        txt_month.setText(month);
                        quarter="";
                        txt_quater.setText("");
                        revenueFilter.setMonth(i);
                        revenueFilter.setQuarter(0);
                        ad.dismiss();
                    }
                });
                break;
            case R.id.choose_year:
                int cyear=HelperMethods.getCurrentYear(HelperMethods.getTodayDate());
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
                        year=""+years.get(i);
                        txt_year.setText(year);
                        revenueFilter.setYear(Integer.parseInt(year));
                        ad.dismiss();
                    }
                });
                break;
            case R.id.choose_quarter:

                showList(quarters, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        quarter=quarters[i];

                        txt_quater.setText(quarter);
                        month="";
                        revenueFilter.setMonth(0);
                        revenueFilter.setQuarter(i);
                        txt_month.setText(month);
                        ad.dismiss();

                    }
                });
                break;
        }
    }

    private void finishSelection() {
        Intent intent = new Intent();
        Bundle b =new Bundle();
        b.putSerializable(Constants.FILTER, revenueFilter);

        intent.putExtras(b);
        Logger.write("finishginnnnn.");
        setResult(RESULT_OK, intent);
        finishActivity(Constants.FILTER_CODE);
        finish();
    }



    public void goBack(View v) {
        onBackPressed();
    }
}