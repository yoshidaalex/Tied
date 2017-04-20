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

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.LineSelectTerritoriesAdapter;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.util.Logger;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by femi on 9/4/2016.
 */
public class LineSelectTerritoriesActivity extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;

    ListView listview;
    LineSelectTerritoriesAdapter territoriesAdapter;

    ImageView img_close;
    TextView txt_done, txt_client_info;
    int page_index;

    private Line line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_territory);

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
            case R.id.txt_done:

                break;
        }
    }

    public void initComponents() {

        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_done = (TextView) findViewById(R.id.txt_done);
        txt_done.setOnClickListener(this);

        txt_client_info = (TextView) findViewById(R.id.txt_client_info);
        if (page_index == 0) {
            txt_client_info.setText("Select Line");
        } else {
            txt_client_info.setText("Select Territory");
        }

        listview = (ListView) findViewById(R.id.listView);

        ArrayList<TerritoryModel> territoryModels = new ArrayList<TerritoryModel>();
        for (int i = 0 ; i < 10; i++) {
            TerritoryModel territoryModel = new TerritoryModel();

            territoryModel.setTerritory_name("Denton Tap, MD");

            territoryModels.add(territoryModel);
        }

        territoriesAdapter = new LineSelectTerritoriesAdapter(territoryModels, this);
        listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();
    }

}
