package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Notification;
import com.tied.android.tiedapp.util.MyUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ExpendableNotificationListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Notification>> _listDataChild;

    public ExpendableNotificationListAdapter(Context context, List<String> listDataHeader,
                                             HashMap<String, ArrayList<Notification>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Notification model = (Notification) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.notification_child_item, null);
        }

        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_time);
        TextView txt_comment = (TextView) convertView.findViewById(R.id.txt_comment);
        TextView txt_new = (TextView) convertView.findViewById(R.id.txt_new);

        String datetime = getTime(model.getCreated());
        String[] time = datetime.split(" ");
        txt_title.setText(datetime);
        txt_comment.setText(model.getComment());

        if (!model.isSeen()) {
            txt_new.setVisibility(View.VISIBLE);
        } else {
            txt_new.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.notification_group_item, null);
        }

        String str = headerTitle;
        String[] date = str.split("-");
        String title = "";
        String day = "";
        if (date[2].equals("01") || date[2].equals("11") || date[2].equals("21") || date[2].equals("31")) {
            if (date[2].equals("01")) day = "1st";
            else day = date[2] + "st";
        } else if (date[2].equals("02") || date[2].equals("12") || date[2].equals("22")){
            if (date[2].equals("02")) day = "2nd";
            else day = date[2] + "nd";
        } else {
            day = date[2] + "th";
        }

        String month = MyUtils.MONTHS_LIST[Integer.valueOf(date[1]).intValue() - 1];
        title = month + " " + day;

        TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
        txt_date.setText(title);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private String getTime(long created) {
        Timestamp stamp = new Timestamp(created);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = new Date(stamp.getTime());
        String time = sdf.format(date);
        return time;
    }
}
