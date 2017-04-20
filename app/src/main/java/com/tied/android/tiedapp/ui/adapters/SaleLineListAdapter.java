package com.tied.android.tiedapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.ui.activities.sales.ActivityLineClientSales;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class SaleLineListAdapter extends ClientParentAdapter {
    public static final String TAG = SaleLineListAdapter.class
            .getSimpleName();

    int source = Constants.LINE_SOURCE;
    ViewHolder v;
    protected List<Line> arraylist = new ArrayList<Line>();
    Context context;
    int page_index;

    public SaleLineListAdapter(int index, List<Line> lines, Context context) {
        super(lines, context);
        this.arraylist = lines;
        this.context = context;
        this.page_index = index;
    }
    public SaleLineListAdapter(int index, List<Line> lines, Context context, int source) {
        this(index, lines, context);
        this.source=source;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sale_line_list_item, viewGroup, false);

            final Line data = (Line) _data.get(i);

            v.item_cell = (RelativeLayout) view.findViewById(R.id.item_cell);
            v.item_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.LINE_DATA, data);
                    bundle.putInt(Constants.SOURCE, source);
                    MyUtils.startRequestActivity((Activity)context, ActivityLineClientSales.class, Constants.REVENUE_LIST, bundle);
                }
            });

            v.imageView = (ImageView) view.findViewById(R.id.dollar_icon);
            v.line_name = (TextView) view.findViewById(R.id.txt_line_name);
            v.line_date = (TextView) view.findViewById(R.id.txt_date);
            v.percent = (TextView) view.findViewById(R.id.txt_percent);
            v.price = (TextView) view.findViewById(R.id.txt_price);

            if (page_index == 0) {
                if (i % 2 == 0) {
                    v.item_cell.setBackgroundResource(R.color.white);
                } else {
                    v.item_cell.setBackgroundResource(R.color.light_grey3);
                }

                v.imageView.setBackgroundResource(R.drawable.dollar_red);
            } else {
                if (i % 2==0) {
                    v.item_cell.setBackgroundResource(R.color.light_grey3);
                    v.imageView.setBackgroundResource(R.drawable.dollar_purple);
                } else {
                    v.item_cell.setBackgroundResource(R.color.white);
                    v.imageView.setBackgroundResource(R.drawable.dollar_blue);
                }
            }

            v.line_name.setText(data.getName()+" Line");
            //v.line_date.setText(String.format(data.getLine_date()));
            //v.percent.setText(String.format("(%s%s)", data.getPercent(), "%"));
            v.price.setText(MyUtils.moneyFormat(data.getTotal_revenue()));

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
        RelativeLayout item_cell;
        ImageView imageView;
        TextView line_name, line_date, price, percent;
    }

}

