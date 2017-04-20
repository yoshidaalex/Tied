package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ScheduleNotifyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/6/2016.
 */
public class ScheduleNotifyAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_reminder;
        ImageView img_reminder;
    }

    public static final String TAG = ScheduleNotifyAdapter.class
            .getSimpleName();

    public List<ScheduleNotifyModel> _data;
    private ArrayList<ScheduleNotifyModel> arraylist;
    Context _c;
    ViewHolder viewHolder;

    public ScheduleNotifyAdapter(List<ScheduleNotifyModel> reminder_list, Context context) {
        _data = reminder_list;
        _c = context;
        this.arraylist = new ArrayList<ScheduleNotifyModel>();
        this.arraylist.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.dialog_schedule_notify_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_reminder = (TextView) view.findViewById(R.id.txt_reminder);
        viewHolder.img_reminder = (ImageView) view.findViewById(R.id.img_reminder);

        final ScheduleNotifyModel data = (ScheduleNotifyModel) _data.get(i);

        viewHolder.txt_reminder.setText(data.getTxt_notify());
        if (data.getCheckStatus()) {
            viewHolder.img_reminder.setBackgroundResource(R.mipmap.circle_check2);
        } else {
            viewHolder.img_reminder.setBackgroundResource(R.mipmap.circle_uncheck);
        }

        view.setTag(data);
        return view;
    }

    public void setSelectedIndex(int index){
        for(int i = 0; i< arraylist.size(); i++){
            if(i != index){
                _data.get(i).setCheckStatus(false);
            }
            else{
                _data.get(i).setCheckStatus(true);
            }
        }
        notifyDataSetChanged();
    }


}

