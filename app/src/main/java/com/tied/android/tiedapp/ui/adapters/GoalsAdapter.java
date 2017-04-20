package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.ui.activities.lines.LineGoalActivity;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZuumaPC on 9/3/2016.
 */
public class GoalsAdapter extends BaseAdapter implements ListAdapterListener {

    private static class ViewHolder {
        TextView txt_title,txt_date, percent;
        ImageView completed;

    }

    NumberFormat nf = NumberFormat.getIntegerInstance();

    public static final String TAG = GoalsAdapter.class
            .getSimpleName();

    public List<Goal> _data;
    Context _c;
    ViewHolder viewHolder;

    public GoalsAdapter(List<Goal> line_list, Context context) {
        _data = line_list;
        _c = context;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

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
            view = li.inflate(R.layout.line_goal_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_title = (TextView) view.findViewById(R.id.title);
        viewHolder.txt_date = (TextView) view.findViewById(R.id.date);
        viewHolder.percent = (TextView)view.findViewById(R.id.percent);
        viewHolder.completed = (ImageView)view.findViewById(R.id.mark_as_completed);

        final Goal data = (Goal) _data.get(i);

        viewHolder.txt_title.setText(data.getTitle());
        String date = "Ends" + data.getDate();
        try {
            Date ddate=MyUtils.parseDate(data.getDate().split(" ")[0]);
            if (ddate.before(new Date())) {
                viewHolder.txt_date.setTextColor(Color.RED);
            }else{
                viewHolder.txt_date.setTextColor(_c.getResources().getColor(R.color.light_gray2));
            }
            String timePassedString = ""+ DateUtils.getRelativeTimeSpanString(ddate.getTime(), System.currentTimeMillis(), DateUtils.WEEK_IN_MILLIS);

            viewHolder.txt_date.setText(timePassedString);

        }catch (Exception e) {
            Logger.write(e);
        }
        double progress=0;
        if(data.getValue()!=null) {
            progress=Double.parseDouble(data.getValue());
        }
        viewHolder.percent.setText(nf.format(progress/Double.parseDouble(data.getTarget()))+"%");
        view.setTag(data);
        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        android.support.v4.view.ViewPager mViewPager = ((LineGoalActivity) _c).mViewPager;
        notifyDataSetChanged();
        mViewPager.getAdapter().notifyDataSetChanged();
    }
}
