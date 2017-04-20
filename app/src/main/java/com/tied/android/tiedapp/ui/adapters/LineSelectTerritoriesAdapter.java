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
import com.tied.android.tiedapp.customs.model.TerritoryModel;

import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LineSelectTerritoriesAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_territoy_name;
        ImageView img_check;
    }

    public static final String TAG = LineSelectTerritoriesAdapter.class
            .getSimpleName();

    public List<TerritoryModel> _data;
    Context _c;
    ViewHolder viewHolder;

    public LineSelectTerritoriesAdapter(List<TerritoryModel> territory_list, Context context) {
        _data = territory_list;
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

        viewHolder.txt_territoy_name = (TextView) view.findViewById(R.id.territory);
        viewHolder.img_check = (ImageView) view.findViewById(R.id.img_check);

        final TerritoryModel data = (TerritoryModel) _data.get(i);

        viewHolder.txt_territoy_name.setText(data.getTerritory_name());

        if (data.isCheck_status()) {
            viewHolder.img_check.setBackgroundResource(R.drawable.circle_check2);
            viewHolder.txt_territoy_name.setTextColor(_c.getResources().getColor(R.color.light_gray));
        } else {
            viewHolder.img_check.setBackgroundResource(R.drawable.unselectd_bg);
            viewHolder.txt_territoy_name.setTextColor(_c.getResources().getColor(R.color.grey));
        }

        viewHolder.img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedIndex(i);
            }
        });

        view.setTag(data);
        return view;
    }

    public void setSelectedIndex(int index){

        if (_data.get(index).isCheck_status()) {
            _data.get(index).setCheck_status(false);
        } else {
            _data.get(index).setCheck_status(true);
        }

        notifyDataSetChanged();
    }
}
