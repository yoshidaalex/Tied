package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.HelpModel;

import java.util.ArrayList;

/**
 * Created by greepon123 on 6/6/2016.
 */
public class HelpAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HelpModel> rowItems;


    public HelpAdapter(Context context, ArrayList<HelpModel> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
//        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHoder viewHoder;
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_help_list_detail, parent, false);

            viewHoder = new ViewHoder();


            viewHoder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHoder.title = (TextView) convertView.findViewById(R.id.title);
            viewHoder.description = (TextView) convertView.findViewById(R.id.description);

            convertView.setTag(viewHoder);

        } else {

            viewHoder = (ViewHoder) convertView.getTag();
        }

        HelpModel beanClass = (HelpModel) getItem(position);

        viewHoder.title.setText(beanClass.getTitle());
        viewHoder.description.setText(beanClass.getDescription());


        return convertView;
    }


    private class ViewHoder{
        ImageView img;
        TextView title;
        TextView description;
    }




}

