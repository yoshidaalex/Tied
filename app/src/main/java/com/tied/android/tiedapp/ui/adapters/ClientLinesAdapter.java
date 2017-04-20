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
import com.tied.android.tiedapp.objects.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientLinesAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_line_name,txt_line_sales;
        ImageView img_check;
    }

    public static final String TAG = ClientLinesAdapter.class
            .getSimpleName();

    public List<Line> _data;
    Context _c;
    ViewHolder viewHolder;
    private List<String> selected=null;
    boolean isMultiple=false;

    public ClientLinesAdapter(List<Line> data, List<String> selected, Context context, boolean isMultiple) {
        _data = data;
        _c = context;
        this.selected=(selected==null?new ArrayList<String>():selected);
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
            view = li.inflate(R.layout.client_lines_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_line_name = (TextView) view.findViewById(R.id.line);
//        viewHolder.txt_line_sales = (TextView) view.findViewById(R.id.sales);
        viewHolder.img_check = (ImageView) view.findViewById(R.id.selector);

        final Line data = (Line) _data.get(i);

        viewHolder.txt_line_name.setText(data.getName());
//        String sales = data.getSales() + "Total sales";
//        viewHolder.txt_line_sales.setText(sales);

        if(isMultiple) {
            if (selected.contains(data.getId())) {
                viewHolder.img_check.setImageResource(R.drawable.selected_bg);
            } else {
                viewHolder.img_check.setImageResource(R.drawable.unselectd_bg);
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
