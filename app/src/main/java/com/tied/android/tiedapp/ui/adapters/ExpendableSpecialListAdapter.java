package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ClientSaleDataModel;
import com.tied.android.tiedapp.customs.model.SpecialDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ExpendableSpecialListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<SpecialDataModel>> _listDataChild;

    public ExpendableSpecialListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, ArrayList<SpecialDataModel>> listChildData) {
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

        SpecialDataModel model = (SpecialDataModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.special_child_item, null);
        }

        TextView txt_line = (TextView) convertView.findViewById(R.id.txt_line);

        if (groupPosition == 0) {
            txt_line.setBackgroundColor(_context.getResources().getColor(R.color.orange_color));
        } else {
            txt_line.setBackgroundColor(_context.getResources().getColor(R.color.red_color));
        }

        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_title);
        TextView time_range = (TextView) convertView.findViewById(R.id.time_range);
        TextView client_count = (TextView) convertView.findViewById(R.id.client_count);

        txt_title.setText(model.getTitle());
        time_range.setText(model.getDate_range());
        client_count.setText(String.format("%s Clients", model.getClient_count()));

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
            convertView = infalInflater.inflate(R.layout.special_group_item, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_group_name);
        lblListHeader.setText(headerTitle);

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
}
