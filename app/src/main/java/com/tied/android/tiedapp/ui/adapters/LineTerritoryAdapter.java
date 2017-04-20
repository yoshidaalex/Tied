package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.TerritoryModel;

import java.util.List;

/**
 * Created by ZuumaPC on 9/03/2016.
 */
public class LineTerritoryAdapter extends BaseAdapter{

    public static final String TAG = LineTerritoryAdapter.class
            .getSimpleName();

    private static class ViewHolder {
        TextView txt_territory_name;
    }

    public List<TerritoryModel> _data;
    Context _c;
    ViewHolder viewHolder;

    public LineTerritoryAdapter(List<TerritoryModel> line_list, Context context) {
        _data = line_list;
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
            view = li.inflate(R.layout.line_territory_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_territory_name = (TextView) view.findViewById(R.id.territory);

        final TerritoryModel data = (TerritoryModel) _data.get(i);

        viewHolder.txt_territory_name.setText(data.getTerritory_name());
        view.setTag(data);
        return view;
    }

}
