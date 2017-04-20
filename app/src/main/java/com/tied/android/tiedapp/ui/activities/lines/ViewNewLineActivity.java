package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.territories.ActivityTerritories;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Femi on 7/30/2016.
 */
public class ViewNewLineActivity extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    RelativeLayout revenue_layout, clients_layout, goals_layout, territory_layout, info_layout;
    LinearLayout back_layout;

    private static ViewNewLineActivity viewLineActivity;

    public static ViewNewLineActivity getInstance() {
        return viewLineActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_line_setup);
        viewLineActivity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = new Bundle();

        initComponent();
    }

    private void initComponent() {

        revenue_layout = (RelativeLayout) findViewById(R.id.revenue_layout);
        revenue_layout.setOnClickListener(this);

        clients_layout = (RelativeLayout) findViewById(R.id.clients_layout);
        clients_layout.setOnClickListener(this);

        goals_layout = (RelativeLayout) findViewById(R.id.goals_layout);
        goals_layout.setOnClickListener(this);

        territory_layout = (RelativeLayout) findViewById(R.id.territory_layout);
        territory_layout.setOnClickListener(this);

        findViewById(R.id.ship_layout).setOnClickListener(this);

        info_layout = (RelativeLayout) findViewById(R.id.info_layout);
        info_layout.setOnClickListener(this);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.revenue_layout:
                MyUtils.startActivity(this, LineNewRevenueActivity.class, bundle);
                break;
            case R.id.clients_layout:
                bundle.putInt(Constants.SHOW_CLIENT, 0);
                MyUtils.startActivity(this, LineAddClientActivity.class, bundle);
                break;
            case R.id.info_layout:
                MyUtils.startActivity(this, LineRelevantInforActivity.class);
                break;
            case R.id.goals_layout:
                MyUtils.startActivity(this, LineCreateGoalActivity.class);
                break;
            case R.id.territory_layout:
                MyUtils.startActivity(this, ActivityTerritories.class);
                break;
            case R.id.ship_layout:
                MyUtils.startActivity(this, LineShipFromInforActivity.class);
                break;
        }
    }

}
