package com.tied.android.tiedapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ScheduleNotifyModel;
import com.tied.android.tiedapp.ui.adapters.ScheduleNotifyAdapter;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateAppointmentFragment;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/6/2016.
 */
public class ScheduleNotifyDialog implements View.OnClickListener{


    public static final String TAG = ScheduleNotifyDialog.class
            .getSimpleName();

    private SelectedListener selectedListener;

    private TextView cancel, done;
    private Dialog dialog;
    private Context context;
    private ScheduleNotifyModel scheduleNotifyModel;
//    private User user;

    int[] id = {1, 2, 3, 4};
    String[] txt_notify = {"20 minutes","1 hour","12 hours","1 day"};
    Boolean[] notify_status = {true,false,false,false};

    ArrayList<ScheduleNotifyModel> listNotify = new ArrayList<ScheduleNotifyModel>();
    ScheduleNotifyAdapter adapter;
    ListView listView;

    public void showDialog(final CreateAppointmentFragment fragment){
        dialog = new Dialog(fragment.getActivity());
        context = fragment.getActivity();
//        this.user = user;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_schedule_notify);

        listView = (ListView) dialog.findViewById(R.id.list);

        cancel = (TextView) dialog.findViewById(R.id.cancel);
        done = (TextView) dialog.findViewById(R.id.yes);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);

        for(int i = 0; i < id.length; i++){
            ScheduleNotifyModel list_reminder = new ScheduleNotifyModel(id[i],txt_notify[i],notify_status[i]);
            listNotify.add(list_reminder);
        }
        adapter = new ScheduleNotifyAdapter(listNotify,context);
        listView.setAdapter(adapter);

        this.scheduleNotifyModel = listNotify.get(0);

        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                adapter.setSelectedIndex(position);
                selectedListener = (CreateAppointmentFragment) fragment;
                scheduleNotifyModel = listNotify.get(position);
                selectedListener.selectedNow(scheduleNotifyModel);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.yes:
                dialog.dismiss();
                break;
        }
    }

    public void changeText(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                scheduleNotifyModel = listNotify.get(position);
            }
        });
    }

    public interface SelectedListener{
        void selectedNow(ScheduleNotifyModel scheduleNotifyModel);
    }
}
