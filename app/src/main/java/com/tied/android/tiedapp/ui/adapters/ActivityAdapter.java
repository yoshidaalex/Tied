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
import com.tied.android.tiedapp.customs.model.ActivityDataModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Notification;
import com.tied.android.tiedapp.util.MyUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ActivityAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView day,month, time_range, title, description;
    }

    public static final String TAG = ActivityAdapter.class
            .getSimpleName();

    public ArrayList<Notification> _data;
    Context _c;
    ViewHolder viewHolder;

    public ActivityAdapter(ArrayList<Notification> activity_list, Context context) {
        _data = activity_list;
        _c = context;
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.coworker_activity_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.day = (TextView) view.findViewById(R.id.day);
        viewHolder.month = (TextView) view.findViewById(R.id.month);
        viewHolder.time_range = (TextView) view.findViewById(R.id.time_range);
        viewHolder.title = (TextView) view.findViewById(R.id.title);
        viewHolder.description = (TextView) view.findViewById(R.id.description);

        final Notification data = (Notification) _data.get(i);

        String datetime = getDate(data.getCreated());
        String[] time = datetime.split(" ");

        viewHolder.day.setText(time[2]);
        viewHolder.month.setText(MyUtils.MONTHS_LIST[Integer.valueOf(time[1]).intValue() - 1]);
//        viewHolder.time_range.setText(data.getTime_range());
        viewHolder.title.setText(data.getComment());
//        viewHolder.description.setText(data.getDescription());

        view.setTag(data);
        return view;
    }

    private String getDate(long created) {
        Timestamp stamp = new Timestamp(created);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(stamp.getTime());
        String time = sdf.format(date);
        return time;
    }
}
