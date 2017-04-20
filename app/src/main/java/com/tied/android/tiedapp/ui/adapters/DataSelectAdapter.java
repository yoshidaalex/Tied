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
import com.tied.android.tiedapp.customs.model.DataModel;

import java.util.List;

/**
 * Created by ZuumaPC on 7/14/2016.
 */
public class DataSelectAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_data;
        ImageView img_data;
    }

    public static final String TAG = DataSelectAdapter.class
            .getSimpleName();

    public List<DataModel> _data;
    Context _c;
    ViewHolder viewHolder;

    public DataSelectAdapter(List<DataModel> industry_list, Context context) {
        _data = industry_list;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.dialog_data_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_data = (TextView) view.findViewById(R.id.txt_data);
        viewHolder.img_data = (ImageView) view.findViewById(R.id.img_data);

        final DataModel data = (DataModel) _data.get(i);

        viewHolder.txt_data.setText(data.getName());
        if (data.isCheck_status()) {
            viewHolder.img_data.setBackgroundResource(R.mipmap.circle_check2);
        } else {
            viewHolder.img_data.setBackgroundResource(R.mipmap.circle_uncheck);
        }

        view.setTag(data);
        return view;
    }

    public void setSelectedIndex(int index){
        for(int i = 0; i< _data.size(); i++){
            if(i != index){
                _data.get(i).setCheck_status(false);
            }
            else{
                _data.get(i).setCheck_status(true);
            }
        }
        notifyDataSetChanged();
    }


}
