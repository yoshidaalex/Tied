package com.tied.android.tiedapp.ui.activities.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.lines.LineGoalActivity;
import com.tied.android.tiedapp.ui.activities.lines.LineTerritoriesActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogNewClient1;
import com.tied.android.tiedapp.ui.dialogs.DialogYesNo;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by femi on 8/10/2016.
 */
public class NewClientActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "ACTIVIY_CLINET";

    private Bundle bundle;
    User user;
    private Client client;

    ImageView img_close, img_edit;
    LinearLayout icon_plus, icon_mail, icon_call;
    TextView txt_delete;
    RelativeLayout total_sales, schedule, special, goal, line, territory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.fragment_view_client);

        initView();
    }

    private void initView() {

        bundle = new Bundle();

        img_close = (ImageView) findViewById(R.id.img_close);
        //img_close.setOnClickListener(this);

        img_edit = (ImageView) findViewById(R.id.img_edit);
        img_edit.setOnClickListener(this);

        txt_delete = (TextView) findViewById(R.id.txt_delete);

        icon_plus = (LinearLayout) findViewById(R.id.icon_plus);
        icon_plus.setOnClickListener(this);

        icon_mail = (LinearLayout) findViewById(R.id.icon_mail);
        icon_mail.setOnClickListener(this);

        icon_call = (LinearLayout) findViewById(R.id.icon_call);
        icon_call.setOnClickListener(this);

        total_sales = (RelativeLayout) findViewById(R.id.total_sale);
        total_sales.setOnClickListener(this);

        schedule = (RelativeLayout) findViewById(R.id.schedule);
        schedule.setOnClickListener(this);

       // special = (RelativeLayout) findViewById(R.id.special);
       // special.setOnClickListener(this);

       // goal = (RelativeLayout) findViewById(R.id.goal);
      // goal.setOnClickListener(this);

        line = (RelativeLayout) findViewById(R.id.line);
        line.setOnClickListener(this);

       // territory = (RelativeLayout) findViewById(R.id.territory);
        //territory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int color = this.getResources().getColor(R.color.schedule_title_bg_color);
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_edit:

                break;
            case R.id.txt_delete:
                color = this.getResources().getColor(R.color.alert_bg_color);
//                DialogYesNo alert_delete = new DialogYesNo(this, null, "DELETE CLIENT","Are you sure want to delete this client","YES DELETE!",color,0);
//                alert_delete.showDialog();
                break;
            case R.id.icon_plus:
                DialogNewClient1 alert = new DialogNewClient1();
                alert.showDialog(this, bundle);
                break;
            case R.id.icon_mail:
                color = this.getResources().getColor(R.color.green_color);
//                DialogYesNo alert_call = new DialogYesNo(this,"CALL CLIENT","Are you sure want to call this client?. Call charges may apply","YES, CALL!",color,1);
//                alert_call.showDialog();
                break;
            case R.id.icon_call:

                break;
            case R.id.sale_layout:
                MyUtils.startActivity(this, TotalSalesActivity.class);
                break;
            case R.id.schedule:

                MyUtils.startActivity(this, ScheduleActivity.class);
                break;
           // case R.id.special:
              //  MyUtils.startActivity(this, SpecialActivity.class);
              //  break;
            case R.id.goal:
                MyUtils.startActivity(this, LineGoalActivity.class);
                break;
            case R.id.line:
                bundle.putInt(Constants.SHOW_LINE, 0);
                MyUtils.startActivity(this, LineTerritoriesActivity.class, bundle);
                break;
            case R.id.territory:
                bundle.putInt(Constants.SHOW_LINE, 1);
                MyUtils.startActivity(this, LineTerritoriesActivity.class, bundle);
                break;
        }
    }

}

