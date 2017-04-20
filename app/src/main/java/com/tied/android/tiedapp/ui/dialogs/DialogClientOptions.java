package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Emmanuel on 7/20/2016.
 */
public class DialogClientOptions implements View.OnClickListener {

    public static final String TAG = DialogClientOptions.class
            .getSimpleName();
    RelativeLayout cancel;
    private TextView create_schedule;
    private Dialog dialog;
    private Client client;
    Activity activity;
    Bundle bundle;

    public DialogClientOptions(Client client, Activity activity, Bundle bundle){
        this.client = client;
        this.activity = activity;
        this.bundle = bundle;
    }

    public void showDialog(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Setting dialogview
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.dialog_client_options);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        create_schedule = (TextView) dialog.findViewById(R.id.create_schedule);
        create_schedule.setOnClickListener(this);

        cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.create_schedule:
                MyUtils.startActivity(activity, CreateAppointmentActivity.class, bundle);
                dialog.dismiss();
                break;
        }
    }

}
