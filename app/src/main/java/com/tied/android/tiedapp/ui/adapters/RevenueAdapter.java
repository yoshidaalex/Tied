package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Femi on 7/28/2016.
 */
public class RevenueAdapter extends BaseAdapter {
    private Context context;
    private List<Revenue> rowItems;


    public RevenueAdapter(Context context, List<Revenue> rowItems) {
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
            convertView = layoutInflater.inflate(R.layout.revenue_lv_item, parent, false);

            viewHoder = new ViewHoder();


           // viewHoder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHoder.title = (TextView) convertView.findViewById(R.id.title);
            viewHoder.description = (TextView) convertView.findViewById(R.id.value);
            viewHoder.dateSold=(TextView)convertView.findViewById(R.id.date_sold);


            convertView.setTag(viewHoder);

        } else {

            viewHoder = (ViewHoder) convertView.getTag();
        }

        Revenue beanClass = (Revenue) getItem(position);

        viewHoder.title.setText(beanClass.getTitle());
        viewHoder.description.setText(MyUtils.moneyFormat(beanClass.getValue()));
        if(beanClass.getDate_sold()!=null) {
            try {

                viewHoder.dateSold.setText(MyUtils.formatDate(MyUtils.parseDate(beanClass.getDate_sold())));

            }catch (Exception e) {
                Logger.write(e);
            }

        }


        return convertView;
    }


    private class ViewHoder{
        ImageView img;
        TextView title;
        TextView description, dateSold;

    }

}
