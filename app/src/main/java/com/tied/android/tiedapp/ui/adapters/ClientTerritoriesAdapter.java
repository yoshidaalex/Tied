package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Territory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientTerritoriesAdapter extends BaseAdapter {
    boolean isMultiple=false;
    private static class ViewHolder {
        TextView txt_territoy_name,txt_territory_client;
        ImageView img_check;
    }

    private List<String> selected=null;

    public static final String TAG = ClientTerritoriesAdapter.class
            .getSimpleName();

    public List<Territory> _data;
    Context _c;
    ViewHolder viewHolder;
    int _page_index;

    public ClientTerritoriesAdapter( List<Territory> territory_list,  List<String> selected, Context context, boolean isMultiple) {
        _data = territory_list;
        _c = context;
        this.selected=selected;
        this.isMultiple=isMultiple;
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
            view = li.inflate(R.layout.client_territories_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_territoy_name = (TextView) view.findViewById(R.id.territory);
        viewHolder.img_check = (ImageView) view.findViewById(R.id.selector);

        final Territory data = (Territory) _data.get(i);

        viewHolder.txt_territoy_name.setText(data.getCounty()+", "+data.getState());
        if(isMultiple) {
            if (selected.contains(data.getId())) {
                viewHolder.img_check.setBackgroundResource(R.drawable.circle_check2);
                viewHolder.txt_territoy_name.setTextColor(_c.getResources().getColor(R.color.light_gray2));
            } else {
                viewHolder.img_check.setBackgroundResource(R.drawable.unselectd_bg);
                viewHolder.txt_territoy_name.setTextColor(_c.getResources().getColor(R.color.grey));
            }
        }else{
            viewHolder.img_check.setVisibility(View.GONE);
        }

        view.setTag(data);
        return view;
    }
    public void setSelected(List<String> selected) {
        this.selected=(selected==null?new ArrayList<String>():selected);
    }
}
