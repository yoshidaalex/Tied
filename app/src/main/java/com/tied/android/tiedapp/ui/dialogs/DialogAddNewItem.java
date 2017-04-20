package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.activities.client.NewClientActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.ui.activities.territories.ActivityTerritories;
import com.tied.android.tiedapp.ui.activities.visits.ActivityAddVisits;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.util.HTTPConnection;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Emmanuel on 7/18/2016.
 */
public class DialogAddNewItem implements View.OnClickListener {

    public static final String TAG = DialogAddNewItem.class
            .getSimpleName();
    RelativeLayout close;
    private TextView edit,delete,mark_as_completed,cancel_appointment, view;
    private Dialog dialog;
    Activity _c;
    Bundle bundle;
    private ScheduleListAdapter adapter;

    public void showDialog(Activity activity, Bundle bundle1){
        _c = activity;
        bundle = bundle1;
        this.adapter = adapter;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.dialog_add_client);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        RelativeLayout schedule_layout = (RelativeLayout) dialog.findViewById(R.id.schedule_layout);
        schedule_layout.setOnClickListener(this);

        RelativeLayout sale_layout = (RelativeLayout) dialog.findViewById(R.id.sale_layout);
        sale_layout.setOnClickListener(this);

        RelativeLayout visit_layout = (RelativeLayout) dialog.findViewById(R.id.visit_layout);
        visit_layout.setOnClickListener(this);

        RelativeLayout client_layout = (RelativeLayout) dialog.findViewById(R.id.client_layout);
        client_layout.setOnClickListener(this);

       // RelativeLayout goal_layout = (RelativeLayout) dialog.findViewById(R.id.goal_layout);
       // goal_layout.setOnClickListener(this);

        RelativeLayout line_layout = (RelativeLayout) dialog.findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

        AppCompatTextView atvCancel = (AppCompatTextView) dialog.findViewById(R.id.atvCancel);
        atvCancel.setOnClickListener(this);

        dialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.atvCancel:
                dialog.dismiss();
                break;
            case R.id.schedule_layout:
                MyUtils.startRequestActivity(_c, CreateAppointmentActivity.class, Constants.CreateSchedule, bundle);
                dialog.dismiss();
                break;
            case R.id.sale_layout:

                dialog.dismiss();
                MyUtils.startRequestActivity(_c, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
            case R.id.client_layout:
                MyUtils.startActivity(_c, AddClientActivity.class, bundle);
               /* MyUtils.showNewLineDialog(_c, "Add New Client", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(final Object response) {
                        AddClientFragment.createClient((Client)response, _c, new HTTPConnection.AjaxCallback() {
                            @Override
                            public void run(int code, String resp) {
                                Logger.write(resp);
                                bundle.putSerializable(Constants.CLIENT_DATA, (Client)response);
                                MyUtils.startActivity(_c, ActivityClientProfile.class, bundle);
                            }
                        });
                    }
                });*/
                dialog.dismiss();
                break;
            case R.id.goal_layout:
                dialog.dismiss();
                break;
            case R.id.line_layout:
                MyUtils.startActivity(_c, AddLinesActivity.class, bundle);
                dialog.dismiss();
                break;
            case R.id.visit_layout:
                MyUtils.startActivity(_c, ActivityAddVisits.class,bundle);
                dialog.dismiss();
                break;

        }
    }

}
