package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.ui.adapters.SaleLineDetailsListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 9/4/2016.
 */
public class LineTerritoriesActivity extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;

    ListView territories_listview;
    LineTerritoriesAdapter territoriesAdapter;

    ImageView img_close, img_edit;
    int page_index;

    TextView txt_client_info, txt_description;

    private Line line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_territory);

        bundle = getIntent().getExtras();
        page_index = bundle.getInt(Constants.SHOW_LINE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        initComponents();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_edit:
                MyUtils.startActivity(this, LineSelectTerritoriesActivity.class, bundle);
                break;
        }
    }

    public void initComponents() {

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        img_edit = (ImageView) findViewById(R.id.img_edit);
        img_edit.setOnClickListener(this);

        txt_client_info = (TextView) findViewById(R.id.txt_client_info);
        txt_description = (TextView) findViewById(R.id.txt_description);

        if (page_index == 0) {
            txt_client_info.setText("Lines");
            txt_description.setText("You currently serve 20 lines for");
        } else {
            txt_client_info.setText("Territories");
            txt_description.setText("You currently serve 20 territories for");
        }
        territories_listview = (ListView) findViewById(R.id.listView);

        ArrayList<Territory> territoryModels = new ArrayList<Territory>();
        for (int i = 0 ; i < 10; i++) {
            Territory territoryModel = new Territory();

            territoryModel.setCounty("Denton Tap, MD");


            territoryModels.add(territoryModel);
        }

        territoriesAdapter = new LineTerritoriesAdapter(territoryModels, this);
        territories_listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Logger.write("REgdlkadf ajsdpfjasdf "+requestCode);
        if(requestCode==Constants.ADD_SALES && requestCode==RESULT_OK) {

        }
    }
}
