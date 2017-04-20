package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class SaleClientDetailsListAdapter extends ClientParentAdapter {
    public static final String TAG = SaleClientDetailsListAdapter.class
            .getSimpleName();

    ViewHolder v;
    protected ArrayList<Revenue> arraylist = new ArrayList<Revenue>();
    protected ArrayList<Line> lines = new ArrayList<Line>();
    Context context;

    public SaleClientDetailsListAdapter(ArrayList<Revenue> revenues, ArrayList<Line> lines, Context context) {
        super(lines, context);
        this._data = revenues;
        this.lines = lines;
        this.context = context;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sale_client_details_list_item, viewGroup, false);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        Revenue data = (Revenue) _data.get(i);


        v.txt_price = (TextView) view.findViewById(R.id.txt_price);
        v.txt_date = (TextView) view.findViewById(R.id.txt_date);
        v.txt_summary = (TextView) view.findViewById(R.id.txt_summary);

        v.txt_price.setText(MyUtils.moneyFormat(data.getValue()));
        try {
            Logger.write(data.getDate_sold());
            if(!data.getDate_sold().isEmpty()) {
                v.txt_date.setText(MyUtils.formatDate(MyUtils.parseDate(data.getDate_sold())));
                v.txt_date.setVisibility(View.VISIBLE);
            }else v.txt_date.setVisibility(View.GONE);
        }catch (Exception e){
            Logger.write(e);
        }
        if(lines!=null){
            Line line= (Line) lines.get(i);
            v.txt_summary.setText(line.getName());
        }else{
            v.txt_summary.setText(data.getTitle());
        }


        view.setTag(v);

        return view;
    }

    public void setLines(ArrayList lines) {
        this.lines=lines;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView txt_price, txt_date, txt_summary;
    }

}

