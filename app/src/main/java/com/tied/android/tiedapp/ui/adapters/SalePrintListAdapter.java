package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.LineDataModel;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class SalePrintListAdapter extends ClientParentAdapter {
    public static final String TAG = SalePrintListAdapter.class
            .getSimpleName();

    ViewHolder v;
    protected ArrayList<LineDataModel> arraylist = new ArrayList<LineDataModel>();
    Context context;

    public SalePrintListAdapter(ArrayList<LineDataModel> lines, Context context) {
        super(lines, context);
        this.arraylist = lines;
        this.context = context;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sale_printer_list_item, viewGroup, false);

            LineDataModel data = (LineDataModel) _data.get(i);

            v.month = (TextView) view.findViewById(R.id.txt_month);
            v.price = (TextView) view.findViewById(R.id.txt_price);
            v.client_count = (TextView) view.findViewById(R.id.txt_client_count);

            v.btnReport = (TextView) view.findViewById(R.id.txt_send);

            v.month.setText(data.getLine_name());
            v.price.setText(data.getLine_date());
            v.client_count.setText(data.getPrice());

            view.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView month, price, client_count, btnReport;
    }

}

